package com.aiegroup.todo.database

import androidx.databinding.adapters.Converters
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.aiegroup.todo.models.ToDo
import com.aiegroup.todo.utils.Converter
/**
 * .ToDo abstract database class important for using room database
 */
@Database(entities = [ToDo::class], version = 1)
@TypeConverters(Converter::class)
abstract  class ToDoDatabase : RoomDatabase() {
    abstract val toDoDao: ToDoDao
}