package com.husainazkas.chatapplication

import android.app.SearchManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isInvisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.husainazkas.chatapplication.LoginActivity.Companion.currentUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_room.*


class ChatRoomActivity : AppCompatActivity() {

    lateinit var friend : UserData
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        friend = intent.getParcelableExtra(FriendListActivity.FRIEND_KEY)!!
        supportActionBar!!.title = friend.name


        rv_chat_room.adapter = adapter

        initView()

    }

    private fun initView() {
        loadDataMessage()
        btn_chat_room_send.setOnClickListener {
            val editText = et_chat_room_text.text
            if (editText.isNullOrBlank()) {
                et_chat_room_text.requestFocus()
            } else {
                sendMessage()
            }
        }
    }

    private fun loadDataMessage() {
        val myId = currentUser.uid
        val friendId = friend.uid
        val messageRef = FirebaseDatabase.getInstance().getReference("/message/$myId/$friendId/")

        messageRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val messageCollection = p0.getValue(MessageData::class.java)
                val uid = FirebaseAuth.getInstance().uid
                if (messageCollection != null) {
                    if (messageCollection.fromId == uid) {
                        adapter.add(AdapterChatTo(messageCollection.text, currentUser))
                    } else {
                        adapter.add(AdapterChatFrom(messageCollection.text, friend))
                    }
                    rv_chat_room.smoothScrollToPosition(adapter.itemCount -1)
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }

        })
    }

    private fun sendMessage() {
        val fromId = FirebaseAuth.getInstance().uid.toString()
        val toId = friend.uid
        val messageDbReferenceMe = FirebaseDatabase.getInstance().getReference("/message/$fromId/$toId").push()
        val messageDbReferenceFriend = FirebaseDatabase.getInstance().getReference("/message/$toId/$fromId").push()
        val id = messageDbReferenceFriend.key.toString()
        val text = et_chat_room_text.text.toString()
        val time = System.currentTimeMillis()/1000

        messageDbReferenceMe.setValue(MessageData(id, fromId, text, toId, time))
            .addOnSuccessListener {
                et_chat_room_text.setText("")
                rv_chat_room.smoothScrollToPosition(adapter.itemCount -1)
                messageDbReferenceFriend.setValue(
                    MessageData(id, fromId, text, toId, time)
                )
            }


        val lastMessage = FirebaseDatabase.getInstance().getReference("/recent-message/$fromId/$toId/")
        val lastMessageTo = FirebaseDatabase.getInstance().getReference("/recent-message/$toId/$fromId/")
        lastMessage.setValue(MessageData(id, fromId, text, toId, time))
            .addOnSuccessListener {
                lastMessageTo.setValue(MessageData(id, fromId, text, toId, time))
            }
    }

    companion object {

        /*fun launchIntent(context: Context) {
            val intent = Intent(context, ChatRoomActivity::class.java)
            context.startActivity(intent)
        }*/
    }
}
