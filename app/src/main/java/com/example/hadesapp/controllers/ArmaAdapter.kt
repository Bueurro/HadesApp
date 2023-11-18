package com.example.hadesapp.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hadesapp.R
import com.example.hadesapp.databinding.ItemArmasBinding
import com.example.hadesapp.models.Arma
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ArmaAdapter(private var armas:MutableList<Arma>, private var listener: OnClickListener) : RecyclerView.Adapter<ArmaAdapter.ViewHolder>() {

    private lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArmaAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_armas, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ArmaAdapter.ViewHolder, position: Int) {
        val arma = armas.get(position) //obtiene la arma a manera de foreach

        with(holder){
            setListener(arma, position)
            binding.txtNombreArma.text = arma.nombre
            Glide.with(context)
                .load(arma.foto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(binding.imgArma)
        }
    }

    //Contador del adapter
    override fun getItemCount(): Int = armas.size


    //Crear esta clase para instanciar el ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        //conectar el binding con el binding deseado
        val binding = ItemArmasBinding.bind(view)

        fun setListener(arma: Arma, position: Int) {
            binding.root.setOnClickListener{ listener.onClickArma(arma, position) }
        }
    }



    fun remove(adapterPosition: Int) {
        val removedArma = armas[adapterPosition]

        Firebase.firestore.collection("Armas")
            .document(removedArma.id)
            .delete()
            .addOnSuccessListener {
                armas.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
            .addOnFailureListener { e: Exception ->
                e.printStackTrace()
            }
    }

    fun edit(adapterPosition: Int) : String {
        val editedArma = armas[adapterPosition]
        return editedArma.id
    }

    fun updateData(newData: MutableList<Arma>) {
        armas = newData
        notifyDataSetChanged()
    }

}