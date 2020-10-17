package com.peachgenz.machatgun.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.peachgenz.machatgun.R
import com.peachgenz.machatgun.model.Message
import com.peachgenz.machatgun.model.MessageHolder
import com.peachgenz.machatgun.model.RecentChat
import com.peachgenz.machatgun.model.User
import kotlinx.android.synthetic.main.activity_chat.*
import kotlinx.android.synthetic.main.message_row.view.*
import java.text.SimpleDateFormat
import java.util.*

class ChatActivity : AppCompatActivity() {

    private var mDatabase: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        mDatabase = FirebaseDatabase.getInstance()
        mAuth = FirebaseAuth.getInstance()
        var myUser: User? =  null
        var friendUser: User? = null

        var userId = mAuth!!.currentUser!!.uid

        if (intent.extras != null){
            var chatId = intent!!.extras!!.get("chatid").toString()
            var friendId = intent!!.extras!!.get("friendid").toString()

            var linearLayoutManager = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)

            var query = mDatabase!!.reference.child("Messages").child(chatId).child("data")
            var option = FirebaseRecyclerOptions.Builder<Message>().setQuery(query, Message::class.java).setLifecycleOwner(this).build()

            var inflater = this.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

            var adapter = object: FirebaseRecyclerAdapter<Message, MessageHolder>(option){
                override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageHolder {
                    return MessageHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_row,parent,false))
                }

                override fun onBindViewHolder(holder: MessageHolder, position: Int, model: Message) {
                    if(model.message != null){
                        var message = model.message
                        var senderName: String?
                        var image: String?

                        if(model.sender == userId){
                            senderName = myUser!!.display_name
                            image = myUser!!.image

                            holder.customView.tv_name_message_row_left.visibility = View.GONE
                            holder.customView.tv_message_row_left.visibility = View.GONE
                            holder.customView.iv_image_message_row_left.visibility = View.GONE

                            holder.customView.tv_name_message_row_right.visibility = View.VISIBLE
                            holder.customView.tv_message_row_right.visibility = View.VISIBLE
                            holder.customView.iv_image_message_row_right.visibility = View.VISIBLE
                        }else{
                            senderName = friendUser!!.display_name
                            image = friendUser!!.image
                        }
                        holder.bind(message!!,senderName!!,image!!)
                    }
                }
            }

            var myRef = mDatabase!!.reference.child("Users").child(userId)
            myRef.addListenerForSingleValueEvent(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        myUser = snapshot.getValue(User::class.java)

                        var friendRef = mDatabase!!.reference.child("Users").child(friendId)
                        friendRef.addValueEventListener(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                friendUser = snapshot.getValue(User::class.java)

                                rv_chat.layoutManager = linearLayoutManager
                                rv_chat.adapter = adapter
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })



            btn_send_chat.setOnClickListener{
                var text = edt_message_chat.text.toString().trim()
                edt_message_chat.setText("")

                if(!TextUtils.isEmpty(text)){
                    var messageRef = mDatabase!!.reference.child("Messages").child(chatId).child("data").push()
                    var message = Message(userId,text)

                    messageRef.setValue(message)

                    var dateFormat = SimpleDateFormat("yyMMddHHmmssSSS")
                    var date = dateFormat.format(Date())

                    var myRecentRef =  mDatabase!!.reference.child("Chat").child(userId).child(friendId).child("Recent")
                    var friendRecentRef = mDatabase!!.reference.child("Chat").child(friendId).child(userId).child("Recent")

                    var recentChat = RecentChat(date,message.message!!)

                    myRecentRef.setValue(recentChat)
                    friendRecentRef.setValue(recentChat)

                }
            }
        }


    }
}