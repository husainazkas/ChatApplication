package com.husainazkas.chatapplication

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_to.view.*

class AdapterChatTo(val text : String, val userdata : UserData) : Item<ViewHolder>() {

    override fun getLayout(): Int {
        return R.layout.item_chat_to
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        itemView.tv_chat_to.text = text
        Picasso.get().load(userdata.ava).into(itemView.civ_chat_to)
    }
}