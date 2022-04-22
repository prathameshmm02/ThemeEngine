package com.quickersilver.themeengine

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.quickersilver.themeengine.databinding.ItemColorBinding

class ColorAdapter(private val colorArray: List<Int>) :
    RecyclerView.Adapter<ColorAdapter.ViewHolder>() {

    var checkedPosition = -1
        set(value) {
            val lastCheckedPosition: Int = checkedPosition
            field = value
            notifyItemChanged(lastCheckedPosition)
            notifyItemChanged(value)
        }

    init {
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return colorArray[position].toLong()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemColorBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.binding) {
            colorView.setBackgroundColorRes(colorArray[position])
            colorView.setImageResource(if (checkedPosition == position) R.drawable.ic_round_check else 0)
        }
    }

    override fun getItemCount() = colorArray.size


    inner class ViewHolder(val binding: ItemColorBinding) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.colorView.setOnClickListener {
                val lastCheckedPosition: Int = checkedPosition
                checkedPosition = adapterPosition
                binding.colorView.setImageResource(R.drawable.ic_round_check)
                notifyItemChanged(lastCheckedPosition)
            }
        }
    }
}