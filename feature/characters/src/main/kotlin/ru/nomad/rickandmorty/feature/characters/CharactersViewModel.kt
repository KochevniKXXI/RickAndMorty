package ru.nomad.rickandmorty.feature.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import ru.nomad.rickandmorty.core.data.repository.CharactersRepository
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Status
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow<PagingData<Character>>(PagingData.empty())
    val uiState get() = _uiState.asStateFlow()

    private val _statusFilter = MutableStateFlow<Status?>(null)
    val statusFilter get() = _statusFilter.asStateFlow()

    private val _genderFilter = MutableStateFlow<Gender?>(null)
    val genderFilter get() = _genderFilter.asStateFlow()

    suspend fun loadCharacters(
        nameFilter: String? = null,
        statusFilter: Status? = null,
        speciesFilter: String? = null,
        typeFilter: String? = null,
        genderFilter: Gender? = null
    ) {
        charactersRepository.getCharacters(
            nameFilter,
            statusFilter,
            speciesFilter,
            typeFilter,
            genderFilter
        ).distinctUntilChanged()
            .cachedIn(viewModelScope)
            .collect { characters ->
                _uiState.value = characters
            }
    }

    fun applyFilters(
        statusFilter: Status?,
        genderFilter: Gender?
    ) {
        _statusFilter.value = statusFilter
        _genderFilter.value = genderFilter
    }
}