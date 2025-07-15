package ru.nomad.rickandmorty.feature.characters.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import kotlinx.serialization.Serializable
import ru.nomad.rickandmorty.feature.characters.CharacterScreen
import ru.nomad.rickandmorty.feature.characters.CharactersScreen

@Serializable
data object CharactersGraphRoute

@Serializable
private data object CharactersRoute

@Serializable
internal data class CharacterRoute(val id: Int)

fun NavController.navigateToCharacter(
    id: Int,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) = navigate(CharacterRoute(id)) { navOptions() }

fun NavGraphBuilder.charactersSection(
    searchQuery: String,
    showFilters: Boolean,
    onFilterSheetHide: (Boolean) -> Unit,
    onCharacterClick: (id: Int) -> Unit,
) {
    navigation<CharactersGraphRoute>(startDestination = CharactersRoute) {
        composable<CharactersRoute> {
            CharactersScreen(
                searchQuery = searchQuery,
                showFilters = showFilters,
                onFilterSheetHide = onFilterSheetHide,
                onCharacterClick = onCharacterClick
            )
        }

        composable<CharacterRoute> {
            CharacterScreen()
        }
    }
}