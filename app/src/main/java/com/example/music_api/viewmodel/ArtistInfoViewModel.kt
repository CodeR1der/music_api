package com.example.music_api.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.music_api.data.api.ApiService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class ArtistInfoViewModel : ViewModel() {

	private val _uiState = MutableStateFlow<ArtistInfoUiState>(ArtistInfoUiState.Loading)
	val uiState: StateFlow<ArtistInfoUiState> = _uiState

	fun loadArtistInfo(artistName: String) {
		_uiState.value = ArtistInfoUiState.Loading
		viewModelScope.launch {
			try {
				val response = ApiService.lastFmApi.getArtistInfo(
					artistName = artistName,
					apiKey = ApiService.APIKEY
				)
				val artist = response.body()?.artist
				if (response.isSuccessful && artist != null) {
					_uiState.value = ArtistInfoUiState.Success(artist)
				} else if (response.code() == 404) {
					_uiState.value = ArtistInfoUiState.Error(ErrorType.UNKNOWN)
				} else {
					_uiState.value = ArtistInfoUiState.Error(ErrorType.ARTIST_NOT_FOUND)
				}
			} catch (e: IOException) {
				_uiState.value = ArtistInfoUiState.Error(ErrorType.CONNECTION_ERROR)
			} catch (e: HttpException) {
				_uiState.value = ArtistInfoUiState.Error(
					if (e.code() == 404) ErrorType.ARTIST_NOT_FOUND else ErrorType.UNKNOWN
				)
			} catch (e: Exception) {
				_uiState.value = ArtistInfoUiState.Error(ErrorType.UNKNOWN)
			}
		}
	}
}

