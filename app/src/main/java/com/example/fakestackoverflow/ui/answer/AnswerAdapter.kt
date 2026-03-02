package com.example.fakestackoverflow.ui.answer

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fakestackoverflow.databinding.ItemAnswerBinding
import com.example.fakestackoverflow.model.AnswerUiModel

class AnswerAdapter : ListAdapter<AnswerUiModel, AnswerAdapter.VH>(DIFF) {

    companion object {
        private val DIFF = object : DiffUtil.ItemCallback<AnswerUiModel>() {
            override fun areItemsTheSame(a: AnswerUiModel, b: AnswerUiModel) =
                a.answerId == b.answerId

            override fun areContentsTheSame(a: AnswerUiModel, b: AnswerUiModel) = a == b
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    inner class VH(private val b: ItemAnswerBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(answer: AnswerUiModel) {
            b.textAnswerAuthor.text = answer.authorName ?: "(unknown)"
            b.textAnswerScore.text = "▲ ${answer.score}"
            b.textAnswerBody.text =
                HtmlCompat.fromHtml(answer.body ?: "(no body)", HtmlCompat.FROM_HTML_MODE_LEGACY)
            b.textAccepted.visibility = if (answer.isAccepted) View.VISIBLE else View.GONE

            if (answer.authorAvatar != null) {
                b.imageAnswerAvatar.load(answer.authorAvatar) {
                    crossfade(true)
                    placeholder(android.R.drawable.sym_def_app_icon)
                }
            } else {
                b.imageAnswerAvatar.setImageResource(android.R.drawable.sym_def_app_icon)
            }
        }
    }
}