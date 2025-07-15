package ru.nomad.rickandmorty.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.nomad.rickandmorty.core.data.model.asExternalModel
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Status
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import ru.nomad.rickandmorty.core.network.model.NetworkCharacter

internal class CharactersPagingSource(
    private val ramNetworkDataSource: RamNetworkDataSource,
    private val nameFilter: String? = null,
    private val statusFilter: Status? = null,
    private val speciesFilter: String? = null,
    private val typeFilter: String? = null,
    private val genderFilter: Gender? = null,
) : PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val currentPage = params.key ?: 1
            val response = ramNetworkDataSource.getCharacters(
                page = currentPage,
                nameFilter = nameFilter,
                statusFilter = statusFilter?.name?.lowercase(),
                speciesFilter = speciesFilter,
                typeFilter = typeFilter,
                genderFilter = genderFilter?.name?.lowercase()
            )
            val characters = response.results.map(NetworkCharacter::asExternalModel)
            LoadResult.Page(
                data = characters,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (characters.isEmpty() || currentPage == response.info.pages) null else currentPage + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}