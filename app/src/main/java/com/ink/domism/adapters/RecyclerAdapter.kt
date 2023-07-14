package com.ink.domism.adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
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
        val origPrice : TextView = itemView.findViewById(R.id.idOrigPrice)
        val discPrice : TextView = itemView.findViewById(R.id.idDiscPrice)
        val Image: ImageView = itemView.findViewById(R.id.idItemImage)
        val count : TextView = itemView.findViewById(R.id.idItemCount)
        val btnRemove : ImageView = itemView.findViewById(R.id.remItem)
        val btnAdd : ImageView = itemView.findViewById(R.id.addItem)
        var i = 0
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
        holder.origPrice.text = "₹" + List[position].origPrice.toString()
        holder.origPrice.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        holder.origPrice.alpha = 0.69F
        holder.discPrice.text = "₹" + List[position].discPrice.toString()

        holder.btnAdd.setOnClickListener {
            holder.i += 1
            holder.count.text = holder.i.toString()
            if(holder.i>0){
                holder.btnRemove.visibility = View.VISIBLE
                holder.count.visibility = View.VISIBLE
            }else{
                holder.btnRemove.visibility = View.GONE
                holder.count.visibility = View.GONE
            }
        }

        holder.btnRemove.setOnClickListener {
            holder.i -= 1
            holder.count.text = holder.i.toString()
            if(holder.i>0){
                holder.btnRemove.visibility = View.VISIBLE
                holder.count.visibility = View.VISIBLE
            }else{
                holder.btnRemove.visibility = View.GONE
                holder.count.visibility = View.GONE
            }
        }

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