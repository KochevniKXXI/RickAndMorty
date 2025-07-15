package ru.nomad.rickandmorty.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import ru.nomad.rickandmorty.R
import ru.nomad.rickandmorty.navigation.RamNavHost

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RamApp(
    appState: RamAppState,
    modifier: Modifier = Modifier
) {
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var showFilters by rememberSaveable { mutableStateOf(false) }
    var filtersIsApplied by rememberSaveable { mutableStateOf(false) }

    Scaffold(
        topBar = {
            RamTopAppBar(
                showSearchBar = appState.currentDestinationIsGraph,
                searchQuery = searchQuery,
                onQueryChange = { searchQuery = it },
                onBackClick = appState.navController::popBackStack
            )
        },
        floatingActionButton = {
            if (appState.currentDestinationIsGraph) {
                FloatingActionButton(
                    onClick = { showFilters = true },
                    containerColor = MaterialTheme.colorScheme.inverseSurface
                ) {
                    BadgedBox(
                        badge = {
                            if (filtersIsApplied) {
                                Badge()
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FilterList,
                            contentDescription = null
                        )
                    }
                }
            }
        },
        modifier = modifier
    ) { contentPadding ->
        RamNavHost(
            appState = appState,
            searchQuery = searchQuery,
            showFilters = showFilters,
            onFilterSheetHide = {
                showFilters = false
                filtersIsApplied = it
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(contentPadding)
                .consumeWindowInsets(contentPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal,
                    ),
                )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RamTopAppBar(
    showSearchBar: Boolean,
    searchQuery: String,
    onQueryChange: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    TopAppBar(
        title = {
            if (showSearchBar) {
                var expanded by rememberSaveable { mutableStateOf(false) }

                SearchBar(
                    inputField = {
                        SearchBarDefaults.InputField(
                            query = searchQuery,
                            onQueryChange = onQueryChange,
                            expanded = expanded,
                            onSearch = {},
                            onExpandedChange = { expanded = it },
                            placeholder = {
                                Text(
                                    text = stringResource(R.string.search_characters)
                                )
                            },
                            trailingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = null
                                )
                            },
                            colors = SearchBarDefaults.inputFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                                unfocusedTextColor = MaterialTheme.colorScheme.inverseOnSurface,
                                cursorColor = MaterialTheme.colorScheme.inverseOnSurface,
                                focusedTrailingIconColor = MaterialTheme.colorScheme.inverseOnSurface
                                    .copy(alpha = 0.5f),
                                unfocusedTrailingIconColor = MaterialTheme.colorScheme.inverseOnSurface
                                    .copy(alpha = 0.5f),
                                focusedPlaceholderColor = MaterialTheme.colorScheme.inverseOnSurface
                                    .copy(alpha = 0.5f),
                                unfocusedPlaceholderColor = MaterialTheme.colorScheme.inverseOnSurface
                                    .copy(alpha = 0.5f)

                            )
                        )
                    },
                    expanded = false,
                    onExpandedChange = { expanded = it },
                    colors = SearchBarDefaults.colors(
                        containerColor = MaterialTheme.colorScheme.inverseSurface
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {}
            }
        },
        navigationIcon = {
            if (!showSearchBar) {
                IconButton(
                    onClick = onBackClick
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            titleContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            navigationIconContentColor = MaterialTheme.colorScheme.inverseOnSurface,
            actionIconContentColor = MaterialTheme.colorScheme.inverseOnSurface
        ),
        modifier = modifier
    )
}