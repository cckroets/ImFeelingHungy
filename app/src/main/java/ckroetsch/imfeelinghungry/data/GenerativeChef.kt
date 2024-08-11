package ckroetsch.imfeelinghungry.data

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.vertexai.Chat
import com.google.firebase.vertexai.type.BlockThreshold
import com.google.firebase.vertexai.type.Content
import com.google.firebase.vertexai.type.GenerateContentResponse
import com.google.firebase.vertexai.type.GenerationConfig
import com.google.firebase.vertexai.type.HarmCategory
import com.google.firebase.vertexai.type.SafetySetting
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.encodeToStream

val UseGenAi = true

private const val TAG = "GenerativeChef"

val HungryJson = Json {
    ignoreUnknownKeys = true
    isLenient = true
    explicitNulls = false
}

@OptIn(ExperimentalSerializationApi::class)
class GenerativeChef(
    private val context: Context,
    private val assets: AssetManager,
    private val userPreferences: UserPreferences
) {
    private val schema by lazy {
        assets.open("nutrition_info/item-schema.json").bufferedReader().use { it.readText() }
    }

    private val systemInstruction = """
        Output the response as JSON using the following schema. Make sure to respect the property types:
        $schema
    """.trimIndent()

    private val generationConfig = GenerationConfig.Builder().apply {
        this.candidateCount = 1
    }.build()

    private val model = Firebase.vertexAI.generativeModel(
        modelName = "gemini-1.5-flash",
        generationConfig = generationConfig,
        systemInstruction = content { text(systemInstruction) },
        safetySettings = HarmCategory.entries.map { SafetySetting(it, BlockThreshold.NONE)}
            .filter { it.harmCategory != HarmCategory.UNKNOWN }
    )

    private val json = HungryJson

    private var lastChat: Chat? = null

    fun regenerateWithInstructions(instructions: String): Flow<Result<MenuItem>> {
        return flow {
            emit(Result.Loading)
            val chat = lastChat
            if (chat == null) {
                emit(Result.Error("Chat not initialized"))
            } else {
                handleResponse(chat.sendMessageStream(
                    """
                    Regenerate the menu items with the same instructions, but with the following suggestion: $instructions
                    """.trimIndent()
                    )
                )
            }

        }.flowOn(Dispatchers.IO)
    }

    fun generateFromChat(): Flow<Result<MenuItem>> {
        val restaurants = userPreferences.restaurantPreferences.likedOptionsAsInput().takeIf{
            it.isNotEmpty()
        }?: listOf(AllRestaurants.take(3).map{it.name})
        //val food = userPreferences.foodPreferences.likedOptionsAsInput()
        val diet = userPreferences.dietaryPreferences.likedOptionsAsInput().takeIf{
            it.isNotEmpty()
        }?: listOf(AllPreferences.take(1).map{it.name})

        val prompt = """
            Generate a custom menu 'hack' item from one of the following restaurants: $restaurants that
            best matches the dietary preferences: $diet. Do not suggest other diets. Only suggest one item, no sides.
            Feel free to make modifications to the menu item as needed (add/remove/substitute) But make sure it is from
            the menu.
        """.trimIndent()

        val content: Content = content { text(prompt) }
        return flow {
            emit(Result.Loading)
            if (!UseGenAi) {
                emit(Result.Success(loadResponseFromFile()))
                return@flow
            }
            Log.i(TAG, "Generating menu item for $diet, $restaurants")
            val chat = lastChat ?: model.startChat().also { lastChat = it }
            handleResponse(chat.sendMessageStream(content))
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<Result<MenuItem>>.handleResponse(
        response: Flow<GenerateContentResponse>
    ) {
        var result = ""
        var emittedError = false

        response.flowOn(Dispatchers.IO).catch { e ->
            Log.e(TAG, "Failed to generate content: ${e.message}", e)
            emittedError = true
            emit(Result.Error("Failed to generate menu item: ${e.message}"))
        }.collect { chunk ->
            Log.i(TAG, "Chunk received")
            result += chunk.text
        }
        if (result.isEmpty() || emittedError) {
            return
        }
        val final = result
            .trim()
            .removePrefix("```json")
            .removeSuffix("```")
            .let {
                try {
                    Log.i(TAG, "Decoding JSON response...")
                    val item = json.decodeFromString<MenuItem>(it)
                    Log.i(TAG, "Saving to local File...")
                    saveResponseToFile(item)
                    Result.Success(item)
                } catch (e: Exception) {
                    if (e is SerializationException) {
                        Log.i(TAG, it)
                    }
                    Log.e("GenerativeChef", "Failed to parse JSON", e)
                    Result.Error("Failed to parse JSON: ${e.message}")
                }
            }
        emit(final)
    }

    private fun saveResponseToFile(menuItem: MenuItem) {
        with(context.openFileOutput("creation_${menuItem.menuItemTitle}.json", Context.MODE_PRIVATE)) {
            json.encodeToStream(MenuItem.serializer(), menuItem, this)
            close()
        }
    }

    private fun loadResponseFromFile(): MenuItem {
        assets.open("creations/menu_item_1128457436.json").bufferedReader().use {
            return json.decodeFromString(MenuItem.serializer(), it.readText())
        }
    }
}

private fun Map<String, Preference>.likedOptionsAsInput(): String {
    return filter { it.value == Preference.LIKED }.keys.joinToString { it }
}


