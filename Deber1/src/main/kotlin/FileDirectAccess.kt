import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import java.io.*
import java.lang.Exception
import java.lang.reflect.Type
import java.nio.file.Files
import java.nio.file.Paths
import java.util.*
import kotlin.collections.ArrayList

class FileDirectAccess<T:Identificable<Id>,Id>(
    val nombreArchivo:String,
    val clase:Class<T>,
    truncate:Boolean = false
):IDirectAccess<T,Id> {
     var out:Formatter? = null
     var input:BufferedReader? = null
     val parseador:Gson
     init {
         parseador = GsonBuilder().setPrettyPrinting().create()
         if (truncate){
             openOut()
             closeOut()
         }
     }
    override fun crear(entidad: T) {
        //empezamos obteniendo la lista, de haberla
        val resultado = leer(entidad.getId())
        if (resultado == null) {
            var listaObtenida = listar()
            if (listaObtenida != null) {
                listaObtenida.add(entidad)
            } else {
                listaObtenida = arrayListOf<T>()
                listaObtenida.add(entidad)
            }
            val aEscribir = parseador.toJson(listaObtenida)
            openOut()
            try {
                out?.format(aEscribir)
            } catch (exception: Exception) {
                println("Error al crear")
            }
            closeOut()
        }
    }

    override fun leer(identificador: Id): T? {
        val listaObtenida = listar()
        val respuestaFiltrada = listaObtenida?.filter { entidad:T ->
            val comparacion = entidad.getId()!!.equals(identificador)
            return@filter comparacion
        }
        if (respuestaFiltrada != null){
            return if(respuestaFiltrada.isEmpty()) null else respuestaFiltrada.get(0)
        }
        else{
            return null
        }
    }

    override fun actualizar(entidad: T) {
         //busca si se encuentra la entidad en la persistencia
        val resultadoBusqueda = leer(entidad.getId())
        if (resultadoBusqueda != null){
            var nuevaLista = listar()?.map {
                var retorno = it
                if (it.getId()!!.equals(entidad.getId())){
                    retorno = entidad
                }
                return@map retorno
            }
            if (nuevaLista != null){
                val aEscribir = parseador.toJson(nuevaLista)
                openOut()
                try {
                    out?.format(aEscribir)
                } catch (exception: Exception) {
                    println("Error al crear")
                }
                closeOut()
            }
        }
    }

    override fun eliminar(entidad: T) {
        //busca si se encuentra la entidad en la persistencia
        val resultadoBusqueda = leer(entidad.getId())
        if (resultadoBusqueda != null){
            var nuevaLista = listar()?.filter {
                return@filter !(it.getId()!!.equals(entidad.getId()))
            }
            if (nuevaLista != null){
                val aEscribir = parseador.toJson(nuevaLista)
                openOut()
                try {
                    out?.format(aEscribir)
                } catch (exception: Exception) {
                    println("Error al crear")
                }
                closeOut()
            }
        }
    }

    override fun listar(): ArrayList<T>? {
        openIn()
        var elements:ArrayList<T>? = null
        var arreglo:Array<T>
        try {
            if (input != null){
                arreglo =
                    parseador.fromJson(input, java.lang.reflect.Array.newInstance(this.clase, 0).javaClass) as Array<T>
                elements = arreglo.toCollection(ArrayList())
            }
        } catch (exception:Exception) {
            closeIn()
        }
        closeIn()
        return elements
    }
    private fun openOut(){
        try {
            out = Formatter(nombreArchivo)
        } catch (excepton:Exception){
            println("Error al abrir el archivo")
        }
    }
    private fun closeOut(){
        try {
            out?.close()
        } catch(exception:Exception) {
            println("Error al cerrar el archivo")
        }
    }
    private fun openIn(){
        try {
            input = BufferedReader(Files.newBufferedReader(Paths.get(
                nombreArchivo
            )))
        } catch (exception:Exception){
            println("Error al abrir el archivo")
        }
    }
    private fun closeIn(){
        try {
            input?.close()
        }catch (excepton:Exception){
            println("Error al cerrar el archivo")
        }
    }
}