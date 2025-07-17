package ru.nomad.rickandmorty.feature.characters

import androidx.compose.foundation.Image
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import kotlinx.coroutines.flow.flowOf
import ru.nomad.rickandmorty.core.designsystem.component.EmptyWidget
import ru.nomad.rickandmorty.core.designsystem.component.ErrorWidget
import ru.nomad.rickandmorty.core.designsystem.component.LoadingWidget
import ru.nomad.rickandmorty.core.designsystem.theme.RamTheme
import ru.nomad.rickandmorty.core.model.Character
import ru.nomad.rickandmorty.core.model.Gender
import ru.nomad.rickandmorty.core.model.Species
import ru.nomad.rickandmorty.core.model.Status
import ru.nomad.rickandmorty.core.designsystem.R as designsystemR

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharactersScreen(
    searchQuery: String,
    showFilters: Boolean,
    onFilterSheetHide: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    onCharacterClick: (id: Int) -> Unit,
    viewModel: CharactersViewModel = hiltViewModel(),
) {
    val uiState = viewModel.uiState.collectAsLazyPagingItems()
    val statusFilter by viewModel.statusFilter.collectAsStateWithLifecycle()
    val speciesFilter by viewModel.speciesFilter.collectAsStateWithLifecycle()
    val typeFilter by viewModel.typeFilter.collectAsStateWithLifecycle()
    val genderFilter by viewModel.genderFilter.collectAsStateWithLifecycle()

    LaunchedEffect(searchQuery, statusFilter, speciesFilter, typeFilter, genderFilter) {
        viewModel.loadCharacters(
            searchQuery.ifEmpty { null },
            statusFilter,
            speciesFilter,
            typeFilter,
            genderFilter
        )
    }

    when {
        uiState.loadState.refresh is LoadState.Loading ->
            LoadingWidget(modifier = modifier)

        uiState.itemCount == 0 ->
            EmptyWidget(modifier = modifier)

        else ->
            CharactersGrid(
                lazyPagingCharacters = uiState,
                onCharacterClick = onCharacterClick,
                modifier = modifier
            )
    }

    if (showFilters) {
        FiltersSheet(
            statusFilter = statusFilter,
            speciesFilter = speciesFilter,
            typeFilter = typeFilter,
            genderFilter = genderFilter,
            onFilterSheetHide = onFilterSheetHide,
            onFiltersApply = viewModel::applyFilters
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun FiltersSheet(
    statusFilter: Status?,
    speciesFilter: Species?,
    typeFilter: String?,
    genderFilter: Gender?,
    onFilterSheetHide: (Boolean) -> Unit,
    onFiltersApply: (Status?, Species?, String?, Gender?) -> Unit,
    modifier: Modifier = Modifier
) {
    ModalBottomSheet(
        onDismissRequest = {
            onFilterSheetHide(
                statusFilter != null || speciesFilter != null || genderFilter != null
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                text = stringResource(R.string.filters),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
            )

            Text(
                text = stringResource(R.string.status),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
            )

            val (selectedStatus, onStatusSelected) = rememberSaveable { mutableStateOf(statusFilter) }
            Column(Modifier.selectableGroup()) {
                Status.entries.forEach { status ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (status == selectedStatus),
                                onClick = { onStatusSelected(status) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = dimensionResource(designsystemR.dimen.m_space)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = status == selectedStatus,
                            onClick = null
                        )
                        Text(
                            text = stringArrayResource(R.array.status)[status.ordinal],
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = dimensionResource(designsystemR.dimen.m_space))
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.gender),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
            )

            val (selectedGender, onGenderSelected) = rememberSaveable { mutableStateOf(genderFilter) }
            Column(Modifier.selectableGroup()) {
                Gender.entries.forEach { gender ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (gender == selectedGender),
                                onClick = { onGenderSelected(gender) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = dimensionResource(designsystemR.dimen.m_space)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = gender == selectedGender,
                            onClick = null
                        )
                        Text(
                            text = stringArrayResource(R.array.gender)[gender.ordinal],
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = dimensionResource(designsystemR.dimen.m_space))
                        )
                    }
                }
            }

            Text(
                text = stringResource(R.string.species),
                modifier = Modifier
                    .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
            )

            val (selectedSpecies, onSpeciesSelected) = rememberSaveable {
                mutableStateOf(
                    speciesFilter
                )
            }
            Column(Modifier.selectableGroup()) {
                Species.entries.forEach { species ->
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .selectable(
                                selected = (species == selectedSpecies),
                                onClick = { onSpeciesSelected(species) },
                                role = Role.RadioButton
                            )
                            .padding(horizontal = dimensionResource(designsystemR.dimen.m_space)),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = species == selectedSpecies,
                            onClick = null
                        )
                        Text(
                            text = species.displayName,
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.padding(start = dimensionResource(designsystemR.dimen.m_space))
                        )
                    }
                }
            }

            val (selectedType, onTypeSelected) = rememberSaveable { mutableStateOf(typeFilter) }
            selectedSpecies?.let {
                Text(
                    text = stringResource(R.string.type),
                    modifier = Modifier
                        .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
                )

                Column(Modifier.selectableGroup()) {
                    it.types.forEach { type ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (type == selectedType),
                                    onClick = { onTypeSelected(type) },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = dimensionResource(designsystemR.dimen.m_space)),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = type == selectedType,
                                onClick = null
                            )
                            Text(
                                text = type.ifEmpty { stringResource(R.string.without_type) },
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = dimensionResource(designsystemR.dimen.m_space))
                            )
                        }
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
            ) {
                Button(
                    onClick = {
                        onFiltersApply(
                            selectedStatus,
                            selectedSpecies,
                            selectedType,
                            selectedGender
                        )
                        onFilterSheetHide(
                            selectedStatus != null || selectedSpecies != null || selectedGender != null
                        )
                    }
                ) {
                    Text(
                        text = stringResource(R.string.apply)
                    )
                }

                Button(
                    onClick = {
                        onFiltersApply(null, null, null, null)
                        onFilterSheetHide(false)
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        text = stringResource(R.string.reset)
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CharactersGrid(
    lazyPagingCharacters: LazyPagingItems<Character>,
    onCharacterClick: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    PullToRefreshBox(
        isRefreshing = lazyPagingCharacters.loadState.refresh is LoadState.Loading,
        onRefresh = lazyPagingCharacters::refresh,
        modifier = modifier
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(dimensionResource(designsystemR.dimen.s_space)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space))
        ) {
            items(
                count = lazyPagingCharacters.itemCount
            ) { index ->
                lazyPagingCharacters[index]?.let {
                    CharacterItem(
                        character = it,
                        onClick = {
                            onCharacterClick(it.id)
                        }
                    )
                }
            }

            item(span = { GridItemSpan(maxLineSpan) }) {
                lazyPagingCharacters.apply {
                    when {
                        loadState.refresh is LoadState.Loading ->
                            LoadingWidget(modifier = Modifier.fillMaxWidth())

                        loadState.refresh is LoadState.Error -> {
                            val error = lazyPagingCharacters.loadState.refresh as LoadState.Error
                            ErrorWidget(error.error.message, Modifier.fillMaxWidth(), ::retry)
                        }

                        loadState.append is LoadState.Loading ->
                            LoadingWidget(modifier = Modifier.fillMaxWidth())

                        loadState.append is LoadState.Error -> {
                            val error = lazyPagingCharacters.loadState.append as LoadState.Error
                            ErrorWidget(error.error.message, Modifier.fillMaxWidth(), ::retry)
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CharacterItem(
    character: Character,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val statusText = stringArrayResource(R.array.status)[character.status.ordinal]
    val genderText = stringArrayResource(R.array.gender)[character.gender.ordinal]

    Card(
        onClick = onClick,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.inverseSurface
        ),
        modifier = modifier
    ) {
        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            if (LocalInspectionMode.current) {
                Image(
                    painter = painterResource(R.drawable.preview_placeholder),
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .requiredSize(200.dp)
                )
            } else {
                GlideImage(
                    model = character.image,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .requiredSize(200.dp)
                )
            }
            Card(
                shape = RoundedCornerShape(topStartPercent = 50),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.scrim
                )
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.xs_space)),
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(dimensionResource(designsystemR.dimen.xs_space))
                ) {
                    Badge(
                        containerColor = when (character.status) {
                            Status.ALIVE -> Color.Green
                            Status.DEAD -> Color.Red
                            Status.UNKNOWN -> Color.Yellow
                        }
                    )
                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }

        Text(
            text = character.name,
            maxLines = 1,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
                .padding(top = dimensionResource(designsystemR.dimen.m_space))
                .basicMarquee()
        )
        Text(
            text = "$genderText | ${character.species.displayName}",
            maxLines = 1,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
                .padding(bottom = dimensionResource(designsystemR.dimen.m_space))
                .basicMarquee()
        )
    }
}

@Preview
@Composable
private fun CharacterItemPreview() {
    RamTheme {
        CharacterItem(
            character = Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = Species.HUMAN,
                type = "",
                gender = Gender.MALE,
                image = ""
            ),
            onClick = {}
        )
    }
}

@Preview
@Composable
private fun CharactersGridPreview() {
    RamTheme {
        CharactersGrid(
            lazyPagingCharacters = flowOf(
                PagingData.from(
                    listOf(
                        Character(
                            id = 1,
                            name = "Rick Sanchez",
                            status = Status.ALIVE,
                            species = Species.HUMAN,
                            type = "",
                            gender = Gender.MALE,
                            image = ""
                        ),
                        Character(
                            id = 2,
                            name = "Rick Sanchez",
                            status = Status.ALIVE,
                            species = Species.HUMAN,
                            type = "",
                            gender = Gender.MALE,
                            image = ""
                        ),
                        Character(
                            id = 3,
                            name = "Rick Sanchez",
                            status = Status.ALIVE,
                            species = Species.HUMAN,
                            type = "",
                            gender = Gender.MALE,
                            image = ""
                        ),
                        Character(
                            id = 4,
                            name = "Rick Sanchez",
                            status = Status.ALIVE,
                            species = Species.HUMAN,
                            type = "",
                            gender = Gender.MALE,
                            image = ""
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            onCharacterClick = {},
            modifier = Modifier.fillMaxSize()
        )
    }
}