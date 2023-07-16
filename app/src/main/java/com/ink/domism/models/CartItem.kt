package com.ink.domism.models

data class CartItem (
    var product : Item,
    var quantity : Int = 0
)