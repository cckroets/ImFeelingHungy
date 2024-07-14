package ckroetsch.imfeelinghungry.data

import com.google.firebase.Firebase
import com.google.firebase.vertexai.vertexAI

class GenerativeChef(val userPreferences: UserPreferences) {
    private val model = Firebase.vertexAI.generativeModel("gemini-1.5-flash")

    suspend fun generateMenuItem(): String {
        return model.generateContent("I'm feeling hungry! Suggest something for me!").text ?: "no answer"
    }
}
