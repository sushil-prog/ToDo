package com.aiegroup.todo.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.room.Room
import com.aiegroup.todo.database.ToDoDatabase
import com.aiegroup.todo.repo.Repository

/**
 * ViewModelProviderFactory used to create viewmodel by constructor with repository as argument
 */
class ViewModelProviderFactory(val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val viewModel = TodoViewModel(Repository(getDatabase(context).toDoDao));
        return viewModel as T
    }

    private lateinit var INSTANCE: ToDoDatabase

    private fun getDatabase(context: Context): ToDoDatabase {
        synchronized(ToDoDatabase::class.java) {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(
                    context.applicationContext,
                    ToDoDatabase::class.java,
                    "ToDoDb"
                ).build()
            }
        }
        return INSTANCE
    }
}