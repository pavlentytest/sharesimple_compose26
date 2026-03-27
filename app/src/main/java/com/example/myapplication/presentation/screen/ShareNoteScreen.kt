package com.example.myapplication.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.repository.ShareRepositoryImpl
import com.example.myapplication.domain.model.Note
import com.example.myapplication.domain.usecase.ShareNoteUseCase
import com.example.myapplication.presentation.model.ShareUiState
import com.example.myapplication.presentation.modelview.ShareViewModel

@Composable
fun ShareNoteScreen() {
    val context = LocalContext.current

    val repository = remember { ShareRepositoryImpl(context) }
    val useCase = remember { ShareNoteUseCase(repository, context) }

    val viewModel: ShareViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ShareViewModel(useCase) as T
            }
        }
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    var noteContent by remember { mutableStateOf("Текст заметки для шаринга...") }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Шаринг файла", style = MaterialTheme.typography.headlineMedium)

            OutlinedTextField(
                value = noteContent,
                onValueChange = { noteContent = it },
                modifier = Modifier.fillMaxWidth()
            )

            Button(
                onClick = { viewModel.shareNote(Note(noteContent)) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Поделиться через Share Sheet")
            }

            when (val s = state) {
                is ShareUiState.Sharing -> CircularProgressIndicator()
                is ShareUiState.Success -> Text(
                    s.message,
                    color = MaterialTheme.colorScheme.primary
                )

                is ShareUiState.Error -> Text(s.message, color = MaterialTheme.colorScheme.error)
                else -> {}
            }
        }
    }
}