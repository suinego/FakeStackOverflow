package com.example.fakestackoverflow.ui.answer

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.fakestackoverflow.data.remote.AnswerModel
import com.example.fakestackoverflow.databinding.ItemAnswerBinding
import com.example.fakestackoverflow.model.AnswerUiModel

class AnswerAdapter : RecyclerView.Adapter<AnswerAdapter.VH>() {

    private val items = mutableListOf<AnswerUiModel>()

    fun submitList(list: List<AnswerUiModel>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemAnswerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    inner class VH(private val b: ItemAnswerBinding) : RecyclerView.ViewHolder(b.root) {
        fun bind(answer: AnswerUiModel) {
            b.textAnswerAuthor.text = answer.authorName ?: "(unknown)"
            b.textAnswerScore.text = answer.score.toString()
            b.textAnswerBody.text = HtmlCompat.fromHtml(answer.body ?: "(no body)", HtmlCompat.FROM_HTML_MODE_LEGACY)

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
