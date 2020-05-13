package com.husainazkas.chatapplication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.item_chat_list.view.*

class AdapterRecentChat(val text: MessageData) : Item<ViewHolder>() {

    lateinit var userData: UserData

    override fun getLayout(): Int {
        return R.layout.item_chat_list
    }

    override fun bind(viewHolder: ViewHolder, position: Int) {
        val itemView = viewHolder.itemView
        val friendId : String
        val uid = FirebaseAuth.getInstance().uid

        if (uid == text.fromId) {
            friendId = text.toId
        } else {
            friendId = text.fromId
        }

        val dataUser = FirebaseDatabase.getInstance().getReference("/user/$friendId")
        dataUser.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                userData = p0.getValue(UserData::class.java)!!
                itemView.tv_home_display_name.text = userData.name
                Picasso.get().load(userData.ava).into(itemView.civ_home_display_pict)
            }
        })
        itemView.tv_home_last_chat.text = text.text
    }
}