package com.example.examen1b

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*
import kotlin.collections.ArrayList

class SQLiteHelperAuto(
    contexto:Context
): SQLiteOpenHelper(contexto,"movExamen",null,1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCreacionTablaAuto =
            """
                CREATE TABLE AUTO(
                id TEXT PRIMARY KEY,
                marca TEXT,
                anio REAL,
                usado BOOLEAN,
                precio REAL,
                puertas INTEGER,
                modelo TEXT,
                numConcesionario INTEGER REFERENCES CONCESIONARIO(numero)
                )
            """.trimIndent()
        db?.execSQL(scriptCreacionTablaAuto)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    fun crearAuto(
         marca:String,
         año: Date,
         esUsado:Boolean,
         precio:Double,
         numPuertas:Int,
         numChasis:String,
         modelo:String,
         concesionario:Int
    ):Boolean{
        val conexionEscritura = writableDatabase
        val aGuardar = ContentValues()
        aGuardar.put("id",numChasis)
        aGuardar.put("marca",marca)
        aGuardar.put("usado",esUsado)
        aGuardar.put("precio",precio)
        aGuardar.put("puertas",numPuertas)
        aGuardar.put("modelo",modelo)
        aGuardar.put("anio",año.time)
        aGuardar.put("numConcesionario",concesionario)

        val resultadoEscritura:Long = conexionEscritura
            .insert(
                "AUTO",
                null,
                aGuardar
            )
        conexionEscritura.close()
        return resultadoEscritura.toInt() != -1
    }

    fun consultarAutoPorChasis(numChasis:String):Auto{
        val consulta = "SELECT * FROM AUTO WHERE id = '${numChasis}'"
        val baseDatosLectura = readableDatabase
        val resultadoLectura = baseDatosLectura.rawQuery(
            consulta,
            null)
        val existeAuto = resultadoLectura.moveToFirst()
        val autoEncontrado = Auto("",Date(),false,0.0,1,"","")
        if (existeAuto){
            do {
                val id = resultadoLectura.getString(0)
                val marca = resultadoLectura.getString(1)
                val año  = Date(resultadoLectura.getLong(2))
                val esUsado = resultadoLectura.getInt(3)== 1
                val precio = resultadoLectura.getDouble(4)
                val puertas = resultadoLectura.getInt(5)
                val modelo = resultadoLectura.getString(6)
                if (id != null) {
                    autoEncontrado.marca = marca
                    autoEncontrado.año = año
                    autoEncontrado.esUsado = esUsado
                    autoEncontrado.precio = precio
                    autoEncontrado.numPuertas = puertas
                    autoEncontrado.numChasis = id
                    autoEncontrado.modelo = modelo
                }
            }while (resultadoLectura.moveToNext())
        }
        resultadoLectura.close()
        baseDatosLectura.close()
        return autoEncontrado
    }

    fun eliminarAuto(numChasis: String):Boolean{
        val conexionEscritura = writableDatabase
        val resultadoEliminacion = conexionEscritura
            .delete(
                "AUTO",
                "id = ?",
                arrayOf(
                    numChasis
                )
            )
        conexionEscritura.close()
        return resultadoEliminacion.toInt() != -1
    }

    fun actualizarAuto(
        marca:String,
        año: Date,
        esUsado:Boolean,
        precio:Double,
        numPuertas:Int,
        numChasis:String,
        modelo:String
    ):Boolean{
        val conexionEscritura = writableDatabase
        val aActualizar = ContentValues()
        aActualizar.put("marca",marca)
        aActualizar.put("usado",esUsado)
        aActualizar.put("precio",precio)
        aActualizar.put("puertas",numPuertas)
        aActualizar.put("modelo",modelo)
        aActualizar.put("anio",año.time)

        val resultadoActualizacion = conexionEscritura
            .update(
                "AUTO",
                aActualizar,
                "id = ?",
                arrayOf(
                    numChasis
                )
            )
        conexionEscritura.close()
        return resultadoActualizacion.toInt() != -1
    }

    fun listarAutosDeConcesionario(numConcesionario:Int):ArrayList<Auto>?{
        val consulta = "SELECT * FROM AUTO WHERE numConcesionario= ${numConcesionario}"
        val baseDatosLectura = readableDatabase
        val resultadoLectura = baseDatosLectura.rawQuery(
            consulta,
            null)
        val existeAuto = resultadoLectura.moveToFirst()
        var listaAutos = arrayListOf<Auto>()
        if(existeAuto){
            do {
                val id = resultadoLectura.getString(0)
                val marca = resultadoLectura.getString(1)
                val año  = Date(resultadoLectura.getLong(2))
                val esUsado = resultadoLectura.getInt(3)== 1
                val precio = resultadoLectura.getDouble(4)
                val puertas = resultadoLectura.getInt(5)
                val modelo = resultadoLectura.getString(6)
                if (id != null) {
                    listaAutos.add(
                        Auto(marca,año,esUsado,precio,puertas,id,modelo)
                    )
                }
            }while (resultadoLectura.moveToNext())
        }
        resultadoLectura.close()
        baseDatosLectura.close()
        return if(listaAutos.size ==0) null else listaAutos
    }
}