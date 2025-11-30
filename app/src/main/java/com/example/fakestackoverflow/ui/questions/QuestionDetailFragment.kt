package com.example.fakestackoverflow.ui.questions

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.text.HtmlCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.fakestackoverflow.data.toUiModel
import com.example.fakestackoverflow.databinding.FragmentQuestionDetailBinding
import com.example.fakestackoverflow.ui.answer.AnswerAdapter
import com.example.fakestackoverflow.ui.questions.DetailState
import com.example.fakestackoverflow.ui.questions.QuestionDetailViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class QuestionDetailFragment : Fragment() {

    private var _binding: FragmentQuestionDetailBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: QuestionDetailViewModel
    private val answersAdapter = AnswerAdapter()
    private var questionId: Long = -1L

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentQuestionDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        questionId = arguments?.getLong("questionId") ?: -1L
        val title = arguments?.getString("title") ?: "(no title)"
        val body = arguments?.getString("body")
        val author = arguments?.getString("author") ?: "(unknown)"
        val score = arguments?.getInt("score") ?: 0

        binding.textDetailTitle.text = title
        binding.textDetailAuthor.text = "$author • score: $score"
        binding.textDetailBody.text = HtmlCompat.fromHtml(body ?: "(no body)", HtmlCompat.FROM_HTML_MODE_LEGACY)

        binding.recyclerAnswers.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerAnswers.adapter = answersAdapter

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
        ).get(QuestionDetailViewModel::class.java)

        viewModel.loadAnswers(questionId)

        lifecycleScope.launch {
            viewModel.answersState.collectLatest { st ->
                when (st) {
                    is com.example.fakestackoverflow.data.repository.Resource.Loading -> {
                        binding.progressAnswers.visibility = View.VISIBLE
                        binding.recyclerAnswers.visibility = View.GONE
                        binding.textOfflineHint.visibility = View.GONE
                        binding.buttonRetry.visibility = View.GONE
                    }
                    is com.example.fakestackoverflow.data.repository.Resource.Success -> {
                        binding.progressAnswers.visibility = View.GONE
                        binding.recyclerAnswers.visibility = View.VISIBLE
                        binding.textOfflineHint.visibility = View.GONE
                        binding.buttonRetry.visibility = View.GONE

                        Log.d("QA", "Loaded answers count = ${st.data.size}")
                        Toast.makeText(requireContext(), "Answers: ${st.data.size}", Toast.LENGTH_SHORT).show()

                        answersAdapter.submitList(st.data) // уже Ui-модели
                    }
                    is com.example.fakestackoverflow.data.repository.Resource.Error -> {
                        binding.progressAnswers.visibility = View.GONE
                        binding.recyclerAnswers.visibility = View.GONE
                        binding.textOfflineHint.visibility = View.VISIBLE
                        binding.textOfflineHint.text = st.message
                        binding.buttonRetry.visibility = View.VISIBLE
                    }
                }
            }
        }


        binding.buttonRetry.setOnClickListener { viewModel.loadAnswers(questionId) }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
