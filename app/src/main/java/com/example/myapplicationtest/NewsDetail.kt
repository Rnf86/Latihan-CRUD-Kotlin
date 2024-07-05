package com.example.myapplicationtest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class NewsDetail : AppCompatActivity() {
    lateinit var newsTitle: TextView
    lateinit var newsSubtitle: TextView
    lateinit var newsImage: ImageView
    lateinit var edit: Button
    lateinit var hapus: Button
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)

        // Init UI Component
        newsTitle = findViewById(R.id.newsTitle)
        newsSubtitle = findViewById(R.id.newsSubtitle)
        newsImage = findViewById(R.id.newsImage)
        edit = findViewById(R.id.editButton)
        hapus = findViewById(R.id.deleteButton)
        db = FirebaseFirestore.getInstance()


        // Get Data From Intent
        var intent = getIntent()
        var id = intent.getStringExtra("id")
        var title = intent.getStringExtra("judul")
        var subtitle = intent.getStringExtra("desc")
        var imageUrl = intent.getStringExtra("imageUrl")

        // Set Data to UI Component
        newsTitle.setText(title)
        newsSubtitle.setText(subtitle)
        Glide.with(this)
            .load(imageUrl)
            .into(newsImage)

        // Button Edit
        edit.setOnClickListener {
            val intent = Intent(this@NewsDetail, NewsAdd::class.java)
            intent.putExtra("judul", title)
            intent.putExtra("desc", subtitle)
            intent.putExtra("imageUrl", imageUrl)
            startActivity(intent)
            finish()
        }

        // Button Hapus
        hapus.setOnClickListener {
            // Delete the news item from the database
            db.collection("news").document(id!!).delete()
                .addOnSuccessListener { documentReference ->
                    Toast.makeText(this, "News item deleted successfully!", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Error deleting News!" + e.message, Toast.LENGTH_SHORT)
                        .show()
                    Log.w("News Detail", "Error deleting document")
                }

        }
    }
}