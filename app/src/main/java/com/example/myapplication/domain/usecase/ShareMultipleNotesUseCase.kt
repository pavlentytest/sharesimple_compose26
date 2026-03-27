package com.example.myapplication.domain.usecase

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.repository.ShareRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class ShareMultipleNotesUseCase(
    private val repository: ShareRepository,
    private val context: Context
) {
    suspend operator fun invoke(notes: List<Note>): Result<String> = withContext(Dispatchers.IO) {
        if (notes.isEmpty()) {
            return@withContext Result.failure(Exception("Нет заметок для отправки"))
        }

        val uris = mutableListOf<Uri>()

        for (note in notes) {
            val uri = repository.createShareableUri(note)
            if (uri != null) {
                uris.add(uri)
            } else {
                return@withContext Result.failure(Exception("Не удалось создать файл для заметки: ${note.title}"))
            }
        }

        val intent = Intent(Intent.ACTION_SEND_MULTIPLE).apply {
            type = "*/*"                                      // или "text/plain"
            putParcelableArrayListExtra(Intent.EXTRA_STREAM, ArrayList(uris))
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }

        try {
            context.startActivity(Intent.createChooser(intent, "Поделиться заметками"))
            Result.success("Открыто меню отправки ${uris.size} файлов")
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}