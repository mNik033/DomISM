package com.ink.domism.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import com.bumptech.glide.Glide
import com.ink.domism.R
import com.ink.domism.models.Item

class RecyclerAdapter(private val List : ArrayList<Item>):
    RecyclerView.Adapter<RecyclerAdapter.ItemViewHolder>(){

    private lateinit var mListener : onItemClickListener
    interface onItemClickListener{
        fun onItemClick(position: Int)
    }
    fun setOnItemClickListener(listener: onItemClickListener){
        mListener = listener
    }

    class ItemViewHolder(itemView: View, listener: onItemClickListener) : RecyclerView.ViewHolder(itemView){
        val itemName : TextView = itemView.findViewById(R.id.idItemName)
        val Desc : TextView = itemView.findViewById(R.id.idItemDescription)
        val Image: ImageView = itemView.findViewById(R.id.idItemImage)

        init {
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.card_view, parent, false)
        return ItemViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.itemName.text = List[position].name
        holder.Desc.text = "Rs" + List[position].discPrice.toString()
        Glide
            .with(holder.Image)
            .load(List[position].image)
            .placeholder(R.drawable.ic_round_fastfood_24)
            .centerCrop()
            .into(holder.Image)
    }

    override fun getItemCount(): Int {
        return List.size
    }

}