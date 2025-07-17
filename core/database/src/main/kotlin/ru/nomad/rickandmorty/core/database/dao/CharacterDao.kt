package ru.nomad.rickandmorty.core.database.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import ru.nomad.rickandmorty.core.database.model.CharacterEntity
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Status

@Dao
abstract class CharacterDao {
    @Upsert
    abstract suspend fun upsertCharacterEntities(characterEntities: List<CharacterEntity>)

    @Query(
        """
        SELECT * FROM characters WHERE
        name LIKE '%' || :nameFilter || '%' AND
        (:statusFilter IS NULL OR status = :statusFilter) AND
        species LIKE :speciesFilter || '%' AND
        (:genderFilter IS NULL OR gender = :genderFilter)
        """
    )
    abstract fun getCharacterEntitiesAsPagingSource(
        nameFilter: String = "",
        statusFilter: Status? = null,
        speciesFilter: String = "",
//        typeFilter: String = "",
        genderFilter: Gender? = null
    ): PagingSource<Int, CharacterEntity>

    @Query(
        """
        DELETE FROM characters WHERE
        name LIKE '%' || :nameFilter || '%' AND
        (:statusFilter IS NULL OR status = :statusFilter) AND
        species LIKE :speciesFilter || '%' AND
        (:genderFilter IS NULL OR gender = :genderFilter)
        """
    )
    internal abstract suspend fun deleteByFilters(
        nameFilter: String = "",
        statusFilter: Status? = null,
        speciesFilter: String = "",
//        typeFilter: String = "",
        genderFilter: Gender? = null
    )

    @Transaction
    open suspend fun refreshCharacterEntities(
        characterEntities: List<CharacterEntity>,
        nameFilter: String = "",
        statusFilter: Status? = null,
        speciesFilter: String = "",
//        typeFilter: String = "",
        genderFilter: Gender? = null
    ) {
        deleteByFilters(
            nameFilter = nameFilter,
            statusFilter = statusFilter,
            speciesFilter = speciesFilter,
//            typeFilter = typeFilter,
            genderFilter = genderFilter
        )
        upsertCharacterEntities(characterEntities)
    }

    @Query("SELECT * FROM characters WHERE id = :id")
    abstract suspend fun getCharacterEntityById(id: Int): CharacterEntity
}