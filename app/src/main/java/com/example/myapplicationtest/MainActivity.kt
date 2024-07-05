package com.example.myapplicationtest

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore


class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var floatingActionButton: FloatingActionButton
    private lateinit var myAdapter: AdapterList
    private lateinit var itemList: ArrayList<ItemList>
    private lateinit var db : FirebaseFirestore
    private lateinit var progressDialog: ProgressDialog


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Initialize Firebase
        FirebaseApp.initializeApp(this)
        db = FirebaseFirestore.getInstance()

        //Initialize UI component
        recyclerView = findViewById(R.id.rcvNews)
        floatingActionButton = findViewById(R.id.floatAddNews)
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Loading...")

        // Setup RecyclerView
        recyclerView.setHasFixedSize(true)
        itemList = arrayListOf()
        myAdapter = AdapterList(itemList)
        recyclerView.adapter = (myAdapter)

        myAdapter.listener = object : AdapterList.OnItemClickListener{
            override fun onItemClick(item: ItemList) {
                var intent = Intent(this@MainActivity, NewsDetail::class.java)

                intent.putExtra("id", item.id)
                intent.putExtra("judul", item.judul)
                intent.putExtra("desc", item.subJudul)
                intent.putExtra("imageUrl", item.imageUrl)
                startActivity(intent)
            }
        }

        // Click Listener
        floatingActionButton.setOnClickListener {
            var toAddPage = Intent(this@MainActivity, NewsAdd::class.java)
            startActivity(toAddPage)

        }

        getData()

    }

    override fun onStart() {
        super.onStart()
        //Fetch data from firestore
        getData()
    }

    private fun getData() {
        progressDialog.show()
        db.collection("news").get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                itemList.clear()
                for (document in task.result) {
                    val item = ItemList(
                        document.getString("title"),
                        document.getString("desc"),
                        document.getString("imageUrl")
                    )
                    item.id = document.id
                    itemList.add(item)
                    Log.d("data", document.id + " => " + document.data)
                }
                myAdapter.notifyDataSetChanged()
            } else {
                Log.w("data", "Error getting documents.", task.exception)
            }
            progressDialog.dismiss()
        }
    }
}