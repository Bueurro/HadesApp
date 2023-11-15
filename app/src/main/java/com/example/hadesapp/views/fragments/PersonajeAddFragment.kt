package com.example.hadesapp.views.fragments

import android.content.ContentValues.TAG
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.example.hadesapp.R
import com.example.hadesapp.databinding.FragmentPersonajeAddBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import android.view.KeyEvent
import android.view.inputmethod.InputMethodManager
import androidx.core.content.getSystemService
import androidx.navigation.fragment.navArgs
import com.example.hadesapp.models.Personaje

class PersonajeAddFragment : Fragment() {

    private val args:PersonajeAddFragmentArgs by navArgs()
    private lateinit var binding: FragmentPersonajeAddBinding
    private var mContext = this.context
    private val db = Firebase.firestore

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        binding = FragmentPersonajeAddBinding.inflate(inflater, container, false)

        if (args.idPersonaje!=null || args.idPersonaje!=""){
            cargarDatos()
            binding.btnGuardar.setOnClickListener {
                fragHideKeyboard()
                actualizarPersonaje() }
            binding.btnLimpiar.visibility = View.GONE
        } else {
            binding.btnGuardar.setOnClickListener {
                fragHideKeyboard()
                guardarPersonaje() }
            binding.btnLimpiar.setOnClickListener {
                limpiarCampos()
                fragHideKeyboard()}
        }

        binding.btnCanelar.setOnClickListener {
            findNavController().popBackStack()
            fragHideKeyboard()}

        return binding.root
    }

    private fun cargarDatos() {
        val personajeBase = db.collection("Personajes").document(args.idPersonaje.toString())

        personajeBase.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val nombre = documentSnapshot.getString("Nombre")
                    val titulo = documentSnapshot.getString("Titulo")
                    val categoria = documentSnapshot.getString("Categoria")
                    val regalo = documentSnapshot.getString("Regalo")
                    val descripcion = documentSnapshot.getString("Descripcion")
                    val foto = documentSnapshot.getString("Foto")

                    binding.inpCrdNombre.setText(nombre)
                    binding.inpCrdTitulo.setText(titulo)
                    binding.inpCrdCategoria.setText(categoria)
                    binding.inpCrdRegalo.setText(regalo)
                    binding.inpCrdDescripcion.setText(descripcion)
                    binding.inpCrdFoto.setText(foto)
                } else {
                    Toast.makeText(mContext, "Error desconocido", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(mContext, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
    }

    private fun actualizarPersonaje() {
        val personajeEditado = Personaje(
            id = "temp",
            nombre = binding.inpCrdNombre.text.toString(),
            foto = binding.inpCrdFoto.text.toString(),
            titulo = binding.inpCrdTitulo.text.toString(),
            categoria = binding.inpCrdCategoria.text.toString(),
            regalo = binding.inpCrdRegalo.text.toString(),
            descripcion = binding.inpCrdDescripcion.text.toString()
        )
        val personajeBase = db.collection("Personajes").document(args.idPersonaje.toString())
        personajeBase
            .update(mapOf(
                "Nombre" to personajeEditado.nombre,
                "Foto" to personajeEditado.foto,
                "Titulo" to personajeEditado.titulo,
                "Categoria" to personajeEditado.categoria,
                "Regalo" to personajeEditado.regalo,
                "Descripcion" to personajeEditado.descripcion
            ))
            .addOnSuccessListener {
                Log.d(TAG, "DocumentSnapshot successfully updated!")
                Toast.makeText(requireContext(), "Personaje: ${personajeEditado.nombre}. Editado satisfactoriamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                    e -> Log.w(TAG, "Error updating document", e)
                Toast.makeText(requireContext(), "Error actualizando el personaje: ${args.idPersonaje}", Toast.LENGTH_SHORT).show()
            }
    }

    fun fragHideKeyboard(){
        val imm = this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.inpCrdDescripcion.windowToken, 0)
    }
    fun Fragment.configurarInputs(){

        binding.inpCrdNombre.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdCategoria.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.inpCrdCategoria.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdTitulo.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.inpCrdTitulo.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdRegalo.requestFocus()
                return@setOnKeyListener true
            }
            false
        }
        binding.inpCrdRegalo.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdDescripcion.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.inpCrdRegalo.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdFoto.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.inpCrdFoto.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                // Mueve el foco al siguiente campo de entrada
                binding.inpCrdNombre.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.inpCrdDescripcion.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                val imm = this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(binding.inpCrdDescripcion.windowToken, 0)
                return@setOnKeyListener true
            }
            false
        }
    }

    fun limpiarCampos() {
        binding.inpCrdFoto.text.clear()
        binding.inpCrdNombre.text.clear()
        binding.inpCrdTitulo.text.clear()
        binding.inpCrdCategoria.text.clear()
        binding.inpCrdRegalo.text.clear()
        binding.inpCrdDescripcion.text.clear()
    }

    fun validarCampos(): Boolean{
        return (binding.inpCrdNombre.text.toString() != "" || binding.inpCrdFoto.text.toString() != "" || binding.inpCrdTitulo.text.toString() != "" || binding.inpCrdCategoria.text.toString() != "" || binding.inpCrdRegalo.text.toString() != "" || binding.inpCrdDescripcion.text.toString() != "")
    }

    private fun guardarPersonaje() {
        if (validarCampos()) {
            val data = hashMapOf(
                "Nombre" to binding.inpCrdNombre.text.toString(),
                "Foto" to binding.inpCrdFoto.text.toString(),
                "Titulo" to binding.inpCrdTitulo.text.toString(),
                "Categoria" to binding.inpCrdCategoria.text.toString(),
                "Regalo" to binding.inpCrdRegalo.text.toString(),
                "Descripcion" to binding.inpCrdDescripcion.text.toString()
            )
            db.collection("Personajes")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d(TAG, "Se ha creado el documento de ID: ${documentReference.id}")
                    Toast.makeText(this.context, "Se ha creado el documento de ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                }
                .addOnFailureListener { e ->
                    Log.w(TAG, "Error añadiendo el documento: ", e)
                    Toast.makeText(this.context, "Error añadiendo el documento: $e", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this.context, "Porfavor rellene todos los campos. No se ha agregado el personaje.", Toast.LENGTH_SHORT).show()
        }
    }


}