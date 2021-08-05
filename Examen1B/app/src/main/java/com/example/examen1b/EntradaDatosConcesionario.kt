package com.example.examen1b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import java.text.SimpleDateFormat
import java.util.*

class EntradaDatosConcesionario : AppCompatActivity() {
    val baseDatosConcesionario = SQLiteHelperConcesionario(this)
    val formatoFecha = SimpleDateFormat("dd/mm/yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrada_datos_concesionario)
        setTitle("Datos de Concesionario")
        val txtNombreConcesionario = findViewById<EditText>(R.id.txtNombreConcesionario)
        val swEstaAbierto = findViewById<Switch>(R.id.swConcesionarioAbierto)
        val txtSuperficieConcesionario = findViewById<EditText>(R.id.txtSuperficieConcesionario)
        val txtNumConcesionario = findViewById<EditText>(R.id.txtNumConcesionario)
        val txtFecha = findViewById<EditText>(R.id.txtFechaApertura)
        val btnListo = findViewById<Button>(R.id.btnAnadirNuevoConcesionario)
        val idConcesionario = intent.getIntExtra("concesionario",-1)
        if(idConcesionario != -1){
            val concesionario = baseDatosConcesionario.consultarConcesionarioPorId(idConcesionario)
            txtNombreConcesionario.setText(concesionario.nombreConcesionario)
            swEstaAbierto.isChecked = concesionario.estaAbierto
            txtSuperficieConcesionario.setText(concesionario.superficie.toString())
            txtNumConcesionario.setText(concesionario.numConsesionario.toString())
            txtNumConcesionario.isEnabled = false
            txtFecha.setText(formatoFecha.format(concesionario.fechaApertura))
        }
        btnListo.setOnClickListener {
            val nombreConcesionario = txtNombreConcesionario.text.toString()
            val estaAbierto = swEstaAbierto.isChecked
            val superficie = txtSuperficieConcesionario.text.toString().toDouble()
            val numConcesionario = txtNumConcesionario.text.toString().toInt()
            var fecha = formatoFecha.parse(txtFecha.text.toString())
            var resultado = false
            if(idConcesionario != -1){
                resultado = baseDatosConcesionario.actualizarConcesionario(nombreConcesionario,
                    estaAbierto,
                    superficie,
                    idConcesionario,
                    fecha)
            } else {
                resultado = baseDatosConcesionario.crearConcesionario(nombreConcesionario,
                    estaAbierto,
                    superficie,
                    numConcesionario,
                    fecha)
            }
            val intento = Intent()
            intento.putExtra("resultado",resultado)
            setResult(RESULT_OK,intento)
            finish()
        }
    }
}