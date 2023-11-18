package com.example.hadesapp.views.fragments.arma

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hadesapp.controllers.AspectoAdapter
import com.example.hadesapp.controllers.OnClickListener
import com.example.hadesapp.databinding.FragmentArmaDetalleBinding
import com.example.hadesapp.models.Arma
import com.example.hadesapp.models.Aspecto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ArmaDetalleFragment : Fragment(), OnClickListener {
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var binding: FragmentArmaDetalleBinding
    private val args:ArmaDetalleFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var aspectoAdapter: AspectoAdapter
    private var mList = mapOf<String, Aspecto>()
    private var mContext = this.context
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentArmaDetalleBinding.inflate(inflater, container, false)

        getArmaById(args.idArma!!) { arma ->
            if (arma != null) {
                binding.imgBtnConsultar.setOnClickListener{
                    findNavController().navigate(ArmaDetalleFragmentDirections.actionArmaDetalleFragmentToAspectoFragment(args.idArma.toString()))
                }
                setupCampos(arma)
            } else {
                Toast.makeText(requireContext(), "alo", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgBtnVolver.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun setupCampos(arma: Arma) {
        binding.txtNombreAr.text = arma.nombre
        binding.txtCondicionAr.text = arma.condicion
        binding.txtEstiloAr.text = arma.estilo
        binding.txtDescipcionAr.text = arma.descripcion
        Glide.with(requireContext())
            .load(arma.foto)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .fitCenter()
            .into(binding.imgArma)
    }

    private fun getArmaById(idArma: String, callback: (Arma?) -> Unit) {
        val db = Firebase.firestore
        db.collection("Armas").document(idArma).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val document = task.result
                if (document != null && document.exists()) {
                    val id = document.id
                    val nombre = document.getString("nombre") ?: ""
                    val foto = document.getString("foto") ?: ""
                    val descripcion = document.getString("descripcion") ?: ""
                    val condicion = document.getString("condicion") ?: ""
                    val estilo = document.getString("estilo") ?: ""
                    val antiguoPortador = document.getString("antiguoPortador") ?: ""
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
                            // Manejar el caso donde los detalles no son un mapa
                            null
                        }
                    }.filterValues { it != null } as Map<String, Aspecto>
                    mList = aspectos
                    val arma = Arma(
                        id,
                        nombre,
                        foto,
                        descripcion,
                        condicion,
                        estilo,
                        antiguoPortador,
                        aspectos
                    )
                    callback(arma)
                } else {
                    callback(null)
                }
            } else {
                callback(null)
            }
        }
    }
}