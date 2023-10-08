package com.example.hadesapp

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hadesapp.databinding.ActivityInicioBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class InicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.Theme_HadesApp)

        //
        val preferences = getPreferences(Context.MODE_PRIVATE)
        val isFirstime = preferences.getBoolean(getString(R.string.sp_firs_time), true)

        // Si es la primera vez
        if ( isFirstime ) {

            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.dialog_title)
                .setPositiveButton(R.string.dialog_confirm, DialogInterface.OnClickListener { dialogInterface, i ->
                    preferences.edit().putBoolean(getString(R.string.sp_firs_time), false).commit()
                })
                .setNegativeButton(R.string.dialog_cancel, null)
                .show()

        }

        binding.btnLoginTrad.setOnClickListener {
            val intent = Intent(this, LoginDefActivity::class.java)
            startActivity(intent)
        }
    }
}