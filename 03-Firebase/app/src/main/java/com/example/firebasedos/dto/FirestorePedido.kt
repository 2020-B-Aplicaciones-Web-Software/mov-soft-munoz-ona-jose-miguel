package com.example.firebasedos.dto

import java.util.*
import kotlin.collections.ArrayList

data class FirestorePedido(
    var fechaPedido:Date? = null,
    var total:Double? = null,
    var calificacion:Int? = null,
    var estado:String? = "Por recibir",
    var usuario:String? = null,
    var restaurante:FirestoreRestaurant? = null,
    var productos:ArrayList<ItemPedido> = arrayListOf(),
    var uid:String? = null
){
    companion object{
        var listaPedidos:List<FirestorePedido> = listOf()
    }
}
