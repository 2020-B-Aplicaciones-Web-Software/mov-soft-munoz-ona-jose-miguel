package com.example.firebasedos.dto

data class ItemPedido(
    var producto:FirestoreProductoDto = FirestoreProductoDto(),
    var cantidad:Int
){
    override fun toString(): String {
        return "${cantidad}   ${producto.nombre}, precio total: ${cantidad*producto.precio}"
    }
}
