package com.example.myapplicationtest

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class NewsAdd : AppCompatActivity() {
    private var PICK_IMAGE_REQUEST = 1

    private lateinit var tvTitle: EditText
    private lateinit var desc: EditText
    private lateinit var imageView: ImageView
    private lateinit var saveNews: Button
    private lateinit var chooseImage: Button
    private var imageUri: Uri? = null
    private lateinit var dbNews: FirebaseFirestore
    private lateinit var storage: FirebaseStorage
    private lateinit var progressDialog: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_add)

        //Initilize Firebase
        dbNews = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        // Initialize UI component
        tvTitle = findViewById(R.id.tv_title)
        desc = findViewById(R.id.tv_desc)
        imageView = findViewById(R.id.imageView)
        saveNews = findViewById(R.id.btnAdd)
        chooseImage = findViewById(R.id.btnChooseImage)

        var intent = getIntent()
        var id = intent?.getStringExtra("id")
        var title = intent?.getStringExtra("judul")
        var subtitle = intent?.getStringExtra("desc")
        var imageUrl = intent?.getStringExtra("imageUrl")


        // Set Data to UI Component
        tvTitle.setText(title)
        desc.setText(subtitle)
        Glide.with(this)
            .load(imageUrl)
            .into(imageView)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")

        chooseImage.setOnClickListener {
            openFileChooser()
        }

        saveNews.setOnClickListener {
            var newsTitle = tvTitle.text.toString().trim()
            var newsDesc = desc.text.toString().trim()

            if (newsTitle.isEmpty() || newsDesc.isEmpty()) {
                Toast.makeText(this@NewsAdd, "Title and description cannot be empty", Toast.LENGTH_SHORT).show()

                return@setOnClickListener
            }
            progressDialog.show()

            if (imageUri != null){
                uploadImageToStorage(id, newsTitle, newsDesc)
            } else {
                saveData(id, newsTitle,newsDesc, imageUrl!!)
            }



        }
    }

    private fun openFileChooser() {
        val intent = Intent()
        intent.setType("image/*")
        intent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(intent, PICK_IMAGE_REQUEST)


    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            imageUri = data.data!!
            imageView.setImageURI(imageUri)
        }
    }

    private fun uploadImageToStorage(id:String?, newsTitle: String, newsDesc: String) {
        if (imageUri != null) {
            var storageRef =
                storage.getReference().child("news_images/" + System.currentTimeMillis() + ".jpg")
            storageRef.putFile(imageUri!!)
                .addOnSuccessListener {
                    storageRef.downloadUrl.addOnSuccessListener { uri ->
                        var imageUrl = uri.toString()
                        saveData(id, newsTitle, newsDesc, imageUrl)
                    }

                }.addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@NewsAdd,
                        "Failed to Upload Image:" + e.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }
    }

    private fun saveData(id:String?, newsTitle: String, newsDesc: String, imageUrl: String) {
        val news = hashMapOf<String, Any>()
            news.put("title", newsTitle)
            news.put("desc", newsDesc)
            news.put("imageUrl", imageUrl)

        if(id!=null){

            dbNews.collection("news")
                .document(id)
                .update(news)
                .addOnSuccessListener { documentReference ->
                    progressDialog.dismiss()
                    Toast.makeText(this@NewsAdd, "News update successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(this@NewsAdd, "Error adding news:" + e.message, Toast.LENGTH_SHORT)
                        .show()
                    Log.w("NewsAdd", "Error adding document", e)
                }
        }else {

            dbNews.collection("news")
                .add(news)
                .addOnSuccessListener { documentReference ->
                    progressDialog.dismiss()
                    Toast.makeText(this@NewsAdd, "News added successfully", Toast.LENGTH_SHORT)
                        .show()
                    finish()
                }
                .addOnFailureListener { e ->
                    progressDialog.dismiss()
                    Toast.makeText(
                        this@NewsAdd,
                        "Error adding news:" + e.message,
                        Toast.LENGTH_SHORT
                    )
                        .show()
                    Log.w("NewsAdd", "Error adding document", e)
                }
        }
    }

}