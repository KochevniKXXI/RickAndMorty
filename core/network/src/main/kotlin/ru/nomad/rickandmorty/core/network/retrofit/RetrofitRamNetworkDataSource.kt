package ru.nomad.rickandmorty.core.network.retrofit

import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import ru.nomad.rickandmorty.core.network.model.NetworkCharacter
import javax.inject.Inject
import javax.inject.Singleton

internal interface RetrofitRamNetworkApi {
    @GET("character")
    suspend fun getCharacters(
        @Query("page") page: Int,
        @Query("name") nameFilter: String? = null,
    ): NetworkResponse<List<NetworkCharacter>>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int): NetworkCharacter
}

@Serializable
@OptIn(InternalSerializationApi::class)
internal data class NetworkResponse<T>(
    val info: PageInfo,
    val results: T,
)

@Serializable
@OptIn(InternalSerializationApi::class)
internal data class PageInfo(
    val count: Int,
    val pages: Int,
    val next: String?,
    val prev: String?,
)

@Singleton
internal class RetrofitRamNetworkDataSource @Inject constructor(
    private val ramApi: RetrofitRamNetworkApi
) : RamNetworkDataSource {
    override suspend fun getCharacters(
        page: Int,
        nameFilter: String?,
    ): List<NetworkCharacter> =
        ramApi.getCharacters(page, nameFilter).results

    override suspend fun getCharacter(id: Int): NetworkCharacter = ramApi.getCharacter(id)
}