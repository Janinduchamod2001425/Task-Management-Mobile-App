package com.example.taskmanagementapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.taskmanagementapp.MainActivity
import com.example.taskmanagementapp.R
import com.example.taskmanagementapp.databinding.FragmentEditTaskBinding
import com.example.taskmanagementapp.model.Task
import com.example.taskmanagementapp.viewmodel.TaskViewModel

class EditTaskFragment : Fragment(R.layout.fragment_edit_task), MenuProvider {

    private var editTaskBinding: FragmentEditTaskBinding? = null
    private val binding get() = editTaskBinding!!

    private lateinit var taskViewModel: TaskViewModel
    private lateinit var currentTask: Task

    private val args: EditTaskFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        editTaskBinding = FragmentEditTaskBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        taskViewModel = (activity as MainActivity).taskViewModel
        currentTask = args.task!!

        binding.editNoteTitle.setText(currentTask.taskname)
        binding.editNoteDesc.setText(currentTask.taskdesc)
        binding.editNotePriority.setText(currentTask.taskpriority)
        binding.editNoteDeadline.setText(currentTask.taskdeadline)

        binding.editNoteFab.setOnClickListener {
            val taskTitle = binding.editNoteTitle.text.toString().trim()
            val taskDesc = binding.editNoteDesc.text.toString().trim()
            val taskpriority = binding.editNotePriority.text.toString().trim()
            val taskdeadline = binding.editNoteDeadline.text.toString().trim()

            if(taskTitle.isNotEmpty()) {
                val task = Task(currentTask.id, taskTitle, taskDesc, taskpriority, taskdeadline)
                taskViewModel.updateTask(task)
                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please fill all fields", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun deleteTask() {
        activity?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Delete Task")
                setMessage("Do you want to delete this task?")
                setPositiveButton("Delete"){_,_ ->
                    taskViewModel.deleteTask(currentTask)
                    Toast.makeText(context, "Task Deleted", Toast.LENGTH_LONG).show()
                    view?.findNavController()?.popBackStack(R.id.homeFragment, false)
                }
                setNegativeButton("Cancel", null)
            }.create().show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edittask, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
       return when(menuItem.itemId){
           R.id.deleteMenu -> {
               deleteTask()
               true
           } else -> false
       }
    }

    override fun onDestroy() {
        super.onDestroy()
        editTaskBinding = null
    }
}