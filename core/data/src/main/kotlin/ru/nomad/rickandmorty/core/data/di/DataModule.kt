package ru.nomad.rickandmorty.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nomad.rickandmorty.core.data.repository.CharactersRepository
import ru.nomad.rickandmorty.core.data.repository.NetworkCharactersRepository

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsCharactersRepository(charactersRepository: NetworkCharactersRepository): CharactersRepository
}