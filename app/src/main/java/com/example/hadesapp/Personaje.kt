package com.example.hadesapp

data class Personaje(val id:Long, var nombre:String, var categoria:String, var titulo:String, var regaloBendicion:String, var foto:String) {

    override fun toString(): String {
        return super.toString()
    }
}