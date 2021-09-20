package com.example.examen2b.dto

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList

data class Concesionario(var nombreConcesionario:String?=null,
                         var estaAbierto:Boolean?=null,
                         var superficie:Double?=null,
                         var numConsesionario:Int?=null,
                         var fechaApertura: Date? = null
) {
    var autos:ArrayList<Auto> = arrayListOf<Auto>()

    fun addAutos(auto:Auto){
        autos.add(auto)
    }
    override fun toString(): String {
        return "${numConsesionario}: ${nombreConcesionario}"
    }

}
