package com.jhbb.todo.fragments.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.jhbb.todo.R
import com.jhbb.todo.data.models.Priority
import com.jhbb.todo.data.models.ToDoData
import com.jhbb.todo.databinding.RowLayoutBinding

class ListAdapter : RecyclerView.Adapter<ListAdapter.MyViewHolder>() {

    var dataList: List<ToDoData> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = RowLayoutBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(dataList[position])
    }

    override fun getItemCount() = dataList.size

    class MyViewHolder(private val binding: RowLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(toDoData: ToDoData) {
            binding.titleTxt.text = toDoData.title
            binding.descriptionTxt.text = toDoData.description
            when (toDoData.priority) {
                Priority.HIGH -> {
                    binding.priorityIndicator.setCardBackgroundColor(
                        ContextCompat.getColor(binding.root.context, R.color.red)
                    )
                }
                Priority.MEDIUM -> {
                    binding.priorityIndicator.setCardBackgroundColor(
                        ContextCompat.getColor(binding.root.context, R.color.yellow)
                    )
                }
                Priority.LOW -> {
                    binding.priorityIndicator.setCardBackgroundColor(
                        ContextCompat.getColor(binding.root.context, R.color.green)
                    )
                }
            }
            binding.rowBackground.setOnClickListener {
                binding.root.findNavController().navigate(
                    ListFragmentDirections.actionListFragmentToUpdateFragment(toDoData)
                )
            }
        }
    }
}