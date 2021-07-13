package com.example.moviles_sw_2021a

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ListaUsuariosBD : AppCompatActivity() {
    val INTENT_R_N_CODE = 200
    val INTENT_R_A_CODE = 201
    var posicionElementoSeleccionado = 0
    val baseDatos = ESqliteHelperUsuario(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lista_usuarios_bd)

        val btnAnadirNuevo = findViewById<FloatingActionButton>(R.id.btnAnadirNuevo)
        btnAnadirNuevo.setOnClickListener {
            abrirActividad(EntradaDatosUsuarioBD::class.java)
        }
        val listaUsuarios = findViewById<ListView>(R.id.listViewUsuariosBD)
        registerForContextMenu(listaUsuarios)
        cargarListView()
    }
    fun abrirActividad(clase:Class<*>){
        val intentExplicito = Intent(this, clase)
        startActivityForResult(intentExplicito,INTENT_R_N_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode){
            INTENT_R_N_CODE -> {
                if (resultCode == RESULT_OK){
                    if(data != null){
                        val estado = data.getBooleanExtra("resultado",false)
                        if (estado){
                            cargarListView()
                        }
                    }
                }
            }
            INTENT_R_A_CODE -> {
                if (resultCode == RESULT_OK){
                    if(data != null){
                        val estado = data.getBooleanExtra("resultado",false)
                        if (estado){
                            cargarListView()
                        }
                    }
                }
            }
        }
    }

    fun cargarListView(){
        val listaUsuarios = findViewById<ListView>(R.id.listViewUsuariosBD)
        val arregloUsuarios = baseDatos.listarUsuarios()
        val adaptador:ArrayAdapter<EUsuarioBDD>
        if (arregloUsuarios != null) {
            adaptador = ArrayAdapter(
                this,
                android.R.layout.simple_expandable_list_item_1,
                arregloUsuarios
            )
            listaUsuarios.adapter = adaptador
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        val inflator = menuInflater
        inflator.inflate(R.menu.menu,menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionElementoSeleccionado = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val arregloUsuarios = baseDatos.listarUsuarios()
        return when(item?.itemId){
            R.id.miEditar ->{
                val intento = Intent(this,EntradaDatosUsuarioBD::class.java)
                val elementoAEditar = arregloUsuarios?.get(posicionElementoSeleccionado)
                if(elementoAEditar != null){
                    intento.putExtra("usuario",elementoAEditar)
                }
                startActivityForResult(intento,INTENT_R_A_CODE)
                return true
            }
            R.id.miEliminar ->{
                val elementoAEliminar = arregloUsuarios?.get(posicionElementoSeleccionado)
                if(elementoAEliminar != null){
                    val resultado = baseDatos.eliminiarUsuarioFormulario(elementoAEliminar.id!!)
                    if(resultado){
                        cargarListView()
                    }
                }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
}