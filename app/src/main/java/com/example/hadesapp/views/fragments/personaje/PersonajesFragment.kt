package com.example.hadesapp.views.fragments.personaje

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hadesapp.R
import com.example.hadesapp.controllers.OnClickListener
import com.example.hadesapp.controllers.PersonajeAdapter
import com.example.hadesapp.databinding.FragmentPersonajesBinding
import com.example.hadesapp.models.Personaje
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale
import java.util.concurrent.LinkedBlockingQueue


class PersonajesFragment : Fragment(), OnClickListener {

    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private lateinit var binding: FragmentPersonajesBinding
    private lateinit var personajeAdapter: PersonajeAdapter
    private var mList = mutableListOf<Personaje>()
    private lateinit var recyclerView: RecyclerView
    private var mediaPlayer : MediaPlayer? = null
    private lateinit var searchView: SearchView
    private var mContext = this.context
    private val db = Firebase.firestore

    override fun onClickPersonaje(personaje: Personaje, position: Int) {
        findNavController().navigate(PersonajesFragmentDirections.actionPersonajesFragmentToPersonajeDetalleFragment(personaje.id))
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        binding = FragmentPersonajesBinding.inflate(inflater, container, false)

        // INICIALIZAR
        personajeAdapter = PersonajeAdapter(mList,this)
        linearLayoutManager = LinearLayoutManager(mContext)
        searchView = binding.search
        recyclerView = binding.rvPersonajes

        // RECYCLEVIEW
        binding.rvPersonajes.layoutManager = linearLayoutManager
        binding.rvPersonajes.adapter = personajeAdapter

        searchView.setOnQueryTextListener(object  : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true
            }

        })

        //Swipe Helper
        setUpSwipeHelper()

        binding.imgBtnVolver.setOnClickListener{
            findNavController().popBackStack()
        }

        binding.imgBtnAgregar.setOnClickListener {
            findNavController().navigate(PersonajesFragmentDirections.actionPersonajesFragmentToPersonajeAddFragment(null))
        }

        // Llama a la función para obtener los personajes
        getPersonaje { personajes ->
            // Actualiza el adaptador con los personajes obtenidos
            personajeAdapter.updateData(personajes)
        }

        //swipe refresher
        swipeRefresher()

        return binding.root
    }

    override fun onPause() {
        super.onPause()
        binding.txtNoResults.visibility = View.GONE
    }

    private fun setUpSwipeHelper() {
        val swipeHelper = ItemTouchHelper(object: ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.START or ItemTouchHelper.END) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {
                    ItemTouchHelper.START -> {
                        val dialogView = layoutInflater.inflate(R.layout.dialog_delete, null)
                        val alertDialog = MaterialAlertDialogBuilder(requireContext()).
                        setView(dialogView)
                            .setCancelable(true)
                            .setTitle(getString(R.string.dialog_confirm))
                            .setPositiveButton(getString(R.string.dialog_confirm),DialogInterface.OnClickListener{ dialogInterface, i ->
                                personajeAdapter.remove(viewHolder.adapterPosition)
                            })
                            .setNegativeButton(getString(R.string.dialog_cancel), DialogInterface.OnClickListener{ dialogInterface, i ->
                                personajeAdapter.notifyDataSetChanged()
                            })
                            .show()
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        positiveButton.setTextColor(Color.GREEN)
                        negativeButton.setTextColor(Color.RED)
                    }
                    ItemTouchHelper.END -> {
                        val idPj = personajeAdapter.edit(viewHolder.adapterPosition)
                        findNavController().navigate(PersonajesFragmentDirections.actionPersonajesFragmentToPersonajeAddFragment(idPj))
                    }
                }
            }

            private fun drawBackground(itemView: View, color: Int) {
                val bg = ColorDrawable(color)
                val pix = -600
                bg.setBounds(itemView.left-pix-80, itemView.top-pix, itemView.right-pix-80, itemView.bottom-pix)
                itemView.translationX = 0f
                itemView.background = bg
            }

            override fun onChildDraw(c: Canvas,recyclerView: RecyclerView,viewHolder: RecyclerView.ViewHolder,dX: Float,dY: Float,actionState: Int,isCurrentlyActive: Boolean) {
                super.onChildDraw(c,recyclerView,viewHolder, dX, dY,actionState,isCurrentlyActive)
                val DIRECTION_END = 1
                val DIRECTION_START = 0

                val direction = if (dX > 0) DIRECTION_END else DIRECTION_START
                when (direction) {
                    DIRECTION_START -> {
                        drawBackground(c, viewHolder.itemView, Color.rgb(183,29,0))
                    }
                    DIRECTION_END -> {
                        drawBackground(c, viewHolder.itemView, Color.rgb(113,25,165))
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                drawBackground(viewHolder.itemView, Color.rgb(138,43,25))
            }


        })
        swipeHelper.attachToRecyclerView(binding.rvPersonajes)
    }

    private fun swipeRefresher() {
        val swipe : SwipeRefreshLayout = binding.swipeRefresherLayout
        swipe.setOnRefreshListener {
            //obtiene la lista denuevo
            getPersonaje { personajes ->
                // Actualiza el adaptador con los personajes obtenidos
                personajeAdapter.updateData(personajes)
            }
            //notifica el cambio
            personajeAdapter.notifyDataSetChanged()
            //detiene el refresh
            swipe.isRefreshing = false
        }
    }

    private fun drawBackground(c: Canvas, itemView: View, color: Int) {
        val bg = ColorDrawable(color)
        bg.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
        bg.draw(c)
    }

    private fun getPersonaje(callback: (MutableList<Personaje>) -> Unit) {
        mList.clear()
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        val db = Firebase.firestore
        val queue = LinkedBlockingQueue<MutableList<Personaje>>()
        Thread {
            db.collection("Personajes").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.GONE
                    for (document in task.result!!) {
                        val id = document.id
                        val nombre = document.getString("Nombre") ?: ""
                        val categoria = document.getString("Categoria") ?: ""
                        val titulo = document.getString("Titulo") ?: ""
                        val regaloBendicion = document.getString("Regalo") ?: ""
                        val foto = document.getString("Foto") ?: ""
                        val descripcion = document.getString("Descripcion") ?: ""

                        val personaje = Personaje(id, nombre, categoria, titulo, regaloBendicion, foto, descripcion)
                        mList.add(personaje)
                    }
                    queue.add(mList)
                    callback(queue.take())
                } else {
                    // Manejar el error de la consulta a Firestore aquí
                    callback(mutableListOf())
                }
            }
        }.start()
    }

    private fun filterList(query: String?) {
        if (query!=null){
            val filteredList = mutableListOf<Personaje>()
            mList.forEach {
                if (it.nombre.lowercase(Locale.getDefault()).contains(query)){
                    filteredList.add(it)
                }
            }
            if (filteredList.isEmpty()){
                binding.txtNoResults.visibility = View.VISIBLE
                personajeAdapter.updateData(filteredList)
            } else {
                binding.txtNoResults.visibility = View.GONE
                personajeAdapter.updateData(filteredList)
            }
        }
    }
}