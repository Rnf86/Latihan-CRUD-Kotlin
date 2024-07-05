package com.example.myapplicationtest

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class AdapterList(var itemList: List<ItemList>) : RecyclerView.Adapter<AdapterList.ViewHolder>() {

    lateinit var listener: OnItemClickListener

    interface OnItemClickListener {
        fun onItemClick(item: ItemList)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var judul: TextView
        lateinit var subjudul: TextView
        lateinit var imageView: ImageView

        init {
            judul = itemView.findViewById(R.id.judul)
            subjudul = itemView.findViewById(R.id.subjudul)
            imageView = itemView.findViewById(R.id.imageView)
        }

    }

    override fun onBindViewHolder(holder: AdapterList.ViewHolder, position: Int) {
       var item = itemList.get(position)
        holder.judul.setText(item.judul)
        holder.subjudul.setText(item.subJudul)
        Glide.with(holder.imageView.context).load(item.imageUrl).into(holder.imageView)
        holder.itemView.setOnClickListener {
            if (listener != null){
                listener.onItemClick(item)
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)

        return ViewHolder(itemView)
    }

    override fun getItemCount() = itemList.size
}