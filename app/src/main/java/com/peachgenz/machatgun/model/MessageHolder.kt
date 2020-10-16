package com.peachgenz.machatgun.model

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.peachgenz.machatgun.R
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.message_row.view.*

class MessageHolder(val customView: View) : RecyclerView.ViewHolder(customView) {
    fun bind(message: String,sender: String,image:String){
        customView.tv_message_row_left.text = message
        customView.tv_name_message_row_left.text = sender
        customView.tv_message_row_right.text = message
        customView.tv_name_message_row_right.text = sender
        Picasso.get().load(image).placeholder(R.drawable.ic_male_user_profile_picture).into(customView.iv_image_message_row_left)
        Picasso.get().load(image).placeholder(R.drawable.ic_male_user_profile_picture).into(customView.iv_image_message_row_right)
    }
}