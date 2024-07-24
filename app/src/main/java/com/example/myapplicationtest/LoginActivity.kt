package com.example.myapplicationtest

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage


class LoginActivity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // initialize firebase
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")



        val tv_forgotpass = findViewById<TextView>(R.id.tv_forgotpass)
        val btn_login = findViewById<Button>(R.id.btn_login)
        val btn_register = findViewById<Button>(R.id.btn_register)
        val et_email = findViewById<EditText>(R.id.et_email)
        val et_password = findViewById<EditText>(R.id.et_password)

        tv_forgotpass.setOnClickListener {
            // Ini adalah error Resources.NotFoundException
            val nonExistentString = resources.getString(12345)
        }

        btn_login.setOnClickListener {
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (email.isEmpty()) {
                Toast.makeText(this, "Email harus di isi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password harus di isi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            getData(email, password)

        }

        btn_register.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun getData(email: String, password: String) {
        db.collection("users")
            .get()
            .addOnCompleteListener { task ->
                progressDialog.dismiss()
                var registered = false
                for (document in task.result) {
                    if(document.getString("email").equals(email)&&document.getString("password").equals(password)){
                       registered = true
                        break
                    }
                }
                if (registered){
                    Toast.makeText(this@LoginActivity, "Logi successfully", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@LoginActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Email atau Password Salah!", Toast.LENGTH_SHORT).show()
                }

            }
            .addOnFailureListener { e ->
                progressDialog.dismiss()
                Toast.makeText(
                    this@LoginActivity,
                    "Error Login:" + e.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                Log.w("Login", "Error Login", e)
            }

    }
}