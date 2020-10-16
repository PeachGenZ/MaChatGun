package com.peachgenz.machatgun.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.peachgenz.machatgun.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    var mDatabase : FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mDatabase = FirebaseDatabase.getInstance()

        if(intent.extras != null){
            var userId = intent!!.extras!!.get("userid")

            mDatabase!!.reference.child("Users").child(userId.toString()).addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var display_name = snapshot!!.child("display_name").value.toString()
                    var image = snapshot!!.child("image").value.toString()
                    var status = snapshot!!.child("status").value.toString()

                    tv_display_name_profile.text = display_name
                    tv_status_profile.text = status

                    if(!image.equals("default")){
                        Picasso.get().load(image).placeholder(R.drawable.ic_male_user_profile_picture).into(iv_user_image)
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    finish()
                }

            })

            btn_send_message.setOnClickListener{
                var intent = Intent(this,ChatActivity::class.java)
                intent.putExtra("userid",userId.toString())
                startActivity(intent)
            }

        }else{
            finish()
        }
    }
}