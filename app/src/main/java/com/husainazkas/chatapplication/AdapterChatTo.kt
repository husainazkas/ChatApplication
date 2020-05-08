package com.husainazkas.chatapplication

import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_from.view.*
import kotlinx.android.synthetic.main.item_friend_list.view.*

class AdapterChatTo : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.item_chat_to
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }
}