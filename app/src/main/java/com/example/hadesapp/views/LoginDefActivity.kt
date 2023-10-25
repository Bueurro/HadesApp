package com.example.hadesapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hadesapp.R

class LoginDefActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_def)

        val btnLoginTrad: Button = findViewById<Button>(R.id.btnLogin)
        val btnRegistrar: Button = findViewById<Button>(R.id.btnRegistrar)

        btnLoginTrad.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }
}