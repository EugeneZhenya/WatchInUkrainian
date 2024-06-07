package ua.dp.klio.wiu.data.server


import com.google.gson.annotations.SerializedName
import ua.dp.klio.wiu.domain.Movie

data class Result(
    @SerializedName("ageLimit")
    val ageLimit: Int,
    @SerializedName("backgroundUrl")
    val backgroundUrl: String,
    @SerializedName("coverUrl")
    val coverUrl: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("id")
    val id: Int,
    @SerializedName("isCartoon")
    val isCartoon: Boolean,
    @SerializedName("originalTitle")
    val originalTitle: String,
    @SerializedName("posterUrl")
    val posterUrl: String,
    @SerializedName("releaseDate")
    val releaseDate: String,
    @SerializedName("slogan")
    val slogan: String,
    @SerializedName("title")
    val title: String,
    @SerializedName("tmdbId")
    val tmdbId: Int,
    @SerializedName("trailerUrl")
    val trailerUrl: String,
    @SerializedName("videoUrl")
    val videoUrl: String,
    @SerializedName("tvId")
    val tvId: Int,
    @SerializedName("season")
    val season: Int,
    @SerializedName("episode")
    val episode: Int,
)

fun Result.toDomainMovie() = Movie(
    id.toLong(),
    title,
    originalTitle,
    description,
    backgroundUrl?.let { "https://image.tmdb.org/t/p/w1280/$it" },
    posterUrl?.let { "https://image.tmdb.org/t/p/w185/$it" },
    coverUrl?.let { "https://image.tmdb.org/t/p/w780/$it" },
    videoUrl,
    trailerUrl,
    releaseDate,
    tvId,
    season,
    episode
)

