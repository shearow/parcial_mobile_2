package com.example.tp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tp.adapter.PeliculaAdapter
import com.example.tp.databinding.ActivityListaPeliculasBinding

class ListaPeliculasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListaPeliculasBinding

    private val vm: PeliculasViewModel get() = (application as App).peliculasViewModel

    private val onEditarClick: (Pelicula) -> Unit = { pelicula ->
        navegarAMainActivity(EstadoFormularioPelicula.EDITAR, pelicula)
    }

    private val onEliminarClick: (Pelicula) -> Unit = { pelicula ->
        confirmarAccion(
            subtitulo = "¿Seguro que quieres eliminar la película ${pelicula.titulo}?",
            onConfirmar = {
                vm.eliminarPelicula(pelicula.id)
                Toast.makeText(this, "Pelicula eliminada correctamente", Toast.LENGTH_SHORT).show()
            }
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityListaPeliculasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvPeliculas.layoutManager = LinearLayoutManager(this)

        initUI()
        initListener()
    }

    private fun initUI(){
        vm.peliculas.observe(this) { pelis ->
            binding.rvPeliculas.adapter = PeliculaAdapter(
                pelis,
                onEditarClick = onEditarClick,
                onEliminarClick = onEliminarClick
            )
        }
    }

    private fun initListener() {
        binding.btnCargarOtraPelicula.setOnClickListener { navegarAMainActivity() }
    }

    private fun navegarAMainActivity(
        estado: EstadoFormularioPelicula = EstadoFormularioPelicula.CREAR,
        pelicula: Pelicula? = null
    ){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("estado", estado.name)
        pelicula?.let {
            intent.putExtra("peliculaEditada", it)
        }
        startActivity(intent)
    }

    private fun confirmarAccion(
        subtitulo: String,
        onConfirmar: () -> Unit,
        titulo: String = "Confirmar"
    ) {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle(titulo)
            .setMessage(subtitulo)
            .setPositiveButton("Sí") { _, _ -> onConfirmar() }
            .setNegativeButton("No", null)
            .show()
    }
}