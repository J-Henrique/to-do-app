package com.jhbb.todo.data.repository

import androidx.lifecycle.LiveData
import com.jhbb.todo.data.ToDoDao
import com.jhbb.todo.data.models.ToDoData

class ToDoRepository(private val toDoDao: ToDoDao) {

    fun getAllToDoData(): LiveData<List<ToDoData>> = toDoDao.getAllData()

    fun sortByHighPriority(): LiveData<List<ToDoData>> = toDoDao.sortByHighPriority()

    fun sortByLowPriority(): LiveData<List<ToDoData>> = toDoDao.sortByLowPriority()

    suspend fun insertData(toDoData: ToDoData) {
        toDoDao.insertData(toDoData)
    }

    suspend fun updateData(toDoData: ToDoData) {
        toDoDao.updateData(toDoData)
    }

    suspend fun deleteData(toDoData: ToDoData) {
        toDoDao.deleteData(toDoData)
    }

    suspend fun deleteAll() {
        toDoDao.deleteAll()
    }

    fun searchDatabase(query: String): LiveData<List<ToDoData>> {
        return toDoDao.searchDatabase(query)
    }
}