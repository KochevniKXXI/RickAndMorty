package ru.nomad.rickandmorty.core.database.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.nomad.rickandmorty.core.database.RamDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {
    @Provides
    @Singleton
    fun providesRamDatabase(
        @ApplicationContext context: Context,
    ): RamDatabase = Room.databaseBuilder(
        context,
        RamDatabase::class.java,
        "ram-database",
    ).build()
}