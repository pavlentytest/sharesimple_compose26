package com.example.myapplication.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
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
import com.example.myapplication.domain.usecase.ShareMultipleNotesUseCase
import com.example.myapplication.presentation.model.ShareUiState
import com.example.myapplication.presentation.modelview.ShareViewModel
import androidx.compose.runtime.*

@Composable
fun ShareMultipleNotesScreen() {
    val context = LocalContext.current


    val repository = remember { ShareRepositoryImpl(context) }
    val useCase = remember { ShareMultipleNotesUseCase(repository, context) }

    val viewModel: ShareViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ShareViewModel(useCase) as T
            }
        }
    )

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val notes = remember {
        mutableStateListOf(
            Note("Заметка 1: Первая важная мысль...", "note1.txt"),
            Note("Заметка 2: Вторая важная мысль...", "note2.txt"),
            Note("Заметка 3: Третья важная мысль...", "note3.txt")
        )
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Text(
                text = "Поделиться заметками",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = "Выбрано заметок: ${notes.size}",
                style = MaterialTheme.typography.titleMedium
            )

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    notes.forEach { note ->
                        Text(
                            text = "• ${note.title}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Button(
                onClick = { viewModel.shareMultipleNotes(notes.toList()) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                enabled = state !is ShareUiState.Sharing
            ) {
                if (state is ShareUiState.Sharing) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                } else {
                    Text("Поделиться ${notes.size} заметками")
                }
            }

            when (val s = state) {
                is ShareUiState.Success -> {
                    Text(s.message, color = MaterialTheme.colorScheme.primary)
                }
                is ShareUiState.Error -> {
                    Text(s.message, color = MaterialTheme.colorScheme.error)
                }
                else -> {}
            }
        }
    }
}