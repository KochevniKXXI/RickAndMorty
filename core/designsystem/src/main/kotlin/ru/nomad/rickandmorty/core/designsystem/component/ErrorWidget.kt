package ru.nomad.rickandmorty.core.designsystem.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import ru.nomad.rickandmorty.core.designsystem.R

@Composable
fun ErrorWidget(
    message: String?,
    modifier: Modifier = Modifier,
    onRetryClick: (() -> Unit)? = null,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(
            dimensionResource(R.dimen.s_space),
            Alignment.CenterVertically
        ),
        modifier = modifier
            .padding(horizontal = dimensionResource(R.dimen.m_space))
    ) {
        Text(text = message ?: stringResource(R.string.unknown_error))
        onRetryClick?.let {
            Button(
                onClick = onRetryClick
            ) {
                Text(
                    text = stringResource(R.string.retry)
                )
            }
        }
    }
}