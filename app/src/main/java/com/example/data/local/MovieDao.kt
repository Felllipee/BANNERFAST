package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.domain.model.Banner
import com.example.domain.model.Movie
import com.example.domain.model.Video
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Query("SELECT * FROM movies WHERE title LIKE '%' || :query || '%'")
    suspend fun searchMoviesLocal(query: String): List<Movie>

    @Query("SELECT * FROM movies WHERE id = :id")
    suspend fun getMovie(id: Int): Movie?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovies(movies: List<Movie>)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMovie(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBanner(banner: Banner)

    @Query("SELECT * FROM banners ORDER BY timestamp DESC")
    fun getBanners(): Flow<List<Banner>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVideo(video: Video)

    @Query("SELECT * FROM videos ORDER BY timestamp DESC")
    fun getVideos(): Flow<List<Video>>
}
