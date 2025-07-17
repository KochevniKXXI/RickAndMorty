package ru.nomad.rickandmorty.feature.characters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.LoadStates
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.nomad.rickandmorty.core.data.repository.CharactersRepository
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Species
import ru.nomad.rickandmorty.core.model.Status
import javax.inject.Inject

@HiltViewModel
class CharactersViewModel @Inject constructor(
    private val charactersRepository: CharactersRepository
) : ViewModel() {
    private var _uiState = MutableStateFlow<PagingData<Character>>(
        PagingData.empty(
            LoadStates(
                LoadState.Loading,
                LoadState.NotLoading(false),
                LoadState.NotLoading(false)
            )
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _statusFilter = MutableStateFlow<Status?>(null)
    val statusFilter = _statusFilter.asStateFlow()

    private val _speciesFilter = MutableStateFlow<Species?>(null)
    val speciesFilter = _speciesFilter.asStateFlow()

    private val _typeFilter = MutableStateFlow<String?>(null)
    val typeFilter = _typeFilter.asStateFlow()

    private val _genderFilter = MutableStateFlow<Gender?>(null)
    val genderFilter = _genderFilter.asStateFlow()

    suspend fun loadCharacters(
        nameFilter: String? = null,
        statusFilter: Status? = null,
        speciesFilter: Species? = null,
        typeFilter: String? = null,
        genderFilter: Gender? = null
    ) {
        charactersRepository.getCharacters(
            nameFilter,
            statusFilter,
            speciesFilter,
            typeFilter,
            genderFilter
        ).cachedIn(viewModelScope)
            .collect { pagingData ->
                _uiState.value = pagingData
            }
    }

    fun applyFilters(
        statusFilter: Status?,
        speciesFilter: Species?,
        typeFilter: String?,
        genderFilter: Gender?,
    ) {
        _statusFilter.value = statusFilter
        _speciesFilter.value = speciesFilter
        _typeFilter.value = typeFilter
        _genderFilter.value = genderFilter
    }
}