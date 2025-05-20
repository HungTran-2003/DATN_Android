package haui.do_an.moive_ticket_booking.DTO

import com.google.gson.annotations.SerializedName

data class MovieDTO(

    val id: Int,
    @SerializedName("name")
    var title: String,
    var description: String?,
    var backdropPath: String?,
    var posterUrl: String,
    var releaseDate: String?,
    var genres: List<Genres>?,
    var actors: List<Actors>?,
    var duration: Int?,
    var language: String?,
    val tmdbScore: Double?,
    var directors: List<Directors>?,
    var type: String?,
    val status: String?,
    val averageRating: Float? = 0f,
    @SerializedName("ratingCount")
    val totalRatings: Int?,
    val totalFavorites: Int?,
    val favortive: Boolean?
)

data class Genres(
    val genreId: Int,
    val name: String
)

data class Actors(
    val actorId: Int,
    val name: String
)

data class Directors(
    val directorId: Int,
    val name: String
)


