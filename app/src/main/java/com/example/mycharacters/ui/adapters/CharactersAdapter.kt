package com.example.mycharacters.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mycharacters.databinding.ElementoListaBinding
import com.example.mycharacters.model.Personaje

/**
 * Creado por Romero Rivera Geovanni el 28/11/23
 */
class CharactersAdapter(

    private var personajes: ArrayList<Personaje>,
    private var onCharacterClicked: (Personaje) -> Unit

) : RecyclerView.Adapter<CharactersAdapter.ViewHolder>() {

    class ViewHolder(private var binding: ElementoListaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(personaje: Personaje) {
            Glide.with(itemView.context)
                .load(personaje.imageUrl)
                .into(binding.ivIcono)
            binding.tvFullName.text = personaje.fullName
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ElementoListaBinding.inflate(LayoutInflater.from(parent.context))
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = personajes.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(personajes[position])

        holder.itemView.setOnClickListener {
            onCharacterClicked(personajes[position])
        }
    }

}