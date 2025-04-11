package io.github.ansellmaximilian.maxmovies.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import io.github.ansellmaximilian.maxmovies.model.Movie
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import io.github.ansellmaximilian.maxmovies.BuildConfig
import io.github.ansellmaximilian.maxmovies.model.ApiError
import io.github.ansellmaximilian.maxmovies.network.movieService
import kotlinx.coroutines.launch
import retrofit2.HttpException

class MovieViewModel : ViewModel() {
    var movies by mutableStateOf<List<Movie>>(emptyList())
        private set

    var isLoading by mutableStateOf(true)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    init {
        getMovies()
    }

    fun getMovies() {
        viewModelScope.launch {
            try {
                val response = movieService.getPopularMovies(BuildConfig.API_KEY)
                movies = response.results
                error = null
            } catch (e: HttpException) {
                val errorJson = e.response()?.errorBody()?.string()

                if(!errorJson.isNullOrEmpty()) {
                    try {
                        val gson = Gson()
                        val errorResponse = gson.fromJson(errorJson, ApiError::class.java)
                        error = errorResponse.statusMessage
                    } catch (e: Exception) {
                        error = "Error parsing error body: ${e.message}"
                    }
                }else {
                    error = "Unknown error: No error body returned"
                }
            } catch (e: Exception) {
                error = e.message + "\n" + e.stackTraceToString()
            } finally {
                isLoading = false
            }
        }
    }

}