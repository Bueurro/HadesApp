package com.example.hadesapp.controllers

import com.example.hadesapp.models.Arma
import com.example.hadesapp.models.Aspecto
import com.example.hadesapp.models.Personaje

interface OnClickListener {
    fun onClickPersonaje(personaje: Personaje, position: Int) {

    }

    fun onClickArma(arma: Arma, position: Int) {

    }

    fun onClickAspecto(aspecto: Aspecto, position: Int) {

    }

}