package com.example.deber2

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

class SearchFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val viewRoot = inflater.inflate(R.layout.fragment_search,container,false)
        val listView = viewRoot.findViewById<ListView>(R.id.lv_trends)
        val trends = arrayListOf<Trend>()
        trends.add(Trend(null,"Ecuador","Cristiano","35.2K"))
        trends.add(Trend("Movies",null,"Harry Potter","25.8K"))
        val adaptador = ArrayAdapter<Trend>(
            this.requireContext(),
            android.R.layout.simple_expandable_list_item_1,
            trends
        )
        listView.adapter = adaptador
        return viewRoot
    }
}