package com.husainazkas.chatapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val currentUser = auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()

    }

    private fun initView() {
        btn_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = et_login_email.text.toString().trim()
        val password = et_login_pw.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener() {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_LONG).show()
                    HomeActivity.launchIntent(this)
                } else {
                    Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG).show()
                }
            }
    }

    companion object {
        fun launchIntent(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
