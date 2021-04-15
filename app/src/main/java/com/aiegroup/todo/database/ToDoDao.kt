package com.aiegroup.todo.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.aiegroup.todo.models.ToDo
import kotlinx.coroutines.Deferred
import java.util.*

@Dao
interface ToDoDao {

    @Query("SELECT * FROM ToDo")
    fun getTasks(): LiveData<List<ToDo>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createTask(toDoItem: ToDo)

    @Delete
    suspend fun delete(toDoItem: ToDo)

    @Query("SELECT COUNT(UUID) FROM ToDo WHERE taskTime=:time AND taskName=:taskname AND uuid!=:taskUUID")
    fun getRowCount(time:String,taskname:String,taskUUID:String):Int



}