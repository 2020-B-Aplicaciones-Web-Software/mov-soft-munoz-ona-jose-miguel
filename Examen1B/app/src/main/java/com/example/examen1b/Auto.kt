package com.example.examen1b

import android.os.Parcel
import android.os.Parcelable
import java.text.SimpleDateFormat
import java.util.*

class Auto(var marca:String?,
           var año: Date,
           var esUsado:Boolean,
           var precio:Double,
           var numPuertas:Int,
           var numChasis:String?,
           var modelo:String?):Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        Date(parcel.readLong()),
        parcel.readByte() != 0.toByte(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun toString(): String {
        val formato = SimpleDateFormat("yyyy")
        return "${numChasis} :${marca} ${modelo} ${numPuertas}p ${formato.format(año)}"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(marca)
        parcel.writeLong(año.time)
        parcel.writeByte(if (esUsado) 1 else 0)
        parcel.writeDouble(precio)
        parcel.writeInt(numPuertas)
        parcel.writeString(numChasis)
        parcel.writeString(modelo)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Auto> {
        override fun createFromParcel(parcel: Parcel): Auto {
            return Auto(parcel)
        }

        override fun newArray(size: Int): Array<Auto?> {
            return arrayOfNulls(size)
        }
    }
}