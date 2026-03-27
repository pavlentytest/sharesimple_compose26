package com.example.myapplication.presentation.model

sealed interface ShareUiState {
    data object Idle : ShareUiState
    data object Sharing : ShareUiState
    data class Success(val message: String) : ShareUiState
    data class Error(val message: String) : ShareUiState
}
