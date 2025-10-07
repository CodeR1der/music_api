package com.example.music_api.data.model

import com.google.gson.annotations.SerializedName

data class TopTracksResponse(
    @SerializedName("toptracks") val topTracks: TopTracks?
)

data class TopTracks(
    @SerializedName("track") val tracks: List<Track>?
)

data class Track(
    @SerializedName("name") val name: String,
    @SerializedName("artist") val artist: TrackArtist?,
    @SerializedName("image") val images: List<Artist.ArtistImage>?
) {
    fun getBestTrackImage(): String? {
        val order = listOf("mega", "medium", "small")
        return order.firstNotNullOfOrNull { size ->
            images?.find { it.size == size }?.url?.takeUnless { it.isLastFmPlaceholder() }
        }    }
}

data class TrackArtist(
    @SerializedName("name") val name: String
)
fun String.isLastFmPlaceholder(): Boolean {
    return this.contains("2a96cbd8b46e442fc41c2b86b821562f") || this.isEmpty()
}