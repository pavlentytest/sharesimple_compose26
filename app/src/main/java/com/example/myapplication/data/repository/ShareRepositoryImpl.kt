package com.example.myapplication.data.repository

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.repository.ShareRepository
import java.io.File

class ShareRepositoryImpl(private val context: Context) : ShareRepository {

    override fun createShareableUri(note: Note): Uri? {
        return try {
            val file = File(context.getExternalFilesDir(null), note.title)
            file.writeText(note.content)

            FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}