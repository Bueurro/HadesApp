package com.example.hadesapp

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.hadesapp.databinding.ItemPersonajesBinding

class PersonajeAdapter(private val personajes:List<Personaje>) : RecyclerView.Adapter<PersonajeAdapter.ViewHolder>(){

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
            binding.txtNombrePj.text = personaje.nombre.toString()
            binding.txtCategoria.text = personaje.categoria
        }
    }


    //Contador del adapter
    override fun getItemCount(): Int = personajes.size


    //Crear esta clase para instanciar el ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        //conectar el binding con el binding deseado
        val binding = ItemPersonajesBinding.bind(view)
    }
}