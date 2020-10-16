package com.peachgenz.machatgun.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.FirebaseDatabase
import com.peachgenz.machatgun.R
import com.peachgenz.machatgun.model.User
import com.firebase.ui.database.FirebaseRecyclerOptions
import com.peachgenz.machatgun.model.UserHolder
import kotlinx.android.synthetic.main.fragment_user.*

class UserFragment : Fragment() {

    var mDatabase: FirebaseDatabase? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_user, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mDatabase = FirebaseDatabase.getInstance()

        var linearLayoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)

        var query = mDatabase!!.reference.child("Users").orderByChild("display_name").limitToLast(30)
        var option = FirebaseRecyclerOptions.Builder<User>().setQuery(query,User::class.java).setLifecycleOwner(this).build()

        var adapter = object: FirebaseRecyclerAdapter<User,UserHolder>(option){
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserHolder {
                return UserHolder(LayoutInflater.from(parent.context).inflate(R.layout.users_row,parent,false))
            }

            override fun onBindViewHolder(holder: UserHolder, position: Int, model: User) {
                holder.bind(model)
            }
        }

        recycle_users.setHasFixedSize(true)
        recycle_users.layoutManager = linearLayoutManager
        recycle_users.adapter = adapter
    }
    
}