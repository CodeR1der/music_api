package com.example.music_api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_api.data.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class ArtistTracksViewModel : ViewModel() {
	private val _uiState = MutableStateFlow<ArtistTracksUiState>(ArtistTracksUiState.Loading)
	val uiState: StateFlow<ArtistTracksUiState> = _uiState

	fun loadTopTracks(artistName: String) {
		_uiState.value = ArtistTracksUiState.Loading
		viewModelScope.launch {
			try {
				val response = ApiService.lastFmApi.getTopTracks(
					artistName = artistName,
					apiKey = ApiService.APIKEY
				)
				val allTracks = if (response.isSuccessful) {
					response.body()?.topTracks?.tracks.orEmpty()
				} else {
					if (response.code() == 404) {
						_uiState.value = ArtistTracksUiState.Error(ErrorType.UNKNOWN)
						return@launch
					} else {
						_uiState.value = ArtistTracksUiState.Error(ErrorType.ARTIST_NOT_FOUND)
						return@launch
					}
				}

				if (allTracks.isNotEmpty()) {
					val randomThree = allTracks.shuffled().take(3)
					_uiState.value = ArtistTracksUiState.Success(randomThree)
				} else {
					_uiState.value = ArtistTracksUiState.Error(ErrorType.ARTIST_NOT_FOUND)
				}
			} catch (e: IOException) {
				_uiState.value = ArtistTracksUiState.Error(ErrorType.CONNECTION_ERROR)
			} catch (e: HttpException) {
				_uiState.value = ArtistTracksUiState.Error(
					if (e.code() == 404) ErrorType.ARTIST_NOT_FOUND else ErrorType.UNKNOWN
				)
			} catch (e: Exception) {
				_uiState.value = ArtistTracksUiState.Error(ErrorType.UNKNOWN)
			}
		}
	}
}


