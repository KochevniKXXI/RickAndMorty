package ru.nomad.rickandmorty.core.data.repository

import ru.nomad.rickandmorty.core.data.model.asExternalModel
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import ru.nomad.rickandmorty.core.network.model.NetworkCharacter
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class NetworkCharactersRepository @Inject constructor(
    private val networkDataSource: RamNetworkDataSource
) : CharactersRepository {
    override suspend fun getCharacters(): List<Character> =
        networkDataSource.getCharacters().map(NetworkCharacter::asExternalModel)
}