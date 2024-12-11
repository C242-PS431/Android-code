package com.example.freshguard.ui.store

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.freshguard.R
import com.example.freshguard.data.local.ModelStore

class Adapter(private val tokoList: List<ModelStore>,
              private val onItemClick: (ModelStore) -> Unit
) : RecyclerView.Adapter<Adapter.TokoViewHolder>() {
    // ViewHolder untuk item toko
    inner class TokoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tokoName: TextView = itemView.findViewById(R.id.tokoName)
        val tokoStatus: TextView = itemView.findViewById(R.id.tokoStatus)
        val tokoLocation: TextView = itemView.findViewById(R.id.tokoLocation) // Menambahkan TextView untuk lokasi
        val tokoAlamat: TextView = itemView.findViewById(R.id.tokoAlamat) // Menambahkan TextView untuk alamat
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.store_item, parent, false)
        return TokoViewHolder(view)
    }
    override fun onBindViewHolder(holder: TokoViewHolder, position: Int) {
        val toko = tokoList[position]
        holder.tokoName.text = toko.name
        holder.tokoStatus.text = toko.status

        // Menampilkan lokasi dalam format yang sesuai (latitude, longitude)
        holder.tokoLocation.text = "Lokasi: ${toko.location.latitude}, ${toko.location.longitude}"
        holder.tokoAlamat.text = toko.alamat // Menampilkan alamat

        // Menangani klik item
        holder.itemView.setOnClickListener {
            onItemClick(toko)
        }
    }

    // Mengembalikan jumlah item dalam daftar toko
    override fun getItemCount(): Int {
        return tokoList.size
    }
}