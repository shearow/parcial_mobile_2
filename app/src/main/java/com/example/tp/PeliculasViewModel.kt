package com.example.tp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlin.collections.mutableListOf

class PeliculasViewModel: ViewModel() {
    private val _peliculas = MutableLiveData(mutableListOf<Pelicula>(
        Pelicula(
            "Spiderman",
            1990,
            "Una pelicula sobre superheroes",
            Generos.ACCION,
            "https://i.blogs.es/c7ed10/screenshot_90/1366_2000.jpeg"
        ),
        Pelicula(
            "Batman",
            1990,
            "El murcielago pa",
            Generos.ACCION,
            "https://media.revistagq.com/photos/621f6d1723b7738d21d91e1d/1:1/w_1439,h_1439,c_limit/batman-uhdpaper.com-4K-17.jpg"
        )
    ))
    val peliculas: LiveData<MutableList<Pelicula>> = _peliculas

    fun agregarPelicula(peli: Pelicula?){
        peli?.let { _peliculas.value?.add(it) }
    }

    fun eliminarPelicula(id: String){
        _peliculas.value?.let { lista ->
            val nuevaLista = lista.filterNot { it.id == id}.toMutableList()
            _peliculas.value = nuevaLista
        }
    }

    fun editarPelicula(peliEditada: Pelicula?){
        peliEditada?.let {
            _peliculas.value?.let { lista ->
                val index = lista.indexOfFirst { it.id == peliEditada.id }
                if(index != -1){
                    lista[index] = peliEditada.copy(id = peliEditada.id)
                }
            }
        }
    }

}