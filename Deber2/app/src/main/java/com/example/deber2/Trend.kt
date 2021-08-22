package com.example.deber2

class Trend(
    val theme:String?,
    val location:String?,
    val name:String,
    val number:String
) {
    override fun toString(): String {
        if (location != null){
            return "Trending in ${location} \n ${name} \n ${number} Tweets"
        }
        if(theme != null){
            return "${theme} Trending \n" +
                    " ${name} \n" +
                    " ${number} Tweets "
        }
        return "Trending \n ${name} \n" +
                " ${number} Tweets"
    }
}