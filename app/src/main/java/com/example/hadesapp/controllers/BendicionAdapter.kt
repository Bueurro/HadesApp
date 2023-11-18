package com.example.hadesapp.controllers

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hadesapp.R
import com.example.hadesapp.databinding.ItemAspectosBinding
import com.example.hadesapp.databinding.ItemBendicionBinding
import com.example.hadesapp.models.Aspecto
import com.example.hadesapp.models.Bendicion

class BendicionAdapter(private var bendiciones:Map<String, Bendicion>, private var listener: OnClickListener) : RecyclerView.Adapter<BendicionAdapter.ViewHolder>()  {

    private lateinit var context : Context
    private lateinit var mList: MutableList<Bendicion>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BendicionAdapter.ViewHolder {
        context = parent.context
        val view = LayoutInflater.from(context).inflate(R.layout.item_bendicion, parent, false)
        mList = bendiciones.values.toMutableList()
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: BendicionAdapter.ViewHolder, position: Int) {
        val bendicion = mList.get(position)

        with(holder){
            setListener(bendicion, position)
            binding.txtNombreBend.text = bendicion.nombre
            binding.txtEfectoDesc.text = bendicion.efectoDesc
            Glide.with(context)
                .load(bendicion.foto)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(binding.imgBendicion)
        }
    }

    override fun getItemCount(): Int = bendiciones.size

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        val binding = ItemBendicionBinding.bind(view)

        fun setListener(bendicion: Bendicion, position: Int) {
            binding.root.setOnClickListener{ listener.onClickBendicion(bendicion, position) }
        }
    }

    fun updateData(newData: Map<String, Bendicion>) {
        bendiciones = newData
        notifyDataSetChanged()
    }

}