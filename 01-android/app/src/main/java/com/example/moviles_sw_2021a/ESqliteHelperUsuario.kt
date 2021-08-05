package com.example.moviles_sw_2021a

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import androidx.core.content.contentValuesOf

class ESqliteHelperUsuario(
    contexto : Context?
):SQLiteOpenHelper(
    contexto,
    "moviles",
    null,
    1
) {
    override fun onCreate(db: SQLiteDatabase?) {
        val scriptCrearTablaUsuario =
            """
                CREATE TABLE USUARIO(
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nombre VARCHAR(50),
                descripcion VARCHAR(50)
                )
            """.trimIndent()
        Log.i("bdd","Creando la tbla de usuario")
        db?.execSQL(scriptCrearTablaUsuario)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    fun crearUsuarioFormulario(
        nombre:String,
        descripcion:String
    ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAGuardar  = ContentValues()
        valoresAGuardar.put("nombre",nombre)
        valoresAGuardar.put("descripcion",descripcion)

        val resultadoEscritura:Long = conexionEscritura
            .insert(
                "USUARIO",
                null,
                valoresAGuardar
            )
        conexionEscritura.close()
        return if (resultadoEscritura.toInt() == -1) false else true
    }

    fun consultarUsuarioPorId(id:Int):EUsuarioBDD{
        val scriptConsultarUsuario = "SELECT * FROM USUARIO WHERE ID = ${id}"
        val baseDatosLectura = readableDatabase
        val resultaConsutaLectura = baseDatosLectura.rawQuery(
            scriptConsultarUsuario,
            null
        )

        val existeUsuario = resultaConsutaLectura.moveToFirst()
        val usuarioEncontrado  = EUsuarioBDD(0, "","")
        if (existeUsuario){
            do {
                val id = resultaConsutaLectura.getInt(0)
                val nombre = resultaConsutaLectura.getString(1)
                val descripcion = resultaConsutaLectura.getString(2)
                if(id != null){
                    usuarioEncontrado.id = id
                    usuarioEncontrado.nombre = nombre
                    usuarioEncontrado.descripcion = descripcion
                }
            }while (resultaConsutaLectura.moveToNext())
        }
        resultaConsutaLectura.close()
        baseDatosLectura.close()
        return usuarioEncontrado
    }

    fun eliminiarUsuarioFormulario(id:Int):Boolean{
        val conexionEscritura = writableDatabase
        val resultadoEliminacion = conexionEscritura
            .delete(
                "USUARIO",
                "id=?",
                arrayOf(
                    id.toString()
                )
            )
        conexionEscritura.close()
        return if(resultadoEliminacion.toInt() == -1) false else true
    }

    fun actualizarUsuarioFormulario(
        nombre: String,
        descripcion: String,
        idActualizar:Int
    ):Boolean{
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre",nombre)
        valoresAActualizar.put("descripcion",descripcion)

        val resultadoActualizacion = conexionEscritura
            .update(
                "USUARIO",
                valoresAActualizar,
                "id=?",
                arrayOf(
                    idActualizar.toString()
                )
            )
        conexionEscritura.close()
        return resultadoActualizacion.toInt() != -1
    }

    fun listarUsuarios():ArrayList<EUsuarioBDD>?{
        val scriptConsulta = "SELECT * FROM USUARIO"
        val baseLectura = readableDatabase
        val resultadoConsulta = baseLectura.rawQuery(
            scriptConsulta,
            null
        )
        val existeUsuario = resultadoConsulta.moveToFirst()
        var listaUsuarios = arrayListOf<EUsuarioBDD>()
        if (existeUsuario){
            do {
                val id = resultadoConsulta.getInt(0)
                val nombre = resultadoConsulta.getString(1)
                val desc = resultadoConsulta.getString(2)
                if (id != null){
                    listaUsuarios.add(EUsuarioBDD(id,nombre,desc))
                }
            }while (resultadoConsulta.moveToNext())
        }
        resultadoConsulta.close()
        baseLectura.close()
        return if(listaUsuarios.size == 0) null else listaUsuarios
    }
}