package com.moviesearcher

import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test

class FavoritesTest : BaseTest() {
    @BeforeAll
    fun login() {
        BaseTest.login()
    }

    @AfterAll
    fun logout() {
        BaseTest.logout()
    }

    @Test
    fun markMovAsFav() {
        openNavDrawer()
        openFavorites()
        toFavoriteMovie()
        removeMovieFromFavorite()
        markMovieAsFavorite()
    }

    @Test
    fun markTvAsFav() {
        openNavDrawer()
        openFavorites()
        toFavoriteTv()
        removeTvFromFavorite()
        markTvAsFavorite()
    }
}

