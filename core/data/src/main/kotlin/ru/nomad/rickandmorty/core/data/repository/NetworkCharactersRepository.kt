package ru.nomad.rickandmorty.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.nomad.rickandmorty.core.data.paging.CharactersPagingSource
import ru.nomad.rickandmorty.core.model.Character
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NetworkCharactersRepository @Inject constructor(
    private val charactersPagingSource: CharactersPagingSource
) : CharactersRepository {
    override fun getCharacters(): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = { charactersPagingSource }
        ).flow
    }
}