import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

fun main(args:Array<String>){
    val manejadorArchivo = FileDirectAccess<Concesionario,Int>("concesionario.txt",Concesionario::class.java)
    val stringInicial = "Elija una opción(numero)\n" +
            "1. Crear concesionario\n" +
            "2. Buscar concesionario por id\n" +
            "3. Actualizar concesionario\n" +
            "4. Borrar concesionario\n" +
            "5. Listar concesionarios\n" +
            "6. Salir"
    val entrada = Scanner(System.`in`)
    var bandera = true
    while (bandera){
        println(stringInicial)
        when (entrada.nextInt()){
            1 -> {
                println("Ingrese el ID del concesionario")
                var id = entrada.nextInt()
                var resultado = manejadorArchivo.leer(id)
                if (resultado!=null) {
                    println("El concesionario con identificador ${id} ya en encuentra registrado")
                    continue
                }
                println("Ingrese el nombre del concesionario")
                var nombre = readLine()
                if (nombre == null){
                    nombre = "sin nombre"
                }
                println("Ingrese la superficie del concesionario (con dos cifras decimales)")
                var area = entrada.nextDouble()
                println("Ingrese true si el concesionario está abierto, caso contrario, false")
                var abierto = readLine().toBoolean()
                println("Ingrese la fecha de inaguracion del concesionario (dd/mm/yyyy)")
                var fecha = SimpleDateFormat("dd/mm/yyyy").parse(readLine())
                var concesionario = Concesionario(nombre,abierto,area,id,fecha)
                println("¿Desea añadir autos?[s/n]")
                var adicion = readLine()
                if (adicion.equals("s")){
                    println("Cuantos autos desea ingresar?")
                    val aIngresar = entrada.nextInt()
                    for (i in 1..aIngresar){
                        println("Ingrese la marca del auto $i")
                        var marca = readLine()!!
                        println("Ingrese la fecha de fabricación del auto $i")
                        var fabricacion = SimpleDateFormat("dd/mm/yyyy").parse(readLine())
                        println("Ingrese true si el auto $i es usado, sino false")
                        var condicion = readLine().toBoolean()
                        println("Ingrese el precio del auto $i")
                        var precio = entrada.nextDouble()
                        println("Ingrese el numero de puertas del auto $i")
                        var puertas = entrada.nextInt()
                        println("Ingrese el numero del chasis del auto $i")
                        var chasis = readLine()!!
                        println("Ingrese el modelo del auto $i")
                        var modelo = readLine()!!
                        concesionario.addAutos(Auto(marca,fabricacion,condicion,precio,puertas,chasis,modelo))
                    }
                }
                manejadorArchivo.crear(concesionario)
            }
            2 -> {
                println("Ingrese el ID del concesionario")
                var id = entrada.nextInt()
                var resultado = manejadorArchivo.leer(id)
                if(resultado!=null){
                    println(resultado)
                }else{
                    println("El concesionario con id ${id} no se encuentra registrado")
                    continue
                }
            }
            3 ->{
                println("Ingrese el ID del concesionario")
                var id = entrada.nextInt()
                var resultado = manejadorArchivo.leer(id)
                if (resultado!=null) {
                    println("Desea añadir autos(a), borrar autos(b), acualizar informacion de un auto (u) o la informacion del concesionario (c)?")
                    var respuesta = readLine()!!
                    when(respuesta){
                        "a" -> {
                            println("Cuantos autos desea ingresar?")
                            val aIngresar = entrada.nextInt()
                            for (i in 1..aIngresar) {
                                println("Ingrese la marca del auto $i")
                                var marca = readLine()!!
                                println("Ingrese la fecha de fabricación del auto $i")
                                var fabricacion = SimpleDateFormat("dd/mm/yyyy").parse(readLine())
                                println("Ingrese true si el auto $i es usado, sino false")
                                var condicion = readLine().toBoolean()
                                println("Ingrese el precio del auto $i")
                                var precio = entrada.nextDouble()
                                println("Ingrese el numero de puertas del auto $i")
                                var puertas = entrada.nextInt()
                                println("Ingrese el numero del chasis del auto $i")
                                var chasis = readLine()!!
                                println("Ingrese el modelo del auto $i")
                                var modelo = readLine()!!
                                resultado.addAutos(Auto(marca, fabricacion, condicion, precio, puertas, chasis, modelo))
                                manejadorArchivo.actualizar(resultado)
                            }
                        }
                        "b" ->{
                            if(resultado.autos.size != 0) {
                                println("Ingrese el numero de chasis del auto")
                                var chasis = readLine()!!
                                var nuevalista = resultado.autos.filter {
                                    return@filter it.getId() != chasis
                                }
                                resultado.autos = nuevalista.toCollection(ArrayList<Auto>())
                                manejadorArchivo.actualizar(resultado)
                            }
                            else{
                                println("Lista de autos vacía")
                            }
                        }
                        "u" ->{
                            if(resultado.autos.size != 0) {
                                println("Ingrese el numero de chasis del auto")
                                var chasis = readLine()!!
                                var nuevalista = resultado.autos.map {
                                    var carro = it
                                    if (it.numChasis.equals(chasis)) {
                                        println("Ingrese la marca del auto")
                                        var marca = readLine()!!
                                        println("Ingrese la fecha de fabricación del auto")
                                        var fabricacion = SimpleDateFormat("dd/mm/yyyy").parse(readLine())
                                        println("Ingrese true si el auto es usado, sino false")
                                        var condicion = readLine().toBoolean()
                                        println("Ingrese el precio del auto")
                                        var precio = entrada.nextDouble()
                                        println("Ingrese el numero de puertas del auto")
                                        var puertas = entrada.nextInt()
                                        println("Ingrese el modelo del auto ")
                                        var modelo = readLine()!!
                                        carro =
                                            Auto(marca, fabricacion, condicion, precio, puertas, it.numChasis, modelo)
                                    }
                                    return@map carro
                                }
                                resultado.autos = nuevalista.toCollection(ArrayList<Auto>())
                                manejadorArchivo.actualizar(resultado)
                            }
                            else{
                                println("Lista de autos vacía")
                            }
                        }
                        "c"->{
                            println("Ingrese el nombre del concesionario")
                            var nombre = readLine()!!
                            println("Ingrese la superficie del concesionario (con dos cifras decimales)")
                            var area = entrada.nextDouble()
                            println("Ingrese true si el concesionario está abierto, caso contrario, false")
                            var abierto = readLine().toBoolean()
                            println("Ingrese la fecha de inaguracion del concesionario (dd/mm/yyyy)")
                            var fecha = SimpleDateFormat("dd/mm/yyyy").parse(readLine())
                            var concesionario = Concesionario(nombre,abierto,area,id,fecha)
                            manejadorArchivo.actualizar(concesionario)
                        }
                    }
                }else{
                    println("Error: No se ha encontrado el concesionario")
                }
            }
            4 ->{
                println("Ingrese el ID del concesionario")
                var id = entrada.nextInt()
                var resultado = manejadorArchivo.leer(id)
                if (resultado!=null) {
                    manejadorArchivo.eliminar(resultado)
                    println("Operacion realizada con exito")
                }else{
                    println("El concesionario con id ${id} no se encuentra registrado")
                }
            }
            5 -> {
                var lista= manejadorArchivo.listar()
                if (lista != null){
                    lista.forEach {
                        println(it)
                    }
                }else{
                    println("Lista vacía")
                }
            }
            6 ->{
                bandera = false
            }
        }
    }

}