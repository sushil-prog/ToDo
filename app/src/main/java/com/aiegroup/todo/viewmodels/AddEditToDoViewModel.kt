package com.aiegroup.todo.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aiegroup.todo.models.ToDo
/**
 * .VieeModel for AddEditToDoItemBottomSheetFragment
 */
class AddEditToDoViewModel : ViewModel() {
    /**
     * .editableTodoItem  bind with layout of AddEditToDoItemBottomSheetFragment
     */
    var editableTodoItem: MutableLiveData<ToDo> = MutableLiveData()
}