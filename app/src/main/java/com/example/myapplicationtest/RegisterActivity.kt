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


class RegisterActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var progressDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // initialize firebase
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")

        val btn_register = findViewById<Button>(R.id.btn_register)
        val et_name = findViewById<EditText>(R.id.et_name)
        val et_email = findViewById<EditText>(R.id.et_email)
        val et_password = findViewById<EditText>(R.id.et_password)


        btn_register.setOnClickListener {
            val name = et_name.text.toString()
            val email = et_email.text.toString()
            val password = et_password.text.toString()

            if (name.isEmpty()) {
                Toast.makeText(this, "Nama harus di isi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (email.isEmpty()) {
                Toast.makeText(this, "Email harus di isi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            } else if (password.isEmpty()) {
                Toast.makeText(this, "Password harus di isi", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            saveData(name, email, password)

        }
    }
    private fun saveData( name: String, email: String, password: String) {
        val data = hashMapOf<String, Any>()
        data.put("name", name)
        data.put("email", email)
        data.put("password", password)

            db.collection("users")
                .add(data)
                .addOnSuccessListener { documentReference ->
                    progressDialog.dismiss()
                    Toast.makeText(this@RegisterActivity, "Register successfully", Toast.LENGTH_SHORT)
                        .show()
                    val intent = Intent(this@RegisterActivity,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@RegisterActivity,
                        "Error adding news:" + e.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.w("Register", "Error adding document", e)
                }

    }
}