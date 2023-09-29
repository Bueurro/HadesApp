package com.example.hadesapp

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton

class BendicionesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?


    ): View? {
        // Infla el diseño del fragmento
        val rootView = inflater.inflate(R.layout.fragment_bendiciones, container, false)

        // Encuentra el botón en la vista raíz del fragmento
        val btnCrud: ImageButton = rootView.findViewById(R.id.boonAres)

        // Configura un clic en el botón
        btnCrud.setOnClickListener {
            val intent = Intent(activity, CrudActivity::class.java)
            startActivity(intent)
        }

        return rootView
    }

}