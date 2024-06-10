package ua.dp.klio.wiu.domain

import java.io.Serializable

/**
 * Movie class represents video entity with title, description, image thumbs and video url.
 */
data class Movie(
    var id: Long = 0,
    var title: String? = null,
    var originalTitle: String? = null,
    var description: String? = null,
    var backgroundImageUrl: String? = null,
    var poster: String? = null,
    var coverImageUrl: String? = null,
    var videoUrl: String? = null,
    var trailerUrl: String? = null,
    var releaseDate: String? = null,
    var tvId: Int = 0,
    var season: Int = 0,
    var episode: Int = 0,
    var isKlio: Boolean = false
) : Serializable {

    override fun toString(): String {
        return "Movie{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", description='" + description + '\'' +
                ", backgroundImageUrl='" + backgroundImageUrl + '\'' +
                ", poster='" + poster + '\'' +
                ", coverImageUrl='" + coverImageUrl + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", trailerUrl='" + trailerUrl + '\'' +
                ", videoUrl='" + releaseDate + '\'' +
                ", tvId=" + tvId +
                ", season=" + season +
                ", episode=" + episode +
                ", isKlio=" + isKlio.toString() +
                '}'
    }

    companion object {
        internal const val serialVersionUID = 727566175075960653L
    }
}