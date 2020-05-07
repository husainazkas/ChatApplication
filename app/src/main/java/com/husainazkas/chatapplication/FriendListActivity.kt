package com.husainazkas.chatapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.AdapterView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_friend_list.*
import kotlinx.android.synthetic.main.friend_list.*

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
                    val userdata = it.getValue(UserData::class.java) as UserData
                    adapter.add(AdapterFriendList(userdata))
                }
                rv_friend_list.adapter = adapter
            }

        })
    }

    companion object {

        fun launchIntent(context: Context) {
            val intent = Intent(context, FriendListActivity::class.java)
            context.startActivity(intent)
        }
    }
}
