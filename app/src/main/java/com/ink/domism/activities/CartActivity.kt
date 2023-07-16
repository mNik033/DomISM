package com.ink.domism.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ink.domism.R
import com.ink.domism.models.CartItem
import io.paperdb.Paper

class CartActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
    }

    companion object{

        fun addItem(item: CartItem){
            val cart = getCart()
            val targetItem = cart.singleOrNull {
                it.product.id == item.product.id
            }
            if(targetItem==null){
                item.quantity++
                cart.add(item)
            }else{
                targetItem.quantity++
            }
            saveCart(cart)
        }

        fun removeItem(item: CartItem){
            val cart = getCart()
            val targetItem = cart.singleOrNull {
                it.product.id == item.product.id
            }
            if(targetItem!=null){
                if(targetItem.quantity>0){
                    targetItem.quantity--
                }else{
                    cart.remove(targetItem)
                }
            }
            saveCart(cart)
        }

        fun getCart() : MutableList<CartItem> {
            return Paper.book().read("cart", mutableListOf())!!
        }

        fun saveCart(cart : MutableList<CartItem>){
            Paper.book().write("cart", cart)
        }

        fun getCartSize(): Int{
            var cartSize = 0
            getCart().forEach {
                cartSize += it.quantity
            }
            return cartSize
        }

        fun getItemQuantity(item: CartItem): Int {
            var itemQuantity = 0
            getCart().forEach {
                if(it.product.id == item.product.id){
                    itemQuantity+= it.quantity
                }
            }
            return itemQuantity
        }

    }

}