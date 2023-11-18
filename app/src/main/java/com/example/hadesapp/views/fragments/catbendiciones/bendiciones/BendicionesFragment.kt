package com.example.hadesapp.views.fragments.catbendiciones.bendiciones

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hadesapp.R
import com.example.hadesapp.controllers.AspectoAdapter
import com.example.hadesapp.controllers.BendicionAdapter
import com.example.hadesapp.controllers.CategoriaBendicionAdapter
import com.example.hadesapp.controllers.OnClickListener
import com.example.hadesapp.databinding.FragmentBendicionesBinding
import com.example.hadesapp.models.Arma
import com.example.hadesapp.models.Aspecto
import com.example.hadesapp.models.Bendicion
import com.example.hadesapp.models.CategoriaBendicion
import com.example.hadesapp.views.fragments.arma.aspecto.AspectoFragmentDirections
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class BendicionesFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentBendicionesBinding
    private lateinit var bendicionAdapter: BendicionAdapter
    private var mList = mapOf<String, Bendicion>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private var mContext = this.context
    private val args:BendicionesFragmentArgs by navArgs()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentBendicionesBinding.inflate(inflater, container, false)

        // INICIALIZAR
        bendicionAdapter = BendicionAdapter(mList, this)
        linearLayoutManager = LinearLayoutManager(requireContext())
        recyclerView = binding.rvBendiciones

        // RECYCLEVIEW
        binding.rvBendiciones.layoutManager = linearLayoutManager
        binding.rvBendiciones.adapter = bendicionAdapter

        binding.imgBtnAgregar.setOnClickListener {
            findNavController().navigate(BendicionesFragmentDirections.actionBendicionesFragmentToBendicionesAddFragment(args.idCatBen))
        }

        getBendicionById(args.idCatBen) { bendicion ->
            if (bendicion != null) {
                // Actualizar el adaptador con los aspectos
                bendicionAdapter.updateData(bendicion.bendiciones)
            } else {
                Toast.makeText(requireContext(), "No se encontraron bendiciones", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgBtnVolver.setOnClickListener {
            findNavController().popBackStack()
        }

        return binding.root
    }

    private fun getBendicionById(idBendicion: String, callback: (CategoriaBendicion?) -> Unit) {
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        val db = Firebase.firestore

        db.collection("CatBendiciones").document(idBendicion).get().addOnCompleteListener { task ->
            progressBar.visibility = View.GONE
            if (task.isSuccessful) {
                val document = task.result

                if (document != null && document.exists()) {
                    val id = document.id
                    val nombreCat = document.getString("nombreCat") ?: ""
                    val foto = document.getString("foto") ?: ""
                    val efecto = document.getString("efecto") ?: ""
                    binding.txtTest.text = nombreCat

                    val bendicionesMap =
                        document.get("bendiciones") as? Map<String, *> ?: emptyMap<String, Any>()

                    val bendiciones = bendicionesMap.mapValues { (nombreCat, detalles) ->
                        if (detalles is Map<*, *>) {
                            Bendicion(
                                nombreCat,
                                detalles["efectoDesc"] as? String ?: "",
                                detalles["foto"] as? String ?: ""
                            )

                        } else {
                            null
                        }
                    }.filterValues { it != null } as Map<String, Bendicion>
                    mList = bendiciones

                    val categoriaBendicion = CategoriaBendicion(
                        id,
                        nombreCat,
                        foto,
                        efecto,
                        bendiciones
                    )

                    callback(categoriaBendicion)
                } else {
                    callback(null)
                }
            } else {
                callback(null)
            }
        }
    }

}