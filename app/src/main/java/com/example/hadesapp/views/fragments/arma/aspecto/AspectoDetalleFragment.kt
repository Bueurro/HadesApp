package com.example.hadesapp.views.fragments.arma.aspecto

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hadesapp.R
import com.example.hadesapp.databinding.FragmentAspectoDetalleBinding
import com.example.hadesapp.models.Arma
import com.example.hadesapp.models.Aspecto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AspectoDetalleFragment : Fragment() {
    private val args: AspectoDetalleFragmentArgs by navArgs()
    private lateinit var binding: FragmentAspectoDetalleBinding
    private var mList = mapOf<String, Aspecto>()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        binding = FragmentAspectoDetalleBinding.inflate(inflater, container, false)

        getAspectoByNameInArma(args.idAr,args.nomAs) { arma ->
            if (arma != null) {
                cargarDatos(arma)
            } else {
                Toast.makeText(requireContext(), "No se encontró el arma", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgBtnVolver.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun cargarDatos(aspecto: Aspecto) {
        binding.txtNombreAs.text = aspecto.nombre
        binding.txtCosteAs.text = aspecto.coste
        binding.txtDescipcionAs.text = aspecto.descripcion
        binding.txtMejoraAs.text = aspecto.mejora
        cargarFoto(aspecto.foto, binding.imgAspecto)
        cargarFoto(aspecto.fotoInGame, binding.imgFotoInGame)
    }

    fun cargarFoto(foto:String,fotoInGame: ImageView){
        Glide.with(requireContext())
            .load(foto)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(fotoInGame)
    }

    private fun getAspectoByNameInArma(idArma: String, aspectoName: String, callback: (Aspecto?) -> Unit) {
        val db = Firebase.firestore
        db.collection("Armas").document(idArma).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val aspectosMap =
                        document.get("aspectos") as? Map<String, *> ?: emptyMap<String, Any>()
                    val aspectos = aspectosMap.mapValues { (nombre, detalles) ->
                        if (detalles is Map<*, *>) {
                            Aspecto(
                                nombre,
                                detalles["coste"] as? String ?: "",
                                detalles["descripcion"] as? String ?: "",
                                detalles["foto"] as? String ?: "",
                                detalles["fotoInGame"] as? String ?: "",
                                detalles["mejora"] as? String ?: ""
                            )
                        } else {
                            null
                        }
                    }.filterValues { it != null } as Map<String, Aspecto>
                    val selectedAspecto = aspectos[aspectoName]
                    callback(selectedAspecto)
                } else {
                    callback(null)
                }
            } else {
                callback(null)
            }
        }
    }


}