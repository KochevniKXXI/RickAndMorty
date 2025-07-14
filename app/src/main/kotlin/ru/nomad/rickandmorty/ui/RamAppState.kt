package ru.nomad.rickandmorty.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController

@Composable
fun rememberRamAppState(
    navController: NavHostController = rememberNavController(),
): RamAppState {
    return remember(
        navController
    ) {
        RamAppState(
            navController = navController
        )
    }
}

@Stable
class RamAppState(
    val navController: NavHostController
) {
    private val previousDestination = mutableStateOf<NavDestination?>(null)

    private val currentDestination: NavDestination?
        @Composable get() {
            val currentEntry = navController.currentBackStackEntryFlow
                .collectAsState(initial = null)

            return currentEntry.value?.destination.also { destination ->
                if (destination != null) {
                    previousDestination.value = destination
                }
            } ?: previousDestination.value
        }

    val currentDestinationIsGraph: Boolean
        @Composable get() = currentDestination?.route == currentDestination?.parent?.startDestinationRoute
}