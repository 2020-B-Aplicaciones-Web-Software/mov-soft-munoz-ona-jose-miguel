package com.example.moviles_sw_2021a

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button

class CIntentExplicitoParametros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cintent_explicito_parametros)

        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val edad = intent.getIntExtra("edad",0)
        val entrenador = intent.getParcelableExtra<BEntrenador>("entrenador")
        Log.i("intent-explicito","${nombre}")
        Log.i("intent-explicito","${apellido}")
        Log.i("intent-explicito","${edad}")
        Log.i("intent-explicito","${entrenador}")

        val botonDevolverRespuesta = findViewById<Button>(R.id.btnDevolverRespuesta)

        botonDevolverRespuesta
            .setOnClickListener {
                val intentDevolverParametros = Intent()
                intentDevolverParametros.putExtra("nombreModificado","Jose")
                intentDevolverParametros.putExtra("edadModificado",23)
                //se pueden ejecutar el siguiente metodo de formas equivalentes
                //this.setResult()
                //setResult(Activity.RESULT_OK)
                setResult(RESULT_OK,intentDevolverParametros)
                //para finalizar esta actividad puedo hacer
                //this.finish() o
                finish()

            }
    }
}