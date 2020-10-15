package com.peachgenz.machatgun.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.peachgenz.machatgun.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    var mAuth: FirebaseAuth? = null
    var mAuthListener: FirebaseAuth.AuthStateListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_login.setOnClickListener{
            var intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_signup.setOnClickListener{
            var intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        mAuth = FirebaseAuth.getInstance()
        mAuthListener = FirebaseAuth.AuthStateListener {
            firebaseAuth: FirebaseAuth ->
            var user = firebaseAuth.currentUser

            if(user != null){
                var intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("userId",mAuth!!.currentUser!!.uid)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        mAuth!!.addAuthStateListener(mAuthListener!!)
    }

    override fun onStop() {
        super.onStop()

        mAuth!!.removeAuthStateListener(mAuthListener!!)
    }
}