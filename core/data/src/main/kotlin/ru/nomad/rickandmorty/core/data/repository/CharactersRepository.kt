package ru.nomad.rickandmorty.core.data.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.nomad.rickandmorty.core.model.Character

interface CharactersRepository {
    fun getCharacters(): Flow<PagingData<Character>>
    suspend fun getCharacter(id: Int): Character
}