package com.moviesearcher.common.utils

object Constants {
    const val BASE_URL = "https://api.themoviedb.org/3/"
    const val IMAGE_URL = "https://image.tmdb.org/t/p/w500"
    const val ACCESS_TOKEN = "Bearer eyJhbGciOiJIUzI1NiJ9.eyJhdWQiOiI4ZDNhZTA2NTQxMmNmNzUzNjg2MzE0MTY2MjhlNzNiMSIsInN1YiI6IjYxMDE5ODdiYTIxN2MwMDA1ZDVmYjNhYyIsInNjb3BlcyI6WyJhcGlfcmVhZCJdLCJ2ZXJzaW9uIjoxfQ.te943glac0IzRm-ahsIIZIgr-M85uNeRwJhkVWpFm1Q"
    const val AUTH_URL = "https://www.themoviedb.org/authenticate/%s"
    const val SUCCESS_SESSION_URL = "https://www.themoviedb.org/authenticate/%s/allow"
    const val YOUTUBE_PREVIEW_URL = "https://i.ytimg.com/vi/%s/hqdefault.jpg"
    const val YOUTUBE_VIDEO_URL = "https://youtube.com/embed/%s"
    const val DARK_MODE = "DARK_MODE"
    const val ERROR_MESSAGE = "Something went wrong '%s'"
    const val MOVIE_ID = "movie_id"
    const val TV_ID = "tv_id"
    const val SEASON_NUMBER = "season_number"
    const val EPISODE_NUMBER = "episode_number"
    const val PERSON_ID = "person_id"
    const val USER_DATA = "user_data"
    const val ADDED_TO_LIST = " (added)"
    const val LIST_ID = "list_id"
}