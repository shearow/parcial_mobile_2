package com.example.tp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.tp.databinding.ActivityMainBinding
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

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
    }

    private fun cargarGeneros() {
        val generos = Generos.values()
        val nombresGeneros = generos.map { it.nombre }

        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            nombresGeneros
        )
        binding.spGenero.adapter = adapter
    }

    private fun initListener() {
        binding.btnGuardar.setOnClickListener {
            val currentYear = Calendar.getInstance().get(Calendar.YEAR)
            val titulo = binding.etTitulo.text.toString().trim()
            val year = binding.etYear.text.toString().toIntOrNull()
            val comentario = binding.etComentario.text.toString().trim()
            val generoSeleccionado = binding.spGenero.selectedItem.toString()
            val generoEnum = Generos.entries.find { it.nombre == generoSeleccionado }
            val imagen = "https://occ-0-8407-90.1.nflxso.net/dnm/api/v6/Z-WHgqd_TeJxSuha8aZ5WpyLcX8/AAAABaAm9ikezHWeiJVLFrHz35HKqm_KFvh74oNzZmPK5AGKzPUlxh51Tvv1bvXTqFPXEF6KfZe1jAQ_eHjeBu4UYJ5Cx1SPtx5UZH_2.jpg?r=b47"

            if(titulo.isEmpty() || titulo.length > 20) {
                Toast.makeText(this, "El título no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(titulo.length > 20) {
                Toast.makeText(this, "El título no puede superar los 20 carácteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(year == null) {
                Toast.makeText(this, "El año no puede estar vacío", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(year !in 1900..currentYear) {
                Toast.makeText(this, "Ingresá un año válido entre 1900 y $currentYear", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(comentario.length > 150) {
                Toast.makeText(this, "El comentario no puede superar los 150 caracteres", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            if(generoEnum == null) {
                Toast.makeText(this, "Seleccioná un género válido", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val nuevaPelicula = Pelicula(titulo, year, comentario, generoEnum, imagen)
            navegarAListaPeliculas(nuevaPelicula, "crear")
        }
    }

    private fun navegarAListaPeliculas(peli: Pelicula, estado: String){
        val intent = Intent(this, ListaPeliculasActivity::class.java)
        intent.putExtra("peliculaGuardada", peli)
        intent.putExtra("estado", estado)
        startActivity(intent)
    }
}