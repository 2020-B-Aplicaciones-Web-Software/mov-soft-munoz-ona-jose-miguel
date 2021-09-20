package com.example.examen2b

import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.examen2b.dto.Auto
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class ListaAutos : AppCompatActivity() {
    var posicionElementoSeleccionado = 0
    var numConcesionario = -1
    var nombreConcesionario:String? = null
    val INTENT_N_AUTO = 300
    val INTENT_A_AUTO = 301
    var arregloAutos = arrayListOf<Auto>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_autos)
        val btnNuevoAuto = findViewById<FloatingActionButton>(R.id.btnNuevoAuto)
        val listaAutos = findViewById<ListView>(R.id.listViewAutos)
        numConcesionario = intent.getIntExtra("numConcesionario",-1)
        nombreConcesionario = intent.getStringExtra("nomConcesionario")
        if ((numConcesionario ==-1) || (nombreConcesionario == null)){
            finish()
        }
        setTitle("Lista de Autos ${nombreConcesionario}")
        btnNuevoAuto.setOnClickListener {
            val intento = Intent(this,EntradaDatosAuto::class.java)
            intento.putExtra("numConcesionario",numConcesionario)
            intento.putExtra("nomConcesionario",nombreConcesionario)
            startActivityForResult(intento,INTENT_N_AUTO)
        }
        cargarListView(numConcesionario)
        registerForContextMenu(listaAutos)
    }
    fun cargarListView(numConcesionario:Int){
        val listaAutos = findViewById<ListView>(R.id.listViewAutos)
        val db = Firebase.firestore
        val reference = db
            .collection("concesionarios").document("${nombreConcesionario}#${numConcesionario}")
            .collection("autos")
        reference.get().addOnSuccessListener {
            arregloAutos = ArrayList(
                it.map {
                    return@map it.toObject<Auto>()
                }
            )
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
            .addOnFailureListener {
                Log.i("Firestore","No se pudieron leer los autos del concesionario")
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
        val db = Firebase.firestore
        return when(item?.itemId){
            R.id.miEditarAuto ->{
                val elementoAEditar = arregloAutos?.get(posicionElementoSeleccionado)
                val intent = Intent(this,EntradaDatosAuto::class.java)
                intent.putExtra("chasis",elementoAEditar?.numChasis)
                intent.putExtra("numConcesionario",numConcesionario)
                intent.putExtra("nomConcesionario",nombreConcesionario)
                startActivityForResult(intent,INTENT_A_AUTO)
                return true
            }
            R.id.miEliminarAuto -> {
                val elementoAEliminar = arregloAutos?.get(posicionElementoSeleccionado)
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar")
                builder.setMessage("Está seguro que desea eliminar el elemento ${elementoAEliminar?.modelo}")
                builder.setPositiveButton(
                    "Sí",
                    DialogInterface.OnClickListener { dialog, which ->
                        if(elementoAEliminar != null) {
                            val referencia = db
                                .collection("concesionarios").document("${nombreConcesionario}#${numConcesionario}")
                                .collection("autos").document(elementoAEliminar.numChasis!!)
                            referencia.delete().addOnSuccessListener {
                                cargarListView(numConcesionario)
                                Log.i("Firestore","Si se elimino el auto ${elementoAEliminar.numChasis!!}")
                            }.addOnFailureListener {
                                Log.i("Firestore","No se puedo eliminar el auto seleccionadp")
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
            R.id.miLocalizarAuto -> {
                val elementoALocalizar = arregloAutos?.get(posicionElementoSeleccionado)
                val intento = Intent(
                    this,
                    VisualizadorMapa::class.java
                )
                intento.putExtra("latitud",elementoALocalizar.latitud)
                intento.putExtra("longitud",elementoALocalizar.longitud)
                intento.putExtra("modelo",elementoALocalizar.modelo)
                startActivity(intento)
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