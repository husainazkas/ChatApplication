package com.husainazkas.chatapplication

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.husainazkas.chatapplication.LoginActivity.Companion.currentUser
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    private val uid = FirebaseAuth.getInstance().uid
    private val adapter = GroupAdapter<ViewHolder>()
    private val hashMap = HashMap<String?, MessageData?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        checkUserLoginAccount()
        recentChat()
        fab_home_to_friend_list.setOnClickListener {
            messageToFriend()
        }
    }

    private fun checkUserLoginAccount() {
        if (uid.isNullOrEmpty()) {
            MainActivity.launchIntentClearTask(this)
        } else {
            fetchUser()
        }
    }

    private fun fetchUser() {
        val db = FirebaseDatabase.getInstance().getReference("/user/$uid")

        db.addValueEventListener(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {
                MainActivity.launchIntentClearTask(applicationContext)
            }

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(UserData::class.java)!!
                title = "Hello, ${currentUser.name}!"
            }
        })
    }

    private fun recentChat() {
        val lastChat = FirebaseDatabase.getInstance().getReference("/recent-message/$uid")

        lastChat.addChildEventListener(object : ChildEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val lastMessage = p0.getValue(MessageData::class.java)
                hashMap[p0.key] = lastMessage
                refreshMessage()
            }

            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val lastMessage = p0.getValue(MessageData::class.java)
                hashMap[p0.key] = lastMessage
                refreshMessage()
            }

            override fun onChildRemoved(p0: DataSnapshot) {

            }
        })

        adapter.setOnItemClickListener { item, view ->
            val friendname = item as AdapterRecentChat
            val intent = Intent(view.context, ChatRoomActivity::class.java)

            intent.putExtra(FriendListActivity.FRIEND_KEY, friendname.userData)
            startActivity(intent)
        }
        rv_home_chat_list.adapter = adapter
    }

    private fun refreshMessage() {
        adapter.clear()
        hashMap.values.forEach {
            adapter.add(AdapterRecentChat(it!!))
        }
    }

    private fun messageToFriend() {
        FriendListActivity.launchIntent(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.nav_menu_home, menu)
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        (menu!!.findItem(R.id.nav_search).actionView as SearchView).apply {
            setSearchableInfo(searchManager.getSearchableInfo(componentName))
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            /*R.id.nav_search ->{
                search()
            }*/
            R.id.nav_settings ->{
                settings()
            }
            R.id.nav_signOut ->{
                signOutUser()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    /*private fun search() {

    }*/

    private fun settings() {

    }

    private fun signOutUser() {
        FirebaseAuth.getInstance().signOut()
        MainActivity.launchIntentClearTask(this)
        Toast.makeText(this, "You've been signed out", Toast.LENGTH_LONG).show()
    }

    companion object {
        fun launchIntentClearTask(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            context.startActivity(intent)
        }
    }
}