package com.example.ai

import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiApiClient {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun generateContent(
        prompt: String,
        systemInstruction: String = "You are ANKIT AI, a brilliant personal AI teacher and exam coach in the ANKIT STUDY AI app managed by Ankit Rajak. You explain complex concepts with clarity, empathy, and pedagogy. You support English, Hindi, and Hinglish naturally.",
        base64Image: String? = null
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            // Secret not configured in secrets panel; return null marker to use built-in engine
            return@withContext ""
        }

        try {
            val root = JSONObject()

            // System instruction
            val sysObj = JSONObject()
            val sysParts = JSONArray().put(JSONObject().put("text", systemInstruction))
            sysObj.put("parts", sysParts)
            root.put("systemInstruction", sysObj)

            // Contents
            val contentsArr = JSONArray()
            val contentObj = JSONObject()
            val partsArr = JSONArray()

            partsArr.put(JSONObject().put("text", prompt))

            if (!base64Image.isNullOrBlank()) {
                val inlineData = JSONObject()
                inlineData.put("mimeType", "image/jpeg")
                inlineData.put("data", base64Image)
                partsArr.put(JSONObject().put("inlineData", inlineData))
            }

            contentObj.put("parts", partsArr)
            contentsArr.put(contentObj)
            root.put("contents", contentsArr)

            // Generation config
            val config = JSONObject()
            config.put("temperature", 0.7)
            config.put("maxOutputTokens", 2048)
            root.put("generationConfig", config)

            val mediaType = "application/json; charset=utf-8".toMediaType()
            val body = root.toString().toRequestBody(mediaType)

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(body)
                .build()

            val response = client.newCall(request).execute()
            if (!response.isSuccessful) {
                return@withContext ""
            }

            val responseBody = response.body?.string() ?: return@withContext ""
            val jsonResponse = JSONObject(responseBody)
            val candidates = jsonResponse.optJSONArray("candidates") ?: return@withContext ""
            if (candidates.length() == 0) return@withContext ""

            val firstCandidate = candidates.getJSONObject(0)
            val content = firstCandidate.optJSONObject("content") ?: return@withContext ""
            val parts = content.optJSONArray("parts") ?: return@withContext ""
            if (parts.length() == 0) return@withContext ""

            parts.getJSONObject(0).optString("text", "")
        } catch (e: Exception) {
            e.printStackTrace()
            ""
        }
    }
}
