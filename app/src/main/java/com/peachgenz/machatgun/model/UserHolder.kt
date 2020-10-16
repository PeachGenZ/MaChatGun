package com.peachgenz.machatgun.model

import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.peachgenz.machatgun.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.users_row.view.*

class UserHolder(val customView: View): RecyclerView.ViewHolder(customView) {

    fun bind(user:User){
        customView.tv_name_row?.text = user.display_name
        customView.tv_status_row?.text = user.status
        if(!user.thumb_image!!.equals("default")){
            Picasso.get().load(user.thumb_image).placeholder(R.drawable.ic_male_user_profile_picture).into(customView.iv_user_row)
        }
    }
}