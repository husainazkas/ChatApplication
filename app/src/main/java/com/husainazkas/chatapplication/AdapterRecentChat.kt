package com.husainazkas.chatapplication

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_list.view.*

class AdapterRecentChat(val text: MessageData/*, val userData: UserData*/) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item_chat_list
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView

        //itemView.tv_home_display_name.text = userData.name
        itemView.tv_home_last_chat.text = text.text
        //Picasso.get().load(userData.ava).into(itemView.civ_home_display_pict)
    }
}