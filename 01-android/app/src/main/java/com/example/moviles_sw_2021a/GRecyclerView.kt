package com.example.moviles_sw_2021a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.TextureView
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class GRecyclerView : AppCompatActivity() {
    var totalLikes = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grecycler_view)
        val listaEntrenador = arrayListOf<BEntrenador>()
        val ligaPokemon = DLiga("Kanto","Liga Kanto")
        listaEntrenador
            .add(
                BEntrenador(
                    "Jose",
                    "1723171714",
                    ligaPokemon
                )
            )
        listaEntrenador
            .add(
                BEntrenador(
                    "Miguel",
                    "1707674857",
                    ligaPokemon
                )
            )

        val recyclerViewEntrenador = findViewById<RecyclerView>(R.id.rv_entrenadores)
       iniciarRecyclerView(listaEntrenador,this,recyclerViewEntrenador)
    }

    fun iniciarRecyclerView(
        lista: List<BEntrenador>,
        actividad:GRecyclerView,
        recyclerView: RecyclerView
    ){
        val adaptador = FRecyclerViewAdaptadorNombreCedula(
            actividad,
            lista,
            recyclerView
        )
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(actividad)
        adaptador.notifyDataSetChanged()

    }
    fun aumentarTotalLikes(){
        totalLikes++
        val textView = findViewById<TextView>(R.id.tv_total_likes)
        textView.text = totalLikes.toString()
    }
}