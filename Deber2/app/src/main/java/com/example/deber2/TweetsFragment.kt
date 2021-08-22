package com.example.deber2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView

class TweetsFragment:Fragment(R.layout.tweets_fragment) {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val listaTweets = arrayListOf<Tweet>()
        listaTweets
            .add(
                Tweet("Nathan A. Bauman","@nathanabauman",2,"Y’all, we sold out in 35 minutes on the dot. \n" +
                        "\n" +
                        "I don’t know how I can ever thank everyone.\n" +
                        "\n" +
                        "I love all of y’all."))
        listaTweets
            .add(
                Tweet("phill","@philngyn",1,"Your work doesn’t define you. You define your work. Remember this when you feel like you’ve been boxed in.")
            )
        listaTweets
            .add(
                Tweet("B R A N D O N L U","@brandontonlu",2,"i wont let it happen again chief")
            )
        listaTweets
            .add(
                Tweet("Forbes","@Forbes",5,"Climbing makes a historic Olympic debut in a controversial new format")
            )
        listaTweets
            .add(
                Tweet("BRENDAN NØRTH","@ImBrendanNorth",6,"A note on minting photo collections: take care to differentiate between your collections and 1/1 work. If it looks like you just minted your entire portfolio, it will be difficult to justify the difference in value of your 1/1 pieces. Have a well defined theme, style, story")
            )
        listaTweets
            .add(
                Tweet("Quinn XCII","@QuinnXCII",22,"Making this album was one of the most fulfilling experiences of my life and getting to do it with a group of my closest friends made it that much better. Change of Scenery II is out everywhere now, I hope you enjoy it as much as we do. I love you guys")
            )
        val raiz = inflater.inflate(R.layout.tweets_fragment,container,false)
        val reciclador = raiz.findViewById<RecyclerView>(R.id.rv_lista_tweets)
        iniciarRecyclerView(this,listaTweets,reciclador)
        return raiz
    }

    fun iniciarRecyclerView(
        actividad:TweetsFragment,
        lista:List<Tweet>,
        recyclerView:RecyclerView
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