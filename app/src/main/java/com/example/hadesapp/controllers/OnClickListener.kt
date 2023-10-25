package com.example.hadesapp.controllers

import com.example.hadesapp.models.Personaje

interface OnClickListener {
    fun onClick(personaje: Personaje, position: Int) {

    }

}