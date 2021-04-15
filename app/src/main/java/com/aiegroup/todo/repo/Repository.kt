package com.aiegroup.todo.repo

import androidx.lifecycle.LiveData
import com.aiegroup.todo.database.ToDoDao
import com.aiegroup.todo.models.ToDo
import kotlinx.coroutines.Deferred
import java.util.*

/**
 * .Repository class isolate data source transactions from app components
 */
class Repository(private val toDoDao: ToDoDao) {

    suspend fun getTaskItems(): LiveData<List<ToDo>> {
        return toDoDao.getTasks();
    }

    suspend fun createTask(todo: ToDo) {
        toDoDao.createTask(todo);
    }

    suspend fun delete(todo: ToDo) {
         toDoDao.delete(todo);
    }

    suspend  fun count(time:String,taskname:String,taskUUID:String): Int {
        return  toDoDao.getRowCount(time,taskname,taskUUID)
    }
}