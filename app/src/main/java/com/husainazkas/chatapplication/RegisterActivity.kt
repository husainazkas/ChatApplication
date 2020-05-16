package com.husainazkas.chatapplication

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_register.*
import java.lang.Exception
import java.util.*
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private val SELECT_PHOTO = 100
    private val PICK_CAMERA = 101
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

        btn_register_ava.setOnClickListener {
            getPhotoFromFile()
            //openCamera()
        }
        btn_register.setOnClickListener {
            emptyField()
        }

        tv_login.setOnClickListener {
            LoginActivity.launchIntent(this)
        }
    }

    private fun getPhotoFromFile() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, SELECT_PHOTO)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        if (intent.resolveActivity(this.packageManager) != null) {
            startActivityForResult(intent, PICK_CAMERA)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == SELECT_PHOTO && resultCode == Activity.RESULT_OK) {
            if (data!!.data != null){
                PHOTO_URI = data.data
                try {
                    PHOTO_URI.let {
                        if (Build.VERSION.SDK_INT < 28) {
                            val bitmap =
                                MediaStore.Images.Media.getBitmap(contentResolver, PHOTO_URI)
                            civ_register_ava.setImageBitmap(bitmap)
                            btn_register_ava.alpha = 0f
                        } else {
                            val source = ImageDecoder.createSource(contentResolver, PHOTO_URI!!)
                            val bitmap = ImageDecoder.decodeBitmap(source)
                            civ_register_ava.setImageBitmap(bitmap)
                            btn_register_ava.alpha = 0f
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        if (requestCode == PICK_CAMERA && resultCode == Activity.RESULT_OK ) {
            val takenByCamera = data?.extras?.get("data") as Bitmap
            civ_register_ava.setImageBitmap(takenByCamera)
        }
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
            et_register_pw.error = "You must set a password"
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
        val auth = FirebaseAuth.getInstance()

        if (PHOTO_URI == null) {
            Snackbar.make(btn_register, "Please select your photo", Snackbar.LENGTH_LONG).show()
            return
        } else {
            auth.createUserWithEmailAndPassword(email, password)
                .addOnSuccessListener {
                    Snackbar.make(btn_register, "Registering your account. Please wait...", Snackbar.LENGTH_LONG).show()
                    uploadPhotoToFirebase()
                }
                .addOnFailureListener {
                    Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun uploadPhotoToFirebase() {
        val photoName = UUID.randomUUID().toString()
        val uploadToFirebase = FirebaseStorage.getInstance().getReference("/chatApp/images/$photoName")

            uploadToFirebase.putFile(PHOTO_URI!!)
                .addOnSuccessListener {
                    uploadToFirebase.downloadUrl.addOnSuccessListener {
                        Toast.makeText(this, "Upload photo has been successful", Toast.LENGTH_SHORT).show()
                        saveAllUserToDatabase(it.toString())
                    }
                }
    }

    private fun saveAllUserToDatabase(photoUrl : String) {
        val user = FirebaseAuth.getInstance().uid
        val db = FirebaseDatabase.getInstance().getReference("/user/$user")

        db.setValue(UserData(
            uid = user.toString(),
            name = et_register_name.text.toString(),
            email = et_register_email.text.toString(),
            ava = photoUrl))
            .addOnSuccessListener {
                Toast.makeText(this, "Welcome to Chat Application", Toast.LENGTH_LONG).show()
                HomeActivity.launchIntentClearTask(this)
            }
            .addOnFailureListener {
                Toast.makeText(this, it.message.toString(), Toast.LENGTH_LONG).show()
            }
    }

    companion object {
        fun launchIntent(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_REORDER_TO_FRONT.or(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP)
            context.startActivity(intent)
        }
    }

}

