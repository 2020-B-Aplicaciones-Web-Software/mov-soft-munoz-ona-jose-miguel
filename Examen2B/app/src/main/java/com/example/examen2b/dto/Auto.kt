package com.example.examen2b.dto

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

data class Auto(var marca:String? = null,
                var año: Date?= null,
                var esUsado:Boolean?= null,
                var precio:Double?= null,
                var numPuertas:Int?= null,
                var numChasis:String?= null,
                var modelo:String?= null,
                var longitud:Double?= null,
                var latitud:Double?= null) {
    override fun toString(): String {
        val formato = SimpleDateFormat("yyyy")
        return "${numChasis} :${marca} ${modelo} ${numPuertas}p ${formato.format(año)}"
    }
}

