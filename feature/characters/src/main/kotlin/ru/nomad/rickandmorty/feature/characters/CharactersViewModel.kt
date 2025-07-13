package ru.nomad.rickandmorty.feature.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nomad.rickandmorty.core.data.repository.CharactersRepository
import ru.nomad.rickandmorty.core.model.Character
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<CharactersUiState>(CharactersUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        getCharacters()
    }

    fun getCharacters() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = CharactersUiState.Loading
            try {
                val characters = charactersRepository.getCharacters()
                _uiState.value = if (characters.isNotEmpty()) {
                    CharactersUiState.Success(characters)
                } else {
                    CharactersUiState.Empty
                }
            } catch (e: Exception) {
                _uiState.value = CharactersUiState.Error(e)
            }
        }
    }
}

sealed interface CharactersUiState {
    data object Loading : CharactersUiState
    data class Error(val cause: Throwable) : CharactersUiState
    data object Empty : CharactersUiState
    data class Success(val characters: List<Character>) : CharactersUiState
}