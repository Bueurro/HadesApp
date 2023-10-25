package com.example.hadesapp.views.fragments

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.hadesapp.R
import com.example.hadesapp.databinding.FragmentPerfilBinding
import com.example.hadesapp.views.SplashActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PerfilFragment : Fragment() {

    private lateinit var binding: FragmentPerfilBinding
    private lateinit var auth: FirebaseAuth
    private var mContext = this.context

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentPerfilBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val user = FirebaseAuth.getInstance().currentUser
        val email = user?.email

        if (email != null) {
            Glide.with(this)
                .load(user.photoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(binding.imgPfp)
            binding.txtCorreoShow.text = email
            binding.txtNombreShow.text = user.displayName
        }


        auth = Firebase.auth
        binding.btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onDetach() {
        super.onDetach()
        mContext = null
    }

    private fun cerrarSesion() {
        val currentUser = auth.currentUser
        if (currentUser != null) {
            try {
                auth.signOut()

                // Borrar preferencias
                val preferences = mContext?.getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)?.edit()
                preferences?.clear()
                preferences?.apply()

                // Redirigir a la actividad de inicio de sesión
                val intent = Intent(mContext, SplashActivity::class.java)
                startActivity(intent)

                Toast.makeText(mContext, "Sesión Cerrada", Toast.LENGTH_SHORT).show()
            } catch (e: Exception) {
                Toast.makeText(mContext, "Error al cerrar la sesión: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
