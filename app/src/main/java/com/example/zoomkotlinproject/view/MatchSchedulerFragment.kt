package com.example.zoomkotlinproject.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.zoomkotlinproject.adapter.MatchScheduleAdapter
import com.example.zoomkotlinproject.databinding.FragmentMatchSchedulerBinding
import com.example.zoomkotlinproject.model.MatchSchedule
import com.example.zoomkotlinproject.utils.Constants
import com.example.zoomkotlinproject.viewmodel.MainViewModel
import com.example.zoomkotlinproject.viewstate.MainViewEvent
import com.example.zoomkotlinproject.viewstate.MainViewState
import com.google.android.material.snackbar.Snackbar


class MatchSchedulerFragment : Fragment() {

    private lateinit var binding: FragmentMatchSchedulerBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        viewModel.viewState.observe(this) { render(it) }
        activity?.actionBar?.show()
    }

    private fun render(viewState: MainViewState) {
        viewState.matchScheduleResponse?.let {
            if (it.data.isNullOrEmpty()) {
                binding.progressBar.visibility = View.GONE
                binding.emptyListTv.visibility = View.VISIBLE
            }
            else
                initializeMatchScheduleAdapter(it.data)
        }
        viewState.error?.let {
            binding.progressBar.visibility = View.GONE
            Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMatchSchedulerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack();
        }
        if (Constants.isOnline(requireContext())) {
            binding.progressBar.visibility = View.VISIBLE
            viewModel.onEvent(MainViewEvent.GetMatchScheduleEvent)
        }
    }

    private fun initializeMatchScheduleAdapter(data: List<MatchSchedule>?) {
        binding.progressBar.visibility = View.GONE
        val adapter = MatchScheduleAdapter()
        binding.matchScheduleRv.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        binding.matchScheduleRv.adapter = adapter
        adapter.submitList(data)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            MatchSchedulerFragment().apply {
            }
    }
}