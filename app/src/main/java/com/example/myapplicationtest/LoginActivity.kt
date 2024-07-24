package com.example.studikasuspoint7

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast


class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
            val intent = Intent(this@LoginActivity,DashboardActivity::class.java)
            startActivity(intent)

        }

        btn_register.setOnClickListener {
            val intent = Intent(this@LoginActivity,RegisterActivity::class.java)
            startActivity(intent)
        }
    }
}