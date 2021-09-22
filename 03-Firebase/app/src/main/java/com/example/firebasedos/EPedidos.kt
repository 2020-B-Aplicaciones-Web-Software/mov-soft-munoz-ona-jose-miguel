package com.example.firebasedos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.firebasedos.dto.FirestorePedido
import com.example.firebasedos.dto.FirestoreProductoDto
import com.example.firebasedos.dto.FirestoreRestaurant
import com.example.firebasedos.dto.ItemPedido
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*

class EPedidos : AppCompatActivity(), AdapterView.OnItemSelectedListener {
    var posR = 0
    var posPr = 0
    var posIPedido = 0
    var listaItems = arrayListOf<ItemPedido>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_epedidos)
        val adaptadorLv = ArrayAdapter(
            this,
            android.R.layout.simple_expandable_list_item_1,
            listaItems
        )
        val lvPedido = findViewById<ListView>(R.id.lv_lista_productos)
        lvPedido.adapter = adaptadorLv
        registerForContextMenu(lvPedido)
        llenarSpinners()
        val botonAdd = findViewById<Button>(R.id.btn_anadir_lista_producto)
        val spinnerRestaurant = findViewById<Spinner>(R.id.sp_restaurantes)
        val spinnerProductos = findViewById<Spinner>(R.id.sp_producto)
        val tvTotal = findViewById<TextView>(R.id.tv_total)
        tvTotal.setText("0.0")
        val etCantidad = findViewById<EditText>(R.id.et_cantidad_producto)
        botonAdd.setOnClickListener {
            val restaurante = if (FirestoreRestaurant.listaRestaurantes.isNotEmpty()) FirestoreRestaurant.listaRestaurantes.get(posR) else null
            val producto = if (FirestoreProductoDto.listaProductos.isNotEmpty()) FirestoreProductoDto.listaProductos.get(posPr) else null
            val cantidad = if (!etCantidad.text.toString().equals("")) etCantidad.text.toString().toInt() else -1
            if(cantidad>0 &&(producto != null) && (restaurante != null)){
                listaItems.add(ItemPedido(producto.nombre,producto.precio,cantidad,producto.uid))
                adaptadorLv.notifyDataSetChanged()
                var precios = listaItems.map {
                    return@map it.cantidad?.times(it.precio)
                }
                var total = precios.reduce { acc, itemPedido ->
                    return@reduce (acc!! + itemPedido!!)
                }
                tvTotal.text = total.toString()
                etCantidad.text.clear()
        }}
        val botonCompletar = findViewById<Button>(R.id.btn_hacer_pedido)
        botonCompletar.setOnClickListener {
            val fechaActual = Date(System.currentTimeMillis())
            if (listaItems.isNotEmpty()){
                val db = Firebase.firestore
                val referencia = db.collection("pedidos")
                referencia.add(
                    FirestorePedido(
                        fechaActual,
                        tvTotal.text.toString().toDouble(),
                        usuario = BAuthUsuario.usuario?.email,
                        restaurante = FirestoreRestaurant.listaRestaurantes.get(posR),
                        productos = listaItems
                        )
                )
                    .addOnSuccessListener {
                        listaItems.clear()
                        adaptadorLv.notifyDataSetChanged()
                        tvTotal.setText("0.0")
                    }
            }
        }
    }

    fun llenarSpinners(){
        val spinnerRestaurant = findViewById<Spinner>(R.id.sp_restaurantes)
        val spinnerProductos = findViewById<Spinner>(R.id.sp_producto)
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
        db.collection("producto")
            .get()
            .addOnSuccessListener {
                val listaProductos = it.map {
                    val producto = it.toObject(FirestoreProductoDto::class.java)
                    producto.uid = it.id
                    return@map producto
                }
                if(listaProductos != null){
                    val adaptador = ArrayAdapter(
                        this,
                        android.R.layout.simple_spinner_dropdown_item,
                        listaProductos
                    )
                    FirestoreProductoDto.listaProductos = listaProductos
                    spinnerProductos.adapter = adaptador
                    spinnerProductos.onItemSelectedListener = this
                }
            }

    }

    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
        val spinnerRestaurant = findViewById<Spinner>(R.id.sp_restaurantes)
        val spinnerProductos = findViewById<Spinner>(R.id.sp_producto)
        if(parent != null){
            if(parent.adapter != null){
                if(parent.adapter.equals(spinnerRestaurant.adapter)){
                    posR = position
                }else if (parent.adapter.equals(spinnerProductos.adapter)){
                    posPr = position
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
        menuInflater.inflate(R.menu.menu_pedido,menu)
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val id = info.position
        posIPedido = id
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.mi_eliminar -> {
                val lista = findViewById<ListView>(R.id.lv_lista_productos).adapter as ArrayAdapter<ItemPedido>
                val tvTotal = findViewById<TextView>(R.id.tv_total)
                listaItems.removeAt(posIPedido)
                lista?.notifyDataSetChanged()
                if (listaItems.isNotEmpty()){
                    var precios = listaItems.map {
                        return@map it.cantidad!! * it.precio
                    }
                    var total = precios.reduce { acc, itemPedido ->
                        return@reduce (acc + itemPedido)
                    }
                    tvTotal.text = total.toString()
                } else{
                    tvTotal.setText("0.0")
                }
                return true
            }
            else ->return super.onContextItemSelected(item)
        }
    }
}