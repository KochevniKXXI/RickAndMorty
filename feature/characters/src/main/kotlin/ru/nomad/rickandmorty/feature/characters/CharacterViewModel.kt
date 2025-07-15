package ru.nomad.rickandmorty.feature.characters

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import ru.nomad.rickandmorty.core.data.repository.CharactersRepository
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.feature.characters.navigation.CharacterRoute
import javax.inject.Inject

@HiltViewModel
class CharacterViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val charactersRepository: CharactersRepository,
) : ViewModel() {
    private val characterId = savedStateHandle.toRoute<CharacterRoute>().id

    private val _uiState = MutableStateFlow<CharacterUiState>(CharacterUiState.Loading)
    val uiState: StateFlow<CharacterUiState>
        get() = _uiState.asStateFlow()

    init {
        loadCharacter()
    }

    fun loadCharacter() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = CharacterUiState.Loading
            _uiState.value = try {
                val character = charactersRepository.getCharacter(characterId)
                CharacterUiState.Success(character)
            } catch (e: Exception) {
                CharacterUiState.Error(e)
            }
        }
    }
}

sealed interface CharacterUiState {
    data object Loading : CharacterUiState
    data class Error(val cause: Throwable) : CharacterUiState
    data class Success(val character: Character) : CharacterUiState
}