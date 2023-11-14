package com.example.hadesapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.hadesapp.R
import com.example.hadesapp.databinding.ActivityDialogBinding

class DialogActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDialogBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDialogBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lottieImg.setAnimation(R.raw.hadesloadapp)
        binding.lottieImg.playAnimation()

        binding.btnEntrar.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        binding.btnVolver.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }


    }
}