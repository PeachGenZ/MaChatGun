package com.peachgenz.machatgun.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase
import com.peachgenz.machatgun.R
import kotlinx.android.synthetic.main.activity_status.*

class StatusActivity : AppCompatActivity() {

    var mCurrentUser: FirebaseUser? = null
    var mDatabase: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_status)

        if(intent.extras != null){
            var status = intent!!.extras!!.get("status").toString()
            edt_status.setText(status)
        }

        btn_status_submit.setOnClickListener {
            var updateStatus = edt_status.text.toString().trim()

            mCurrentUser = FirebaseAuth.getInstance().currentUser
            var uid = mCurrentUser!!.uid

            mDatabase = FirebaseDatabase.getInstance()
            var statusRef = mDatabase!!.reference.child("Users").child(uid).child("status")
            statusRef.setValue(updateStatus).addOnCompleteListener {
                task:Task<Void> ->
                if(task.isSuccessful){
                    Toast.makeText(this,"update successful",Toast.LENGTH_LONG).show()
                    finish()
                }else{
                    Toast.makeText(this,"update fail",Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}