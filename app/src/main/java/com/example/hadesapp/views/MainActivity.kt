package com.example.hadesapp.views

import android.content.Context
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.hadesapp.views.fragments.MainFragment
import com.example.hadesapp.views.fragments.PerfilFragment
import com.example.hadesapp.R
import com.example.hadesapp.views.fragments.SettingsFragment
import com.example.hadesapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

enum class ProviderType {
    BASIC,
    GOOGLE
}
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private var mediaPlayer : MediaPlayer? = null
    private var position: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Setup
        val bundle = intent.extras
        val email = bundle?.getString("email")
        val provider = bundle?.getString("provider")

        setup(email?:"",provider?:"")

        // SP
        val preferences = getSharedPreferences(getString(R.string.preferences_file), Context.MODE_PRIVATE).edit()
        preferences.putString("email",email)
        preferences.putString("provider",provider)
        preferences.apply()



        //Inicio Bottom Navigation View
        bottomNavigationView = findViewById(R.id.top_menu)
        bottomNavigationView.selectedItemId = R.id.topHome
        bottomNavigationView.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.topAjustes -> {
                    replaceFragment(SettingsFragment())
                    true
                }
                R.id.topHome -> {
                    replaceFragment(MainFragment())
                    true
                }
                R.id.topPerfil -> {
                    replaceFragment(PerfilFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(MainFragment())
        //Fin Bottom Navigation View
    }

    private fun setup(email: String, provider: String){

    }

    private fun replaceFragment(fragment: Fragment){
        supportFragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit()
    }

    //states: SE EJECUTAN CUANDO
    //INICIA LA APP
    override fun onStart() {
        super.onStart()
        mediaPlayer = MediaPlayer.create(this, R.raw.hohmusica)
        mediaPlayer?.start()
    }

    //SE VUELVE A LA APP
    override fun onResume() {
        super.onResume()
        mediaPlayer?.seekTo(position)
        mediaPlayer?.start()

    }

    //LA APP ESTÁ MINIMIZADA
    override fun onPause() {
        super.onPause()
        mediaPlayer?.pause()
        if(mediaPlayer != null){
            position = mediaPlayer!!.currentPosition
        }


    }

    //SE EJECUTA Y VERIFICA SI LA APP ESTA DAÑADA O NO
    //SI PASA SIN PROBLEMAS PASA AL RESTART
    //SI TIENE PROBLEMAS PASA AL ONCREATE
    override fun onStop() {
        super.onStop()
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    //SE EJECUTA CUANDO SE VUELVE A LA APP Y NO TIENE PROBLEMAS
    override fun onRestart() {
        super.onRestart()
    }

    // CUANDO LA APP TIENE PROBLEMAS
    override fun onDestroy() {
        super.onDestroy()
    }

}