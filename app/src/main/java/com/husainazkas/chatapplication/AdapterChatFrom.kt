package com.husainazkas.chatapplication

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_from.view.*

class AdapterChatFrom(val text : String, val userdata : UserData) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item_chat_from
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        itemView.tv_chat_from.text = text
        Picasso.get().load(userdata.ava).into(itemView.civ_chat_from)
    }

}