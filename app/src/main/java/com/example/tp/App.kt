package com.example.tp

import android.app.Application

class App : Application() {
    val peliculasViewModel: PeliculasViewModel by lazy {
        PeliculasViewModel()
    }
}