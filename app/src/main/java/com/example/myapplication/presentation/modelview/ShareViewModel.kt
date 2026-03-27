package com.example.myapplication.presentation.modelview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.usecase.ShareNoteUseCase
import com.example.myapplication.presentation.model.ShareUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShareViewModel(
    private val shareNoteUseCase: ShareNoteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ShareUiState>(ShareUiState.Idle)
    val uiState: StateFlow<ShareUiState> = _uiState.asStateFlow()

    fun shareNote(note: Note) {
        viewModelScope.launch {
            _uiState.value = ShareUiState.Sharing

            val result = shareNoteUseCase(note)

            _uiState.value = if (result.isSuccess) {
                ShareUiState.Success(result.getOrNull() ?: "Успешно")
            } else {
                ShareUiState.Error(result.exceptionOrNull()?.message ?: "Ошибка")
            }
        }
    }
}