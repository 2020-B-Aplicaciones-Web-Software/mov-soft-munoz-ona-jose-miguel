package com.example.examen1b

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.util.*
import kotlin.collections.ArrayList

class SQLiteHelperConcesionario(
    contexto:Context?
): SQLiteOpenHelper(
    contexto,
    "movExamen",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCreacion =
            """
                CREATE TABLE CONCESIONARIO(
                numero INTEGER PRYMARY KEY,
                nombre TEXT,
                abierto BOOLEAN,
                superficie REAL,
                fechaApertura REAL
                )
            """.trimIndent()
        db?.execSQL(scriptCreacion)
        val scriptCreacionTablaAuto = """
                CREATE TABLE AUTO(
                id TEXT PRIMARY KEY,
                marca TEXT,
                anio REAL,
                usado BOOLEAN,
                precio REAL,
                puertas INTEGER,
                modelo TEXT,
                numConcesionario INTEGER REFERENCES CONCESIONARIO(numero)
                ON UPDATE CASCADE ON DELETE CASCADE
                )
            """.trimIndent()
        db?.execSQL(scriptCreacionTablaAuto)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }
    fun crearConcesionario(
         nombreConcesionario:String,
         estaAbierto:Boolean,
         superficie:Double,
         numConsesionario:Int,
         fechaApertura: Date
    ):Boolean{
        val conexionEscritura = writableDatabase
        val aGuardar = ContentValues()
        aGuardar.put("numero",numConsesionario)
        aGuardar.put("nombre",nombreConcesionario)
        aGuardar.put("abierto",estaAbierto)
        aGuardar.put("superficie",superficie)
        aGuardar.put("fechaApertura",fechaApertura.time)

        val resultadoEscritura:Long = conexionEscritura
            .insert(
            "CONCESIONARIO",
            null,
            aGuardar
            )
        conexionEscritura.close()
        return resultadoEscritura.toInt() != -1
    }

    fun consultarConcesionarioPorId(id:Int):Concesionario{
        val consulta = "SELECT * FROM CONCESIONARIO WHERE numero = ${id}"
        val baseDatosLectura = readableDatabase
        val resultadoLectura = baseDatosLectura.rawQuery(
            consulta,
            null)
        val existeConcesionario = resultadoLectura.moveToFirst()
        val concesionarioEncontrado = Concesionario("",false,0.0,1,Date())
        if (existeConcesionario){
            do {
                val id = resultadoLectura.getInt(0)
                val nombre = resultadoLectura.getString(1)
                val estaAbierto = resultadoLectura.getInt(2) == 1
                val superficie = resultadoLectura.getDouble(3)
                val fechaApertura = Date(resultadoLectura.getLong(4))
                if (id != null) {
                    concesionarioEncontrado.nombreConcesionario =nombre
                    concesionarioEncontrado.estaAbierto = estaAbierto
                    concesionarioEncontrado.superficie = superficie
                    concesionarioEncontrado.numConsesionario = id
                    concesionarioEncontrado.fechaApertura = fechaApertura
                }
            }while (resultadoLectura.moveToNext())
        }
        resultadoLectura.close()
        baseDatosLectura.close()
        return concesionarioEncontrado
    }

    fun eliminarConcesionario(id: Int):Boolean{
        val conexionEscritura = writableDatabase
        val resultadoEliminacion = conexionEscritura
            .delete(
                "CONCESIONARIO",
                "numero=?",
                arrayOf(
                    id.toString()
                )
            )
        conexionEscritura.close()
        return resultadoEliminacion.toInt() != -1
    }

    fun actualizarConcesionario(
        nombreConcesionario:String,
        estaAbierto:Boolean,
        superficie:Double,
        numConsesionarioAActualizar:Int,
        fechaApertura: Date
    ):Boolean{
        val conexionEscritura = writableDatabase
        val aActualizar = ContentValues()
        aActualizar.put("nombre",nombreConcesionario)
        aActualizar.put("abierto",estaAbierto)
        aActualizar.put("superficie",superficie)
        aActualizar.put("fechaApertura",fechaApertura.time)
        val resultadoActualizacion = conexionEscritura
            .update(
                "CONCESIONARIO",
                aActualizar,
                "numero=?",
                arrayOf(
                    numConsesionarioAActualizar.toString()
                )
            )
        conexionEscritura.close()
        return resultadoActualizacion.toInt() != -1
    }

    fun listarConcesionarios():ArrayList<Concesionario>?{
        val consulta = "SELECT * FROM CONCESIONARIO"
        val baseLectura = readableDatabase
        val resultadoConsulta = baseLectura.rawQuery(
            consulta,
            null
        )
        val existeConcesionario = resultadoConsulta.moveToFirst()
        var listaConcesionarios = arrayListOf<Concesionario>()
        if(existeConcesionario){
            do {
                val id = resultadoConsulta.getInt(0)
                val nombre = resultadoConsulta.getString(1)
                val estaAbierto = resultadoConsulta.getInt(2) == 1
                val superficie = resultadoConsulta.getDouble(3)
                val fechaApertura = Date(resultadoConsulta.getLong(4))
                if (id != null){
                    listaConcesionarios.add(
                        Concesionario(nombre,estaAbierto,superficie,id,fechaApertura)
                    )
                }
            }while (resultadoConsulta.moveToNext())
        }
        resultadoConsulta.close()
        baseLectura.close()
        return if(listaConcesionarios.size == 0) null else listaConcesionarios
    }
}