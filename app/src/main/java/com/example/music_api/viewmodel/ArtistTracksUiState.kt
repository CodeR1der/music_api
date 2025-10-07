package com.example.music_api.viewmodel

import com.example.music_api.data.model.Track

sealed interface ArtistTracksUiState {
    object Loading : ArtistTracksUiState
    data class Success(val tracks: List<Track>) : ArtistTracksUiState
    data class Error(val type: ErrorType) : ArtistTracksUiState
}
