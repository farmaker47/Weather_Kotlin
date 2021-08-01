package com.george.news

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class NewsActivity : AppCompatActivity() {

    // Load native library
    init {
        System.loadLibrary("native-lib")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        //Delete navGraph from xml and set here
        // Details: https://developer.android.com/guide/navigation/navigation-pass-data#start
        findNavController(R.id.nav_host_fragment).setGraph(R.navigation.nav_graph)

    }

}

external fun getAPIKey(): String

