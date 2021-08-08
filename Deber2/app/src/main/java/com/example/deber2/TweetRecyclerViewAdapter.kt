package com.example.deber2

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TweetRecyclerViewAdapter(
    private val context:Class<*>,
    private val listaTweets:List<Tweet>,
    private val recyclerView:RecyclerView
):RecyclerView.Adapter<TweetRecyclerViewAdapter.TweetViewHolder>() {
    inner class TweetViewHolder(view:View):RecyclerView.ViewHolder(view){
        val txvNombreUsuario:TextView;
        val txvArroba:TextView;
        val txvHoraTweet:TextView;
        val txvContenido:TextView;
        val btnComentario:ImageButton;
        val txvNumeroComentarios:TextView;
        val btnRetweet:ImageButton;
        val txvNumeroRetweet:TextView;
        val btnLike:ImageButton;
        val txvNumeroLikes:TextView;
        var numComentarios = 0;
        var numRetweets = 0;
        var numLikes = 0;
        init {
            txvNombreUsuario = view.findViewById(R.id.txv_nombre_usuario);
            txvArroba = view.findViewById(R.id.txv_arroba);
            txvHoraTweet = view.findViewById(R.id.txv_hora_pub);
            txvContenido = view.findViewById(R.id.txv_contenido_tweet);
            btnComentario = view.findViewById(R.id.btn_reply);
            btnComentario.setOnClickListener{
                sumarComentario()
            }
            txvNumeroComentarios = view.findViewById(R.id.txv_numero_replies);
            btnRetweet = view.findViewById(R.id.btn_retweet);
            btnRetweet.setOnClickListener {
                sumarRT()
            }
            txvNumeroRetweet = view.findViewById(R.id.txv_numero_retweets);
            btnLike = view.findViewById(R.id.btn_like);
            btnLike.setOnClickListener {
                sumarLike()
            }
            txvNumeroLikes = view.findViewById(R.id.txv_numero_likes);
        }
        fun sumarLike(){
            numLikes += 1
            txvNumeroLikes.text = numLikes.toString()
        }
        fun sumarComentario(){
            numComentarios += 1
            txvNumeroComentarios.text = numComentarios.toString()
        }
        fun sumarRT(){
            numRetweets += 1
            txvNumeroRetweet.text = numRetweets.toString()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TweetViewHolder {
        val itemV = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_tweet,
            parent,
            false)
        return TweetViewHolder(itemV)
    }

    override fun onBindViewHolder(holder: TweetViewHolder, position: Int) {
        val item = listaTweets[position]
        holder.txvNombreUsuario.text = item.nombreUsuario
        holder.txvArroba.text = item.arroba
        holder.txvHoraTweet.text = item.numeroHoras.toString()+"h"
        holder.txvContenido.text = item.contenido
    }

    override fun getItemCount(): Int {
        return listaTweets.size
    }
}