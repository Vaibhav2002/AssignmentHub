package com.vaibhav.assignmenthub.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.vaibhav.assignmenthub.data.models.Assignment
import com.vaibhav.assignmenthub.databinding.RecyclerItemBinding


class AssignmentAdapter(
    private val onAssignmentClicked: (Assignment) -> Unit
) :
    ListAdapter<Assignment, AssignmentAdapter.viewHolder>(DiffCall()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding = RecyclerItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    inner class viewHolder(private val binding: RecyclerItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                onAssignmentClicked(currentList[adapterPosition])
            }
        }


        fun bind(data: Assignment) {
            binding.assignment = data
            binding.initialText.text = data.subject[0].toString()
        }
    }

    class DiffCall : DiffUtil.ItemCallback<Assignment>() {
        override fun areItemsTheSame(
            oldItem: Assignment,
            newItem: Assignment
        ): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: Assignment,
            newItem: Assignment
        ): Boolean {
            return oldItem == newItem
        }
    }

}
