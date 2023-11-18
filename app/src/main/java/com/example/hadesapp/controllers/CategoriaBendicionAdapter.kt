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
import com.example.hadesapp.databinding.ItemCatBendicionBinding
import com.example.hadesapp.models.CategoriaBendicion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CategoriaBendicionAdapter(private var categorias:MutableList<CategoriaBendicion>, private var listener: OnClickListener) : RecyclerView.Adapter<CategoriaBendicionAdapter.ViewHolder>() {

    private lateinit var context : Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriaBendicionAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_cat_bendicion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoriaBendicionAdapter.ViewHolder, position: Int) {
        val categoria = categorias.get(position)

        with(holder){
            setListener(categoria, position)
            binding.txtNombreCat.text = categoria.nombreCat
            binding.txtEfecto.text = categoria.efecto
            Glide.with(context)
                .load(categoria.foto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(binding.imgBoon)
        }
    }

    override fun getItemCount(): Int = categorias.size


    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemCatBendicionBinding.bind(view)

        fun setListener(categoria: CategoriaBendicion, position: Int) {
            binding.root.setOnClickListener{ listener.onClickCategoria(categoria, position) }
        }
    }

    fun remove(adapterPosition: Int) {
        val removedCategoria = categorias[adapterPosition]

        Firebase.firestore.collection("CatBendiciones")
            .document(removedCategoria.id)
            .delete()
            .addOnSuccessListener {
                categorias.removeAt(adapterPosition)
                notifyItemRemoved(adapterPosition)
            }
            .addOnFailureListener { e: Exception ->
                e.printStackTrace()
            }
    }

    fun edit(adapterPosition: Int) : String {
        val editedCat = categorias[adapterPosition]
        return editedCat.id
    }

    fun updateData(newData: MutableList<CategoriaBendicion>) {
        categorias = newData
        notifyDataSetChanged()
    }
}