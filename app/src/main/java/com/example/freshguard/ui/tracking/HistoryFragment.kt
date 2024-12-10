package com.example.freshguard.ui.tracking

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.freshguard.data.repository.HistoryRepository
import com.example.freshguard.data.retrofit.ApiCon
import com.example.freshguard.data.retrofit.ApiConfig
import com.example.freshguard.databinding.FragmentHistoryBinding

class HistoryFragment : Fragment() {
    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        ApiCon.init(requireContext())
        val apiHistory = ApiCon.apiHistoryService

        // Inisialisasi Repository dan ViewModel dengan Factory
        val repository = HistoryRepository(apiHistory)
        val factory = HistoryViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[HistoryViewModel::class.java]

        adapter = HistoryAdapter { dataItem ->
            Toast.makeText(requireContext(), "Clicked: ${dataItem.produce?.name}", Toast.LENGTH_SHORT).show()
        }

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
