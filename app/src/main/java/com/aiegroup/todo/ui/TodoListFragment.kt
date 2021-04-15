package com.aiegroup.todo.ui

import android.content.Intent
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import com.aiegroup.todo.R
import com.aiegroup.todo.databinding.FragmentTodoListBinding
import com.aiegroup.todo.models.ToDo
import com.aiegroup.todo.service.NotificationForegroundService
import com.aiegroup.todo.viewmodels.TodoViewModel
import com.aiegroup.todo.viewmodels.ViewModelProviderFactory
import java.util.*


/**
 * ToDoList Fragment for showing the list of task list.
 */
class TodoListFragment : Fragment() {

    /**
     * One way to delay creation of the  TodoViewModel until an appropriate lifecycle method is to use
     * lazy. This requires that viewModel not be referenced before onActivityCreated, which we
     * do in this Fragment.
     */
    private val viewModel: TodoViewModel by lazy {
        val viewModelProviderFactory =
            ViewModelProviderFactory(
                this.requireContext()
            )

        ViewModelProvider(
            this,
            viewModelProviderFactory
        ).get(TodoViewModel::class.java)
    }

    /**
     * RecyclerView Adapter for showing list of task items
     */
    private var rowItemAdapter: RowItemAdapter? = null

    private var _binding: FragmentTodoListBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)
        var view = _binding!!.root
        _binding!!.fabAddTask.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                viewModel.initEditableToDoItem();
                openBottomSheet(viewModel.editableTodoItem.value, false)
            }
        });
        val dividerItemDecoration = DividerItemDecoration(
            _binding?.recycleView?.getContext(),
            VERTICAL
        )
        _binding?.recycleView?.addItemDecoration(dividerItemDecoration)

        rowItemAdapter = RowItemAdapter(object : TaskItemClick {
            override fun edit(toDoItem: ToDo) {
                openBottomSheet(toDoItem, true)
            }

            override fun delete(toDoItem: ToDo) {
                viewModel.deleteToDo(toDoItem)
            }
        })

        _binding!!.recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rowItemAdapter
        }
        // start forground service for showing network status
        val intent = Intent(context, NotificationForegroundService::class.java)
        activity?.startService(intent)
        //
        return view
    }
    private var mLastClickTime: Long = 0

    lateinit var modalbottomSheetFragment: AddEditToDoItemBottomSheetFragment
    fun openBottomSheet(toDo: ToDo?, isEditMode: Boolean) {
        // check for last click event to avoid open multiple bottom sheet dialog immediately
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        modalbottomSheetFragment = AddEditToDoItemBottomSheetFragment.newInstance(
            toDo,
            isEditMode,
            object : ValidateCallback {
                override fun validate(toDoItem: ToDo) {
                    // check for task name and task create empty value
                    if (toDoItem?.taskName?.isEmpty()!! || toDoItem?.taskCreater.isEmpty()!!) {

                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(), resources.getString(
                                    R.string.error_empty_value
                                ), Toast.LENGTH_SHORT
                            ).show()

                        }

                    }
                    // check for in create task past time in not allowed
                    else if (!isEditMode && isPastTime(toDoItem)) {

                        activity?.runOnUiThread {
                            Toast.makeText(
                                requireContext(), resources.getString(
                                    R.string.error_past_time
                                ), Toast.LENGTH_SHORT
                            ).show()

                        }

                    } else {

                        viewModel.insertToDo(toDoItem, object : TransactionCallback {
                            override fun failure() {
                                activity?.runOnUiThread {

                                    Toast.makeText(
                                        requireContext(), resources.getString(
                                            R.string.error
                                        ), Toast.LENGTH_SHORT
                                    ).show()

                                }

                            }

                            override fun success() {
                                modalbottomSheetFragment.dismiss()
                            }
                        })
                    }
                }

            })
        activity?.let { it1 ->
            // show bottom sheet dialog for create and eoit task
            modalbottomSheetFragment.show(
                it1.supportFragmentManager,
                modalbottomSheetFragment.tag
            )

        }

    }

    /**
     * Called immediately after onCreateView() has returned, and fragment's
     * view hierarchy has been created. It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.items?.observe(viewLifecycleOwner, Observer<List<ToDo>> { list ->
            list?.apply {
                rowItemAdapter?.items = list
                if (list.size <= 0) {
                    _binding?.emptyLayout?.visibility = VISIBLE
                } else {
                    _binding?.emptyLayout?.visibility = GONE
                }
            }
        })
    }

    /**
      compare current time with selected time by user
     */
    fun isPastTime(toDoItem: ToDo): Boolean {
        val c1 = Calendar.getInstance()
        c1.timeInMillis = System.currentTimeMillis();
        c1.set(Calendar.SECOND, 0)
        val c2 = Calendar.getInstance()
        c2.timeInMillis = toDoItem.taskDate.time
        c1.set(Calendar.SECOND, 0)
        return c1.time >= c2.time
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     interface for edit and delete task
     */
    interface TaskItemClick {
        fun edit(toDoItem: ToDo);
        fun delete(toDoItem: ToDo);
    }

    /**
    interface for valid task values
     */
    interface ValidateCallback {
        fun validate(toDoItem: ToDo);
    }
    /**
    interface for insert task item in room database
     */
    interface TransactionCallback {
        fun success();
        fun failure();
    }

}