package ru.nomad.rickandmorty.core.network

import ru.nomad.rickandmorty.core.network.model.NetworkCharacter

interface RamNetworkDataSource {
    suspend fun getCharacters(): List<NetworkCharacter>
}