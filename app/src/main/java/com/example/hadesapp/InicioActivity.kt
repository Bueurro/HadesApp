package com.example.hadesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_inicio)
        setTheme(R.style.Theme_HadesApp)

        val btnLoginTrad: Button = findViewById<Button>(R.id.btnLoginTrad)

        btnLoginTrad.setOnClickListener {
            val intent = Intent(this, LoginDefActivity::class.java)
            startActivity(intent)


        }
    }
}