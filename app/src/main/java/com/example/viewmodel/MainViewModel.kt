package com.example.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.MovieRepository
import com.example.domain.model.Movie
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(private val repository: MovieRepository) : ViewModel() {
    private val _searchResults = MutableStateFlow<List<Movie>>(emptyList())
    val searchResults: StateFlow<List<Movie>> = _searchResults.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _selectedMovie = MutableStateFlow<Movie?>(null)
    val selectedMovie: StateFlow<Movie?> = _selectedMovie.asStateFlow()

    fun searchMovies(query: String) {
        if (query.isBlank()) return
        viewModelScope.launch {
            _isLoading.value = true
            _searchResults.value = repository.search(query)
            _isLoading.value = false
        }
    }

    fun getMovieDetails(id: Int, isSeries: Boolean) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedMovie.value = repository.getMovieDetails(id, isSeries)
            _isLoading.value = false
        }
    }
}
