package com.ink.domism.activities

import android.graphics.Paint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.ink.domism.R
import com.ink.domism.adapters.RecyclerAdapter
import com.ink.domism.models.CartItem
import com.ink.domism.models.Item
import io.paperdb.Paper

class ModalBottomSheet : BottomSheetDialogFragment(){

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view : View =  inflater.inflate(R.layout.modal_bottom_sheet_content, container, false)
        var itemList : ArrayList<Item> = arrayListOf()

        CartActivity.getCart().forEach {
            if(it.quantity!=0){
                itemList += it.product
            }
        }

        val adapter = RecyclerAdapter(itemList)
        val recyclerView = view.findViewById<RecyclerView>(R.id.idrecyclerViewOrder)

        recyclerView.layoutManager = LinearLayoutManager(CartActivity())
        recyclerView.adapter = adapter

        val totalOGP = view.findViewById<TextView>(R.id.totalOrigPrice)
        val totalDP = view.findViewById<TextView>(R.id.totalDiscPrice)
        val totalSP = view.findViewById<TextView>(R.id.totalStorePrice)

        totalOGP.text = "₹" + CartActivity.getTotalOrigPrice().toString()
        totalOGP.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        totalOGP.alpha = 0.69F
        totalDP.text = "₹" + CartActivity.getTotalDiscPrice().toString()
        totalSP.text = "₹" + CartActivity.getTotalStorePrice().toString()

        val locationMenu = view.findViewById<MaterialAutoCompleteTextView>(R.id.locationMenu)
        val locations = arrayOf("Location 1", "Location 2", "Location 3", "Location 4")
        locationMenu.setOnFocusChangeListener { v, hasFocus -> locationMenu.setSimpleItems(locations) }
        //TODO: Fetch locations from database

        val btnCheckout = view.findViewById<TextView>(R.id.buttonCheckout)

        btnCheckout.append(" (₹" + CartActivity.getTotalDiscPrice().toString()+")")
        btnCheckout.setOnClickListener {
            if(locationMenu.text.isNotEmpty()){
                // do the thang
            }else{
                Toast.makeText(it.context, "Please select a location", Toast.LENGTH_SHORT).show()
            }
        }
        adapter.setOnItemClickListener(object: RecyclerAdapter.onItemClickListener{
            override fun onItemClick(position : Int){
            }
        })

        return view
    }

    companion object{
        const val TAG ="ModalBottomSheet"
    }
}

class CartActivity : AppCompatActivity() {

    private lateinit var itemList : ArrayList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_cart)

        itemList = arrayListOf()

        getCart().forEach {
            if(it.quantity!=0){
                itemList += it.product
            }
        }

        val adapter = RecyclerAdapter(itemList)
        val recyclerView = findViewById<RecyclerView>(R.id.idrecyclerViewCart)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        val mToolbar = findViewById<MaterialToolbar>(R.id.cart_toolbar)
        mToolbar.setNavigationOnClickListener {
            finish()
        }

        val modalBottomSheet = ModalBottomSheet()
        //val modalBottomSheetBehavior = (modalBottomSheet.dialog as BottomSheetDialog).behavior
        val btnPay = findViewById<Button>(R.id.buttonPay)
        btnPay.setOnClickListener {
            modalBottomSheet.show(supportFragmentManager, ModalBottomSheet.TAG)
        }


        adapter.setOnItemClickListener(object: RecyclerAdapter.onItemClickListener{
            override fun onItemClick(position : Int){
            }
        })

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

        fun getTotalOrigPrice(): Int{
            var totalOrigPrice = 0
            getCart().forEach{
                totalOrigPrice += it.product.origPrice * it.quantity
            }
            return totalOrigPrice
        }

        fun getTotalDiscPrice(): Int{
            var totalDiscPrice = 0
            getCart().forEach{
                totalDiscPrice += it.product.discPrice * it.quantity
            }
            return totalDiscPrice
        }

        fun getTotalStorePrice(): Int{
            var totalStorePrice = 0
            getCart().forEach{
                totalStorePrice += it.product.storePrice * it.quantity
            }
            return totalStorePrice
        }

    }

}