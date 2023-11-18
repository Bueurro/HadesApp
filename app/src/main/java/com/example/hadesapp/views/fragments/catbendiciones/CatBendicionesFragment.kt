package com.example.hadesapp.views.fragments.catbendiciones

import android.content.DialogInterface
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.SearchView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.hadesapp.R
import com.example.hadesapp.controllers.CategoriaBendicionAdapter
import com.example.hadesapp.controllers.OnClickListener
import com.example.hadesapp.databinding.FragmentCatBendicionesBinding
import com.example.hadesapp.models.Bendicion
import com.example.hadesapp.models.CategoriaBendicion
import com.example.hadesapp.views.fragments.arma.ArmasFragmentDirections
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Locale
import java.util.concurrent.LinkedBlockingQueue

class CatBendicionesFragment : Fragment(), OnClickListener {
    private lateinit var binding: FragmentCatBendicionesBinding
    private lateinit var categoriaBendicionAdapter: CategoriaBendicionAdapter
    private var mList = mutableListOf<CategoriaBendicion>()
    private lateinit var recyclerView: RecyclerView
    private lateinit var linearLayoutManager: RecyclerView.LayoutManager
    private var mContext = this.context
    private val db = Firebase.firestore
    private lateinit var searchView: SearchView

    override fun onClickCategoria(categoria: CategoriaBendicion, position: Int) {
        findNavController().navigate(CatBendicionesFragmentDirections.actionCatBendicionesFragmentToBendicionesFragment(categoria.id))
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentCatBendicionesBinding.inflate(inflater, container, false)

        // INICIALIZAR
        categoriaBendicionAdapter = CategoriaBendicionAdapter(mList, this)
        linearLayoutManager = LinearLayoutManager(requireContext())
        searchView = binding.search
        recyclerView = binding.rvCatBend

        // RECYCLEVIEW
        binding.rvCatBend.layoutManager = linearLayoutManager
        binding.rvCatBend.adapter = categoriaBendicionAdapter

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

        binding.imgBtnVolver.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.imgBtnAgregar.setOnClickListener {
            findNavController().navigate(
                CatBendicionesFragmentDirections.actionCatBendicionesFragmentToCatBendicionesAddFragment2(
                    null
                )
            )
        }

        // Llama a la función para obtener los personajes
        getCategorias { categorias ->
            // Actualiza el adaptador con los personajes obtenidos
            categoriaBendicionAdapter.updateData(categorias)
        }

        //swipe refresher
        swipeRefresher()

        return binding.root
    }


    override fun onPause() {
        super.onPause()
        binding.txtNoResults.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        binding.txtNoResults.visibility = View.GONE
    }

    private fun setUpSwipeHelper() {
        val swipeHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
            0,
            ItemTouchHelper.START or ItemTouchHelper.END
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean = false

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {

                when (direction) {
                    ItemTouchHelper.START -> {
                        val dialogView = layoutInflater.inflate(R.layout.dialog_delete, null)
                        val alertDialog =
                            MaterialAlertDialogBuilder(requireContext()).setView(dialogView)
                                .setCancelable(true)
                                .setTitle(getString(R.string.dialog_confirm))
                                .setPositiveButton(getString(R.string.dialog_confirm),
                                    DialogInterface.OnClickListener { dialogInterface, i ->
                                        categoriaBendicionAdapter.remove(viewHolder.adapterPosition)
                                    })
                                .setNegativeButton(
                                    getString(R.string.dialog_cancel),
                                    DialogInterface.OnClickListener { dialogInterface, i ->
                                        categoriaBendicionAdapter.notifyDataSetChanged()
                                    })
                                .show()
                        val positiveButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                        val negativeButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE)
                        positiveButton.setTextColor(Color.GREEN)
                        negativeButton.setTextColor(Color.RED)
                    }

                    ItemTouchHelper.END -> {
                        val idCatben = categoriaBendicionAdapter.edit(viewHolder.adapterPosition)
                        findNavController().navigate(
                            CatBendicionesFragmentDirections.actionCatBendicionesFragmentToCatBendicionesAddFragment2(
                                idCatben
                            )
                        )
                    }
                }
            }

            private fun drawBackground(itemView: View, color: Int) {
                val bg = ColorDrawable(color)
                val pix = -600
                bg.setBounds(
                    itemView.left - pix - 80,
                    itemView.top - pix,
                    itemView.right - pix - 80,
                    itemView.bottom - pix
                )
                itemView.translationX = 0f
                itemView.background = bg
            }

