package ru.nomad.rickandmorty.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import ru.nomad.rickandmorty.feature.characters.navigation.CharactersGraphRoute
import ru.nomad.rickandmorty.feature.characters.navigation.charactersSection
import ru.nomad.rickandmorty.feature.characters.navigation.navigateToCharacter
import ru.nomad.rickandmorty.ui.RamAppState

@Composable
fun RamNavHost(
    appState: RamAppState,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = CharactersGraphRoute,
        modifier = modifier
    ) {
        charactersSection(
            searchQuery = searchQuery,
            onCharacterClick = navController::navigateToCharacter
        )
    }
}