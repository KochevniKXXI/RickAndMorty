package ru.nomad.rickandmorty.feature.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.stateIn
import ru.nomad.rickandmorty.core.data.repository.CharactersRepository
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    charactersRepository: CharactersRepository
) : ViewModel() {
    val uiState = charactersRepository.getCharacters()
        .distinctUntilChanged()
        .cachedIn(viewModelScope)
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = PagingData.empty()
        )
}