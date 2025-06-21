package com.example.tp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.tp.Pelicula
import com.example.tp.R
import com.example.tp.databinding.ItemPeliculaBinding

class PeliculaAdapter(
    private val list: List<Pelicula>,
    private val onEditarClick: (Pelicula) -> Unit,
    private val onEliminarClick: (Pelicula) -> Unit
): RecyclerView.Adapter<PeliculaViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PeliculaViewHolder {
        val binding = ItemPeliculaBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PeliculaViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: PeliculaViewHolder, position: Int) {
        val item = list[position]

        holder.binding.tvTitulo.text = item.titulo
        holder.binding.tvGenero.text = item.genero.nombre
        holder.binding.tvYear.text = item.estreno.toString()
        holder.binding.tvComentario.text = item.comentario

        Glide.with(holder.itemView.context)
            .load(item.imagenUri.toString())
            .placeholder(R.drawable.no_image)
            .into(holder.binding.itemImgPel)

        holder.binding.btnEditar.setOnClickListener { onEditarClick(item) }
        holder.binding.btnBorrar.setOnClickListener { onEliminarClick(item) }
    }
}