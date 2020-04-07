package com.husainazkas.chatapplication

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_register.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

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
        setContentView(R.layout.activity_register)

        initView()

    }

    private fun initView() {
        btn_register.setOnClickListener {
            emptyField()
        }
    }

    private fun emptyField() {
        val mail = et_register_email.text
        val pw = et_register_pw.text

            if (mail.isNullOrBlank()) {
                et_register_email.error = "Email harus diisi"
                et_register_email.requestFocus()
            } else if (!emailPattern.matcher(mail).matches()) {
                et_register_pw.error = "Email tidak valid"
                et_register_pw.requestFocus()
            } else if (pw.isNullOrBlank()) {
                et_register_pw.error = "Password harus diisi"
                et_register_pw.requestFocus()
            } else if (!passPattern.matcher(pw).matches()) {
                et_register_pw.error = "Password harus 6 digit atau lebih"
                et_register_pw.requestFocus()
            } else {
                registerUserToFirebase()
            }
    }

    private fun registerUserToFirebase() {
        val email = et_register_email.text.toString().trim()
        val password = et_register_pw.text.toString()
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (it.isSuccessful) {
                    Toast.makeText(this, "Register has been successful", Toast.LENGTH_LONG).show()
                    HomeActivity.launchIntent(this)
                } else {
                    Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener(this) {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        fun launchIntent(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }

}

