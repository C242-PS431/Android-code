package com.example.freshguard.ui.tracking

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.freshguard.R
import com.example.freshguard.data.response.DataItem
import com.example.freshguard.databinding.ScanItemBinding

class HistoryAdapter(
    private val onItemClick: (DataItem) -> Unit
) : ListAdapter<DataItem, HistoryAdapter.HistoryViewHolder>(DIFF_CALLBACK) {

    inner class HistoryViewHolder(private val binding: ScanItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: DataItem) {
            with(binding) {
                tvNameText.text = data.produce?.name ?: root.context.getString(R.string.text_track4)
                tvScoreText.text = root.context.getString(R.string.freshness_score_label, data.freshnessScore ?: "-")
                tvCreateAt.text = root.context.getString(R.string.text_track6, data.createdAt ?: "-")
                // Event klik item
                root.setOnClickListener {
                    onItemClick(data)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ScanItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val dataItem = getItem(position)
        holder.bind(dataItem)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<DataItem>() {
            override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
