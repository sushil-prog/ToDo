package com.aiegroup.todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aiegroup.todo.models.ToDo

@Dao
interface ToDoDao {

    @Query("SELECT * FROM ToDo")
    fun getTasks(): LiveData<List<ToDo>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createTask(toDoItem: ToDo)

    @Delete
    suspend fun delete(note: ToDo)

}