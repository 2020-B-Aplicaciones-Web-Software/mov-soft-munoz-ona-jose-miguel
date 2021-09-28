package com.example.examen2b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import com.example.examen2b.dto.Concesionario
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat

class EntradaDatosConcesionario : AppCompatActivity() {
    val formatoFecha = SimpleDateFormat("dd/MM/yyyy")
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
        val idConcesionario = intent.getIntExtra("numConcesionario",-1)
        val id2Concesionario = intent.getStringExtra("nombreConcesionario")
        if(idConcesionario != -1){ // cuando se llega a esta actividad desde la edición de un concesionario
            val db = Firebase.firestore
            val referecia = db.collection("concesionarios")
            referecia.document("${id2Concesionario}#${idConcesionario}")
                .get().addOnSuccessListener {
                    var concesionario = it.toObject<Concesionario>()
                    txtNombreConcesionario.setText(concesionario?.nombreConcesionario)
                    if (concesionario?.estaAbierto != null){
                        swEstaAbierto.isChecked = concesionario.estaAbierto!!
                    }
                    txtSuperficieConcesionario.setText(concesionario?.superficie.toString())
                    txtNumConcesionario.setText(concesionario?.numConsesionario.toString())
                    txtNombreConcesionario.isEnabled = false
                    txtNumConcesionario.isEnabled = false
                    txtFecha.setText(formatoFecha.format(concesionario?.fechaApertura))
                }
        }
        btnListo.setOnClickListener {
            val nombreConcesionario = txtNombreConcesionario.text.toString()
            val estaAbierto = swEstaAbierto.isChecked
            val superficie = txtSuperficieConcesionario.text.toString().toDouble()
            val numConcesionario = txtNumConcesionario.text.toString().toInt()
            var fecha = formatoFecha.parse(txtFecha.text.toString())
            var resultado = false
            val db = Firebase.firestore
            val colectionReference = db.collection("concesionarios")
            if(idConcesionario != -1){
                colectionReference.document("${nombreConcesionario}#${numConcesionario}")
                    .update(mapOf(
                        "estaAbierto" to estaAbierto,
                        "fechaApertura" to fecha,
                        "nombreConcesionario" to nombreConcesionario,
                        "superficie" to superficie
                    ))
                    .addOnSuccessListener {
                        resultado = true
                        terminarActividad(resultado)
                    }
                    .addOnFailureListener {
                        Log.i("Firestore","No se pudo actualizar el concesionario correctamente")
                        terminarActividad(resultado)
                    }
            } else {
                colectionReference.document("${nombreConcesionario}#${numConcesionario}")
                    .set(Concesionario(
                        nombreConcesionario,
                        estaAbierto,
                        superficie,
                        numConcesionario,
                        fecha
                    ))
                    .addOnSuccessListener {
                        resultado = true
                        terminarActividad(resultado)
                    }
                    .addOnFailureListener {
                        Log.i("Firestore","No se pudo registrar el concesionario correctamente")
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