package ru.nomad.rickandmorty.core.data.model

import ru.nomad.rickandmorty.core.database.model.CharacterEntity
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Species
import ru.nomad.rickandmorty.core.model.Status
import ru.nomad.rickandmorty.core.network.model.NetworkCharacter
import kotlin.text.uppercase

fun NetworkCharacter.asEntity() = CharacterEntity(
    id = id,
    name = name,
    status = Status.valueOf(status.uppercase()),
    species = Species.valueOf(species.uppercase()),
    type = type,
    gender = Gender.valueOf(gender.uppercase()),
    image = image
)

fun CharacterEntity.asExternalModel() = Character(
    id = id,
    name = name,
    status = status,
    species = species,
    type = type,
    gender = gender,
    image = image,
)