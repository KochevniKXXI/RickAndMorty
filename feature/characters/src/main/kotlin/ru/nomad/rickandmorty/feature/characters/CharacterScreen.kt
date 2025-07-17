package ru.nomad.rickandmorty.feature.characters

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
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
fun CharacterScreen(
    modifier: Modifier = Modifier,
    viewModel: CharacterViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    PullToRefreshBox(
        isRefreshing = uiState is CharacterUiState.Loading,
        onRefresh = viewModel::loadCharacter,
        modifier = modifier
    ) {
        CharacterScreen(
            uiState = uiState,
            onRetryClick = viewModel::loadCharacter,
            modifier = Modifier
                .fillMaxSize()
        )
    }
}

@Composable
private fun CharacterScreen(
    uiState: CharacterUiState,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (uiState) {
        CharacterUiState.Loading ->
            LoadingWidget(modifier = modifier)

        is CharacterUiState.Error ->
            ErrorWidget(
                message = uiState.cause.message,
                onRetryClick = onRetryClick,
                modifier = modifier
            )

        is CharacterUiState.Success ->
            CharacterDetails(
                character = uiState.character,
                modifier = modifier
            )
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun CharacterDetails(
    character: Character,
    modifier: Modifier = Modifier
) {
    val statusText =
        stringArrayResource(R.array.status)[character.status.ordinal]
    val genderText =
        stringArrayResource(R.array.gender)[character.gender.ordinal]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(designsystemR.dimen.s_space)),
        modifier = modifier
            .verticalScroll(rememberScrollState())
    ) {
        if (LocalInspectionMode.current) {
            Image(
                painter = painterResource(R.drawable.preview_placeholder),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
        } else {
            GlideImage(
                model = character.image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(200.dp)
                    .clip(CircleShape)
            )
        }

        Text(
            text = character.name,
            style = MaterialTheme.typography.titleLarge
        )

        Text(
            text = "$genderText | ${character.species.displayName} | ${character.type.ifBlank {
                stringResource(R.string.without_type) 
            }}",
            style = MaterialTheme.typography.bodyMedium,
        )

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
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Preview
@Composable
private fun CharacterDetailsPreview() {
    RamTheme {
        CharacterDetails(
            character = Character(
                id = 1,
                name = "Rick Sanchez",
                status = Status.ALIVE,
                species = Species.HUMAN,
                type = "",
                gender = Gender.MALE,
                image = ""
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}