package com.example.hadesapp.models

data class CategoriaBendicion(
    val id : String,
    var nombreCat : String,
    var foto : String,
    var efecto : String,
    var bendiciones: Map<String, Bendicion>
)

data class Bendicion(
    var nombre : String,
    var efectoDesc : String,
    var foto : String
)


