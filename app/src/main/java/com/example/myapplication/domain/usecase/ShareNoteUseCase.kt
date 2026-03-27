package com.example.myapplication.domain.usecase

import android.content.Context
import android.content.Intent
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.repository.ShareRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ShareNoteUseCase(
    private val repository: ShareRepository,
    private val context: Context
) {
    suspend operator fun invoke(note: Note): Result<String> = withContext(Dispatchers.IO) {
        val uri = repository.createShareableUri(note)
            ?: return@withContext Result.failure(Exception("Не удалось создать файл"))

        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, note.title)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(Intent.createChooser(intent, "Поделиться файлом"))
            Result.success("Меню шаринга открыто")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}