package com.example.hadesapp.views.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.hadesapp.R
import com.example.hadesapp.controllers.AspectoAdapter
import com.example.hadesapp.controllers.OnClickListener
import com.example.hadesapp.databinding.FragmentAspectoBinding
import com.example.hadesapp.models.Arma
import com.example.hadesapp.models.Aspecto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AspectoFragment : Fragment(), OnClickListener{
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var binding: FragmentAspectoBinding
    private val args:AspectoFragmentArgs by navArgs()
    private lateinit var recyclerView: RecyclerView
    private lateinit var aspectoAdapter: AspectoAdapter
    private var mList = mapOf<String, Aspecto>()
    private var mContext = this.context

    override fun onClickAspecto(aspecto: Aspecto, position: Int) {
        findNavController().navigate(AspectoFragmentDirections.actionAspectoFragmentToAspectoDetalleFragment(args.idAr))
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentAspectoBinding.inflate(inflater, container, false)

        // INICIALIZAR
        aspectoAdapter = AspectoAdapter(mList, this)
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView = binding.rvAspectos

        // RECYCLEVIEW
        binding.rvAspectos.layoutManager = linearLayoutManager
        binding.rvAspectos.adapter = aspectoAdapter

        binding.imgBtnAgregar.setOnClickListener {
            findNavController().navigate(AspectoFragmentDirections.actionAspectoFragmentToAspectoAddFragment())
        }

        getArmaById(args.idAr) { arma ->
            if (arma != null) {
                // Actualizar el adaptador con los aspectos
                aspectoAdapter.updateData(arma.aspectos)
            } else {
                Toast.makeText(requireContext(), "No se encontró el arma", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgBtnVolver.setOnClickListener {
            findNavController().popBackStack()
        }
        return binding.root
    }

    private fun getArmaById(idArma: String, callback: (Arma?) -> Unit) {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        val db = Firebase.firestore

        db.collection("Armas").document(idArma).get().addOnCompleteListener { task ->
            progressBar.visibility = View.GONE
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
                    binding.txtTest.text = "Aspectos de ${nombre}"

                    val aspectosMap =
                        document.get("aspectos") as? Map<String, *> ?: emptyMap<String, Any>()

                    // Convertir el mapa a un mapa de aspectos esperado
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

                    // Crear la instancia de Arma
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
                    // Manejar el caso donde el documento no existe
                    callback(null)
                }
            } else {
                // Manejar el error de la consulta a Firestore aquí
                callback(null)
            }
        }
    }

}