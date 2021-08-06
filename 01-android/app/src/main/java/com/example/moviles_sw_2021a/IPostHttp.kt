package com.example.moviles_sw_2021a

class IPostHttp(
    val id:Int,
    var userId:Any,
    var title:String,
    var body:String
) {
    init {
        if (userId is String){
            userId = (userId as String).toInt()
        }
        if (userId is Int){
            userId = userId
        }
    }
}