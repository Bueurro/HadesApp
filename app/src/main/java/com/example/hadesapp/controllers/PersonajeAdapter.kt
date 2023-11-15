package com.example.hadesapp.controllers

import android.content.Context
import android.util.Log
import android.util.LogPrinter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hadesapp.R
import com.example.hadesapp.models.Personaje
import com.example.hadesapp.databinding.ItemPersonajesBinding
import com.example.hadesapp.views.fragments.PersonajesFragment
import com.example.hadesapp.views.fragments.PersonajesFragmentDirections
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonajeAdapter(private var personajes:MutableList<Personaje>, private var listener: OnClickListener) : RecyclerView.Adapter<PersonajeAdapter.ViewHolder>(){

    private lateinit var context : Context

    //Sirve para Inflar la vista
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_personajes, parent, false)
        return ViewHolder(view)
    }


    //Rellena la vista (puede que contenga algo asi como PersonajeAdapter.ViewHolder!! borrar en caso de)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val personaje = personajes.get(position) //obtiene el pj a manera de foreach

        with(holder){
            setListener(personaje, position)
            binding.txtNombrePj.text = personaje.nombre.toString()
            binding.txtCategoria.text = personaje.categoria
            Glide.with(context)
                .load(personaje.foto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgBtnPersonaje)
        }
    }


    //Contador del adapter
    override fun getItemCount(): Int = personajes.size


    //Crear esta clase para instanciar el ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        //conectar el binding con el binding deseado
        val binding = ItemPersonajesBinding.bind(view)

        fun setListener(personaje: Personaje, position: Int) {
            binding.root.setOnClickListener{ listener.onClick(personaje, position) }
        }
    }
    fun updateData(newData: MutableList<Personaje>) {
        personajes = newData
        notifyDataSetChanged()
    }

    fun remove(adapterPosition: Int) {
        val removedPersonaje = personajes[adapterPosition]

        Firebase.firestore.collection("Personajes")
            .document(removedPersonaje.id)
            .delete()
            .addOnSuccessListener {
                personajes.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
            .addOnFailureListener { e: Exception ->
                e.printStackTrace()
            }
    }

    fun edit(adapterPosition: Int) : String {
        val editedPersonaje = personajes[adapterPosition]
        return editedPersonaje.id
    }


}