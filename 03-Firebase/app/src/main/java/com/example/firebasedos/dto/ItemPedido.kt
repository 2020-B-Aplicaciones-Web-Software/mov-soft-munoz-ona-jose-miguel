package com.example.firebasedos.dto

data class ItemPedido(
    var nombre:String = "",
    var precio:Double = 0.0,
    var cantidad:Int? = 0,
    var uid:String? = null
){
    override fun toString(): String {
        return "${cantidad}   ${nombre}, precio total: ${cantidad?.times(precio)}"
    }
}
