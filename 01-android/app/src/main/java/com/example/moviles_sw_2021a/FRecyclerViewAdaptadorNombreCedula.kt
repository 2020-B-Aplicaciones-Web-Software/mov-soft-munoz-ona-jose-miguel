package com.example.moviles_sw_2021a

import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FRecyclerViewAdaptadorNombreCedula (
    private val contexto:GRecyclerView,
    private val listaEntrenador:List<BEntrenador>,
    private val recyclerView:RecyclerView
    ):RecyclerView.Adapter<FRecyclerViewAdaptadorNombreCedula.MyViewHolder>(){
        inner class MyViewHolder(view:View):RecyclerView.ViewHolder(view){
            val nombreTextView:TextView
            val cedulaTextView:TextView
            val likesTextView:TextView
            val accionButton:Button
            var numeroLikes = 0
            init {
                nombreTextView = view.findViewById(R.id.tv_nombre)
                cedulaTextView = view.findViewById(R.id.tv_Cedula)
                accionButton = view.findViewById(R.id.btn_dar_like)
                likesTextView = view.findViewById(R.id.tv_likes)
                accionButton.setOnClickListener {
                    this.anadirLike()
                }
            }
            fun anadirLike(){
                this.numeroLikes = this.numeroLikes +1
                likesTextView.text = this.numeroLikes.toString()
                contexto.aumentarTotalLikes()
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView  = LayoutInflater
            .from(parent.context)
            .inflate(
                R.layout.recycler_view_vista, //definimos la vista de nuestro recyclerView
                 parent,
                false
            )
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        //nos permite setear los datos de cada iteracion del arreglo
        val entrenador = listaEntrenador[position]
        holder.nombreTextView.text = entrenador.nombre
        holder.cedulaTextView.text = entrenador.descripcion
        holder.accionButton.text = "Like ${entrenador.nombre}"
        holder.likesTextView.text = "0"
    }

    override fun getItemCount(): Int {
        //retorna el tamano del arreglo
        return listaEntrenador.size
    }
}