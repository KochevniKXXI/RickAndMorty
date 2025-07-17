package ru.nomad.rickandmorty.core.database.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nomad.rickandmorty.core.database.RamDatabase
import ru.nomad.rickandmorty.core.database.dao.CharacterDao

@Module
@InstallIn(SingletonComponent::class)
internal object DaosModule {
    @Provides
    fun providesCharactersDao(
        database: RamDatabase,
    ): CharacterDao = database.characterDao()
}