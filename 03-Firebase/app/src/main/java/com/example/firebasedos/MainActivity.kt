package com.example.firebasedos

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.example.firebasedos.dto.FirestoreUsuarioDto
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val CODIGO_INICIO_SESION = 102
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val botonLogin = findViewById<Button>(R.id.btn_login)
        botonLogin.setOnClickListener{
            llamarLoginUsuario()
        }

        val botonSalir = findViewById<Button>(R.id.btn_salir)
        botonSalir.setOnClickListener {
            solicitarSalirDelAplicativo()
        }

        val botonProducto = findViewById<Button>(R.id.btn_producto)
        botonProducto.setOnClickListener {
            irProducto()
        }

        val botonRestaurante = findViewById<Button>(R.id.btn_ir_restaurante)
        botonRestaurante.setOnClickListener {
            val intent = Intent(
                this,
                DRestaurante::class.java
            )
            startActivity(intent)
        }

        val botonPedidos = findViewById<Button>(R.id.btn_ir_pedidos)
        botonPedidos.setOnClickListener {
            val intent = Intent(
                this,
                EPedidos::class.java
            )
            startActivity(intent)
        }
    }

    fun irProducto(){
        val intent = Intent(
            this,
            CProducto::class.java
        )
        startActivity(intent)
    }

    fun llamarLoginUsuario(){
        val providers = arrayListOf(
            //lista de proveedores
            AuthUI.IdpConfig.EmailBuilder().build()
        )
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setTosAndPrivacyPolicyUrls(
                    "https://example.com/terms.html",
                    "https://example.com/privacyPolicy.html"
                )
                .build(),
            CODIGO_INICIO_SESION
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            CODIGO_INICIO_SESION -> {
                if(resultCode == Activity.RESULT_OK){
                    val usuario:IdpResponse? = IdpResponse.fromResultIntent(data)
                    if(usuario != null){
                        if(usuario.isNewUser == true){
                            Log.i("firebase-login","Nuevo usuario")
                            registrarUsuarioPorPrimeraVez(usuario)
                        }else{
                            setearUsuarioFirebase()
                            Log.i("firebase-login","Usuario Antiguo")
                        }
                    }
                } else{
                    Log.i("firebase-login","El usuario cancelo")
                }
            }
        }
    }

    fun registrarUsuarioPorPrimeraVez(usuario:IdpResponse){
        val usuarioLogueado = FirebaseAuth
            .getInstance()
            .getCurrentUser()
        if(usuario.email != null && usuarioLogueado != null){
            //vamos a enviar una lista de roles y un UID

            //obtenemos la referencia a la base de datos de Firestore
            val db = Firebase.firestore
            val rolesUsuario = arrayListOf<String>("usuario")
            val nuevoUsuario = hashMapOf<String,Any>( //estructura clave valor
                "roles" to rolesUsuario,
                "uid" to usuarioLogueado.uid,
                "email" to usuario.email.toString()
            )
            val identificadorUsuario = usuario.email

            db.collection("usuario")
            //Forma a) Firestore crea el identificador
             //   .add(nuevoUsuario)
            //Forma b) yo seteo el identificador
                .document(identificadorUsuario.toString())
                .set(nuevoUsuario)
                .addOnSuccessListener {
                    Log.i("firebase-firestore","Se creo")
                    setearUsuarioFirebase()
                }
                .addOnFailureListener{
                    Log.i("firebase-firestore","Fallo")
                }
        }


    }

    fun setearBienvenida(){
        val botonLogin = findViewById<Button>(R.id.btn_login)
        val botonSalir = findViewById<Button>(R.id.btn_salir)
        val botonProducto = findViewById<Button>(R.id.btn_producto)
        val textViewBienvenida = findViewById<TextView>(R.id.tv_bienvenida)
        val botonRestaurante = findViewById<Button>(R.id.btn_ir_restaurante)
        val botonPedidos = findViewById<Button>(R.id.btn_ir_pedidos)
        if(BAuthUsuario.usuario != null){
            textViewBienvenida.text = "Bienvenido ${BAuthUsuario.usuario?.email}"
            botonLogin.visibility = View.INVISIBLE
            botonSalir.visibility = View.VISIBLE
            botonProducto.visibility = View.VISIBLE
            botonRestaurante.visibility = View.VISIBLE
            botonPedidos.visibility = View.VISIBLE
        } else{
            textViewBienvenida.text = "Ingresa al aplicativo"
            botonLogin.visibility = View.VISIBLE
            botonSalir.visibility = View.INVISIBLE
            botonProducto.visibility = View.INVISIBLE
            botonRestaurante.visibility = View.INVISIBLE
            botonPedidos.visibility = View.INVISIBLE
        }
    }

    fun setearUsuarioFirebase(){
        val instanciaAuth = FirebaseAuth.getInstance()
        val usuarioLocal = instanciaAuth.currentUser
        if(usuarioLocal != null){
            if(usuarioLocal.email != null){
                //buscar en el firestore el usuario y traerlo con toods los datos
                val db = Firebase.firestore
                val referencia = db
                    .collection("usuario")
                    .document(usuarioLocal.email.toString())

                referencia
                    .get()
                    .addOnFailureListener{
                        Log.i("firebase-firestore","Fallo al cargar usuario")
                    }
                    .addOnSuccessListener {
                        val usuarioCargado = it.toObject(FirestoreUsuarioDto::class.java)
                        if(usuarioCargado != null) {
                            BAuthUsuario.usuario = BUsuarioFirebase(
                                usuarioCargado.uid,
                                usuarioCargado.email,
                                usuarioCargado.roles
                            )
                            setearBienvenida()
                        }
                        Log.i("firebase-firestore","Usuario cargado")
                    }
            }
        }
    }
    fun solicitarSalirDelAplicativo(){
        AuthUI
            .getInstance()
            .signOut(this)
            .addOnCompleteListener {
                BAuthUsuario.usuario = null
                setearBienvenida()
            }
    }

}