package com.husainazkas.chatapplication

import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.friend_list.view.*

class AdapterFriendList(val userData: UserData) : Item<ViewHolder>() {
    override fun getLayout(): Int {
        return R.layout.friend_list
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        itemView.tv_friend_list.text = userData.name
        Picasso.get().load(userData.ava).into(itemView.civ_friend_list)
    }

}