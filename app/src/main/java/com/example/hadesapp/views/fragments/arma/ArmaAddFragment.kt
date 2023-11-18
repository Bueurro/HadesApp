package com.example.hadesapp.views.fragments.arma

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hadesapp.databinding.FragmentArmaAddBinding
import com.example.hadesapp.models.Arma
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ArmaAddFragment : Fragment() {
    private lateinit var binding : FragmentArmaAddBinding
    private val args:ArmaAddFragmentArgs by navArgs()
    private var mContext = this.context
    private val db = Firebase.firestore

    override fun onCreateView(  inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentArmaAddBinding.inflate(inflater, container, false)

        if (args.idAr!=null){
            cargarDatos()
            binding.btnGuardar.setOnClickListener {
                fragHideKeyboard()
                actualizarArma() }
            binding.btnLimpiar.visibility = View.GONE
        } else {
            binding.btnGuardar.setOnClickListener {
                fragHideKeyboard()
                guardarArma() }
            binding.btnLimpiar.visibility = View.VISIBLE
            binding.btnLimpiar.setOnClickListener {
                limpiarCampos()
                fragHideKeyboard()}
        }

        binding.btnCanelar.setOnClickListener {
            findNavController().popBackStack()
            fragHideKeyboard()
        }

        return binding.root
    }


    private fun cargarDatos() {
        val armaBase = db.collection("Armas").document(args.idAr.toString())

        armaBase.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val nombre = documentSnapshot.getString("nombre")
                    val antiguoPortador = documentSnapshot.getString("antiguoPortador")
                    val condicion = documentSnapshot.getString("condicion")
                    val estilo = documentSnapshot.getString("estilo")
                    val descripcion = documentSnapshot.getString("descripcion")
                    val foto = documentSnapshot.getString("foto")

                    binding.inpCrdNombre.setText(nombre)
                    binding.inpCrdAP.setText(antiguoPortador)
                    binding.inpCrdCondicion.setText(condicion)
                    binding.inpCrdEstilo.setText(estilo)
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

    private fun actualizarArma() {
        val armaEditada = Arma(
            id = "temp",
            nombre = binding.inpCrdNombre.text.toString(),
            foto = binding.inpCrdFoto.text.toString(),
            descripcion = binding.inpCrdAP.text.toString(),
            condicion = binding.inpCrdCondicion.text.toString(),
            estilo = binding.inpCrdEstilo.text.toString(),
            antiguoPortador = binding.inpCrdDescripcion.text.toString(),
            aspectos = emptyMap()
        )
        val armaBase = db.collection("Armas").document(args.idAr.toString())
        armaBase
            .update(mapOf(
                "nombre" to armaEditada.nombre,
                "foto" to armaEditada.foto,
                "descripcion" to armaEditada.descripcion,
                "condicion" to armaEditada.condicion,
                "estilo" to armaEditada.estilo,
                "antiguoPortador" to armaEditada.antiguoPortador
            ))
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!")
                Toast.makeText(requireContext(), "Arma: ${armaEditada.nombre}. Editada satisfactoriamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error updating document", e)
                Toast.makeText(requireContext(), "Error actualizando el personaje: ${args.idAr}", Toast.LENGTH_SHORT).show()
            }
    }

    fun fragHideKeyboard(){
        val imm = this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }
    fun Fragment.configurarInputs(){

        binding.inpCrdNombre.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdAP.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.inpCrdAP.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdCondicion.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.inpCrdCondicion.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdDescripcion.requestFocus()
                return@setOnKeyListener true
            }
            false
        }
        binding.inpCrdDescripcion.setOnKeyListener { _, keyCode, event ->
            if (keyCode == KeyEvent.KEYCODE_ENTER && event.action == KeyEvent.ACTION_UP) {
                binding.inpCrdEstilo.requestFocus()
                return@setOnKeyListener true
            }
            false
        }

        binding.inpCrdEstilo.setOnKeyListener { _, keyCode, event ->
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
        binding.inpCrdEstilo.text.clear()
        binding.inpCrdAP.text.clear()
        binding.inpCrdCondicion.text.clear()
        binding.inpCrdDescripcion.text.clear()
    }

    fun validarCampos(): Boolean{
        return (binding.inpCrdNombre.text.toString() != "" || binding.inpCrdFoto.text.toString() != "" || binding.inpCrdEstilo.text.toString() != "" || binding.inpCrdAP.text.toString() != "" || binding.inpCrdCondicion.text.toString() != "" || binding.inpCrdDescripcion.text.toString() != "")
    }

    private fun guardarArma() {
        if (validarCampos()) {
            val data = hashMapOf(
                "nombre" to binding.inpCrdNombre.text.toString(),
                "foto" to binding.inpCrdFoto.text.toString(),
                "condicion" to binding.inpCrdCondicion.text.toString(),
                "estilo" to binding.inpCrdEstilo.text.toString(),
                "antiguoPortador" to binding.inpCrdAP.text.toString(),
                "descripcion" to binding.inpCrdDescripcion.text.toString(),
                "aspectos" to mapOf<String,String>()
            )
            db.collection("Armas")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    Log.d(ContentValues.TAG, "Se ha creado el documento de ID: ${documentReference.id}")
                    Toast.makeText(this.context, "Se ha creado el documento de ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
                    limpiarCampos()
                }
                .addOnFailureListener { e ->
                    Log.w(ContentValues.TAG, "Error añadiendo el documento: ", e)
                    Toast.makeText(this.context, "Error añadiendo el documento: $e", Toast.LENGTH_SHORT).show()
                }
        } else {
            Toast.makeText(this.context, "Porfavor rellene todos los campos. No se ha agregado la Arma.", Toast.LENGTH_SHORT).show()
        }
    }

}