package com.husainazkas.chatapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.husainazkas.chatapplication.LoginActivity.Companion.currentUser
import com.husainazkas.chatapplication.ChatRoomActivity.Companion.friend
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    val uid = FirebaseAuth.getInstance().uid
    val adapter = GroupAdapter<ViewHolder>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkUserLoginAccount()

        recentChat()
    }

    private fun recentChat() {

        val lastChat = FirebaseDatabase.getInstance().getReference("/recent-message/$uid")

        lastChat.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {
                TODO("Not yet implemented")
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val lastMessage = p0.getValue(MessageData::class.java)

                adapter.add(AdapterRecentChat(lastMessage!!))
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val lastMessage = p0.getValue(MessageData::class.java)

                adapter.add(AdapterRecentChat(lastMessage!!))
            }

            override fun onChildRemoved(p0: DataSnapshot) {
                val lastMessage = p0.getValue(MessageData::class.java)

                adapter.remove(AdapterRecentChat(lastMessage!!))
            }

        })

        rv_home_chat_list.adapter = adapter

    }

    private fun fetchUser() {
        val db = FirebaseDatabase.getInstance().getReference("/user/$uid")

        db.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(UserData::class.java)!!
            }
        })

    }

    private fun checkUserLoginAccount() {
        if (uid.isNullOrEmpty()) {
            MainActivity.launchIntentClearTask(this)
        } else {
            fetchUser()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_home, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.nav_signOut ->{
                signOutUser()
            }
            R.id.nav_message ->{
                messageToFriend()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun messageToFriend() {
        FriendListActivity.launchIntent(this)
    }

    private fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
        MainActivity.launchIntentClearTask(this)
        Toast.makeText(this, "You've been signed out", Toast.LENGTH_LONG).show()
    }

    /*
    override fun onBackPressed() {
        val alert_exit = AlertDialog.Builder(this)
        alert_exit.setTitle("Keluar")
        alert_exit.setPositiveButton("Ya", { dialogInterface: DialogInterface, i: Int -> finish() })
        alert_exit.setNegativeButton("Tidak", { dialogInterface: DialogInterface, i: Int -> })
        alert_exit.show()
    }
     */

    companion object {

        fun launchIntentClearTask(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }

    }
}
