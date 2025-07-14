package ru.nomad.rickandmorty.core.network

import ru.nomad.rickandmorty.core.network.model.NetworkCharacter

interface RamNetworkDataSource {
    suspend fun getCharacters(page: Int): List<NetworkCharacter>
    suspend fun getCharacter(id: Int): NetworkCharacter
}