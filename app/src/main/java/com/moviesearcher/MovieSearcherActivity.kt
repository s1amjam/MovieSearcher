package com.moviesearcher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MovieSearcherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie_searcher)

        val isFragmentContainerEmpty = savedInstanceState == null

        if (isFragmentContainerEmpty) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragmentContainer, MovieSearcherFragment.newInstance())
                .commit()
        }
    }
}