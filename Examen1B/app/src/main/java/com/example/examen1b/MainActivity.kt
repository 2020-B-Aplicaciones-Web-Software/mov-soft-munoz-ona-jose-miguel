package com.example.examen1b

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    var posicionElementoSeleccionado = 0
    val baseDatosConcesionario = SQLiteHelperConcesionario(this)
    val INTENT_N_CODE = 200
    val INTENT_E_CODE = 201
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("Lista de Concesionarios")
        val btnNuevoConcesionario = findViewById<FloatingActionButton>(R.id.bntNuevoConcesionario)
        btnNuevoConcesionario
            .setOnClickListener {
                startActivityForResult(Intent(this,EntradaDatosConcesionario::class.java),INTENT_N_CODE)
            }
        val listViewConcesionario = findViewById<ListView>(R.id.listViewConcesionarios)
        registerForContextMenu(listViewConcesionario)
        llenarListView()
    }

    fun llenarListView(){
        val listViewConcesionario = findViewById<ListView>(R.id.listViewConcesionarios)
        val arregloConcesionario = baseDatosConcesionario.listarConcesionarios()
        if (arregloConcesionario != null){
            val adaptador = ArrayAdapter<Concesionario>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                arregloConcesionario
            )
            listViewConcesionario.adapter = adaptador
        }else{
            listViewConcesionario.adapter = null
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflador = menuInflater
        inflador.inflate(R.menu.menuconcesionario,menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id= info.position
        posicionElementoSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val arregloConcesionario = baseDatosConcesionario.listarConcesionarios()
        return when(item?.itemId){
            R.id.miEditarConcesionario ->{
                val intento = Intent(this,EntradaDatosConcesionario::class.java)
                val elementoAEditar = arregloConcesionario?.get(posicionElementoSeleccionado)
                if(elementoAEditar != null){
                   intento.putExtra("concesionario",elementoAEditar.numConsesionario)
                }
                startActivityForResult(intento,INTENT_E_CODE)
                return true
            }
            R.id.miVerAutos ->{
                val intento = Intent(this,ListaAutos::class.java)
                val elementoAExpandir = arregloConcesionario?.get(posicionElementoSeleccionado)
                intento.putExtra("concesionario", elementoAExpandir?.numConsesionario)
                startActivity(intento)
                return true
            }
            R.id.miEliminarConcesionario ->{
                var elementoAEliminar = arregloConcesionario?.get(posicionElementoSeleccionado)
                val builder = AlertDialog.Builder(this)
                var resultado = false
                builder.setTitle("Eliminar")
                builder.setMessage("Está seguro que desea eliminar el elemento ${elementoAEliminar?.nombreConcesionario}")
                builder.setPositiveButton(
                    "Sí",
                    DialogInterface.OnClickListener { dialog, which ->
                        if(elementoAEliminar != null) {
                            resultado = baseDatosConcesionario.eliminarConcesionario(elementoAEliminar.numConsesionario)
                            if(resultado){
                                llenarListView()
                            }
                        }
                    }
                )
                builder.setNegativeButton(
                    "No",
                    null
                )
                val dialogo = builder.create()
                dialogo.show()
                return true
            }
            else-> return super.onContextItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            INTENT_N_CODE -> {
                if(resultCode == RESULT_OK){
                    if(data != null){
                        val estado = data.getBooleanExtra("resultado",false)
                        if(estado){
                            llenarListView()
                        }
                    }
                }
            }
            INTENT_E_CODE -> {
                if(resultCode == RESULT_OK){
                    if(data != null){
                        val estado = data.getBooleanExtra("resultado",false)
                        if(estado){
                            llenarListView()
                        }
                    }
                }
            }
        }
    }
}