package com.jhbb.todo.data.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.jhbb.todo.data.ToDoDatabase
import com.jhbb.todo.data.models.ToDoData
import com.jhbb.todo.data.repository.ToDoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ToDoViewModel(application: Application) : AndroidViewModel(application) {

    private val toDoDao = ToDoDatabase.getDatabase(application).toDoDao()
    private val repository: ToDoRepository = ToDoRepository(toDoDao)

    val getAllData: LiveData<List<ToDoData>> = repository.getAllToDoData()
    val sortByHighPriority: LiveData<List<ToDoData>> = repository.sortByHighPriority()
    val sortByLowPriority: LiveData<List<ToDoData>> = repository.sortByLowPriority()

    fun insertData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(toDoData)
        }
    }

    fun updateData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateData(toDoData)
        }
    }

    fun deleteData(toDoData: ToDoData) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteData(toDoData)
        }
    }

    fun deleteAll() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAll()
        }
    }

    fun searchDatabase(query: String): LiveData<List<ToDoData>> {
        return repository.searchDatabase(query)
    }
}