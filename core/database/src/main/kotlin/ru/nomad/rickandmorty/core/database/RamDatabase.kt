package ru.nomad.rickandmorty.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import ru.nomad.rickandmorty.core.database.dao.CharacterDao
import ru.nomad.rickandmorty.core.database.model.CharacterEntity

@Database(
    entities = [
        CharacterEntity::class
    ],
    version = 1,
    exportSchema = false
)
internal abstract class RamDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}