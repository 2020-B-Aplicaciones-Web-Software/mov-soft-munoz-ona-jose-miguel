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
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaAutos : AppCompatActivity() {
    var posicionElementoSeleccionado = 0
    val baseDatosAuto = SQLiteHelperAuto(this)
    val baseDatosConcesionario = SQLiteHelperConcesionario(this)
    var numConcesionario = -1
    val INTENT_N_AUTO = 300
    val INTENT_A_AUTO = 301
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_autos)
        val btnNuevoAuto = findViewById<FloatingActionButton>(R.id.btnNuevoAuto)
        val listaAutos = findViewById<ListView>(R.id.listViewAutos)
        numConcesionario = intent.getIntExtra("concesionario",-1)
        if (numConcesionario == -1){
            finish()
        }
        val nombreCon = baseDatosConcesionario.consultarConcesionarioPorId(numConcesionario)
        setTitle("Lista de Autos ${nombreCon?.nombreConcesionario}")
        btnNuevoAuto.setOnClickListener {
            val intento = Intent(this,EntradaDatosAuto::class.java)
            intento.putExtra("numConcesionario",numConcesionario)
            startActivityForResult(intento,INTENT_N_AUTO)
        }
        cargarListView(numConcesionario)
        registerForContextMenu(listaAutos)
    }

    fun cargarListView(numConcesionario:Int){
        val listaAutos = findViewById<ListView>(R.id.listViewAutos)
        val arregloAutos = baseDatosAuto.listarAutosDeConcesionario(numConcesionario)
        if(arregloAutos != null){
            val adaptador = ArrayAdapter<Auto>(
                this,
                android.R.layout.simple_expandable_list_item_1,
                arregloAutos
            )
            listaAutos.adapter = adaptador
        } else{
            listaAutos.adapter = null
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflador = menuInflater
        inflador.inflate(R.menu.menuauto,menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id= info.position
        posicionElementoSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val arregloAutos = baseDatosAuto.listarAutosDeConcesionario(numConcesionario)
        return when(item?.itemId){
            R.id.miEditarAuto ->{
                val elementoAEditar = arregloAutos?.get(posicionElementoSeleccionado)
                val intent = Intent(this,EntradaDatosAuto::class.java)
                intent.putExtra("chasis",elementoAEditar?.numChasis)
                intent.putExtra("numConcesionario",numConcesionario)
                startActivityForResult(intent,INTENT_A_AUTO)
                return true
            }
            R.id.miEliminarAuto -> {
                val elementoAEliminar = arregloAutos?.get(posicionElementoSeleccionado)
                val builder = AlertDialog.Builder(this)
                var resultado = false
                builder.setTitle("Eliminar")
                builder.setMessage("Está seguro que desea eliminar el elemento ${elementoAEliminar?.modelo}")
                builder.setPositiveButton(
                    "Sí",
                    DialogInterface.OnClickListener { dialog, which ->
                        if(elementoAEliminar != null) {
                            resultado = baseDatosAuto.eliminarAuto(elementoAEliminar.numChasis!!)
                            if(resultado){
                                cargarListView(numConcesionario)
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
            else -> return super.onContextItemSelected(item)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            INTENT_N_AUTO -> {
                if(resultCode == RESULT_OK){
                    if(data != null){
                        val estado = data.getBooleanExtra("resultado",false)
                        if (estado) cargarListView(numConcesionario)
                    }
                }
            }
            INTENT_A_AUTO ->{
                if(resultCode == RESULT_OK){
                    if(data != null){
                        val estado = data.getBooleanExtra("resultado",false)
                        if (estado) cargarListView(numConcesionario)
                    }
                }
            }
        }
    }
}