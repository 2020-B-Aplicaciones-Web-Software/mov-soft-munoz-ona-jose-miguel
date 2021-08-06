package com.example.moviles_sw_2021a

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.beust.klaxon.Klaxon
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost
import com.github.kittinunf.result.Result

class HHttpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hhttp)

        //http -> Metodo http -> url -> parametros
        metodoGet()
        metodoPost()
    }

    fun metodoPost(){
        val parametros:List<Pair<String,*>> = listOf(
            "title" to "Titulo moviles",
            "body" to "descripcion moviles",
            "userId" to 1
        )

        val url = "https://jsonplaceholder.typicode.com/posts"
        url
            .httpPost(parametros)
            .responseString{ req, res,result ->
                when(result){
                    is Result.Failure ->{
                        val error = result.getException()
                        Log.i("http-klaxon","Error: ${error}")
                    }
                    is Result.Success ->{
                        val respuestaString = result.get()
                        Log.i("http-klaxon","${respuestaString}")
                        val post = Klaxon()
                            .parse<IPostHttp>(respuestaString)
                        //si quisieramos parsean un arreglo, hay que usar el metodo
                        // parseArray
                        Log.i("http-klaxon","${post?.body}")
                    }
                }
            }
    }

    fun metodoGet(){
        val identificadorPost = 1
        val url = "https://jsonplaceholder.typicode.com/posts/"+ identificadorPost.toString()
        url
            .httpGet()
            .responseString{ req,res,result ->
                when(result){
                    is Result.Failure ->{
                        val error = result.getException()
                        Log.i("http-klaxon","Error: ${error}")
                    }
                    is Result.Success ->{
                        val respuestaString = result.get()
                        Log.i("http-klaxon","${respuestaString}")
                        val post = Klaxon()
                            .parse<IPostHttp>(respuestaString)
                        //si quisieramos parsean un arreglo, hay que usar el metodo
                        // parseArray
                        Log.i("http-klaxon","${post?.body}")
                    }
                }
            }
    }
}