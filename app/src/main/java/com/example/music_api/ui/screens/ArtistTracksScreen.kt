package com.example.music_api.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import com.example.music_api.data.model.Track
import com.example.music_api.viewmodel.ArtistTracksUiState
import com.example.music_api.viewmodel.ArtistTracksViewModel
import com.example.music_api.viewmodel.ErrorType
import com.example.music_api.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArtistTracksScreen(
    onBack: () -> Unit
) {
    val viewModel: ArtistTracksViewModel = viewModel()
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
                text = "Лучшие треки",
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
                        viewModel.loadTopTracks(artistNameInput.trim())
                        artistNameInput = ""
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
                        viewModel.loadTopTracks(artistNameInput.trim())
                    }
                }
            )

            Spacer(modifier = Modifier.height(24.dp))

            when (val state = uiState) {
                is ArtistTracksUiState.Loading -> {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                    }
                }

                is ArtistTracksUiState.Success -> {
                    if (state.tracks.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Треки не найдены",
                                color = Color.Black,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 16.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(state.tracks) { track ->
                                TrackItem(track = track)
                            }
                        }
                    }
                }

                is ArtistTracksUiState.Error -> {
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
                                    viewModel.loadTopTracks(artistNameInput.trim())
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
fun TrackItem(track: Track) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(MaterialTheme.shapes.medium)
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        val imageUrl = track.getBestTrackImage() ?: ""
        if (imageUrl.isNotEmpty()) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Обложка трека",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(8.dp))
            )
        } else {
            Box(
                modifier = Modifier
                    .size(96.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.3f))
            )
        }

        Spacer(modifier = Modifier.width(24.dp))

        Column(horizontalAlignment = Alignment.Start) {
            Text(
                text = track.name,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 24.sp),
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )
        }
    }
}
