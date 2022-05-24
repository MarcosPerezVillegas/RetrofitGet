package com.example.retrofitget.ViewHolder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.retrofitget.databinding.ItemPerroBinding
import com.squareup.picasso.Picasso

class PerroViewHolder(view:View):RecyclerView.ViewHolder(view) {

    private val binding = ItemPerroBinding.bind(view)

    fun bind (imagen:String){
        Picasso.get().load(imagen).into(binding.ivPerro)
    }
}