package com.example.freshguard.ui.tracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshguard.R
import com.example.freshguard.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HistoryViewModel by viewModels()
    private lateinit var adapter: HistoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
       _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeHistoryData()

        // Memuat data riwayat scan dari ViewModel (misal halaman pertama dan 10 data)
        viewModel.getHistoryScan(page = 1, perPage = 10)
    }

    private fun observeHistoryData() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
        viewModel.historyResult.observe(viewLifecycleOwner) { response ->
            val historyList = response?.data

            if (!historyList.isNullOrEmpty()) {
                // Jika ada data, tampilkan teks dan isi RecyclerView
                binding.tvTextTrack.visibility = View.VISIBLE
                adapter.submitList(historyList)
            } else {
                // Jika tidak ada data, sembunyikan teks
                binding.tvTextTrack.visibility = View.GONE
            }
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            message?.let {
                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupRecyclerView() {
        binding.rvTrack.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@HistoryFragment.adapter
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Menghapus binding untuk menghindari memory leak
    }
}