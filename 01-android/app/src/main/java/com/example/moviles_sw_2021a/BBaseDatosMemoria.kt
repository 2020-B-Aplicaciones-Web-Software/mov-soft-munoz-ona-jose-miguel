package com.example.moviles_sw_2021a

class BBaseDatosMemoria {
    companion object{
        val arregloBEntrenador = arrayListOf<BEntrenador>()
        init {
            arregloBEntrenador
                .add(
                    BEntrenador("Miguel","m@epn.edu")
                )
            arregloBEntrenador
                .add(
                    BEntrenador("Jose","j@epn.edu")
                )
            arregloBEntrenador
                .add(
                    BEntrenador("Renato","r@epn.edu")
                )
        }
    }
}