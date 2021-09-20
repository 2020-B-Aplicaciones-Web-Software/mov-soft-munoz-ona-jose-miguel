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
import com.example.examen2b.dto.Concesionario
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    var posicionElementoSeleccionado = 0
    val INTENT_N_CODE = 200
    val INTENT_E_CODE = 201
    var arregloConcesionario = arrayListOf<Concesionario>()
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
        val db = Firebase.firestore
        val concesionariosRef = db.collection("concesionarios")
        concesionariosRef.get().addOnSuccessListener {
            arregloConcesionario = ArrayList( it.map {
                return@map it.toObject<Concesionario>()
            })
            if (arregloConcesionario != null){
                val adaptador = ArrayAdapter(
                    this,
                    android.R.layout.simple_expandable_list_item_1,
                    arregloConcesionario
                )
                listViewConcesionario.adapter = adaptador
            }else{
                listViewConcesionario.adapter = null
            }
        }
            .addOnFailureListener {
                Log.i("Firestore","Error al traer los concesionarios")
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
        val db = Firebase.firestore
        return when(item?.itemId){
            R.id.miEditarConcesionario ->{
                val intento = Intent(this,EntradaDatosConcesionario::class.java)
                val elementoAEditar = arregloConcesionario?.get(posicionElementoSeleccionado)
                if(elementoAEditar != null){
                    intento.putExtra("numConcesionario",elementoAEditar.numConsesionario)
                    intento.putExtra("nombreConcesionario",elementoAEditar.nombreConcesionario)
                }
                startActivityForResult(intento,INTENT_E_CODE)
                return true
            }
            R.id.miVerAutos ->{
                val intento = Intent(this,ListaAutos::class.java)
                val elementoAExpandir = arregloConcesionario?.get(posicionElementoSeleccionado)
                intento.putExtra("numConcesionario", elementoAExpandir?.numConsesionario)
                intento.putExtra("nomConcesionario", elementoAExpandir?.nombreConcesionario)
                startActivity(intento)
                return true
            }
            R.id.miEliminarConcesionario ->{
                var elementoAEliminar = arregloConcesionario?.get(posicionElementoSeleccionado)
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Eliminar")
                builder.setMessage("Está seguro que desea eliminar el elemento ${elementoAEliminar?.nombreConcesionario}")
                builder.setPositiveButton(
                    "Sí",
                    DialogInterface.OnClickListener { dialog, which ->
                        if(elementoAEliminar != null) {
                            val reference = db.collection("concesionarios")
                            reference
                                .document("${elementoAEliminar.nombreConcesionario}#${elementoAEliminar.numConsesionario}")
                                .delete()
                                .addOnSuccessListener {
                                    llenarListView()
                                }
                                .addOnFailureListener {
                                    Log.i("Firestore", "No se ha podido eliminar el concesionario especificado")
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