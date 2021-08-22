package com.example.deber2

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            supportFragmentManager.commit {
                add(R.id.fl_contenedor,TweetsFragment())
            }
        }

        val navBar = findViewById<BottomNavigationView>(R.id.navbar)
        navBar.setOnNavigationItemSelectedListener { item->
            when(item.itemId){
                R.id.mi_home ->{
                    supportFragmentManager.commit {
                        replace(R.id.fl_contenedor,TweetsFragment())
                    }
                    true
                }
                R.id.mi_buscar ->{
                    supportFragmentManager.commit {
                        replace(R.id.fl_contenedor,SearchFragment())
                    }
                    true
                }
                R.id.mi_notificaciones ->{
                    supportFragmentManager.commit {
                        replace(R.id.fl_contenedor,Notifications())
                    }
                    true
                }
                R.id.mi_msj ->{
                    supportFragmentManager.commit {
                        replace(R.id.fl_contenedor,Messages())
                    }
                    true
                }
                else -> false
            }

        }
    }

}