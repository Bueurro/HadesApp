package com.example.hadesapp.views.fragments.catbendiciones

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hadesapp.R
import com.example.hadesapp.databinding.FragmentCatBendicionesAddBinding
import com.example.hadesapp.models.Arma
import com.example.hadesapp.models.CategoriaBendicion
import com.example.hadesapp.views.fragments.arma.ArmaAddFragmentArgs
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CatBendicionesAddFragment : Fragment() {
    private lateinit var binding: FragmentCatBendicionesAddBinding
    private val args: CatBendicionesAddFragmentArgs by navArgs()
    private var mContext = this.context
    private val db = Firebase.firestore
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentCatBendicionesAddBinding.inflate(inflater, container, false)

        if (args.idCatBen!=null){
            cargarDatos()
            binding.btnGuardar.setOnClickListener {
                fragHideKeyboard()
                actualizarCatBendicion() }
            binding.btnLimpiar.visibility = View.GONE
        } else {
            binding.btnGuardar.setOnClickListener {
                fragHideKeyboard()
                guardarCatBendicion() }
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
        val armaBase = db.collection("CatBendiciones").document(args.idCatBen.toString())

        armaBase.get()
            .addOnSuccessListener { documentSnapshot ->
                if (documentSnapshot.exists()) {
                    val nombreCat = documentSnapshot.getString("nombreCat")
                    val efecto = documentSnapshot.getString("efecto")
                    val foto = documentSnapshot.getString("foto")

                    binding.inpCrdNombre.setText(nombreCat)
                    binding.inpCrdEfecto.setText(efecto)
                    binding.inpCrdFoto.setText(foto)

                } else {
                    Toast.makeText(mContext, "Error desconocido", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(mContext, "Error al cargar datos", Toast.LENGTH_SHORT).show()
            }
    }
    private fun actualizarCatBendicion() {
        val CatBendEditada = CategoriaBendicion(
            id = "temp",
            nombreCat = binding.inpCrdNombre.text.toString(),
            foto = binding.inpCrdFoto.text.toString(),
            efecto = binding.inpCrdEfecto.text.toString(),
            bendiciones = emptyMap()
        )
        val CatBendBase = db.collection("CatBendiciones").document(args.idCatBen.toString())
        CatBendBase
            .update(mapOf(
                "nombreCat" to CatBendEditada.nombreCat,
                "efecto" to CatBendEditada.efecto,
                "foto" to CatBendEditada.foto
            ))
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "DocumentSnapshot successfully updated!")
                Toast.makeText(requireContext(), "Categoría: ${CatBendEditada.nombreCat}. Editada satisfactoriamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {e ->
                Log.w(ContentValues.TAG, "Error updating document", e)
                Toast.makeText(requireContext(), "Error actualizando La Categoría: ${args.idCatBen}", Toast.LENGTH_SHORT).show()
            }
    }

    fun fragHideKeyboard(){
        val imm = this.context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.root.windowToken, 0)
    }

    fun limpiarCampos() {
        binding.inpCrdFoto.text.clear()
        binding.inpCrdNombre.text.clear()
        binding.inpCrdEfecto.text.clear()
    }

    fun validarCampos(): Boolean{
        return (binding.inpCrdNombre.text.toString() != "" || binding.inpCrdFoto.text.toString() != "" || binding.inpCrdEfecto.text.toString() != "")
    }

    private fun guardarCatBendicion() {
        if (validarCampos()) {
            val data = hashMapOf(
                "nombreCat" to binding.inpCrdNombre.text.toString(),
                "foto" to binding.inpCrdFoto.text.toString(),
                "efecto" to binding.inpCrdEfecto.text.toString(),
                "bendiciones" to mapOf<String,String>()
            )
            db.collection("CatBendiciones")
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
            Toast.makeText(this.context, "Porfavor rellene todos los campos. No se ha agregado la Categoría.", Toast.LENGTH_SHORT).show()
        }
    }

}