package com.ink.domism.firebase

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.ink.domism.activities.BaseActivity
import com.ink.domism.activities.IntroActivity
import com.ink.domism.activities.MainActivity
import com.ink.domism.models.User

class FirebaseClass: BaseActivity() {

    private val database: DatabaseReference = Firebase.database(
        "https://domism-fe381-default-rtdb.asia-southeast1.firebasedatabase.app/").reference

    fun registerUser(context: Context, userInfo: User){
        database.child("users").child(userInfo.uid).setValue(userInfo)
            .addOnSuccessListener {
                Toast.makeText(context, "Registered successfully!", Toast.LENGTH_SHORT).show()

                val intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                context.startActivity(intent)

            }.addOnFailureListener{
                Toast.makeText(context, it.toString(), Toast.LENGTH_SHORT).show()
                Log.e("firebase", "Error setting data", it)
            }
    }

    /*database.ref.child("users/${userID}").get().addOnSuccessListener {
        it.value
    }.addOnFailureListener {
        Log.e("firebase", "Error getting data", it)
    }*/

}