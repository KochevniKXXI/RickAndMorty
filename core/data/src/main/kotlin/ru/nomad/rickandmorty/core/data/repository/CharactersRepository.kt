package ru.nomad.rickandmorty.core.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Species
import ru.nomad.rickandmorty.core.model.Status

interface CharactersRepository {
    fun getCharacters(
        nameFilter: String? = null,
        statusFilter: Status? = null,
        speciesFilter: Species? = null,
        typeFilter: String? = null,
        genderFilter: Gender? = null
    ): Flow<PagingData<Character>>

    suspend fun getCharacter(id: Int): Character
}