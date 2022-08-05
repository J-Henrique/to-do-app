package com.jhbb.todo.fragments.list

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.jhbb.todo.R
import com.jhbb.todo.data.models.ToDoData
import com.jhbb.todo.data.viewModel.ToDoViewModel
import com.jhbb.todo.databinding.FragmentListBinding
import com.jhbb.todo.fragments.SharedViewModel
import com.jhbb.todo.fragments.list.adapter.ListAdapter

class ListFragment : Fragment() {

    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!
    private val adapter: ListAdapter by lazy { ListAdapter() }
    private val viewModel: ToDoViewModel by viewModels()
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentListBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.sharedViewModel = sharedViewModel

        setupRecyclerView()

        viewModel.getAllData.observe(viewLifecycleOwner) {
            sharedViewModel.checkIfDatabaseEmpty(it)
            adapter.dataList = it
        }
        return binding.root
    }

    private fun setupRecyclerView() {
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        val swipeToDeleteCallback = object : SwipeToDelete() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val item = adapter.dataList[viewHolder.adapterPosition]
                viewModel.deleteData(item)
                adapter.notifyItemChanged(viewHolder.adapterPosition)
                restoreDeletedData(viewHolder.itemView, item, viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeToDeleteCallback)
        itemTouchHelper.attachToRecyclerView(binding.recyclerView)
    }

    fun restoreDeletedData(view: View, deletedItem: ToDoData, position: Int) {
        val snackbar = Snackbar.make(
            view, "Deleted '${deletedItem.title}'", Snackbar.LENGTH_LONG
        )
        snackbar.setAction("Undo") {
            viewModel.insertData(deletedItem)
            adapter.notifyItemChanged(position)
        }
        snackbar.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.list_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    android.R.id.home -> requireActivity().onBackPressed()
                    R.id.menu_delete_all -> confirmDeleteAll()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun confirmDeleteAll() {
        AlertDialog.Builder(requireContext())
            .setPositiveButton("Yes") { _,_ ->
                viewModel.deleteAll()
                Toast.makeText(requireContext(), "Successfully deleted all data!", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("No") { _, _ -> }
            .setMessage("Are you sure you want to remove all data?")
            .setTitle("Delete everything")
            .create()
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}