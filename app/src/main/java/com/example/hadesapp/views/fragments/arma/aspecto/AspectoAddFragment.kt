package com.example.hadesapp.views.fragments.arma.aspecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.hadesapp.databinding.FragmentAspectoAddBinding
import com.example.hadesapp.models.Aspecto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AspectoAddFragment : Fragment() {
    private lateinit var binding : FragmentAspectoAddBinding
    private val args : AspectoAddFragmentArgs by navArgs()
    private var mContext = this.context
    private val db = Firebase.firestore

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentAspectoAddBinding.inflate(inflater, container, false)

        binding.btnGuardarAspecto.setOnClickListener {
            agregarAspecto()
        }

        binding.btnCancelarAspecto.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.btnLimpiarAspecto.setOnClickListener {
            limpiarCampos()
        }

        return binding.root
    }

    private fun limpiarCampos() {
        binding.inpAspectoNombre.text.clear()
         binding.inpAspectoCoste.text.clear()
        binding.inpAspectoDescripcion.text.clear()
        binding.inpAspectoFoto.text.clear()
        binding.inpAspectoFotoInGame.text.clear()
        binding.inpAspectoMejora.text.clear()
    }

    private fun agregarAspecto() {
        val nombre = binding.inpAspectoNombre.text.toString()
        val coste = binding.inpAspectoCoste.text.toString()
        val descripcion = binding.inpAspectoDescripcion.text.toString()
        val foto = binding.inpAspectoFoto.text.toString()
        val fotoInGame = binding.inpAspectoFotoInGame.text.toString()
        val mejora = binding.inpAspectoMejora.text.toString()

        if (nombre.isNotEmpty()) {
            val aspecto = Aspecto(nombre, coste, descripcion, foto, fotoInGame, mejora)
            val idArma = args.idAr
            db.collection("Armas").document(idArma)
                .update("aspectos.${nombre}", aspecto)
                .addOnSuccessListener {
                    Toast.makeText(
                        requireContext(),
                        "Aspecto '$nombre' agregado correctamente",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(
                        requireContext(),
                        "Error al agregar el aspecto: $e",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        } else {
            Toast.makeText(
                requireContext(),
                "El nombre del aspecto no puede estar vacío",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

}