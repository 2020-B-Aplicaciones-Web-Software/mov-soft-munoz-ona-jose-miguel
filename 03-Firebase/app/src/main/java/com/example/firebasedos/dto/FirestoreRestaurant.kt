package com.example.firebasedos.dto

data class FirestoreRestaurant(
    var nombre:String ="",
    var calificacionPromedio:Double?=null,
    var sumCalificaciones:Int = 0,
    var usuariosCalificado:Int = 0,
    var uid:String? = null
){
    override fun toString(): String {
        return nombre
    }
    fun calcularPromedio(){
        val camposConValores = arrayOf(sumCalificaciones,usuariosCalificado).all {
            return@all it>0 }
        if (camposConValores){
            calificacionPromedio = sumCalificaciones.div(usuariosCalificado.toDouble())
        }
    }
    companion object{
        var listaRestaurantes:List<FirestoreRestaurant> = listOf()
    }
}
