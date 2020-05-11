package com.husainazkas.chatapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_friend_list.*
import kotlinx.android.synthetic.main.item_friend_list.*

class FriendListActivity : AppCompatActivity() {

    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_friend_list)

        fetchUser()

    }

    private fun fetchUser() {
        val db = FirebaseDatabase.getInstance().getReference("/user/")

        db.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(p0: DataSnapshot) {
                p0.children.forEach {
                    val uid = FirebaseAuth.getInstance().uid
                    val userdata = it.getValue(UserData::class.java) as UserData
                    if (userdata.uid != uid) {
                        adapter.add(AdapterFriendList(userdata))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val friendname = item as AdapterFriendList
                    val intent = Intent(view.context, ChatRoomActivity::class.java)
                    intent.putExtra(FRIEND_KEY, friendname.userData)
                    startActivity(intent)

                }

                rv_friend_list.adapter = adapter
            }

        })
    }

    companion object {

        val FRIEND_KEY = "friend_key"
        fun launchIntent(context: Context) {
            val intent = Intent(context, FriendListActivity::class.java)
            context.startActivity(intent)
        }
    }
}
