package com.example.fakestackoverflow.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fakestackoverflow.R
import com.example.fakestackoverflow.data.repository.Resource
import com.example.fakestackoverflow.databinding.FragmentQuestionBinding
import com.example.fakestackoverflow.model.QuestionUIModel
import com.google.android.material.chip.Chip
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QuestionFragment : Fragment() {

    private var _binding: FragmentQuestionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: QuestionViewModel
    private lateinit var adapter: QuestionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuestionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = QuestionAdapter { model -> openDetail(model) }
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(QuestionViewModel::class.java)

        binding.swipe.setOnRefreshListener { viewModel.loadQuestions() }

        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?) = false
            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText.orEmpty()
                return true
            }
        })

        lifecycleScope.launch {
            viewModel.availableTags.collectLatest { tags -> updateTagChips(tags) }
        }

        lifecycleScope.launch {
            viewModel.state.collectLatest { res ->
                when (res) {
                    is Resource.Loading -> {
                        binding.swipe.isRefreshing = true
                    }
                    is Resource.Success -> {
                        binding.swipe.isRefreshing = false
                        adapter.submitList(res.data)
                    }
                    is Resource.Error -> {
                        binding.swipe.isRefreshing = false
                        Toast.makeText(requireContext(), res.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun updateTagChips(tags: List<String>) {
        binding.chipGroupTags.removeAllViews()
        tags.forEach { tag ->
            val chip = Chip(requireContext()).apply {
                text = tag
                isCheckable = true
            }
            binding.chipGroupTags.addView(chip)
        }
        binding.chipGroupTags.setOnCheckedStateChangeListener { group, checkedIds ->
            val selected = checkedIds.firstOrNull()?.let { group.findViewById<Chip>(it) }
            viewModel.selectedTag.value = selected?.text?.toString()
        }
    }

    private fun openDetail(model: QuestionUIModel) {
        val frag = QuestionDetailFragment().apply {
            arguments = Bundle().apply {
                putLong("questionId", model.questionId)
                putString("title", model.title)
                putString("body", model.body)
                putString("author", model.author)
                putInt("score", model.score)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(R.id.container, frag)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}