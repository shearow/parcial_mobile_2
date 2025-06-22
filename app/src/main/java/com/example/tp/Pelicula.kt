package com.example.tp

import java.io.Serializable
import java.util.UUID

data class Pelicula(
    val titulo: String,
    val estreno: Int,
    val comentario: String,
    val genero: Genero,
    val imagenUri: String,
    val id: String = UUID.randomUUID().toString()
): Serializable