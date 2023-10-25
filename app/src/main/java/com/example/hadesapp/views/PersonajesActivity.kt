package com.example.hadesapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hadesapp.controllers.OnClickListener
import com.example.hadesapp.controllers.PersonajeAdapter
import com.example.hadesapp.models.Personaje
import com.example.hadesapp.databinding.ActivityPersonajesBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
class PersonajesActivity : AppCompatActivity(), OnClickListener {
    override fun onClick(personaje: Personaje, position: Int) {
        Toast.makeText(this, "${personaje.id}: ${personaje.toString()}", Toast.LENGTH_SHORT).show()
    }

    private lateinit var binding: ActivityPersonajesBinding
    private lateinit var personajeAdapter: PersonajeAdapter
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPersonajesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // INICIALIZAR

        personajeAdapter = PersonajeAdapter(emptyList(), this)
        linearLayoutManager = LinearLayoutManager(this)

        // RECYCLEVIEW
        binding.rvPersonajes.apply {
            layoutManager = linearLayoutManager
            adapter = personajeAdapter
        }

        binding.imgBtnVolver.setOnClickListener{
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Llama a la función para obtener los personajes
        getPersonaje { personajes ->
            // Actualiza el adaptador con los personajes obtenidos
            personajeAdapter.updateData(personajes)
        }
    }

    private fun getPersonaje(callback: (List<Personaje>) -> Unit) {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        val listaPersonajes = mutableListOf<Personaje>()

        db.collection("Personajes").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progressBar.visibility = View.GONE
                for (document in task.result!!) {
                    val id = document.id
                    val nombre = document.getString("Nombre") ?: ""
                    val categoria = document.getString("Categoria") ?: ""
                    val titulo = document.getString("Titulo") ?: ""
                    val regaloBendicion = document.getString("RegaloBendicion") ?: ""
                    val foto = document.getString("Foto") ?: ""
                    val descripcion = document.getString("Descripcion") ?: ""

                    val personaje = Personaje(id, nombre, categoria, titulo, regaloBendicion, foto, descripcion)
                    listaPersonajes.add(personaje)
                }
                callback(listaPersonajes)
            } else {
                // Manejar el error de la consulta a Firestore aquí
                callback(emptyList())
            }
        }
    }
}