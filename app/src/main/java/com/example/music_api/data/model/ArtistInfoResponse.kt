package com.example.music_api.data.model
import com.google.gson.annotations.SerializedName

data class ArtistInfoResponse(
    @SerializedName("artist") val artist: Artist?
)

data class Artist(
    @SerializedName("name") val name: String,
    @SerializedName("bio") val bio: Bio?,
    @SerializedName("image") val images: List<ArtistImage>?
) {
    fun getBestImage(): String? {
        val order = listOf("extralarge", "large", "medium", "small")
        return order.firstNotNullOfOrNull { size ->
            images?.find { it.size == size }?.url?.takeUnless { it.isLastFmPlaceholder() }
        }
    }
data class Bio(
    @SerializedName("summary") val summary: String?,
    @SerializedName("content") val content: String?
)

data class ArtistImage(
    @SerializedName("#text") val url: String,
    @SerializedName("size") val size: String
)

fun String.isLastFmPlaceholder(): Boolean {
    return this.contains("2a96cbd8b46e442fc41c2b86b821562f") || this.isEmpty()
}
}