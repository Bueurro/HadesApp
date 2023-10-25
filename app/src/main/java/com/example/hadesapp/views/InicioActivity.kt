package com.example.hadesapp.views

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.hadesapp.R
import com.example.hadesapp.databinding.ActivityInicioBinding
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class InicioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityInicioBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var oneTapClient: SignInClient
    private lateinit var signInRequest: BeginSignInRequest
    private val GOOGLE_SIGN_IN = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInicioBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setTheme(R.style.Theme_HadesApp)
        sesion()

        auth = Firebase.auth // Usar la instancia de auth de la actividad

        val currentUser = auth.currentUser
        if (currentUser != null) {
            Toast.makeText(this, "?" + currentUser.email, Toast.LENGTH_SHORT).show()
        }

        binding.btnLogin.setOnClickListener {
            val email = binding.txtEmail.text.toString()
            val pass = binding.txtPass.text.toString()

            iniciarSesion(email, pass)
        }

        binding.btnLoginGoogle.setOnClickListener {
            val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build()

            val googleClient = GoogleSignIn.getClient(this, googleConf)

            startActivityForResult(googleClient.signInIntent, GOOGLE_SIGN_IN)
        }

        binding.btnLoginFacebook.setOnClickListener {
            // Manejar inicio de sesión con Facebook
        }

        binding.btnRegistrar.setOnClickListener {
            val intent = Intent(this, RegistroActivity::class.java)
            startActivity(intent)
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)

            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            irHome(account.email ?: "", ProviderType.GOOGLE)
                        } else {
                            alertaError("Error autenticando al usuario con Google")
                        }
                    }
                }
            } catch (e: ApiException) {
                alertaError("Error al obtener cuenta de Google")
            }
        }
    }

    private fun sesion() {
        val preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)
        val email = preferences.getString("email", null)
        val provider = preferences.getString("provider", null)

        if (!email.isNullOrEmpty() && !provider.isNullOrEmpty()) {
            irHome(email, ProviderType.valueOf(provider))
        }
    }

    private fun iniciarSesion(correo: String, pass: String) {
        if (verificarLogin(correo, pass)) {
            try {
                auth.signInWithEmailAndPassword(correo, pass)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                            irHome(task.result?.user?.email ?: "", ProviderType.BASIC)

                            val preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE)
                            preferences.edit().putBoolean(getString(R.string.sp_remember_me), binding.swRecuerdame.isChecked).apply()
                        } else {
                            alertaError("Error al iniciar sesión con correo y contraseña")
                        }
                    }
            } catch (e: Exception) {
                alertaError("Error inesperado al iniciar sesión")
            }
        }
    }

    private fun irHome(email: String, provider: ProviderType) {
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(intent)
    }

    private fun verificarLogin(correo: String, pass: String): Boolean {
        if (correo.isBlank() || pass.isBlank() || !correo.contains("@") || !correo.contains(".")) {
            Toast.makeText(this, "Por favor ingrese una dirección de correo válida y una contraseña", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }

    private fun alertaError(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage(message)
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun updateUI(user: FirebaseUser?) {
        // Actualizar la interfaz de usuario según el usuario autenticado
    }
}
