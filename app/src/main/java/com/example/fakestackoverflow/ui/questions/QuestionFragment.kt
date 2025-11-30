package com.example.fakestackoverflow.ui.questions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fakestackoverflow.databinding.FragmentQuestionBinding

import com.example.fakestackoverflow.data.repository.Resource
import com.example.fakestackoverflow.data.local.QuestionEntity
import com.example.fakestackoverflow.data.toUiModel
import com.example.fakestackoverflow.model.QuestionUIModel
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

        viewModel = ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application))
            .get(QuestionViewModel::class.java)

        binding.swipe.setOnRefreshListener { viewModel.loadQuestions() }

        lifecycleScope.launch {
            viewModel.state.collectLatest { res ->
                when (res) {
                    is Resource.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                        binding.swipe.isRefreshing = true
                    }
                    is Resource.Success -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipe.isRefreshing = false
                        val uiList = res.data.map { it.toUiModel() } // toUiModel extension from your mappers
                        adapter.submitList(uiList)
                    }
                    is Resource.Error -> {
                        binding.progressBar.visibility = View.GONE
                        binding.swipe.isRefreshing = false
                        Toast.makeText(requireContext(), res.message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    private fun openDetail(model: QuestionUIModel) {
        val frag = com.example.fakestackoverflow.ui.questions.QuestionDetailFragment().apply {
            arguments = Bundle().apply {
                putLong("questionId", model.questionId)
                putString("title", model.title)
                putString("body", model.body)
                putString("author", model.author)
                putInt("score", model.score)
            }
        }
        parentFragmentManager.beginTransaction()
            .replace(com.example.fakestackoverflow.R.id.container, frag)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
