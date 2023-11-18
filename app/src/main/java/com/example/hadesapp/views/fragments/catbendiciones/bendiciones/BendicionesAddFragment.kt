package com.example.hadesapp.views.fragments.catbendiciones.bendiciones

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hadesapp.R
import com.example.hadesapp.databinding.FragmentBendicionesAddBinding
import com.example.hadesapp.models.Aspecto
import com.example.hadesapp.models.Bendicion
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BendicionesAddFragment : Fragment() {
    private lateinit var binding: FragmentBendicionesAddBinding
    private val args : BendicionesAddFragmentArgs by navArgs()
    private val db = Firebase.firestore
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentBendicionesAddBinding.inflate(inflater, container, false)

        binding.btnGuardar.setOnClickListener {
            agregarBendicion()
        }

        binding.btnCanelar.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLimpiar.setOnClickListener {
            limpiarCampos()
        }

        return binding.root
    }

    private fun limpiarCampos() {
        binding.inpCrdNombre.text.clear()
        binding.inpCrdEfecto.text.clear()
        binding.inpCrdFoto.text.clear()
    }

    private fun agregarBendicion() {
        val nombre = binding.inpCrdNombre.text.toString()
        val foto = binding.inpCrdFoto.text.toString()
        val efectoDesc = binding.inpCrdEfecto.text.toString()

        if (nombre.isNotEmpty()) {
            val bendicion = Bendicion(nombre, efectoDesc, foto)
            val idCatBen = args.idCatBen
            db.collection("CatBendiciones").document(idCatBen)
                .update("bendiciones.${nombre}", bendicion)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Bendición '$nombre' agregada correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error al agregar la bendición: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(
                requireContext(),
                "El nombre de la bendición no puede estar vacío",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}