package com.husainazkas.chatapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import org.w3c.dom.Text
import java.util.regex.Pattern

class LoginActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val passPattern = Pattern.compile("^" + ".{6,}" + "$")
    val emailPattern = Pattern.compile("[a-zA-Z0-9\\.\\_\\%\\-\\+]{1,256}" +
            "\\@" +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
            "(" +
            "\\." +
            "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
            ")"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initView()

    }

    private fun initView() {
        btn_login.setOnClickListener {
            blankField()
        }
    }

    private fun blankField() {
        val email = et_login_email.text
        val pw = et_login_pw.text

        if (email.isNullOrBlank() || pw.isNullOrBlank()) {
            et_login_email.error = "Enter your email" // Blank email
            et_login_email.requestFocus()
            et_login_pw.error = "Enter your password" // Blank password
            et_login_pw.requestFocus()
        } else if (!emailPattern.matcher(email).matches()) {
            et_login_email.error = "Incorrect email"  // Email format is invalid
            et_login_email.requestFocus()
        } else if (!passPattern.matcher(pw).matches()) {
            et_login_pw.error = "Incorrect password"  // Password format is invalid
            et_login_pw.requestFocus()
        } else {
            login()
        }
    }

    private fun login() {
        val email = et_login_email.text.toString().trim()
        val password = et_login_pw.text.toString()
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener() {
                Toast.makeText(this, "Welcome back!", Toast.LENGTH_LONG).show()
                HomeActivity.launchIntentClearTask(this)
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        fun launchIntent(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}
