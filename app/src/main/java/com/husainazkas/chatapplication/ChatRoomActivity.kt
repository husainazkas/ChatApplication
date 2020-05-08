package com.husainazkas.chatapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_room.*

class ChatRoomActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        adapter.add(AdapterChatFrom())
        adapter.add(AdapterChatTo())
        adapter.add(AdapterChatFrom())
        adapter.add(AdapterChatFrom())
        adapter.add(AdapterChatTo())
        adapter.add(AdapterChatTo())
        adapter.add(AdapterChatFrom())

        rv_chat_room.adapter = adapter

    }

    companion object {

        fun launchIntent(context: Context) {
            val intent = Intent(context, ChatRoomActivity::class.java)
            context.startActivity(intent)
        }
    }
}
