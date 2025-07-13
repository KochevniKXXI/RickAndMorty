package ru.nomad.rickandmorty.core.data.model

import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Status
import ru.nomad.rickandmorty.core.network.model.NetworkCharacter

fun NetworkCharacter.asExternalModel() = Character(
    id = id,
    name = name,
    status = Status.valueOf(status.uppercase()),
    species = species,
    gender = Gender.valueOf(gender.uppercase()),
    image = image,
)