            override fun onChildDraw(
                c: Canvas,
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                dX: Float,
                dY: Float,
                actionState: Int,
                isCurrentlyActive: Boolean
            ) {
                super.onChildDraw(
                    c,
                    recyclerView,
                    viewHolder,
                    dX,
                    dY,
                    actionState,
                    isCurrentlyActive
                )
                val DIRECTION_END = 1
                val DIRECTION_START = 0

                val direction = if (dX > 0) DIRECTION_END else DIRECTION_START
                val iconSize = resources.getDimensionPixelSize(R.dimen.icon_size)

                when (direction) {
                    DIRECTION_START -> {
                        drawBackground(c, viewHolder.itemView, Color.rgb(183, 29, 0))
                        val rightIcon =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_delete)
                        val rightIconMargin =
                            resources.getDimensionPixelSize(R.dimen.right_icon_margin)
                        rightIcon?.let {
                            val rightIconTop =
                                viewHolder.itemView.top + (viewHolder.itemView.height - iconSize) / 2
                            val rightIconBottom = rightIconTop + iconSize
                            val rightIconRight = viewHolder.itemView.right - rightIconMargin
                            val rightIconLeft = rightIconRight - iconSize
                            rightIcon.setBounds(
                                rightIconLeft,
                                rightIconTop,
                                rightIconRight,
                                rightIconBottom
                            )
                            rightIcon.draw(c)
                        }
                    }

                    DIRECTION_END -> {
                        drawBackground(c, viewHolder.itemView, Color.rgb(113, 25, 165))
                        val leftIcon =
                            AppCompatResources.getDrawable(requireContext(), R.drawable.ic_save)
                        val leftIconMargin =
                            resources.getDimensionPixelSize(R.dimen.left_icon_margin) // Margen izquierdo
                        leftIcon?.let {
                            val leftIconTop =
                                viewHolder.itemView.top + (viewHolder.itemView.height - iconSize) / 2
                            val leftIconBottom = leftIconTop + iconSize
                            val leftIconLeft = viewHolder.itemView.left + leftIconMargin
                            val leftIconRight = leftIconLeft + iconSize
                            leftIcon.setBounds(
                                leftIconLeft,
                                leftIconTop,
                                leftIconRight,
                                leftIconBottom
                            )
                            leftIcon.draw(c)
                        }
                    }
                }
            }

            override fun clearView(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder
            ) {
                super.clearView(recyclerView, viewHolder)
                drawBackground(viewHolder.itemView, Color.rgb(214,162,31))
            }


        })
        swipeHelper.attachToRecyclerView(binding.rvCatBend)
    }

    private fun swipeRefresher() {
        val swipe: SwipeRefreshLayout = binding.swipeRefresherLayout
        swipe.setOnRefreshListener {
            getCategorias { categorias ->
                categoriaBendicionAdapter.updateData(categorias)
            }
            categoriaBendicionAdapter.notifyDataSetChanged()
            swipe.isRefreshing = false
        }
    }

    private fun getCategorias(callback: (MutableList<CategoriaBendicion>) -> Unit) {
        mList.clear()
        val progressBar = binding.progressBar
        progressBar.visibility = View.VISIBLE
        val db = Firebase.firestore
        val queue = LinkedBlockingQueue<MutableList<CategoriaBendicion>>()
        Thread {
            db.collection("CatBendiciones").get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    progressBar.visibility = View.GONE
                    for (document in task.result!!) {
                        val id = document.id
                        val nombreCat = document.getString("nombreCat") ?: ""
                        val foto = document.getString("foto") ?: ""
                        val efecto = document.getString("efecto") ?: ""

                        val bendicionesMap =
                            document.get("bendiciones") as? Map<String, *> ?: emptyMap<String, Any>()

                        val bendiciones = bendicionesMap.mapValues { (nombreCat, detalles) ->
                            if (detalles is Map<*, *>) {
                                Bendicion(
                                    nombreCat,
                                    detalles["efectoDesc"] as? String ?: "",
                                    detalles["foto"] as? String ?: ""
                                )

                            } else {
                                null
                            }
                        }.filterValues { it != null } as Map<String, Bendicion>
                        val categoria = CategoriaBendicion(
                            id,
                            nombreCat,
                            foto,
                            efecto,
                            bendiciones)
                        mList.add(categoria)
                    }
                    queue.add(mList)
                    callback(queue.take())
                } else {
                    callback(mutableListOf())
                }
            }
        }.start()
    }

    private fun drawBackground(c: Canvas, itemView: View, color: Int) {
        val bg = ColorDrawable(color)
        bg.setBounds(itemView.left, itemView.top, itemView.right, itemView.bottom)
        bg.draw(c)
    }

    private fun filterList(query: String?) {
        if (query != null) {
            val filteredList = mutableListOf<CategoriaBendicion>()
            mList.forEach {
                if (it.nombreCat.lowercase(Locale.getDefault()).contains(query)) {
                    filteredList.add(it)
                }
            }
            if (filteredList.isEmpty()) {
                binding.txtNoResults.visibility = View.VISIBLE
                categoriaBendicionAdapter.updateData(filteredList)
            } else {
                binding.txtNoResults.visibility = View.GONE
                categoriaBendicionAdapter.updateData(filteredList)
            }
        }
    }

}