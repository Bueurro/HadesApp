package com.example.hadesapp.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hadesapp.databinding.ActivityRegistroBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegistroActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegistroBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegistroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrear.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val pass = binding.txtPaaa.text.toString()
            val confirmpass = binding.txtConfirmarPass.text.toString()
            val valid = verificarLogin(email,pass,confirmpass)

            if ( valid ) {
                crearUsuario(email,pass) { exitoso ->
                    if (exitoso){
                        startActivity(Intent(this, DialogActivity::class.java))
                    }else {
                        Toast.makeText(this, "fallo en redireccion por favor ir a log-in", Toast.LENGTH_SHORT).show()                        
                    }
                }
            }
        }

        binding.btnLogin.setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
        }

    }


    private fun verificarLogin(correo: String, pass: String, confirmpass: String) : Boolean{

        if ( correo.isBlank() || pass.isBlank() || confirmpass.isBlank() ) {
            Toast.makeText(this, "Por favor rellene todos los campos", Toast.LENGTH_LONG).show()
        } else {
           if( pass != confirmpass ) {
               Toast.makeText(this, "Las contraseñas deben coincidir", Toast.LENGTH_SHORT).show()
               binding.txtConfirmarPass.requestFocus()
           } else {
               return true
           }
        }
        return false
    }

    private fun crearUsuario(correo: String, pass: String, callback: (Boolean) -> Unit) {
        auth = Firebase.auth
        auth.createUserWithEmailAndPassword(correo, pass)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Usuario creado correctamente", Toast.LENGTH_SHORT).show()
                    callback(true)
                } else {
                    Toast.makeText(this, "Error al crear usuario: ${task.exception}", Toast.LENGTH_SHORT).show()
                    callback(false)
                }
            }
    }


}