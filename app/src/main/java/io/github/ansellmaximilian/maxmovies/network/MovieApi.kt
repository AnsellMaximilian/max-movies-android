package io.github.ansellmaximilian.maxmovies.network

import io.github.ansellmaximilian.maxmovies.model.Movie
import io.github.ansellmaximilian.maxmovies.model.PaginatedResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

private const val BASE_URL = "https://api.themoviedb.org/3/"

private val retrofit = Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build()

interface MovieApiService {
    @GET("discover/movie")
    suspend fun getPopularMovies(
        @Query("api_key") apiKey: String,
        @Query("include_adult") includeAdult: Boolean = false,
        @Query("include_video") includeVideo: Boolean = false,
        @Query("sort_by") sortBy: String = "popularity.desc",
        @Query("language") language: String = "en-US",
        @Query("page") page: Int = 1,
    ): PaginatedResponse<Movie>
}

val movieService: MovieApiService by lazy {
    retrofit.create(MovieApiService::class.java)
}