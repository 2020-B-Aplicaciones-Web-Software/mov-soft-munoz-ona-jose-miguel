package com.example.examen1b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import java.text.SimpleDateFormat

class EntradaDatosAuto : AppCompatActivity() {
    var numConcesionario = -1
    val formatoFecha = SimpleDateFormat("dd/mm/yyyy")
    val baseDatosAuto = SQLiteHelperAuto(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrada_datos_auto)
        setTitle("Datos de Auto")
        numConcesionario = intent.getIntExtra("numConcesionario",-1)
        if (numConcesionario == -1){
            val intento = Intent()
            setResult(RESULT_CANCELED,intento)
            finish()
        }
        val txtMarca = findViewById<EditText>(R.id.txtMarcaAuto)
        val txtModelo = findViewById<EditText>(R.id.txtModeloAuto)
        val txtChasis = findViewById<EditText>(R.id.txtNumChasisAuto)
        val txtPuertas = findViewById<EditText>(R.id.txtNumPuertasAuto)
        val txtPrecio = findViewById<EditText>(R.id.txtPrecioAuto)
        val swEsUsado = findViewById<Switch>(R.id.swUsadoAuto)
        val txtAño = findViewById<EditText>(R.id.txtAnioAuto)
        val btnListo = findViewById<Button>(R.id.btnAnadirNuevoAuto)
        val idAuto = intent.getStringExtra("chasis")
        if(idAuto != null){
            val auto = baseDatosAuto.consultarAutoPorChasis(idAuto)
            txtMarca.setText(auto.marca)
            txtModelo.setText(auto.modelo)
            txtChasis.setText(auto.numChasis)
            txtChasis.isEnabled = false
            txtPuertas.setText(auto.numPuertas.toString())
            txtPrecio.setText(auto.precio.toString())
            swEsUsado.isChecked = auto.esUsado
            txtAño.setText(formatoFecha.format(auto.año).toString())
        }
        btnListo.setOnClickListener {
            val marca = txtMarca.text.toString()
            val modelo = txtModelo.text.toString()
            val chasis = txtChasis.text.toString()
            val puertas = txtPuertas.text.toString().toInt()
            val precio = txtPrecio.text.toString().toDouble()
            val esUsado = swEsUsado.isChecked
            val año = formatoFecha.parse(txtAño.text.toString())
            var resultado = false
            if(idAuto == null){
                resultado = baseDatosAuto.crearAuto(marca,
                    año,
                    esUsado,
                    precio,
                    puertas,
                    chasis,
                    modelo,
                    numConcesionario)
            }else{
                resultado = baseDatosAuto.actualizarAuto(marca,
                    año,
                    esUsado,
                    precio,
                    puertas,
                    idAuto,
                    modelo)
            }
            val intento = Intent()
            intento.putExtra("resultado",resultado)
            setResult(RESULT_OK,intento)
            finish()
        }
    }
}