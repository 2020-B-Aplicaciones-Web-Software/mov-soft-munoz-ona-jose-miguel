package com.example.firebasedos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.firebasedos.dto.FirestorePedido
import com.example.firebasedos.dto.FirestoreRestaurant
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class BusquedaOrdenes : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var posR:Int = 0
    var posF:Int = 0
    var templateMenu:Int = R.menu.menu_vacio
    var posicionOrden:Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_busqueda_ordenes)
        llenarSpinners()
        val botonFiltro = findViewById<Button>(R.id.btn_filtrar_ordenes)
        botonFiltro.setOnClickListener {
            llenarPedidos()
        }
        val listaPedidos = findViewById<ListView>(R.id.lv_pedidos_realizados)
        registerForContextMenu(listaPedidos)
    }

    fun llenarSpinners(){
        val spinnerRestaurant = findViewById<Spinner>(R.id.sp_elegir_restaurante)
        val spinnerFiltro = findViewById<Spinner>(R.id.sp_elegir_filtro)
        ArrayAdapter.createFromResource(
            this,
            R.array.lista_estados,
            android.R.layout.simple_spinner_item
        ).also {
            it.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerFiltro.adapter = it
            spinnerFiltro.onItemSelectedListener = this
        }

        val db = Firebase.firestore
        db.collection("restaurante")
            .get()
            .addOnSuccessListener {
                val listaRestaurantes = it.map {
                    val restaurante = it.toObject(FirestoreRestaurant::class.java)
                    restaurante.uid = it.id
                    return@map restaurante
                }
                if(listaRestaurantes != null){
                    val adaptador = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        listaRestaurantes
                    )
                    FirestoreRestaurant.listaRestaurantes = listaRestaurantes
                    spinnerRestaurant.adapter = adaptador
                    spinnerRestaurant.onItemSelectedListener = this
                }
            }

    }

    fun llenarPedidos(){
        val listaPedidos = findViewById<ListView>(R.id.lv_pedidos_realizados)
        val spinnerFiltro = findViewById<Spinner>(R.id.sp_elegir_filtro)
        val restauranteSeleccionado = FirestoreRestaurant.listaRestaurantes.get(posR)
        val db = Firebase.firestore
        val coleccionRef = db.collection("pedidos")
        coleccionRef
            .whereEqualTo("restaurante.uid",restauranteSeleccionado.uid)
            .whereEqualTo("estado",spinnerFiltro.getItemAtPosition(posF))
            .get()
            .addOnSuccessListener {
                val listaOrdenesPedidas = it.map {
                    var pedido = it.toObject<FirestorePedido>()
                    pedido.uid = it.id
                    return@map pedido
                }
                if(listaOrdenesPedidas != null){
                    FirestorePedido.listaPedidos = listaOrdenesPedidas
                    val adaptador = ArrayAdapter(
                        this,
                        android.R.layout.simple_expandable_list_item_1,
                        FirestorePedido.listaPedidos
                    )

                    listaPedidos.adapter = adaptador
                    adaptador.notifyDataSetChanged()
                }
            }
    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val spinnerRestaurant = findViewById<Spinner>(R.id.sp_elegir_restaurante)
        val spinnerFiltro = findViewById<Spinner>(R.id.sp_elegir_filtro)
        if(parent != null){
            if(parent.adapter.equals(spinnerRestaurant.adapter)){
                posR = position
            }
            else if (parent.adapter.equals(spinnerFiltro.adapter)){
                posF = position
                when(posF){
                    0 -> {
                        templateMenu = R.menu.menu_por_recibir
                    }
                    1 -> {
                        templateMenu = R.menu.menu_preparando
                    }
                    else ->{
                        templateMenu = R.menu.menu_vacio
                    }
                }
            }
        }
    }

    override fun onNothingSelected(parent: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(templateMenu,menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posicionOrden = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val listaPedidos = findViewById<ListView>(R.id.lv_pedidos_realizados)
        val adaptador = listaPedidos.adapter as ArrayAdapter<FirestorePedido>
        var pedido = FirestorePedido.listaPedidos.get(posicionOrden)
        val db = Firebase.firestore
        val referencia = db.collection("pedidos")
        return when(item.itemId){
            R.id.mi_r_preparando ->{
                referencia.document(pedido.uid!!)
                    .update("estado","Preparando")
                    .addOnSuccessListener {
                        adaptador.remove(pedido)
                        adaptador.notifyDataSetChanged()
                    }
                return true
            }
            R.id.mi_r_cancelado -> {
                referencia.document(pedido.uid!!)
                    .update("estado","Cancelado")
                    .addOnSuccessListener {
                        adaptador.remove(pedido)
                        adaptador.notifyDataSetChanged()
                    }
                return true
            }
            R.id.mi_p_cancelado -> {
                referencia.document(pedido.uid!!)
                    .update("estado","Cancelado")
                    .addOnSuccessListener {
                        adaptador.remove(pedido)
                        adaptador.notifyDataSetChanged()
                    }
                return true
            }
            R.id.mi_p_enviado -> {
                referencia.document(pedido.uid!!)
                    .update("estado","Enviado")
                    .addOnSuccessListener {
                        adaptador.remove(pedido)
                        adaptador.notifyDataSetChanged()
                    }
                return true
            }
            else -> return super.onContextItemSelected(item)
        }
    }
}