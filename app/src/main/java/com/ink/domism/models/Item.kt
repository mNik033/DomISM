package com.ink.domism.models

data class Item (
    val id : String = "",
    val name : String = "",
    val image : String = "",
    val origPrice : Int = 0,
    val discPrice : Int = 0,
    val storePrice : Int = 0
)