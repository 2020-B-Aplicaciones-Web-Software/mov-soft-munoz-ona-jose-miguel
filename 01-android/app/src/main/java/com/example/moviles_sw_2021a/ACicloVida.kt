package com.example.moviles_sw_2021a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Button
import android.widget.TextView

class ACicloVida : AppCompatActivity() {

    var numero = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aciclo_vida)
        Log.i("ciclo-vida","onStart")
        val botonCicloVida = findViewById<Button>(
            R.id.btnCicloVida
        )
        actualizarNumeroEnPantalla()
        botonCicloVida.setOnClickListener{
            aumentarNumero()
            actualizarNumeroEnPantalla()
        }
    }
    
    fun aumentarNumero(){
        numero = numero +1
    }

    fun actualizarNumeroEnPantalla(){
        val textViewCicloVida = findViewById<TextView>(
            R.id.txvCicloVida
        )
        textViewCicloVida.text = numero.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.run {
            //Aqui guardamos cualquier primitivo
            // y solo primitivos
            putInt("numeroGuardado",numero)
        }
        super.onSaveInstanceState(outState)
        Log.i("ciclo-vida","onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val numeroRecuperado:Int? = savedInstanceState.getInt("numeroGuardado")
        if (numeroRecuperado != null){
            numero = numeroRecuperado
            val textViewACicloVida = findViewById<TextView>(R.id.txvCicloVida)
            textViewACicloVida.text = numero.toString()
        }
        Log.i("ciclo-vida","onRestoreInstanceState")
    }

    override fun onStart() {
        super.onStart()
        Log.i("ciclo-vida","onStart")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i("ciclo-vida","onRestart")
    }

    override fun onPause() {
        super.onPause()
        Log.i("ciclo-vida","onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i("ciclo-vida","onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i("ciclo-vida","onDestroy")
    }
}