package com.aiegroup.todo.ui

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProviders
import com.aiegroup.todo.R
import com.aiegroup.todo.databinding.LayoutAdditemBottomSheetBinding
import com.aiegroup.todo.models.ToDo
import com.aiegroup.todo.utils.Converter
import com.aiegroup.todo.viewmodels.AddEditToDoViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.STATE_DRAGGING
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.lang.reflect.Field
import java.util.*

class AddEditToDoItemBottomSheetFragment : BottomSheetDialogFragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.SomeStyle)

    }

    lateinit var _binding: LayoutAdditemBottomSheetBinding
    lateinit var validateCallback: TodoListFragment.ValidateCallback


    companion object {
        const val KEY_TODO_ITEM = "toToItem"
        const val KEY_EDIT_MODE = "isEditMode"
        fun newInstance(
            toDoItem: ToDo?,
            isEditMode: Boolean,
            valdiate: TodoListFragment.ValidateCallback
        ) = AddEditToDoItemBottomSheetFragment().apply {
            validateCallback = valdiate
            arguments = Bundle().apply {
                putParcelable(KEY_TODO_ITEM, toDoItem)
                putBoolean(KEY_EDIT_MODE, isEditMode)
            }
        }
    }


    lateinit var addEditToDoViewModel: AddEditToDoViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        addEditToDoViewModel = ViewModelProviders.of(this).get(AddEditToDoViewModel::class.java)
        // Inflate the layout for this fragment
        _binding = LayoutAdditemBottomSheetBinding.inflate(inflater, container, false)
        _binding.lifecycleOwner = this
        val view = _binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        var doDoItem =
            arguments?.getParcelable<ToDo>(AddEditToDoItemBottomSheetFragment.KEY_TODO_ITEM)
        var mode =
            arguments?.getBoolean(AddEditToDoItemBottomSheetFragment.KEY_EDIT_MODE)
        _binding.mode = mode
        _binding.viewModel = addEditToDoViewModel
        (_binding.viewModel as AddEditToDoViewModel).editableTodoItem.value = doDoItem
        _binding.dateButton.setOnClickListener(this)
        _binding.timeButton.setOnClickListener(this)
        _binding.fabAddTask.setOnClickListener(this);
        _binding.taskNameTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                addEditToDoViewModel?.editableTodoItem.value =
                    addEditToDoViewModel?.editableTodoItem?.value?.let {
                        ToDo(
                            it.uuid, it.taskTime, s.toString(), it.taskCreater, it.taskDate
                        )
                    }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })

        _binding.taskCreateTextView.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable) {
                addEditToDoViewModel?.editableTodoItem.value =
                    addEditToDoViewModel?.editableTodoItem?.value?.let {
                        ToDo(
                            it.uuid, it.taskTime, it.taskName, s.toString(), it.taskDate
                        )
                    }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int,
                before: Int, count: Int
            ) {

            }
        })


    }

    override fun onClick(v: View?) {
        if (v == _binding.dateButton) {
            val calendar = Calendar.getInstance()
            calendar.timeInMillis = _binding.viewModel?.editableTodoItem?.value?.taskDate?.time!!
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)
            val year = calendar.get(Calendar.YEAR)
            val dpd = DatePickerDialog(
                requireContext(),
                { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
//                    lblDate.setText("" + dayOfMonth + " " + MONTHS[monthOfYear] + ", " + year)
                    val calendar = Calendar.getInstance()
                    calendar.time = _binding.viewModel?.editableTodoItem?.value?.taskDate
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    calendar.set(Calendar.YEAR, year)
                    calendar.set(Calendar.MONTH, monthOfYear)
                    (_binding.viewModel as AddEditToDoViewModel)?.editableTodoItem.value?.taskDate?.time =
                        calendar.timeInMillis
                    addEditToDoViewModel?.editableTodoItem.value =
                        addEditToDoViewModel?.editableTodoItem?.value?.let {
                            ToDo(
                                it.uuid,
                                Converter.ToDDMMMYYYYHHMM(calendar.time),
                                it.taskName,
                                it.taskCreater,
                                calendar.time
                            )
                        }


                },
                year,
                month,
                day
            ).apply {
                show()
            }
            dpd.datePicker.minDate = calendar.timeInMillis
        } else if (v == _binding.timeButton) {
            val calendar = Calendar.getInstance()
//            calendar.timeInMillis = _binding.item.taskDate.time
            calendar.timeInMillis = _binding.viewModel?.editableTodoItem?.value?.taskDate?.time!!
            val hour = calendar.get(Calendar.HOUR_OF_DAY)
            val minute = calendar.get(Calendar.MINUTE)
            val dpd = TimePickerDialog(
                requireContext(), TimePickerDialog.OnTimeSetListener()
                { view, hour_of_day, minute ->

                    val calendar = Calendar.getInstance()
                    calendar.time = _binding.viewModel?.editableTodoItem?.value?.taskDate
                    calendar.set(Calendar.HOUR_OF_DAY, hour_of_day)
                    calendar.set(Calendar.MINUTE, minute)
//                    (_binding.item as ToDo).taskDate.time = calendar.timeInMillis
                    addEditToDoViewModel?.editableTodoItem.value =
                        addEditToDoViewModel?.editableTodoItem?.value?.let {
                            ToDo(
                                it.uuid,
                                Converter.ToDDMMMYYYYHHMM(calendar.time),
                                it.taskName,
                                it.taskCreater,
                                calendar.time
                            )
                        }
                },
                hour,
                minute, true

            ).apply {
                show()
            }

        } else if (v == _binding.fabAddTask) {
            addEditToDoViewModel?.editableTodoItem.value?.taskName =
                _binding.taskNameTextView.text.toString()
            addEditToDoViewModel?.editableTodoItem.value?.taskCreater =
                _binding.taskCreateTextView.text.toString()
            validateCallback.validate(addEditToDoViewModel?.editableTodoItem.value as ToDo)
        }
    }
}