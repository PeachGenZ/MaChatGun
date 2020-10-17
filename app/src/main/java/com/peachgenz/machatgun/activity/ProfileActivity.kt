package com.peachgenz.machatgun.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.peachgenz.machatgun.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    var mDatabase: FirebaseDatabase? = null
    var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()

        if(intent.extras != null){
            var userId = mAuth!!.currentUser!!.uid
            var friendId = intent!!.extras!!.get("userid").toString()

            mDatabase!!.reference.child("Users").child(friendId).addValueEventListener(object : ValueEventListener{
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

                var chatId:String?
                var chatRef = mDatabase!!.reference.child("Chat").child(userId).child(friendId).child("chat_id")

                chatRef.addListenerForSingleValueEvent(object :ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists() ){
                            chatId = snapshot.value.toString()
                        }else{
                            var messageRef = mDatabase!!.reference.child("Messages").push()

                            var userList = HashMap<String,String>()
                            userList.put("0",userId)
                            userList.put("1",friendId)

                            messageRef.child("user_list").setValue(userList)

                            chatId = messageRef.key.toString()

                            var userDataRef = mDatabase!!.reference.child("Chat").child(userId).child(friendId).child("chat_id")
                            userDataRef.setValue(chatId)

                            var friendDataRef = mDatabase!!.reference.child("Chat").child(friendId).child(userId).child("chat_id")
                            friendDataRef.setValue(chatId)

                        }

                        var intent = Intent(this@ProfileActivity,ChatActivity::class.java)
                        intent.putExtra("chatid",chatId)
                        intent.putExtra("friendid",friendId)
                        startActivity(intent)
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }

                })
            }

        }else{
            finish()
        }
    }
}