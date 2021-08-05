fun main(){
    println("Hola mundo");
    //en java Int edad = 12;
    //kotlin es interoperable con java
    var edadProfesor = 31;
    //duck typing: a pesar de que no defini el tipo, kotlin ya sabe
    //si quiero definir el tipo podria hacer edadProfesor:Int
    var edadEstudiante:Int = 22;

    //los tipos de variables que tenemos
    //Mutables e inmutables (mutar implica reasignar, inmutable no se puede re asignar)
    //variables mutables
    var edadCachorro: Int = 0
    edadCachorro = 1
    edadCachorro =2 //estamos reasignando los valores
    var sueldo =45
    //inmutables con VAL
    val numeroCedula = 1818181818
    //numeroCedula= 44
    //hay que preferir las INMUTABLES porque podriamos llegar a tener problemas
    //con eso el codigo se hace mucho menos peligroso

    //Tipos de variables (los mismos en java) -> primitivos
    //Int Double Float Boolean Char
    //y tambien tenemos acceso a las clases de java

    //condicionales
    if (true){

    } else {

    }
    val estadoCivil = 'c'
    //forma de escribir un switch
    when(estadoCivil){
        ('c') ->{
            println("huir")
        }
        'S'-> {
            println("conversar")
        }
        else ->{
            println("no tiene estado civil")
        }
    }
    //if en una sola linea
    val sueldoMayorAlEstablecido = if(sueldo>12.2) 500 else 0


    imprimirNombre("Mikkel")
    calcularSueldo(100.0)
    calcularSueldo(100.0,14.0)
    calcularSueldo(100.0,14.0,25.0)

    //parametros nombrados
    calcularSueldo(bono = 3.0, sueldo = 1000.0)
    //como usamos parametros nombrados podemos modificar el orden

    //arreglos estaticos
    var arregloEstatico: Array<Int> = arrayOf(1,2,3)
    //arrayEstadico.add(12) no tenemos aqui, no se puede modificar los elementos del arreglo

    //arreglo dinamico
    val arregloDinamico: ArrayList<Int> = arrayListOf(1,2,3,4)
    println(arregloDinamico)
    arregloDinamico.add(5)
    arregloDinamico.add(9)
    println(arregloDinamico)

    //OPERADORES -> sirven para arreglos estaticos y dinamicos
    //como vamos a usar variables inmutables, no vamos a usar for y while
    //las operaciones funcionan para cualquier tipo de iterable
    //todos los operadores devuelven algo

    //FOR EACH -> unit
    //para iterar un arreglo
    //al for each le pasamos una funcion con llaves {}
    val respuestaForEach:Unit = arregloDinamico
        .forEach{ valorActual:Int -> //parametros que vamos a recibir con una arrow function
            println("valor acual: ${valorActual}")
        }
    //cuando yo solo necesito un parametro, puedo hacer lo siguiente
    arregloDinamico.forEach { //el valor por defecto es it
        println("Valor actual ${it}")
    }
    //como imprimo los indices?
    //cualquier ooerador tiene su mismo operador pero indexado
    arregloDinamico.forEachIndexed { indice:Int, valorActual:Int -> println("Valor actual ${valorActual}, indice actual ${indice}")  }

    //OPERADOR MAP
    //MAP -> muta el arreglo (cambia el arreglo)
    //devuelve una lista List<...>
    //1. enviaemos el nuevo valor de la iteracion
    //2. nos devuelve un nuevo arreglo con los valores modificados

    val respuestaMap:List<Double> = arregloDinamico.map {
        valorActual:Int ->
        return@map valorActual.toDouble()
    }
    println(respuestaMap)

    val respuestaMap2:List<Double> = arregloDinamico.map {
            valorActual:Int ->
        return@map valorActual.toDouble() +100.0
    }
    println(respuestaMap2)

    //OPERADOR FILTER -> filtrar el arreglo
    //1. Devolver una expresion (true o false)
    //2. Nuevo arreglo filtrado
    val respuestaFitler: List<Int> = arregloDinamico.filter {   valorActual:Int ->
        val mayoresACinco:Boolean = valorActual>5 //expresion condicional
        return@filter mayoresACinco //boolean: si la expresion es verdadera, se añade al nuevo arreglo
    }
    println(respuestaFitler)

    // OR AND
    val respuestaAny: Boolean = arregloDinamico
        .any{ valorActual:Int ->
            return@any (valorActual>5)
        }
    println(respuestaAny)
    val respuestaAll = arregloDinamico
        .all{ it ->
            return@all (it>5)
        }
    println(respuestaAll) //false

    //Operador que nos va a ayudar a hacer operaciones sumando elementos del arreglo
    //ej queremos sacar el total de una factura o asi
    //OPERADOR REDUCE -> Valor acumulado
    //el valor acumulado para el lenguaje Kotlin es 0 y siempre 0
    //ej [1 2 3 4 5] y quiero sumar todos los valores del arreglo
    //valorIteracion1 = valorEmpieza + 1 (el valor con el que empieza o valor acumulado es 0)
    //valorIteracion 2 = valorIteracion1 + siguienteValorActualQueAhoritaEs2
    //valorIteracion3 = valorIteracion2 + siguienteValorActualQueAhoritaEs3
    val respuestaReduce:Int = arregloDinamico
        .reduce { acumulado:Int, valorActual:Int -> // el valor acumulado siempre empieza en 0
            return@reduce (acumulado+valorActual) //aqui es la operacion de logica del negocio
        }
    println(respuestaReduce)

    //ahora si quisieramos reducir ? y si quiero poner un valor inicial? ej de 100 de vida voy restando
    // OPERADOR FOLD
    val arregloDaño = arrayListOf<Int>(12,15,8,10)
    val respuestaReduceFold =  arregloDaño
        .fold(
            100, //este es mi acumulador inicial
            {acumulado, valorActualIteracion ->
                return@fold acumulado-valorActualIteracion
            }
        )
    println(respuestaReduceFold)

    //concatenando operadores
    val vidaActual:Double = arregloDinamico
        .map{it * 2.3}
        .filter{it >20}
        .fold(100.0, {acc, i -> acc-i})
        .also { println(it) }
    println("Valor vida actual ${vidaActual}")



    //ejemplos de las clases
    val ejemploUno = Suma (1, 5)
    val ejemploDos = Suma(null,2)
    val ejemploTres = Suma(2,null)
    val ejemploCuatro = Suma(null,null)
    println(ejemploCuatro.sumar())
    println(ejemploTres.sumar())
    println(ejemploDos.sumar())
    println(ejemploUno.sumar())

} //fin del MAIN

