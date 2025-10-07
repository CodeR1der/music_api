package com.example.music_api.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.music_api.data.model.Artist
import com.example.music_api.viewmodel.ArtistInfoUiState
import com.example.music_api.viewmodel.ErrorType
import com.example.music_api.viewmodel.ArtistInfoViewModel
import com.example.music_api.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistInfoScreen(
    onBack: () -> Unit
) {
    val viewModel: ArtistInfoViewModel = viewModel()
    val uiState by viewModel.uiState.collectAsState()
    var artistNameInput by remember { mutableStateOf("") }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = Color.White,
        bottomBar = { BackFooter(onBack) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .padding(horizontal = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Биография",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 24.dp)
                    .align(Alignment.Start),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(32.dp))

            SearchRow(
                value = artistNameInput,
                onValueChange = { artistNameInput = it },
                placeholder = "Кого ищем?",
                onSearch = {
                    if (artistNameInput.isNotBlank()) {
                        viewModel.loadArtistInfo(artistNameInput.trim())
                    }
                }
            )

            GradientUnderline()

            Spacer(modifier = Modifier.height(24.dp))

            GradientButton(
                text = "Искать",
                enabled = artistNameInput.isNotBlank(),
                onClick = {
                    if (artistNameInput.isNotBlank()) {
                        viewModel.loadArtistInfo(artistNameInput.trim())
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = uiState) {
                is ArtistInfoUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                    }
                }

                is ArtistInfoUiState.Success -> {
                    ArtistInfoContent(artist = state.artist)
                }

                is ArtistInfoUiState.Error -> {
                    val errorMessage = when (state.type) {
                        ErrorType.ARTIST_NOT_FOUND -> "Артист не найден"
                        ErrorType.CONNECTION_ERROR -> "Ошибка соединения с сервером"
                        ErrorType.UNKNOWN -> stringResource(R.string.error_loading_artist)
                    }
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Ошибка",
                            tint = MaterialTheme.colorScheme.error
                        )
                        Text(
                            text = errorMessage,
                            color = MaterialTheme.colorScheme.error,
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 16.sp,
                            modifier = Modifier.padding(top = 16.dp),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        SolidButton(
                            text = "Повторить",
                            enabled = artistNameInput.isNotBlank(),
                            onClick = {
                                if (artistNameInput.isNotBlank()) {
                                    viewModel.loadArtistInfo(artistNameInput.trim())
                                    artistNameInput = ""
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ArtistInfoContent(artist: Artist) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(vertical = 16.dp),
        horizontalAlignment = Alignment.Start
    ) {
        val imageUrl = artist.getBestImage()

        if (!imageUrl.isNullOrBlank()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Фото артиста",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(180.dp)
                    .clip(MaterialTheme.shapes.medium)
            )
        } else {
            Box(
                modifier = Modifier
                    .size(180.dp)
                    .clip(MaterialTheme.shapes.medium)
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }

        Text(
            text = artist.name,
            fontFamily = FontFamily.SansSerif,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Start
        )

        val rawBio = artist.bio?.content?.takeIf { it.isNotBlank() }
            ?: artist.bio?.summary?.takeIf { it.isNotBlank() }
            ?: "Биография недоступна"
        var bioText = sanitizeLastFmLink(rawBio)

        if (bioText == "") bioText = "Мы ничего не знаем об этом исполнителе"
        Text(
            text = bioText,
            fontFamily = FontFamily.SansSerif,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(top = 16.dp),
            textAlign = TextAlign.Start
        )
    }
}


private fun sanitizeLastFmLink(text: String): String {
    val patterns = listOf(
        Regex("\\s*<a[^>]*>\\s*Read more on Last\\.fm\\s*</a>\\.?", RegexOption.IGNORE_CASE),
        Regex("User-contributed text is available under the Creative Commons By-SA License; additional terms may apply\\.", RegexOption.IGNORE_CASE)
    )

    return patterns.fold(text) { acc, pattern ->
        acc.replace(pattern, "").trim()
    }
}