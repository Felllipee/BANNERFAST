package com.example.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    val id: Int,
    val title: String,
    val originalTitle: String,
    val overview: String,
    val posterPath: String?,
    val backdropPath: String?,
    val releaseDate: String,
    val voteAverage: Double,
    val type: String = "movie",
    val runtime: Int = 0,
    val genres: List<String> = emptyList(),
    val country: String = "",
    val language: String = "",
    val director: String = "",
    val budget: Long = 0,
    val revenue: Long = 0,
    val cast: List<String> = emptyList(),
    val castProfiles: List<String> = emptyList()
)

@Entity(tableName = "banners")
data class Banner(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val movieId: Int,
    val filePath: String,
    val timestamp: Long = System.currentTimeMillis()
)

@Entity(tableName = "videos")
data class Video(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val movieId: Int,
    val filePath: String,
    val timestamp: Long = System.currentTimeMillis()
)
