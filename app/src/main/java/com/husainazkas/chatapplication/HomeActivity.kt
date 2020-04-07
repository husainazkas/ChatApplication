package com.husainazkas.chatapplication

import android.app.LauncherActivity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import java.security.AccessControlContext
import kotlin.system.exitProcess

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
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
        fun launchIntent(context: Context) {
            val intent = Intent(context, HomeActivity::class.java)
            context.startActivity(intent)
        }
    }
}
