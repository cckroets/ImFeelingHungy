package ckroetsch.imfeelinghungry.data

import android.content.res.AssetManager
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.vertexai.type.Content
import com.google.firebase.vertexai.type.content
import com.google.firebase.vertexai.vertexAI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json

sealed class Result<out T> {
    data class Success<T>(
        val data: T
    ) : Result<T>()

    data class Error(
        val message: String
    ) : Result<Nothing>()

    object Loading : Result<Nothing>()
}

class GenerativeChef(
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

    private val model = Firebase.vertexAI.generativeModel(
        modelName = "gemini-1.5-flash",
        systemInstruction = content { text(systemInstruction) }
    )

    @OptIn(ExperimentalSerializationApi::class)
    fun generateMenuItem(): Flow<Result<MenuItem>> {
        val restaurants = userPreferences.restaurantPreferences.likedOptionsAsInput()
        val food = userPreferences.foodPreferences.likedOptionsAsInput()
        val diet = userPreferences.dietaryPreferences.likedOptionsAsInput()

        val prompt = """
            Generate a custom menu item from one of the following restaurants: $restaurants that
            best matches these food preferences: $food and dietary preferences: $diet . Do not suggest other diets.
            Feel free to make modifications to the menu item as needed (add/remove/substitute).
        """.trimIndent()

        val content: Content = content {
            text(prompt)
        }

        val json = Json {
            ignoreUnknownKeys = true
            isLenient = true
            explicitNulls = false
        }

        return flow {
            emit(Result.Loading)
                var result = ""
                model.generateContentStream(content).collect { chunk -> result += chunk.text }

                val final = result
                    .removePrefix("```json\n")
                    .removeSuffix("\n```")
                    .let {
                        try {
                            val item = json.decodeFromString<MenuItem>(it)
                            Result.Success(item)
                        } catch (e: Exception) {
                            Log.e("GenerativeChef", "Failed to parse JSON", e)
                            Result.Error("Failed to parse JSON: ${e.message}")
                        }
                    }
                emit(final)
        }.flowOn(Dispatchers.Main)
    }
}

private fun Map<String, Preference>.likedOptionsAsInput(): String {
    return filter { it.value == Preference.LIKED }.keys.joinToString { it }
}


