package ru.nomad.rickandmorty.feature.characters

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
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
import ru.nomad.rickandmorty.core.model.Status
import ru.nomad.rickandmorty.core.designsystem.R as designsystemR

@Composable
fun CharactersScreen(
    modifier: Modifier = Modifier,
    viewModel: CharactersViewModel = viewModel(),
) {
    val uiState = viewModel.uiState.collectAsLazyPagingItems()

    if (uiState.itemCount == 0) {
        EmptyWidget(modifier = modifier)
    } else {
        CharactersGrid(
            lazyPagingCharacters = uiState,
            modifier = modifier
        )
    }
}

@Composable
private fun CharactersGrid(
    lazyPagingCharacters: LazyPagingItems<Character>,
    modifier: Modifier = Modifier,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(dimensionResource(designsystemR.dimen.s_space)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
        horizontalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
        modifier = modifier
    ) {
        items(
            count = lazyPagingCharacters.itemCount
        ) { index ->
            lazyPagingCharacters[index]?.let {
                CharacterItem(character = it)
            }
        }

        lazyPagingCharacters.apply {
            when {
                loadState.refresh is LoadState.Loading -> {
                    item { LoadingWidget() }
                }

                loadState.refresh is LoadState.Error -> {
                    val error = lazyPagingCharacters.loadState.refresh as LoadState.Error
                    item { ErrorWidget(error.error.message) }
                }

                loadState.append is LoadState.Loading -> {
                    item { LoadingWidget() }
                }

                loadState.append is LoadState.Error -> {
                    val error = lazyPagingCharacters.loadState.append as LoadState.Error
                    item { ErrorWidget(error.error.message) }
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun CharacterItem(
    character: Character,
    modifier: Modifier = Modifier,
) {
    val statusText = stringArrayResource(R.array.status)[character.status.ordinal]
    val genderText = stringArrayResource(R.array.gender)[character.gender.ordinal]

    Card(
        onClick = {},
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
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
                .padding(top = dimensionResource(designsystemR.dimen.m_space))
        )
        Text(
            text = "$genderText | ${character.species}",
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = dimensionResource(designsystemR.dimen.m_space))
                .padding(bottom = dimensionResource(designsystemR.dimen.m_space))
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
                species = "Human",
                gender = Gender.MALE,
                image = ""
            )
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
                            species = "Human",
                            gender = Gender.MALE,
                            image = ""
                        ),
                        Character(
                            id = 2,
                            name = "Rick Sanchez",
                            status = Status.ALIVE,
                            species = "Human",
                            gender = Gender.MALE,
                            image = ""
                        ),
                        Character(
                            id = 3,
                            name = "Rick Sanchez",
                            status = Status.ALIVE,
                            species = "Human",
                            gender = Gender.MALE,
                            image = ""
                        ),
                        Character(
                            id = 4,
                            name = "Rick Sanchez",
                            status = Status.ALIVE,
                            species = "Human",
                            gender = Gender.MALE,
                            image = ""
                        )
                    )
                )
            ).collectAsLazyPagingItems(),
            modifier = Modifier.fillMaxSize()
        )
    }
}