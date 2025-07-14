package ru.nomad.rickandmorty.core.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import ru.nomad.rickandmorty.core.data.model.asExternalModel
import ru.nomad.rickandmorty.core.data.paging.CharactersPagingSource
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NetworkCharactersRepository @Inject constructor(
    private val networkDataSource: RamNetworkDataSource,
) : CharactersRepository {
    override fun getCharacters(
        nameFilter: String?
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(20),
            pagingSourceFactory = {
                CharactersPagingSource(
                    ramNetworkDataSource = networkDataSource,
                    nameFilter = nameFilter
                )
            }
        ).flow
    }

    override suspend fun getCharacter(id: Int): Character =
        networkDataSource.getCharacter(id).asExternalModel()
}