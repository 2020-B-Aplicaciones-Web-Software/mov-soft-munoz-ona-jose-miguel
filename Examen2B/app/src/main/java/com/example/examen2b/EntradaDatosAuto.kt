package com.example.examen2b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.example.examen2b.dto.Auto
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class EntradaDatosAuto : AppCompatActivity() {
    var numConcesionario = -1
    var nomConcesionario:String? = null
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_entrada_datos_auto)
        setTitle("Datos de Auto")
        numConcesionario = intent.getIntExtra("numConcesionario",-1)
        nomConcesionario = intent.getStringExtra("nomConcesionario")
        if (numConcesionario == -1 || nomConcesionario == null){
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
        val etLat = findViewById<EditText>(R.id.et_Latitud)
        val etLong = findViewById<EditText>(R.id.et_Longitud)
        val idAuto = intent.getStringExtra("chasis")
        if(idAuto != null){ //editar auto
            val db = Firebase.firestore
            val referenciaAuto = db
                .collection("concesionarios").document("${nomConcesionario}#${numConcesionario}")
                .collection("autos").document(idAuto)
            referenciaAuto.get().addOnSuccessListener {
                val auto = it.toObject<Auto>()
                txtMarca.setText(auto?.marca)
                txtModelo.setText(auto?.modelo)
                txtChasis.setText(auto?.numChasis)
                txtChasis.isEnabled = false
                txtPuertas.setText(auto?.numPuertas.toString())
                txtPrecio.setText(auto?.precio.toString())
                swEsUsado.isChecked = auto?.esUsado!!
                txtAño.setText(formatoFecha.format(auto?.año).toString())
                etLat.setText(auto?.latitud.toString())
                etLong.setText(auto?.longitud.toString())
            }

        }
        btnListo.setOnClickListener {
            val marca = txtMarca.text.toString()
            val modelo = txtModelo.text.toString()
            val chasis = txtChasis.text.toString()
            val puertas = txtPuertas.text.toString().toInt()
            val precio = txtPrecio.text.toString().toDouble()
            val esUsado = swEsUsado.isChecked
            val lat = etLat.text.toString().toDouble()
            val lon = etLong.text.toString().toDouble()
            val año = formatoFecha.parse(txtAño.text.toString())
            var resultado = false
            val db = Firebase.firestore
            val referenciaAuto = db
                .collection("concesionarios").document("${nomConcesionario}#${numConcesionario}")
                .collection("autos")
            if(idAuto == null){
                //insercion de nuevo documento
                    referenciaAuto.document(chasis)
                        .set(Auto(
                            marca,
                            año,
                            esUsado,
                            precio,
                            puertas,
                            chasis,
                            modelo,
                            lon,
                            lat
                        ))
                        .addOnSuccessListener {
                            resultado = true
                            terminarActividad(resultado)
                        }

            }else{
                referenciaAuto.document(idAuto)
                    .update(mapOf(
                        "año" to año,
                        "esUsado" to esUsado,
                        "latitud" to lat,
                        "longitud" to lon,
                        "marca" to marca,
                        "modelo" to modelo,
                        "numPuertas" to puertas,
                        "precio" to precio
                    ))
                    .addOnSuccessListener {
                        resultado = true
                        terminarActividad(resultado)
                    }
            }

        }
    }

    fun terminarActividad(resultado:Boolean){
        val intento = Intent()
        intento.putExtra("resultado",resultado)
        setResult(RESULT_OK,intento)
        finish()
    }
}