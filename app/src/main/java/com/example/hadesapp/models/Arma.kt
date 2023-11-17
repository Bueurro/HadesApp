package com.example.hadesapp.models

data class Arma(
    val id: String,
    var nombre: String,
    var foto: String,
    var descripcion: String,
    var condicion: String,
    var estilo: String,
    var antiguoPortador: String,
    var aspectos: Map<String, Aspecto>
)

data class Aspecto(
    var nombre: String,
    var coste: String,
    var descripcion: String,
    var foto: String,
    var fotoInGame: String,
    var mejora: String
){
    override fun toString(): String {
        return super.toString()
    }
}