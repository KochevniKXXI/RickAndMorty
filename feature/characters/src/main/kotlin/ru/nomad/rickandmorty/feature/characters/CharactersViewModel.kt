package ru.nomad.rickandmorty.feature.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import ru.nomad.rickandmorty.core.data.repository.CharactersRepository
import ru.nomad.rickandmorty.core.model.Character
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PagingData<Character>>(PagingData.empty())
    val uiState get() = _uiState.asStateFlow()

    fun fetchCharacters(
        nameFilter: String? = null
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            charactersRepository.getCharacters(nameFilter)
                .distinctUntilChanged()
                .cachedIn(viewModelScope)
                .collect { characters ->
                    _uiState.value = characters
                }
        }
    }
}