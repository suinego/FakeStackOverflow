package com.example.fakestackoverflow.ui.questions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fakestackoverflow.databinding.ItemQuestionBinding
import com.example.fakestackoverflow.model.QuestionUIModel

class QuestionAdapter(
    private val onItemClick: (QuestionUIModel) -> Unit
) : ListAdapter<QuestionUIModel, QuestionAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<QuestionUIModel>() {
            override fun areItemsTheSame(a: QuestionUIModel, b: QuestionUIModel) =
                a.questionId == b.questionId

            override fun areContentsTheSame(a: QuestionUIModel, b: QuestionUIModel) =
                a.title == b.title && a.score == b.score &&
                a.author == b.author && a.tags == b.tags
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: QuestionUIModel) {
            binding.textTitle.text = model.title
            binding.textAuthor.text = model.author ?: "(unknown)"
            binding.textScore.text = "▲ ${model.score}"
            binding.textTags.text = model.tags.take(4).joinToString(" ") { "#$it" }

            if (model.authorAvatar != null) {
                binding.imageAvatar.load(model.authorAvatar) {
                    crossfade(true)
                    placeholder(android.R.drawable.sym_def_app_icon)
                }
            } else {
                binding.imageAvatar.setImageResource(android.R.drawable.sym_def_app_icon)
            }

            binding.root.setOnClickListener { onItemClick(model) }
        }
    }
}