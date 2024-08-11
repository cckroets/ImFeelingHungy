package ckroetsch.imfeelinghungry.data

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import ckroetsch.imfeelinghungry.onboarding.AllPreferences
import ckroetsch.imfeelinghungry.onboarding.AllRestaurants
import ckroetsch.imfeelinghungry.onboarding.Restaurant
import com.google.firebase.Firebase
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

sealed class Result<out T> {
    data class Success<T>(
        val data: T
    ) : Result<T>()

    data class Error(
        val message: String
    ) : Result<Nothing>()

    data object Loading : Result<Nothing>()
}

val UseGenAi = true

private const val TAG = "GenerativeChef"

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
        Output the response as JSON using the following schema:
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

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        explicitNulls = false
    }

    private val chat by lazy { model.startChat() }

    fun regenerateWithInstructions(instructions: String): Flow<Result<MenuItem>> {
        return flow {
            emit(Result.Loading)
            handleResponse(
                chat.sendMessageStream(
                    """
                    Regenerate the menu items with the same instructions, but with the following suggestion: $instructions
                    """.trimIndent()
                )
            )
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
            best matches the dietary preferences: $diet. Do not suggest other diets.
            Feel free to make modifications to the menu item as needed (add/remove/substitute) But make sure it is from
            the menu. 
        """.trimIndent()

        val content: Content = content {
            text(prompt)
        }

        return flow {
            emit(Result.Loading)
            if (!UseGenAi) {
                emit(Result.Success(loadResponseFromFile()))
                return@flow
            }

            Log.i(TAG, "Generating menu item for $diet, $restaurants")
            var result = ""
            var emittedError = false

            handleResponse(chat.sendMessageStream(content))
        }.flowOn(Dispatchers.IO)
    }

    private suspend fun FlowCollector<Result<MenuItem>>.handleResponse(
        response: Flow<GenerateContentResponse>
    ) {
        var result = ""
        var emittedError = false

        response.flowOn(Dispatchers.IO).catch { e ->
            Log.e(TAG, "Failed to generate content: ${e.message}", IllegalStateException(e))
            emittedError = true
            emit(Result.Error("Failed to generate content: ${e.message}"))
        }.collect { chunk ->
            Log.i(TAG, "Chunk received")
            result += chunk.text
        }
        if (result.isEmpty() || emittedError) {
            return
        }
        val final = result
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

    fun generateMenuItem(): Flow<Result<MenuItem>> {
        val restaurants = userPreferences.restaurantPreferences.likedOptionsAsInput()
        val food = userPreferences.foodPreferences.likedOptionsAsInput()
        val diet = userPreferences.dietaryPreferences.likedOptionsAsInput()

        val prompt = """
            Generate a custom menu 'hack' item from one of the following restaurants: $restaurants that
            best matches these food preferences: $food and dietary preferences: $diet. Do not suggest other diets.
            Feel free to make modifications to the menu item as needed (add/remove/substitute).
        """.trimIndent()

        val content: Content = content {
            text(prompt)
        }

        return flow {
            emit(Result.Loading)
            if (!UseGenAi) {
                emit(Result.Success(loadResponseFromFile()))
                return@flow
            }

            Log.i(TAG, "Generating menu item for $food, $diet, $restaurants")
            var result = ""
            var emittedError = false
            model.generateContentStream(content)
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.e(TAG, "Failed to generate content: ${e.message}, ${e.javaClass}", IllegalStateException(e))
                    emittedError = true
                    emit(Result.Error("Failed to generate content: ${e.message}"))
                }
                .collect { chunk ->
                    Log.i(TAG, "Chunk received")
                    result += chunk.text
                }
            Log.i(TAG, "Generation complete: ${result.length}" )
            if (result.isEmpty() || emittedError) {
                return@flow
            }
            val final = result
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
        }.flowOn(Dispatchers.IO)
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


