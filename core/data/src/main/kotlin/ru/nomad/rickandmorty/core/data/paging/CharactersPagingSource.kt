package ru.nomad.rickandmorty.core.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import ru.nomad.rickandmorty.core.data.model.asExternalModel
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import ru.nomad.rickandmorty.core.network.model.NetworkCharacter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class CharactersPagingSource @Inject constructor(
    private val ramNetworkDataSource: RamNetworkDataSource
) : PagingSource<Int, Character>() {
    override fun getRefreshKey(state: PagingState<Int, Character>): Int? = state.anchorPosition

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        return try {
            val currentPage = params.key ?: 1
            val characters = ramNetworkDataSource.getCharacters(currentPage)
                .map(NetworkCharacter::asExternalModel)
            LoadResult.Page(
                data = characters,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (characters.isEmpty()) null else currentPage + 1
            )
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }
}