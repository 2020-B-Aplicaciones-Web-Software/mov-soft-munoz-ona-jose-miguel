import java.io.Serializable
import java.util.*

class Auto(val marca:String,
           var año:Date,
           val esUsado:Boolean,
           val precio:Double,
           val numPuertas:Int,
           val numChasis:String,
           val modelo:String):Serializable,Identificable<String>
{
    override fun toString(): String {
        return "Marca: ${marca}, modelo: ${modelo}, con precio ${precio} y ${numPuertas} puertas. Del año ${año}"
    }
    override fun getId():String{
        return numChasis
    }
}