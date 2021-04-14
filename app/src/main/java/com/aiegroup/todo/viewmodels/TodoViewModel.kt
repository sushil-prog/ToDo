package com.aiegroup.todo.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiegroup.todo.models.ToDo
import com.aiegroup.todo.repo.Repository
import kotlinx.coroutines.launch
import java.util.*

class TodoViewModel(private val repository: Repository) : ViewModel() {


    var items: LiveData<List<ToDo>>? = null;
    lateinit var editableTodoItem: MutableLiveData<ToDo>


    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        fetchDataFromRepository()
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    private fun fetchDataFromRepository() {
        viewModelScope.launch {
            items = repository.getTaskItems();
        }
    }

    /**
     * init mutable todo item for creating new one
     */
    fun initEditableToDoItem() {
        var toDoItem  = ToDo(UUID.randomUUID().toString(), "", "", "Test Task ");
        editableTodoItem.postValue(toDoItem)

    }

    /**
     * Insert single todoItem Item in repository .Use a coroutine launch to run in a
     * background thread.
     */
    fun createToDo(todo: ToDo) =
        viewModelScope.launch {
            repository.createTask(todo)
        }

    /**
     * Delete single todoItem Item in repository .Use a coroutine launch to run in a
     * background thread.
     */
    fun deleteToDo(todo: ToDo) =
        viewModelScope.launch {
            //repository.delete(todo)
        }

}