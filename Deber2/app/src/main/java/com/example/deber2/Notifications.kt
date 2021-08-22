package com.example.deber2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class Notifications : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val listaTweets = arrayListOf<Tweet>()
        listaTweets.add(Tweet("J. Walsh","@jwalsh_photos",1,"@simonstalenhag Fantastic wildlife and nature shots. easy follow"))
        listaTweets.add(Tweet("Zachary Edgerton","@DrBioBrain",23,"@simonstalenhag Am I crazy or does this Simon Stålenhag photo look a hell of a lot like a Simon Stålenhag painting"))
        listaTweets.add(Tweet("BreaK","@TSM_Break",19,"@simonstalenhag Genuinely don't know if you've painted this or if it's an actual photo."))
        val raiz = inflater.inflate(R.layout.fragment_notifications,container,false)
        val reciclador = raiz.findViewById<RecyclerView>(R.id.rv_notificaciones)
        iniciarRecyclerView(this,listaTweets,reciclador)
        return raiz
    }

    fun iniciarRecyclerView(
        actividad:Notifications,
        lista:List<Tweet>,
        recyclerView: RecyclerView
    ){
        val adaptador = TweetRecyclerViewAdapter(
            actividad::class.java,
            lista,
            recyclerView
        )
        recyclerView.adapter = adaptador
        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
        recyclerView.layoutManager = androidx.recyclerview.widget.LinearLayoutManager(actividad.context)
        adaptador.notifyDataSetChanged()
    }
}