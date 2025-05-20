package haui.do_an.moive_ticket_booking.DTO

import com.google.gson.annotations.SerializedName

data class MovieTmdbDTO(
    val id: Int,
    var title: String,
    var overview: String?,
    var backdrop_path: String?,
    val poster_path: String,
    var release_date: String?,
    var genres: List<Genre>?,
    var actors: List<Actor>?,
    @SerializedName("runtime")
    var duration: Int?,
    @SerializedName("original_language")
    var language: String?,
    @SerializedName("vote_average")
    val tmdbScore: Double?,
    var director: List<Director>?,
    var type: String?
)

data class SearchMovies(
    val results: List<MovieTmdbDTO>
)

data class Genre(
    val id: Int, val name: String)

data class Actor(
    val id: Int, val name: String)

data class Director(
    val id: Int, val name: String)


data class CreditsResponse(
    @SerializedName("cast")
    val actors: List<Actor>,
    val crew: List<Crew>
)

data class Crew(
    val id: Int,
    val name: String,
    val job: String
)
