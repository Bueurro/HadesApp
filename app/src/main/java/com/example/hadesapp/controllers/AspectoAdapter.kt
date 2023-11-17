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
import com.example.hadesapp.databinding.ItemAspectosBinding
import com.example.hadesapp.databinding.ItemPersonajesBinding
import com.example.hadesapp.models.Arma
import com.example.hadesapp.models.Aspecto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AspectoAdapter(private var aspectos:Map<String, Aspecto>, private var listener: OnClickListener) : RecyclerView.Adapter<AspectoAdapter.ViewHolder>() {

    private lateinit var context : Context
    private lateinit var mList: MutableList<Aspecto>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AspectoAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_aspectos, parent, false)
        mList = aspectos.values.toMutableList()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: AspectoAdapter.ViewHolder, position: Int) {
        val aspecto = mList.get(position) //obtiene la arma a manera de foreach

        with(holder){
            setListener(aspecto, position)
            binding.txtNombreAs.text = aspecto.nombre
            binding.txtCondicionAs.text = aspecto.descripcion
            Glide.with(context)
                .load(aspecto.foto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(binding.imgAspecto)
        }
    }

    //Contador del adapter
    override fun getItemCount(): Int = aspectos.size

    //Crear esta clase para instanciar el ViewHolder
    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        //conectar el binding con el binding deseado
        val binding = ItemAspectosBinding.bind(view)

        fun setListener(aspecto: Aspecto, position: Int) {
            binding.root.setOnClickListener{ listener.onClickAspecto(aspecto, position) }
        }
    }

    fun updateData(newData: Map<String, Aspecto>) {
        aspectos = newData
        notifyDataSetChanged()
    }

}