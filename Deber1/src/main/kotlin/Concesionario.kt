import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class Concesionario(
    val nombreConcesionario:String,
    val estaAbierto:Boolean,
    val superficie:Double,
    val numConsesionario:Int,
    val fechaApertura:Date
):Serializable, Identificable<Int> {
    var autos:ArrayList<Auto>
    init {
        autos = arrayListOf<Auto>()
    }

    fun addAutos(auto: Auto){
        autos.add(auto)
    }
    override fun getId():Int{
        return numConsesionario
    }

    override fun toString(): String {
        return "${nombreConcesionario}, superficie de ${superficie}, se encuentra abierto: ${estaAbierto}\n" +
                "fecha de apertura: ${fechaApertura}, con los siguientes autos:\n" +
                "${autos}\n"
    }
}