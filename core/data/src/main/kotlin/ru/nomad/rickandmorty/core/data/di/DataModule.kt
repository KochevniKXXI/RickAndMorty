package ru.nomad.rickandmorty.core.data.di

import androidx.paging.PagingSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nomad.rickandmorty.core.data.paging.CharactersPagingSource
import ru.nomad.rickandmorty.core.data.repository.CharactersRepository
import ru.nomad.rickandmorty.core.data.repository.NetworkCharactersRepository
import ru.nomad.rickandmorty.core.model.Character

@Module
@InstallIn(SingletonComponent::class)
internal interface DataModule {

    @Binds
    fun bindsCharactersPagingSource(charactersPagingSource: CharactersPagingSource): PagingSource<Int, Character>

    @Binds
    fun bindsCharactersRepository(charactersRepository: NetworkCharactersRepository): CharactersRepository
}