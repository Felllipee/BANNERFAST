package com.example.data

import com.example.data.local.MovieDao
import com.example.data.remote.RetrofitClient
import com.example.domain.model.Movie
import kotlinx.coroutines.flow.Flow

class MovieRepository(private val dao: MovieDao) {
    suspend fun search(query: String): List<Movie> {
        return try {
            val response = RetrofitClient.tmdbApi.searchMulti(query)
            val movies = response.results.mapNotNull { result ->
                if (result.title != null || result.name != null) {
                    Movie(
                        id = result.id,
                        title = result.title ?: result.name ?: "",
                        originalTitle = result.originalTitle ?: result.originalName ?: "",
                        overview = result.overview ?: "",
                        posterPath = result.posterPath,
                        backdropPath = result.backdropPath,
                        releaseDate = result.releaseDate ?: result.firstAirDate ?: "",
                        voteAverage = result.voteAverage ?: 0.0,
                        type = if (result.name != null) "series" else "movie"
                    )
                } else null
            }
            dao.insertMovies(movies)
            movies
        } catch (e: Exception) {
            dao.searchMoviesLocal(query)
        }
    }

    suspend fun getMovieDetails(id: Int, isSeries: Boolean): Movie? {
        return try {
            val details = if (isSeries) {
                RetrofitClient.tmdbApi.getSeriesDetails(id)
            } else {
                RetrofitClient.tmdbApi.getMovieDetails(id)
            }
            val movie = Movie(
                id = details.id,
                title = details.title ?: details.name ?: "",
                originalTitle = details.originalTitle ?: details.originalName ?: "",
                overview = details.overview ?: "",
                posterPath = details.posterPath,
                backdropPath = details.backdropPath,
                releaseDate = details.releaseDate ?: details.firstAirDate ?: "",
                voteAverage = details.voteAverage ?: 0.0,
                type = if (isSeries) "series" else "movie",
                runtime = details.runtime ?: details.episodeRunTime?.firstOrNull() ?: 0,
                genres = details.genres?.map { it.name } ?: emptyList(),
                country = details.productionCountries?.firstOrNull()?.name ?: "",
                language = details.spokenLanguages?.firstOrNull()?.name ?: "",
                director = details.credits?.crew?.firstOrNull { it.job == "Director" }?.name ?: "",
                budget = details.budget ?: 0,
                revenue = details.revenue ?: 0,
                cast = details.credits?.cast?.take(4)?.map { it.name } ?: emptyList(),
                castProfiles = details.credits?.cast?.take(4)?.map { it.profilePath ?: "" } ?: emptyList()
            )
            dao.insertMovie(movie)
            movie
        } catch (e: Exception) {
            dao.getMovie(id)
        }
    }
}
