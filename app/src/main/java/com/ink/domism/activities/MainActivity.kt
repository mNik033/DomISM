package com.ink.domism.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ink.domism.R
import com.ink.domism.adapters.RecyclerAdapter
import com.ink.domism.models.Item
import io.paperdb.Paper

class MainActivity : AppCompatActivity() {

    private lateinit var itemList : ArrayList<Item>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        itemList = ArrayList()

        val recyclerView = findViewById<RecyclerView>(R.id.idrecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val buttonCart = findViewById<ImageView>(R.id.btnCart)
        buttonCart.setOnClickListener {
            startActivity(Intent(this, CartActivity::class.java))
        }

        getItemInfo(recyclerView)

        Paper.init(this)

    }

    private fun getItemInfo(recyclerView: RecyclerView){

        val database: DatabaseReference = Firebase.database(
            "https://domism-fe381-default-rtdb.asia-southeast1.firebasedatabase.app/"
        ).reference

        database.ref.child("items").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    itemList.clear()
                    for(itemSnapshot in snapshot.children){
                        val itemInfo = itemSnapshot.getValue(Item::class.java)
                        itemList.add(itemInfo!!)
                    }

                    val adapter = RecyclerAdapter(itemList)
                    recyclerView.adapter=adapter

                    adapter.setOnItemClickListener(object: RecyclerAdapter.onItemClickListener{
                        override fun onItemClick(position : Int){
                        }
                    })

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@MainActivity, error.toString(), Toast.LENGTH_SHORT).show()
            }
        })
    }
}