package com.example.firebasedos.dto

data class FirestoreRestaurant(
    val nombre:String =""
){
    override fun toString(): String {
        return nombre
    }

    companion object{
        var listaRestaurantes:List<FirestoreRestaurant> = listOf()
    }
}
