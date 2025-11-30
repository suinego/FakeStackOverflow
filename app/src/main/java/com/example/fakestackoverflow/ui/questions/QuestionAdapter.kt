package com.example.fakestackoverflow.ui.questions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fakestackoverflow.databinding.ItemQuestionBinding
import com.example.fakestackoverflow.model.QuestionUIModel

class QuestionAdapter(
    private val onItemClick: (QuestionUIModel) -> Unit
) : RecyclerView.Adapter<QuestionAdapter.VH>() {

    private val items = mutableListOf<QuestionUIModel>()

    fun submitList(list: List<QuestionUIModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class VH(private val binding: ItemQuestionBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(model: QuestionUIModel) {
            binding.textTitle.text = model.title
            binding.textAuthor.text = model.author ?: "(unknown)"
            binding.textScore.text = model.score.toString()
            model.authorAvatar?.let {
                binding.imageAvatar?.load(it) {
                    crossfade(true)
                    placeholder(android.R.drawable.sym_def_app_icon)
                }
            } ?: run {
                binding.imageAvatar?.setImageResource(android.R.drawable.sym_def_app_icon)
            }
            binding.root.setOnClickListener { onItemClick(model) }
        }
    }
}
