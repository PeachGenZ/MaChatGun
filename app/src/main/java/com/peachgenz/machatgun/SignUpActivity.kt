package com.peachgenz.machatgun

import android.content.Intent
import android.icu.number.NumberFormatter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.btn_login_submit
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null
    var mDatabase: FirebaseDatabase? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mAuth = FirebaseAuth.getInstance()
        mDatabase = FirebaseDatabase.getInstance()

        btn_signup_submit.setOnClickListener {
            var display_name = edt_display_name.text.toString().trim()
            var email = edt_email.text.toString().trim()
            var password = edt_password.text.toString().trim()

            createUser(display_name, email, password)

        }
    }

    private fun createUser(display_name: String, email: String, password: String){
        if(!TextUtils.isEmpty(display_name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth!!.createUserWithEmailAndPassword(email,password).addOnCompleteListener {
                    task: Task<AuthResult> ->

                if(task.isSuccessful){
                    sendUserDataToFirebase(display_name)
                }
            }
        }
    }

    private fun sendUserDataToFirebase(display_name: String){
        var user = mAuth!!.currentUser
        var userId = user!!.uid

        var userRef = mDatabase!!.reference.child("Users").child(userId)

        var userObject = HashMap<String, String>()
        userObject.put("display_name", display_name)
        userObject.put("status", "Hello I'm $display_name")
        userObject.put("image", "default")
        userObject.put("thumb_image", "default")

        userRef.setValue(userObject).addOnCompleteListener {
                task: Task<Void> ->

            if(task.isSuccessful){
                Toast.makeText(this, "Create Successful", Toast.LENGTH_LONG).show()
                var intent = Intent(this, DashboardActivity::class.java)
                intent.putExtra("userId",userId)
                startActivity(intent)
                finish()
            }else{
                Toast.makeText(this, "Create Unsuccessful", Toast.LENGTH_LONG).show()
            }
        }
    }
}