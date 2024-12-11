package com.example.freshguard.ui.store

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.freshguard.R
import com.example.freshguard.data.local.ModelStore
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class StoreFragment : Fragment() {
    private lateinit var mapView: MapView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Menggunakan Configuration.getInstance().load() di onCreate
        val sharedPreferences = requireContext().getSharedPreferences("your_preference_name", Context.MODE_PRIVATE)
        Configuration.getInstance().load(requireContext(), sharedPreferences)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout untuk fragment
        return inflater.inflate(R.layout.fragment_store, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Inisialisasi MapView
        mapView = view.findViewById(R.id.mapView)

        // Atur sumber peta, kontrol multi-sentuh, zoom, dan posisi awal
        // Mengubah posisi awal peta ke Surabaya
        val initialLocation = GeoPoint(-7.2504, 112.7501) // Surabaya

        mapView.setTileSource(TileSourceFactory.MAPNIK)
        mapView.setMultiTouchControls(true)

        val zoomButtons = mapView.zoomController
        zoomButtons.setZoomInEnabled(true)
        zoomButtons.setZoomOutEnabled(true)

        mapView.controller.setZoom(15.0)
        mapView.controller.setCenter(initialLocation)

        // Menambahkan marker untuk setiap toko
        val tokoList = listOf(
            ModelStore("Hokky Buah", GeoPoint(-7.26691, 112.74051), "Buka", "Jl. Raya Darmo No. 51, Surabaya"),
            ModelStore("Prima Buah", GeoPoint(-7.28559, 112.75698), "Buka", "Jl. Manyar Kertoarjo No. 8, Surabaya"),
            ModelStore("Buah Segar", GeoPoint(-7.29455, 112.78267), "Buka", "Jl. Raya Jemursari No. 60, Surabaya"),
            ModelStore("Buah Lestari", GeoPoint(-7.30643, 112.78134), "Buka", "Jl. Diponegoro No. 10, Surabaya"),
            ModelStore("Tropicana Fresh Fruit", GeoPoint(-7.28647, 112.79161), "Buka", "Jl. Raya Tenggilis No. 30, Surabaya"),
            ModelStore("Hasan Buah Surabaya", GeoPoint(-7.30895, 112.78648), "Buka", "Jl. Mulyosari No. 100, Surabaya")
        )



        tokoList.forEach { toko ->
            val marker = Marker(mapView)
            marker.position = toko.location
            marker.title = toko.name
            mapView.overlays.add(marker)
            marker.setOnMarkerClickListener { _, _ ->
                mapView.controller.animateTo(toko.location)
                true
            }
        }

        // Setup RecyclerView
        setupRecyclerView(view, tokoList)
    }

    private fun setupRecyclerView(view: View, tokoList: List<ModelStore>) {
        val recyclerView = view.findViewById<RecyclerView>(R.id.rv_store)
        val adapter = Adapter(tokoList) { toko ->
            // Animasi zoom ke lokasi toko yang dipilih
            mapView.controller.animateTo(toko.location)
            mapView.controller.setZoom(15.0)
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDetach()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }
}
