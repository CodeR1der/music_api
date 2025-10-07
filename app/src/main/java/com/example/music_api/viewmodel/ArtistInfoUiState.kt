package com.example.music_api.viewmodel

import com.example.music_api.data.model.Artist

sealed interface ArtistInfoUiState {
    object Loading : ArtistInfoUiState
    data class Success(val artist: Artist) : ArtistInfoUiState
    data class Error(val type: ErrorType) : ArtistInfoUiState
}