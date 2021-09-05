package com.example.firebasedos.dto

data class FirestoreProductoDto(
    var nombre:String="",
    var precio:Double=0.0
){
    override fun toString(): String {
        return nombre
    }
    companion object{
        var listaProductos:List<FirestoreProductoDto> = listOf()
    }
}
