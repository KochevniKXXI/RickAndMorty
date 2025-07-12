package ru.nomad.rickandmorty.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import ru.nomad.rickandmorty.core.network.RamNetworkDataSource
import ru.nomad.rickandmorty.core.network.retrofit.RetrofitRamNetworkApi
import ru.nomad.rickandmorty.core.network.retrofit.RetrofitRamNetworkDataSource
import javax.inject.Singleton

private const val RAM_BASE_URL = "https://rickandmortyapi.com/api/"

@Module
@InstallIn(SingletonComponent::class)
internal interface NetworkModule {

    @Provides
    @Singleton
    fun providesNetworkJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun providesRamApi(
        json: Json
    ): RetrofitRamNetworkApi = Retrofit.Builder()
        .baseUrl(RAM_BASE_URL)
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .build()
        .create(RetrofitRamNetworkApi::class.java)

    @Binds
    fun binds(impl: RetrofitRamNetworkDataSource): RamNetworkDataSource
}