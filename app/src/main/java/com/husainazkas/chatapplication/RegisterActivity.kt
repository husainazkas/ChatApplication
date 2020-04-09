package com.husainazkas.chatapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    val auth = FirebaseAuth.getInstance()
    val PICK_PHOTO = 100
    val PICK_CAMERA = 101
    var PHOTO_URI : Uri? = null
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
        iv_register_avatar.setOnClickListener {
            getPhotoFromFile()
        }
    }

    private fun getPhotoFromFile() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, PICK_PHOTO)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PHOTO) {
            if (resultCode == Activity.RESULT_OK && data!!.data !=null){
                PHOTO_URI = data.data
                try {
                    PHOTO_URI?.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, PHOTO_URI)
                            iv_register_avatar.setImageBitmap(bitmap)
                        } else {
                            val source = ImageDecoder.createSource(contentResolver, PHOTO_URI!!)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            iv_register_avatar.setImageBitmap(bitmap)
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        /*if (requestCode == PICK_CAMERA) {
            if (resultCode == Activity.RESULT_OK){
                val file = File(PICK_CAMERA)
                val uri = Uri.fromFile(file)
                iv_register_avatar.setImageURI(uri)
            }
        }*/
    }

    private fun emptyField() {
        val name = et_register_name.text
        val email = et_register_email.text
        val pw = et_register_pw.text

            if (
                name.isNullOrBlank() ||
                email.isNullOrBlank() ||
                pw.isNullOrBlank()
            ) {
                et_register_name.error = "What's your name?"
                et_register_name.requestFocus()
                et_register_email.error = "You'll need this when you login"
                et_register_email.requestFocus()
                et_register_pw.error = "You must set a password to login"
                et_register_pw.requestFocus()
            } else if (!emailPattern.matcher(email).matches()) {
                et_register_email.error = "Invalid email address"
                et_register_email.requestFocus()
            } else if (!passPattern.matcher(pw).matches()) {
                et_register_pw.error = "Password must at least 6 characters"
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
                    // register has been successful
                    uploadPhotoToFirebase()
                } else {
                    Toast.makeText(this, it.result.toString(), Toast.LENGTH_LONG).show()
                }
            }
            .addOnFailureListener(this) {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    private fun uploadPhotoToFirebase() {

        val photoName = UUID.randomUUID().toString()
        val uploadToFirebase = FirebaseStorage.getInstance().getReference("chatApp/images/$photoName")

        uploadToFirebase.putFile(PHOTO_URI!!)
            .addOnSuccessListener {
                uploadToFirebase.downloadUrl.addOnSuccessListener {
                    Toast.makeText(this, "Upload photo has been successful", Toast.LENGTH_SHORT).show()
                    saveAllUserToDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun saveAllUserToDatabase(photoUrl : String) {
        val user = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("user/$user")

        db.setValue(UserData(name = et_register_name.text.toString(), email = et_register_email.text.toString(), ava = photoUrl))
            .addOnSuccessListener {
                HomeActivity.launchIntentClearTask(this)
                // Data has been saved
                Toast.makeText(this, "Register has been successful", Toast.LENGTH_LONG).show()
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        fun launchIntent(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }

}

