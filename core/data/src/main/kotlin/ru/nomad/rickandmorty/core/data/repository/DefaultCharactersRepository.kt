package ru.nomad.rickandmorty.core.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.nomad.rickandmorty.core.data.model.asExternalModel
import ru.nomad.rickandmorty.core.data.paging.CharactersRemoteMediator
import ru.nomad.rickandmorty.core.database.dao.CharacterDao
import ru.nomad.rickandmorty.core.database.model.CharacterEntity
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Species
import ru.nomad.rickandmorty.core.model.Status
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class DefaultCharactersRepository @Inject constructor(
    private val networkDataSource: RamNetworkDataSource,
    private val characterDao: CharacterDao,
) : CharactersRepository {

    @OptIn(ExperimentalPagingApi::class)
    override fun getCharacters(
        nameFilter: String?,
        statusFilter: Status?,
        speciesFilter: Species?,
        typeFilter: String?,
        genderFilter: Gender?
    ): Flow<PagingData<Character>> {
        return Pager(
            config = PagingConfig(20),
            remoteMediator = CharactersRemoteMediator(
                nameFilter = nameFilter,
                statusFilter = statusFilter,
                speciesFilter = speciesFilter,
                typeFilter = typeFilter,
                genderFilter = genderFilter,
                ramNetworkDataSource = networkDataSource,
                characterDao = characterDao
            )
        ) {
            characterDao.getCharacterEntitiesAsPagingSource(
                nameFilter = nameFilter ?: "",
                statusFilter = statusFilter,
                speciesFilter = speciesFilter,
                typeFilter = typeFilter,
                genderFilter = genderFilter
            )
        }.flow.map { pagingData ->
            pagingData.map(CharacterEntity::asExternalModel)
        }
    }

    override suspend fun getCharacter(id: Int): Character =
        characterDao.getCharacterEntityById(id).asExternalModel()
}