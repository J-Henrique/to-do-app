package com.jhbb.todo.fragments.update

import android.app.AlertDialog
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.jhbb.todo.R
import com.jhbb.todo.data.models.ToDoData
import com.jhbb.todo.data.viewModel.ToDoViewModel
import com.jhbb.todo.databinding.FragmentUpdateBinding
import com.jhbb.todo.fragments.SharedViewModel

class UpdateFragment : Fragment() {

    private var _binding: FragmentUpdateBinding? = null
    private val binding get() = _binding!!
    private val args by navArgs<UpdateFragmentArgs>()
    private val sharedViewModel: SharedViewModel by viewModels()
    private val viewModel: ToDoViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentUpdateBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.navArgs = args

        binding.currentPrioritiesSpinner.onItemSelectedListener = sharedViewModel.listener
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.update_fragment_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                when (menuItem.itemId) {
                    R.id.menu_save -> updateItem()
                    R.id.menu_delete -> confirmDeleteItem()
                    android.R.id.home -> requireActivity().onBackPressed()
                }
                return true
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun confirmDeleteItem() {
       AlertDialog.Builder(requireContext())
           .setPositiveButton("Yes") { _,_ ->
               viewModel.deleteData(args.currentItem)
               Toast.makeText(requireContext(), "Successfully removed!", Toast.LENGTH_LONG).show()
               findNavController().navigate(R.id.action_updateFragment_to_listFragment)
           }
           .setNegativeButton("No") { _, _ -> }
           .setMessage("Are you sure you want to remove?")
           .setTitle("Delete item")
           .create()
           .show()
    }

    private fun updateItem() {
        with(binding) {
            val title = currentTitleEt.text.toString()
            val description = currentDescriptionEt.text.toString()
            val priority = currentPrioritiesSpinner.selectedItem.toString()

            if (title.isNotEmpty().and(description.isNotEmpty())) {
                val updatedData = ToDoData(
                    args.currentItem.id, title, sharedViewModel.parsePriority(priority), description
                )
                viewModel.updateData(updatedData)
                Toast.makeText(requireContext(), "Successfully updated!", Toast.LENGTH_LONG).show()
                findNavController().navigate(R.id.action_updateFragment_to_listFragment)
            } else {
                Toast.makeText(requireContext(), "Please fill out all fields!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}