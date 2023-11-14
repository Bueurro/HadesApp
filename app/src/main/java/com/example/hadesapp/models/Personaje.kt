package com.example.hadesapp.models

data class Personaje(val id:String, var nombre:String, var categoria:String, var titulo:String, var regalo:String, var foto:String, var descripcion : String) {

    override fun toString(): String {
        return super.toString()
    }
}