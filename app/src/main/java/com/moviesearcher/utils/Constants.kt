package com.moviesearcher.utils

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
    const val ACCESS_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiJmZTUzMDdiYTk0NWE0NjNjOWI4ZjM1MGJhMjFhNTNiYiIsInN1YiI6IjYwYWU2MjM4YmIyNjAyMDAyOWVmZmVlZiIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.otmpWZ2AqaSG7jAQz8D92FMJS2sX9AaNlOV666Kz2u4"

    enum class MediaType(s: String) {
        ALL("all"), MOVIE("movie"), TV("tv"), PERSON("person")
    }

    enum class TimeWindow(s: String) {
        DAY("day"), WEEK("week")
    }
}