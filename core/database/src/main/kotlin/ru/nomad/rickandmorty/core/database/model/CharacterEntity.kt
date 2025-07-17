package ru.nomad.rickandmorty.core.database.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Species
import ru.nomad.rickandmorty.core.model.Status

@Entity(tableName = "characters")
data class CharacterEntity(
    @PrimaryKey
    val id: Int,
    val name: String,
    val status: Status,
    val species: Species,
    val type: String,
    val gender: Gender,
    val image: String,
)
