package com.example.firebasedos

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import com.example.firebasedos.dto.FirestorePedido
import com.example.firebasedos.dto.FirestoreRestaurant
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class PedidosUsuario : AppCompatActivity() {
    var query:Query? = null
    lateinit var adaptador:ArrayAdapter<FirestorePedido>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pedidos_usuario)
        val lista = findViewById<ListView>(R.id.lv_ordenes_usuario)
        adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_expandable_list_item_1,
            arrayListOf<FirestorePedido>()
        )
        lista.adapter = adaptador
        consultar()
        val botonCargar = findViewById<Button>(R.id.btn_cargar)
        botonCargar.setOnClickListener {
            consultar()
        }
        lista.setOnItemClickListener{ adapterView, view, posicion, id ->
            val elemento = adapterView.adapter.getItem(posicion) as FirestorePedido
            if(elemento.estado.equals("Enviado")){
                var calificacion = 2
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Califica tu entrega")
                val opciones = resources.getStringArray(R.array.calificaciones_pedido)
                builder.setSingleChoiceItems(opciones,0,{
                    dialog, value ->
                    calificacion = 2 - value
                })
                builder.setPositiveButton(
                    "OK",
                    DialogInterface.OnClickListener{ dialog, which ->
                       elemento.calificacion = calificacion
                        elemento.estado = "Entregado"
                       actualizarDocumento(elemento)
                    }
                )
                val dialogo = builder.create()
                dialogo.show()
            }
            adaptador.notifyDataSetChanged()
        }
    }

    fun consultar(){
        val db = Firebase.firestore
        val consultaInicial = db.collection("pedidos")
            .whereEqualTo("usuario",BAuthUsuario.usuario?.email)
            .orderBy("fechaPedido",Query.Direction.DESCENDING)
            .limit(1)
        var tarea: Task<QuerySnapshot>? = null
        if(query == null){
            tarea = consultaInicial.get()
        } else{
            tarea = query!!.get()
        }
        if(tarea != null){
            tarea.addOnSuccessListener {
                guardarEstadoConsulta(it,consultaInicial)
                val listaOrdenes = it.map {
                    var orden = it.toObject<FirestorePedido>()
                    orden.uid = it.id
                    return@map orden
                }
                listaOrdenes.forEach {
                    adaptador.add(it)
                }
                adaptador.notifyDataSetChanged()
            }
        }
    }

    fun guardarEstadoConsulta(documenSnapshot:QuerySnapshot, consulta:Query){
        if(documenSnapshot.size()>0){
            val lastDoc = documenSnapshot.documents.get(documenSnapshot.size()-1)
            query = consulta.startAfter(lastDoc)
        }
    }

    fun actualizarDocumento(elemento:FirestorePedido){
        val restaurant = elemento.restaurante
        val db = Firebase.firestore
        val referenciaPedido = db.collection("pedidos").document(elemento.uid!!)
        referenciaPedido
            .update(mapOf(
                "calificacion" to elemento.calificacion,
                "estado" to elemento.estado
            ))
            .addOnFailureListener {
                Log.i("Firestore","Error al actualizar pedido")
            }
        if (restaurant != null){
            val referenciaRest = db.collection("restaurante").document(restaurant.uid!!)
            restaurant.sumCalificaciones += elemento.calificacion!!
            restaurant.usuariosCalificado += 1
            restaurant.calcularPromedio()
            referenciaRest
                .update(mapOf(
                    "calificacionPromedio" to restaurant.calificacionPromedio,
                    "sumCalificaciones" to restaurant.sumCalificaciones,
                    "usuariosCalificado" to restaurant.usuariosCalificado,
                    "uid" to restaurant.uid
                ))
        }
    }
}