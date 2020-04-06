package com.husainazkas.chatapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.husainazkas.chatapplication.HomeActivity.Companion.launchIntent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initView()

    }

    private fun initView() {
        btn_main_login.setOnClickListener {
            LoginActivity.launchIntent(this)
        }
        btn_main_register.setOnClickListener {
            RegisterActivity.launchIntent(this)
        }
    }
}
