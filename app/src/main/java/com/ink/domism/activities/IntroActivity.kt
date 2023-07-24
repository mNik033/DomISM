package com.ink.domism.activities

import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.Typeface
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.view.WindowCompat
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.ErrorCodes
import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.ink.domism.R
import com.ink.domism.firebase.FirebaseClass

var isRegistered : Boolean = false

class IntroActivity : BaseActivity() {

    private lateinit var btnSignIn: Button

    private lateinit var topText: TextView

    private lateinit var iMobileLayout: TextInputLayout

    private lateinit var iMobile : TextInputEditText

    private lateinit var fab: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.activity_intro)

        btnSignIn = findViewById(R.id.buttonSignIn)

        topText = findViewById(R.id.toptv)

        iMobileLayout = findViewById(R.id.imobilelayout)

        iMobile = findViewById(R.id.imobile)

        fab = findViewById(R.id.idContinuefab)

        val user = FirebaseAuth.getInstance().currentUser

        if(user!=null){startActivity(Intent(this, MainActivity::class.java))
            finish()}else{signInLauncher.launch(signInIntent)}

        btnSignIn.setOnClickListener{
            signInLauncher.launch(signInIntent)
        }
    }

    private val signInLauncher = registerForActivityResult(
        FirebaseAuthUIActivityResultContract(),
    ) { res ->
        this.onSignInResult(res)
    }

    private fun onSignInResult(result: FirebaseAuthUIAuthenticationResult) {

        val response = result.idpResponse

        if (result.resultCode == RESULT_OK) {
            // Successfully signed in
            val user = FirebaseAuth.getInstance().currentUser

            if(user!=null) {

                btnSignIn.visibility = View.GONE

                topText.text =
                    "\nWelcome, ${user?.displayName}!\n\nPlease fill out your contact information to continue"
                topText.textSize = 24F

                topText.setTypeface(null, Typeface.NORMAL)

                iMobileLayout.visibility = View.VISIBLE

                fab.visibility = View.VISIBLE

                //validate phone number is eligible
                fab.setOnClickListener {
                    val mobile: Long = iMobile.text.toString().toLong()
                    val userInfo =
                        com.ink.domism.models.User(user.uid, user.displayName!!, user.email!!, mobile, false)
                    FirebaseClass().registerUser(this, userInfo)
                }
            }
        } else {
            // Sign in failed
            if (response == null) {
                // User pressed back button
                showErrorSnackbar("Sign-in cancelled");
                return;
            }

            if (response.getError()?.getErrorCode() == ErrorCodes.NO_NETWORK) {
                showErrorSnackbar("No internet connection");
                return;
            }

            if (response.getError()?.getErrorCode() == ErrorCodes.PROVIDER_ERROR) {
                showErrorSnackbar("Only people belonging to IIT ISM are allowed to use the app. Please use your institute email to sign in.");
                return;
            }

            showErrorSnackbar("Unkown error");
            Log.e(TAG, "Sign-in error: ", response.getError());
        }
    }

    // Choose authentication providers
    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
    )

    // Create and launch sign-in intent
    val signInIntent = AuthUI.getInstance()
        .createSignInIntentBuilder()
        .setAvailableProviders(providers)
        .setIsSmartLockEnabled(false)
        .build()

    override fun onPause(){
        super.onPause()
        if(!isRegistered){
            // Sign out the user if they haven't completed registration
            FirebaseAuth.getInstance().signOut()
        }
    }

}