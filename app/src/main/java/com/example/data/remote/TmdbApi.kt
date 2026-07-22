package com.example.data.remote

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import retrofit2.http.GET
import retrofit2.http.Query

@JsonClass(generateAdapter = true)
data class TmdbSearchResponse(
    val page: Int,
    val results: List<TmdbMovieResult>
)

@JsonClass(generateAdapter = true)
data class TmdbMovieResult(
    val id: Int,
    val title: String?,
    val name: String?, // For TV shows
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "original_name") val originalName: String?, // For TV shows
    val overview: String?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "first_air_date") val firstAirDate: String?, // For TV shows
    @Json(name = "vote_average") val voteAverage: Double?
)

@JsonClass(generateAdapter = true)
data class TmdbMovieDetails(
    val id: Int,
    val title: String?,
    val name: String?,
    @Json(name = "original_title") val originalTitle: String?,
    @Json(name = "original_name") val originalName: String?,
    val overview: String?,
    @Json(name = "poster_path") val posterPath: String?,
    @Json(name = "backdrop_path") val backdropPath: String?,
    @Json(name = "release_date") val releaseDate: String?,
    @Json(name = "first_air_date") val firstAirDate: String?,
    @Json(name = "vote_average") val voteAverage: Double?,
    val runtime: Int?,
    @Json(name = "episode_run_time") val episodeRunTime: List<Int>?,
    val genres: List<TmdbGenre>?,
    @Json(name = "production_countries") val productionCountries: List<TmdbCountry>?,
    @Json(name = "spoken_languages") val spokenLanguages: List<TmdbLanguage>?,
    val budget: Long?,
    val revenue: Long?,
    val credits: TmdbCredits?
)

@JsonClass(generateAdapter = true)
data class TmdbGenre(
    val id: Int,
    val name: String
)

@JsonClass(generateAdapter = true)
data class TmdbCountry(
    val name: String
)

@JsonClass(generateAdapter = true)
data class TmdbLanguage(
    val name: String
)

@JsonClass(generateAdapter = true)
data class TmdbCredits(
    val cast: List<TmdbCast>?,
    val crew: List<TmdbCrew>?
)

@JsonClass(generateAdapter = true)
data class TmdbCast(
    val name: String,
    @Json(name = "profile_path") val profilePath: String?
)

@JsonClass(generateAdapter = true)
data class TmdbCrew(
    val name: String,
    val job: String
)

interface TmdbApi {
    @GET("search/multi")
    suspend fun searchMulti(
        @Query("query") query: String,
        @Query("language") language: String = "pt-BR",
        @Query("include_adult") includeAdult: Boolean = false
    ): TmdbSearchResponse

    @GET("movie/{movie_id}?append_to_response=credits")
    suspend fun getMovieDetails(
        @retrofit2.http.Path("movie_id") movieId: Int,
        @Query("language") language: String = "pt-BR"
    ): TmdbMovieDetails
    
    @GET("tv/{series_id}?append_to_response=credits")
    suspend fun getSeriesDetails(
        @retrofit2.http.Path("series_id") seriesId: Int,
        @Query("language") language: String = "pt-BR"
    ): TmdbMovieDetails
}
