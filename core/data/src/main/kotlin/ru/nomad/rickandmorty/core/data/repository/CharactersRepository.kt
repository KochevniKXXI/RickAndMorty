package ru.nomad.rickandmorty.core.data.repository

import ru.nomad.rickandmorty.core.model.Character

interface CharactersRepository {
    suspend fun getCharacters(): List<Character>
}