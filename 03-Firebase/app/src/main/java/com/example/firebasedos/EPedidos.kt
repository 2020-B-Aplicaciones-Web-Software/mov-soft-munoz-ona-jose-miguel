package com.example.firebasedos

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.*
import com.example.firebasedos.dto.FirestoreProductoDto
import com.example.firebasedos.dto.FirestoreRestaurant
import com.example.firebasedos.dto.ItemPedido
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.ArrayList

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
        val etCantidad = findViewById<EditText>(R.id.et_cantidad_producto)
        botonAdd.setOnClickListener {
            val restaurante = if (FirestoreRestaurant.listaRestaurantes.isNotEmpty()) FirestoreRestaurant.listaRestaurantes.get(posR) else null
            val producto = if (FirestoreProductoDto.listaProductos.isNotEmpty()) FirestoreProductoDto.listaProductos.get(posPr) else null
            val cantidad = if (!etCantidad.text.toString().equals("")) etCantidad.text.toString().toInt() else -1
            if(cantidad>0 &&(producto != null) && (restaurante != null)){
            listaItems.add(ItemPedido(producto,cantidad))
            adaptadorLv.notifyDataSetChanged()}
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
                    return@map it.toObject(FirestoreRestaurant::class.java)
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
                    return@map it.toObject(FirestoreProductoDto::class.java)
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
                    posR += position
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
                listaItems.removeAt(posIPedido)
                lista?.notifyDataSetChanged()
                return true
            }
            else ->return super.onContextItemSelected(item)
        }
    }
}