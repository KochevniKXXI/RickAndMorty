package ru.nomad.rickandmorty.core.data.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import ru.nomad.rickandmorty.core.data.model.asEntity
import ru.nomad.rickandmorty.core.database.dao.CharacterDao
import ru.nomad.rickandmorty.core.database.model.CharacterEntity
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Status
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import ru.nomad.rickandmorty.core.network.model.NetworkCharacter

@OptIn(ExperimentalPagingApi::class)
class CharactersRemoteMediator(
    private val nameFilter: String? = null,
    private val statusFilter: Status? = null,
    private val speciesFilter: String? = null,
    private val typeFilter: String? = null,
    private val genderFilter: Gender? = null,
    private val characterDao: CharacterDao,
    private val ramNetworkDataSource: RamNetworkDataSource,
) : RemoteMediator<Int, CharacterEntity>() {
    private var currentPage = 0

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        return try {
            val page = when (loadType) {
                LoadType.REFRESH -> {
                    currentPage = 1
                    currentPage
                }

                LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)

                LoadType.APPEND -> {
                    val lastItem = state.lastItemOrNull()
                    if (lastItem == null) {
                        return MediatorResult.Success(endOfPaginationReached = true)
                    }
                    state.pages
                    currentPage++
                }
            }

            val response = ramNetworkDataSource.getCharacters(
                page = page,
                nameFilter = nameFilter,
                statusFilter = statusFilter?.name?.lowercase(),
                speciesFilter = speciesFilter,
                typeFilter = typeFilter,
                genderFilter = genderFilter?.name?.lowercase(),
            )

            if (loadType == LoadType.REFRESH) {
                characterDao.refreshCharacterEntities(
                    nameFilter = nameFilter ?: "",
                    statusFilter = statusFilter,
                    speciesFilter = speciesFilter ?: "",
//                typeFilter = typeFilter ?: "",
                    genderFilter = genderFilter,
                    characterEntities = response.results.map(NetworkCharacter::asEntity)
                )
            } else {
                characterDao.upsertCharacterEntities(
                    characterEntities = response.results.map(NetworkCharacter::asEntity)
                )
            }

            MediatorResult.Success(
                endOfPaginationReached = response.info.next == null
            )
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}