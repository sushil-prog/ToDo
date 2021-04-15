package com.aiegroup.todo.viewmodels

import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aiegroup.todo.models.ToDo
import com.aiegroup.todo.repo.Repository
import com.aiegroup.todo.ui.TodoListFragment
import com.aiegroup.todo.utils.Converter
import com.google.android.material.button.MaterialButton
import kotlinx.coroutines.*
import java.util.*


class TodoViewModel(private val repository: Repository) : ViewModel() {

    /**
     * mutable live data for tasks items observe changes database transactions
     */
    var items: LiveData<List<ToDo>>? = null;

    lateinit var editableTodoItem: MutableLiveData<ToDo>



    companion object {
        @JvmStatic
        @BindingAdapter("bindTaskDate")
        fun bindTaskDate(button: MaterialButton, date: Date) {
            button.text = Converter.dateToDDMMMYYYY(date)
        }

        @JvmStatic
        @BindingAdapter("bindTaskTime")
        fun bindTaskTime(button: MaterialButton, date: Date) {
            button.text = Converter.dateToHHMM(date)
        }

    }

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        fetchToDoItemsFromRepository()
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    private fun fetchToDoItemsFromRepository() {
        viewModelScope.launch {
            items = repository.getTaskItems();
        }
    }


    /**
     * init mutable todo item for creating new one
     */
    fun initEditableToDoItem() {
        editableTodoItem = MutableLiveData();
        var toDoItem = ToDo(
            UUID.randomUUID().toString(),
            Converter.ToDDMMMYYYYHHMM(Calendar.getInstance().time),
            "",
            "",
            Calendar.getInstance().time
        );
        editableTodoItem.value = toDoItem

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
     * Insert single todoItem Item in repository ( also check for existing record with same time and task name ) .Use a coroutine launch to run in a
     * background thread.
     */
    fun insertToDo(todo: ToDo, trasactionCallback: TodoListFragment.TransactionCallback) {
        CoroutineScope(Dispatchers.IO).launch {
            val count = repository.count(todo.taskTime,todo.taskName,todo.uuid)
            if (count <= 0) {
                repository.createTask(todo)
                trasactionCallback.success()
            } else {
                trasactionCallback.failure()
            }
        }
    }

    /**
     * Delete single todoItem Item in repository .Use a coroutine launch to run in a
     * background thread.
     */
    fun deleteToDo(todo: ToDo) =
        viewModelScope.launch {
            repository.delete(todo)
        }

}