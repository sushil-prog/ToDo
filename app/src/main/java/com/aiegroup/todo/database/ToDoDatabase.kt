package com.aiegroup.todo.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aiegroup.todo.models.ToDo

@Database(entities = [ToDo::class], version = 1)
abstract  class ToDoDatabase : RoomDatabase() {
    abstract val toDoDao: ToDoDao
}