package com.aiegroup.todo.repo

import androidx.lifecycle.LiveData
import com.aiegroup.todo.database.ToDoDao
import com.aiegroup.todo.models.ToDo


class Repository(private val toDoDao: ToDoDao) {

    suspend fun getTaskItems(): LiveData<List<ToDo>> {
        return toDoDao.getTasks();
    }

    suspend fun createTask(todo: ToDo) {
        toDoDao.createTask(todo);
    }

//    suspend fun delete(todo: ToDo) {
////        toDoDao.delete(todo);
//    }
}