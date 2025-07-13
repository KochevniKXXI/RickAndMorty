package ru.nomad.rickandmorty.core.network.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import ru.nomad.rickandmorty.core.network.retrofit.RetrofitRamNetworkDataSource

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkBindsModule {
    @Binds
    fun bindsNetworkDataSource(networkDataSource: RetrofitRamNetworkDataSource): RamNetworkDataSource
}