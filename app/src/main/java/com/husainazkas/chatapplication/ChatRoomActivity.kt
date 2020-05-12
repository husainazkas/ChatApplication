package com.husainazkas.chatapplication

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
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

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_room)

        friend = intent.getParcelableExtra(FriendListActivity.FRIEND_KEY)!!
        supportActionBar!!.title = friend.name

        rv_chat_room.adapter = adapter

        initView()

    }

    private fun loadDataMessage() {
        val MyId = currentUser.uid
        val FriendId = friend.uid
        val messageRef = FirebaseDatabase.getInstance().getReference("/message/$MyId/$FriendId/")

        messageRef.addChildEventListener(object : ChildEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
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
                }
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                TODO("Not yet implemented")
            }

        })
    }

    private fun initView() {
        loadDataMessage()
        btn_chat_room_send.setOnClickListener {
            sendMessage()
        }
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
                Toast.makeText(this, "Message has sent", Toast.LENGTH_LONG).show()
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

        lateinit var friend : UserData
        /*fun launchIntent(context: Context) {
            val intent = Intent(context, ChatRoomActivity::class.java)
            context.startActivity(intent)
        }*/
    }
}
