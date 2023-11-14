package com.example.hadesapp.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hadesapp.R
import com.example.hadesapp.controllers.OnClickListener
import com.example.hadesapp.controllers.PersonajeAdapter
import com.example.hadesapp.databinding.FragmentPersonajesBinding
import com.example.hadesapp.models.Personaje
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.LinkedBlockingQueue


class PersonajesFragment : Fragment(), OnClickListener {

    private lateinit var binding: FragmentPersonajesBinding
    private var mContext = this.context
    private lateinit var personajeAdapter: PersonajeAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private val db = Firebase.firestore

    override fun onClick(personaje: Personaje, position: Int) {
        findNavController().navigate(PersonajesFragmentDirections.actionPersonajesFragmentToPersonajeDetalleFragment(personaje.id))
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
         binding = FragmentPersonajesBinding.inflate(inflater, container, false)

        // INICIALIZAR
        personajeAdapter = PersonajeAdapter(emptyList(), this)
        linearLayoutManager = LinearLayoutManager(mContext)

        // RECYCLEVIEW
        binding.rvPersonajes.apply {
            layoutManager = linearLayoutManager
            adapter = personajeAdapter
        }

        binding.imgBtnVolver.setOnClickListener{
            findNavController().navigate(R.id.action_personajesFragment_to_mainFragment)
        }

        // Llama a la función para obtener los personajes
        getPersonaje { personajes ->
            // Actualiza el adaptador con los personajes obtenidos
            personajeAdapter.updateData(personajes)
        }

        //swipe refresher
        val swipe : SwipeRefreshLayout = binding.swipeRefresherLayout
        swipe.setOnRefreshListener {
            //obtiene la lista denuevo
            getPersonaje { personajes ->
                // Actualiza el adaptador con los personajes obtenidos
                personajeAdapter.updateData(personajes)
            }
            //notifica el cambio
            personajeAdapter.notifyDataSetChanged()
            //detiene el refresh
            swipe.isRefreshing = false
        }

        return binding.root
    }

    private fun getPersonaje(callback: (List<Personaje>) -> Unit) {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        val db = Firebase.firestore
        val listaPersonajes = mutableListOf<Personaje>()
        val queue = LinkedBlockingQueue<MutableList<Personaje>>()
        Thread {
            db.collection("Personajes").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.GONE
                    for (document in task.result!!) {
                        val id = document.id
                        val nombre = document.getString("Nombre") ?: ""
                        val categoria = document.getString("Categoria") ?: ""
                        val titulo = document.getString("Titulo") ?: ""
                        val regaloBendicion = document.getString("Regalo") ?: ""
                        val foto = document.getString("Foto") ?: ""
                        val descripcion = document.getString("Descripcion") ?: ""

                        val personaje = Personaje(id, nombre, categoria, titulo, regaloBendicion, foto, descripcion)
                        listaPersonajes.add(personaje)
                    }
                    queue.add(listaPersonajes)
                    callback(queue.take())
                } else {
                    // Manejar el error de la consulta a Firestore aquí
                    callback(emptyList())
                }
            }
        }.start()
    }

}