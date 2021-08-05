package com.example.examen1b

import android.os.Parcel
import android.os.Parcelable
import java.util.*
import kotlin.collections.ArrayList

class Concesionario(
    var nombreConcesionario:String?,
    var estaAbierto:Boolean,
    var superficie:Double,
    var numConsesionario:Int,
    var fechaApertura:Date
):Parcelable {
    var autos:ArrayList<Auto>

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readInt(),
        Date(parcel.readLong())
    ) {

    }

    init{
        autos = arrayListOf<Auto>()
    }

    fun addAutos(auto:Auto){
        autos.add(auto)
    }

    override fun toString(): String {
        return "${numConsesionario}: ${nombreConcesionario}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombreConcesionario)
        parcel.writeByte(if (estaAbierto) 1 else 0)
        parcel.writeDouble(superficie)
        parcel.writeInt(numConsesionario)
        parcel.writeLong(fechaApertura.time)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Concesionario> {
        override fun createFromParcel(parcel: Parcel): Concesionario {
            return Concesionario(parcel)
        }

        override fun newArray(size: Int): Array<Concesionario?> {
            return arrayOfNulls(size)
        }
    }
}