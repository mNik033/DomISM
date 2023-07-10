package com.ink.domism.models

data class User(
    val uid : String = "",
    val name : String = "",
    val email : String = "",
    val mobile : Long = 0,
    val admin : Boolean = false,
    val fcmToken : String = ""
)