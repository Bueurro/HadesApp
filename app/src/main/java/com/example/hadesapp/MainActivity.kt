package com.example.hadesapp

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNavigationView: BottomNavigationView
    private var mediaPlayer : MediaPlayer? = null
    private var position: Int = 0;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.top_menu)

        bottomNavigationView.selectedItemId = R.id.topHome

        bottomNavigationView.setOnItemSelectedListener{ menuItem ->
            when(menuItem.itemId){
                R.id.topHome -> {
                    replaceFragment(MainFragment())
                    true
                }
                R.id.topPersonajes -> {
                    replaceFragment(PersonajesFragment())
                    true
                }
                R.id.topArmasInfernales -> {
                    replaceFragment(ArmasInfernalesFragment())
                    true
                }
                R.id.topBonds -> {
                    replaceFragment(BendicionesFragment())
                    true
                }
                else -> false
            }
        }
        replaceFragment(MainFragment())



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

    //VUELVE A LA APP
    override fun onResume() {
        super.onResume()
        mediaPlayer?.seekTo(position)
        mediaPlayer?.start()
    }

    //TENEMOS LA APP MINIMIZADA
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