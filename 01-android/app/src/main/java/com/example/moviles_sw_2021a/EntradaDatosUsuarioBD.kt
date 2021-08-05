package com.example.moviles_sw_2021a

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class EntradaDatosUsuarioBD : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrada_datos_usuario_bd)
        val eTxtNombre = findViewById<EditText>(R.id.etxtNombre)
        val eTxtDescripcion = findViewById<EditText>(R.id.etxtDescripcion)
        val botonListo = findViewById<Button>(R.id.btnListo)
        val baseDatos = ESqliteHelperUsuario(this)
        val infoUsuario = intent.getParcelableExtra<EUsuarioBDD>("usuario")
        eTxtNombre.setText(infoUsuario?.nombre)
        eTxtDescripcion.setText(infoUsuario?.descripcion)
        botonListo.setOnClickListener {
            val nombre = eTxtNombre.text.toString()
            val descripcion = eTxtDescripcion.text.toString()
            var resultado = false
            if(infoUsuario != null){
                resultado = baseDatos.actualizarUsuarioFormulario(nombre,descripcion,infoUsuario.id!!)
            }else{
                resultado = baseDatos.crearUsuarioFormulario(nombre,descripcion)
            }
            val intent = Intent()
            intent.putExtra("resultado",resultado)
            setResult(RESULT_OK,intent)
            finish()
        }
    }

}