//funciones
//en jaca seria void imprimirNombre(String n){}

fun imprimirNombre(nombre:String): Unit{
    println("Nombre ${nombre}") //template strings, no existe void sino unit
}

//parametros requeridos y opcionales

fun calcularSueldo(
    sueldo:Double, //parametro requerido
    tasa: Double = 12.00, //Es un parametro por defecto, lo que implica que es opcional
    bono:Double? = null //variables que pueden ser nulas
): Double{
    //si algo puede ser nulo se pone asi
    //String -> String?
    //Int -> Int?
    //Date -> Date?
    //nos ahorramos errores del tipo nullPointerException
    if (bono!= null){
        return sueldo*(100/tasa) +bono
    } else{
        return sueldo*(100/tasa)
    }
}

//CLASES
abstract class NumerosJava{
    protected val numeroUno:Int //propiedad de la clase
    private val numeroDos:Int //propiedad de la clase
    constructor(
        uno:Int, //parametros requeridos
        dos:Int
    ){
        //dentro de kotlin se puede usar la misma sintaxis del this
        //sin embargo, podemos referirnos para una propiedad o metodo sin la palabra this
        this.numeroUno = uno
        numeroDos = dos
        println("Inicializar")
    }
}
//si quisieramos crear la instancia tendriamos dos numeros como propiedad
//ahora en kotlin podemos escribir lo mismo de arriba pero mas kotlinistico:
abstract class Numeros( //podemos tener directamente nuestro constructor primario
    protected var numeroUno:Int, //si no pusieramos los modificadores de acceso, estos serian parametros
    protected var numeroDos:Int
 ){
    //en kotlin los getters y setters ya estan directos
    init {
        //este es el bloque inicio del constructor primario
        println("Inicializar")
    }
}

//creemos una clase suma
class Suma( //constructor primario
//si no quiero que se creen propiedades directamente aqui puedo
// no poner los modificadores de acceso
    uno:Int,
    dos:Int
//queremos heredar? pues como en C++
): Numeros(//constructor papa ergo super
    uno,
    dos
 ){
    init {
        this.numeroDos //es una propiedad
        //recordemos que en este momento, hacer this.uno no hay porque esos solo son parametros
    }

    //segundo constructor
    constructor(
        uno: Int?, //parametros
        dos: Int
    ): this( //llamada a constructor primario
        if (uno ==null) 0 else uno,
        dos
    )
    //tercer constructor
    constructor(
        uno: Int, //parametros
        dos: Int?
    ): this( //llamada a constructor primario o al superior
        uno,
        if (dos ==null) 0 else dos
    )
    //cuarto constructor
    constructor(
        uno: Int?, //parametros
        dos: Int?
    ): this( //llamada a constructor superior
        if (uno ==null) 0 else uno,
        if (dos ==null) 0 else dos
    )

    //funciones y metodos en clases
    public fun sumar(): Int {
        //en kotlin no es necesario escribir el modificador de acceso publico por que es el por defecto
        val total = numeroDos + numeroUno
        return total
    }
    //metodos y propiedades estaticas en kotlin no existe la misma forma de trabajar como en java
//la forma es usando un patron singleton que ya esta predefinido en kotlin
    companion object {
        val historialSumas = arrayListOf<Int>()
        fun agregarHistorial(valorNuevaSuma:Int){
            historialSumas.add(valorNuevaSuma)
            println(historialSumas)
        }
    }
}

