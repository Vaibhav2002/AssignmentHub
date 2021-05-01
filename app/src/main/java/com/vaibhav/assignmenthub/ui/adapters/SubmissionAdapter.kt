package com.vaibhav.assignmenthub.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.assignmenthub.data.models.Submission
import com.vaibhav.assignmenthub.databinding.SubmissionRecyclerItemBinding

class SubmissionAdapter(
    private val onSubmissionClicked: (Submission) -> Unit
) : ListAdapter<Submission, SubmissionAdapter.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = SubmissionRecyclerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: SubmissionRecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onSubmissionClicked(currentList[adapterPosition])
            }
        }


        fun bind(data: Submission) {
            binding.submission = data
            binding.initialText.text = data.studentName[0].toString()
        }
    }

    class DiffCall : DiffUtil.ItemCallback<Submission>() {
        override fun areItemsTheSame(
            oldItem: Submission,
            newItem: Submission
        ): Boolean {
            return oldItem.submissionId == newItem.submissionId
        }

        override fun areContentsTheSame(
            oldItem: Submission,
            newItem: Submission
        ): Boolean {
            return oldItem == newItem
        }
    }

}
