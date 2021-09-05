package com.example.firebasedos.dto

data class FirestoreUsuarioDto(
    var uid:String = "",
    var email:String = "",
    var roles:ArrayList<String> = arrayListOf()
) {
}