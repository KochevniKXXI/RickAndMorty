package ru.nomad.rickandmorty.core.model

data class Character(
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val image: String,
)