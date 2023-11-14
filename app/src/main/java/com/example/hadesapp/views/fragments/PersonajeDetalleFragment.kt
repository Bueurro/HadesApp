package com.example.hadesapp.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hadesapp.R
import com.example.hadesapp.databinding.FragmentPersonajeDetalleBinding
import com.example.hadesapp.databinding.FragmentPersonajesBinding
import com.example.hadesapp.models.Personaje
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class PersonajeDetalleFragment : Fragment() {

    private val args:PersonajeDetalleFragmentArgs by navArgs()
    private lateinit var binding: FragmentPersonajeDetalleBinding
    private var mContext = this.context
    private val db = Firebase.firestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentPersonajeDetalleBinding.inflate(inflater, container, false)

        binding.imgBtnVolver.setOnClickListener{
            findNavController().navigate(R.id.action_personajeDetalleFragment_to_personajesFragment)
        }

        obtenerPersonaje(args.idPersonaje) { personaje ->
            // Manejar el resultado aquí
            if (personaje != null) {
                cargarDatos(personaje)
            } else {
                Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    fun obtenerPersonaje(id: String, callback: (Personaje?) -> Unit) {
        val db = Firebase.firestore

        val docRef = db.collection("Personajes").document(id.toString())
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val id = document.id
                    val nombre = document.getString("Nombre") ?: ""
                    val categoria = document.getString("Categoria") ?: ""
                    val titulo = document.getString("Titulo") ?: ""
                    val regalo = document.getString("Regalo") ?: ""
                    val foto = document.getString("Foto") ?: ""
                    val descripcion = document.getString("Descripcion") ?: ""

                    val pj = Personaje(id, nombre, categoria, titulo, regalo, foto, descripcion)
                    callback(pj)
                } else {
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(mContext, "Error al recuperar personaje", Toast.LENGTH_SHORT).show()
                callback(null)
            }
    }


    fun cargarDatos(personaje:Personaje){
        binding.txtNombrePj.text = personaje.nombre.toString()
        binding.txtCategoriaPj.text = personaje.categoria.toString()
        binding.txtTituloPj.text = personaje.titulo.toString()
        binding.txtRegaloPj.text = personaje.regalo.toString()
        binding.txtDescripcionPj.text = personaje.descripcion.toString()
        Glide.with(requireContext())
            .load(personaje.foto)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .centerCrop()
            .into(binding.imgPjPic)

    }


}