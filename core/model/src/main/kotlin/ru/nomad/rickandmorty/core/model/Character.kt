package ru.nomad.rickandmorty.core.model

data class Character(
    val id: Int,
    val name: String,
    val status: Status,
    val species: String,
    val gender: Gender,
    val image: String,
)

enum class Status {
    ALIVE,
    DEAD,
    UNKNOWN,
}

enum class Gender {
    MALE,
    FEMALE,
    GENDERLESS,
    UNKNOWN,
}