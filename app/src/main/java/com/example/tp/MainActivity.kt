package com.example.tp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tp.databinding.ActivityMainBinding
import java.util.Calendar

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val vm: PeliculasViewModel get() = (application as App).peliculasViewModel

    private var estadoGuardado: EstadoFormularioPelicula = EstadoFormularioPelicula.CREAR
    private var peliculaAEditar: Pelicula? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        initListener()
    }

    private fun initUI(){
        cargarGeneros()
        cargarEstado()
        if(estadoGuardado == EstadoFormularioPelicula.EDITAR) { cargarPeliculaAEditar() }
    }

    private fun cargarPeliculaAEditar() {
        peliculaAEditar = intent.getSerializableExtra("peliculaEditada") as? Pelicula

        peliculaAEditar?.let {
            binding.etTitulo.setText(peliculaAEditar?.titulo ?: "")
            val nombresGeneros = Genero.entries.map { it.nombre }
            val indexGenero = nombresGeneros.indexOf(peliculaAEditar?.genero?.nombre)
            if (indexGenero != -1) {
                binding.spGenero.setSelection(indexGenero)
            }
            binding.etYear.setText(peliculaAEditar?.estreno?.toString() ?: "")
            binding.etComentario.setText(peliculaAEditar?.comentario ?: "")
            binding.etUrlImagen.setText(peliculaAEditar?.imagenUri ?: "")
        }
    }

    private fun initListener() {
        binding.btnGuardar.setOnClickListener { guardarPelicula() }
        binding.btnCancelar.setOnClickListener { navegarAListaPeliculas() }
    }

    private fun guardarPelicula(){
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        val titulo = binding.etTitulo.text.toString().trim()
        val generoSeleccionado = binding.spGenero.selectedItem.toString()
        val generoEnum = Genero.entries.find { it.nombre == generoSeleccionado }
        val year = binding.etYear.text.toString().toIntOrNull()
        val comentario = binding.etComentario.text.toString().trim()
        val imagenUrl = binding.etUrlImagen.text.toString().trim()

        if(titulo.isEmpty() || titulo.length > 20) {
            Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }
        if(year == null) {
            Toast.makeText(this, "El año no puede estar vacío", Toast.LENGTH_SHORT).show()
            return
        }
        if(year !in 1900..currentYear) {
            Toast.makeText(this, "Ingresá un año válido entre 1900 y $currentYear", Toast.LENGTH_SHORT).show()
            return
        }
        if(comentario.length > 150) {
            Toast.makeText(this, "El comentario no puede superar los 150 caracteres", Toast.LENGTH_SHORT).show()
            return
        }
        if(generoEnum == null) {
            Toast.makeText(this, "Seleccioná un género válido", Toast.LENGTH_SHORT).show()
            return
        }

        val id = peliculaAEditar?.id ?: java.util.UUID.randomUUID().toString()
        val nuevaPelicula = Pelicula(titulo, year, comentario, generoEnum, imagenUrl, id)

        if(estadoGuardado == EstadoFormularioPelicula.CREAR){
            vm.agregarPelicula(nuevaPelicula)
            Toast.makeText(this, "Pelicula agregada correctamente", Toast.LENGTH_SHORT).show()
        }

        if(estadoGuardado == EstadoFormularioPelicula.EDITAR){
            vm.editarPelicula(nuevaPelicula)
            Toast.makeText(this, "Pelicula editada correctamente", Toast.LENGTH_SHORT).show()
        }
        navegarAListaPeliculas()
    }
    private fun cargarGeneros() {
        val generos = Genero.entries.toTypedArray()
        val nombresGeneros = generos.map { it.nombre }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            nombresGeneros
        )
        binding.spGenero.adapter = adapter
    }

    private fun cargarEstado() {
        estadoGuardado = intent.getStringExtra("estado")?.let {
            EstadoFormularioPelicula.valueOf(it)
        } ?: EstadoFormularioPelicula.CREAR
    }

    private fun navegarAListaPeliculas(){
        val intent = Intent(this, ListaPeliculasActivity::class.java)
        startActivity(intent)
    }
}