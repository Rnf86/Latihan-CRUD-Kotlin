package com.example.studikasuspoint7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

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
            val intent = Intent(this@RegisterActivity,DashboardActivity::class.java)
            startActivity(intent)

        }
    }
}