package ru.nomad.rickandmorty.core.network

import ru.nomad.rickandmorty.core.network.model.NetworkCharacter
import ru.nomad.rickandmorty.core.network.retrofit.NetworkResponse

interface RamNetworkDataSource {
    suspend fun getCharacters(
        page: Int,
        nameFilter: String? = null,
        statusFilter: String? = null,
        speciesFilter: String? = null,
        typeFilter: String? = null,
        genderFilter: String? = null
    ): NetworkResponse<List<NetworkCharacter>>

    suspend fun getCharacter(id: Int): NetworkCharacter
}