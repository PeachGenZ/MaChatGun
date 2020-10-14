package com.peachgenz.machatgun

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.w3c.dom.Text

class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()

        btn_login_submit.setOnClickListener {
            var email = edt_email_login.text.toString().trim()
            var password = edt_password_login.text.toString().trim()

            loginWithEmailPassword(email, password)
        }
    }

    private fun loginWithEmailPassword(email: String, password: String){
        if(!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password)){
            mAuth!!.signInWithEmailAndPassword(email,password).addOnCompleteListener {
                    task: Task<AuthResult> ->
                if(task.isSuccessful){
                    Toast.makeText(this, "Login Successful.", Toast.LENGTH_LONG).show()
                    var intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("userId",mAuth!!.currentUser!!.uid)
                    startActivity(intent)
                    finish()
                }else{
                    Toast.makeText(this, "Please check your email and password.", Toast.LENGTH_LONG).show()
                }
            }
        }else{
            Toast.makeText(this, "Please check your email and password.", Toast.LENGTH_LONG).show()
        }
    }
}