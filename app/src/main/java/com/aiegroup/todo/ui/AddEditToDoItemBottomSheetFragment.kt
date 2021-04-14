package com.aiegroup.todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aiegroup.todo.databinding.LayoutAdditemBottomSheetBinding
import com.aiegroup.todo.models.ToDo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AddEditToDoItemBottomSheetFragment : BottomSheetDialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    lateinit var _binding: LayoutAdditemBottomSheetBinding

//    companion object {
//        const val KEY_TODO_ITEM = "toToItem"
//
//        fun newInstance(toDoItem: ToDo) = ModalBottomSheet.apply {
//            Bundle().apply {
//                putParcelable(KEY_TODO_ITEM, toDoItem)
//
//            }
//            arguments.
//
//
//        }
//    }

    companion object {
        const val KEY_TODO_ITEM = "toToItem"
        fun newInstance(toDoItem: ToDo?) = AddEditToDoItemBottomSheetFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_TODO_ITEM, toDoItem)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = LayoutAdditemBottomSheetBinding.inflate(inflater, container, false)
        val view = _binding.root
        _binding.item =
            return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var doDoItem = arguments?.getParcelable<ToDo>(AddTodoBottomSheetFragment.KEY_TODO_ITEM)
        _binding.item = doDoItem;


    }
}