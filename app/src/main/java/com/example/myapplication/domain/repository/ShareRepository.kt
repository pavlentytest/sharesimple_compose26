package com.example.myapplication.domain.repository

import android.net.Uri
import com.example.myapplication.domain.model.Note

interface ShareRepository {
    fun createShareableUri(note: Note): Uri?
}