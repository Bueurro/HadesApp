package com.example.hadesapp.views

import android.content.Context
import android.media.MediaPlayer
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.example.hadesapp.R
import com.example.hadesapp.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

enum class ProviderType {
    BASIC,
    GOOGLE
}
class MainActivity : AppCompatActivity() {

    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer : MediaPlayer? = null
    private var position: Int = 0
    private var isExpanded = false
    private var currentView = ""
    private val fromBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.from_botom_fab)
    }
    private val toBottomFabAnim : Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.to_bottom_fab)
    }
    private val rotateClockWise : Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_clock_wise)
    }
    private val rotateAntiClockWise : Animation by lazy {
        AnimationUtils.loadAnimation(this, R.anim.rotate_anti_clock_wise)
    }

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

        // Inicio Bottom Navigation View
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.hostFragmentManager) as NavHostFragment
        navController = navHostFragment.navController
        val bottomNavigationView: BottomNavigationView = findViewById<BottomNavigationView>(R.id.top_menu)
        setupWithNavController(bottomNavigationView, navController)

        // Oculta el Bottom Nav View
        navController.addOnDestinationChangedListener { _, nd: NavDestination, _ ->
            if (nd.id == R.id.mainFragment || nd.id == R.id.settingsFragment || nd.id == R.id.perfilFragment) {
                bottomNavigationView.visibility = View.VISIBLE
                binding.menuFabBtn.visibility = View.GONE
                currentView = nd.id.toString()
            } else {
                bottomNavigationView.visibility = View.GONE
                binding.menuFabBtn.visibility = View.VISIBLE
                currentView = nd.id.toString()
            }
        }

        // Fab Button

        binding.menuFabBtn.setOnClickListener{
            if (isExpanded){
                shrinkFab()
            } else {
                expandFab()
            }
        }

        binding.addFabBtn.setOnClickListener {

        }
    }

    private fun expandFab() {

        binding.menuFabBtn.startAnimation(rotateClockWise)
        binding.addFabBtn.startAnimation(fromBottomFabAnim)
        binding.deleteFabBtn.startAnimation(fromBottomFabAnim)

        isExpanded = !isExpanded
    }

    private fun shrinkFab() {

        binding.menuFabBtn.startAnimation(rotateAntiClockWise)
        binding.addFabBtn.startAnimation(toBottomFabAnim)
        binding.deleteFabBtn.startAnimation(toBottomFabAnim)

        isExpanded = !isExpanded
    }

    fun setup(email: String, provider: String){

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