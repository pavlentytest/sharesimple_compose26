package com.example.myapplication.domain.model

data class Note(
    val content: String,
    val title: String = "note.txt"
)