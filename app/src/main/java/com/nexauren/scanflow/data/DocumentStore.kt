package com.nexauren.scanflow.data

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.compose.runtime.mutableStateListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
import java.util.UUID

class DocumentStore(context: Context) {
    private val appContext = context.applicationContext
    private val preferences = appContext.getSharedPreferences("scanflow_local_store", Context.MODE_PRIVATE)
    val documents = mutableStateListOf<ScanDocument>()

    fun load() {
        val stored = preferences.getString(KEY_DOCUMENTS, "[]") ?: "[]"
        val parsed = runCatching {
            val json = JSONArray(stored)
            buildList {
                for (index in 0 until json.length()) {
                    val item = json.getJSONObject(index)
                    val document = ScanDocument(
                        id = item.optString("id"),
                        name = item.optString("name"),
                        path = item.optString("path"),
                        mimeType = item.optString("mimeType", "application/octet-stream"),
                        size = item.optLong("size"),
                        createdAt = item.optLong("createdAt"),
                        folder = item.optString("folder", "Imported"),
                        favorite = item.optBoolean("favorite")
                    )
                    if (document.id.isNotBlank() && File(document.path).exists()) add(document)
                }
            }
        }.getOrDefault(emptyList())

        documents.clear()
        documents.addAll(parsed.sortedByDescending { it.createdAt })
    }

    suspend fun importUri(uri: Uri): ScanDocument? = withContext(Dispatchers.IO) {
        runCatching {
            val resolver = appContext.contentResolver
            var displayName = "Document"
            resolver.query(uri, arrayOf(OpenableColumns.DISPLAY_NAME), null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val column = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (column >= 0) displayName = cursor.getString(column) ?: displayName
                }
            }

            val mimeType = resolver.getType(uri) ?: "application/octet-stream"
            val originalExtension = displayName.substringAfterLast('.', "")
            val extension = originalExtension.ifBlank {
                when (mimeType) {
                    "application/pdf" -> "pdf"
                    "image/png" -> "png"
                    "image/jpeg" -> "jpg"
                    "text/plain" -> "txt"
                    else -> "bin"
                }
            }.lowercase()

            val safeName = displayName.substringAfterLast('/').replace(Regex("[^A-Za-z0-9._ -]"), "_")
            val destinationFolder = File(appContext.filesDir, "Documents/Imported")
            destinationFolder.mkdirs()
            val destination = File(
                destinationFolder,
                System.currentTimeMillis().toString() + "_" + UUID.randomUUID().toString().take(8) + "." + extension
            )

            val input = resolver.openInputStream(uri) ?: return@withContext null
            input.use { source ->
                destination.outputStream().use { output -> source.copyTo(output) }
            }

            ScanDocument(
                id = UUID.randomUUID().toString(),
                name = safeName,
                path = destination.absolutePath,
                mimeType = mimeType,
                size = destination.length(),
                createdAt = System.currentTimeMillis(),
                folder = "Imported"
            )
        }.getOrNull()
    }

    fun addCapturedFile(file: File): ScanDocument {
        val document = ScanDocument(
            id = UUID.randomUUID().toString(),
            name = "Scan_" + System.currentTimeMillis() + ".jpg",
            path = file.absolutePath,
            mimeType = "image/jpeg",
            size = file.length(),
            createdAt = System.currentTimeMillis(),
            folder = "Scanned"
        )
        insert(document)
        return document
    }

    fun insert(document: ScanDocument) {
        documents.removeAll { it.id == document.id }
        documents.add(0, document)
        persist()
    }

    fun toggleFavorite(document: ScanDocument) {
        val index = documents.indexOfFirst { it.id == document.id }
        if (index >= 0) {
            documents[index] = documents[index].copy(favorite = !documents[index].favorite)
            persist()
        }
    }

    fun delete(document: ScanDocument) {
        runCatching { File(document.path).delete() }
        documents.removeAll { it.id == document.id }
        persist()
    }

    fun isDarkTheme(): Boolean = preferences.getBoolean(KEY_DARK, true)

    fun setDarkTheme(enabled: Boolean) {
        preferences.edit().putBoolean(KEY_DARK, enabled).apply()
    }

    fun language(): String = preferences.getString(KEY_LANGUAGE, "pt") ?: "pt"

    fun setLanguage(language: String) {
        preferences.edit().putString(KEY_LANGUAGE, language).apply()
    }

    private fun persist() {
        val json = JSONArray()
        documents.forEach { document ->
            json.put(
                JSONObject()
                    .put("id", document.id)
                    .put("name", document.name)
                    .put("path", document.path)
                    .put("mimeType", document.mimeType)
                    .put("size", document.size)
                    .put("createdAt", document.createdAt)
                    .put("folder", document.folder)
                    .put("favorite", document.favorite)
            )
        }
        preferences.edit().putString(KEY_DOCUMENTS, json.toString()).apply()
    }

    private companion object {
        const val KEY_DOCUMENTS = "documents"
        const val KEY_DARK = "dark_theme"
        const val KEY_LANGUAGE = "language"
    }
}
