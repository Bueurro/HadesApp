package com.example.hadesapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.example.hadesapp.databinding.ActivityRegistroBinding

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrear.setOnClickListener {
            startActivity(Intent(this, DialogActivity::class.java))
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, LoginDefActivity::class.java)
            startActivity(intent)
        }

    }
}