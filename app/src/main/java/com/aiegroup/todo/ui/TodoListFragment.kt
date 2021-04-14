package com.aiegroup.todo.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.aiegroup.todo.databinding.FragmentTodoListBinding
import com.aiegroup.todo.models.ToDo
import com.aiegroup.todo.viewmodels.TodoViewModel
import com.aiegroup.todo.viewmodels.ViewModelProviderFactory
import java.util.*


/**
 * ToDoList Fragment for showing the list of task list.
 */
class TodoListFragment : Fragment() {

    /**
     * One way to delay creation of the viewModel until an appropriate lifecycle method is to use
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
     * RecyclerView Adapter for showing list of todos items
     */
    private var rowItemAdapter: RowItemAdapter? = null

    private var _binding: FragmentTodoListBinding? = null

    fun onSearchClick(v: View?) {
        Toast.makeText(activity, "X", Toast.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentTodoListBinding.inflate(inflater, container, false)


        var view = _binding!!.root
        _binding!!.fabAddTask.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                var todo =
                    ToDo(UUID.randomUUID().toString(), "New Task", "Sushil Kumar", "Test Task ");
                viewModel.createToDo(todo);


            }

        });
        rowItemAdapter = RowItemAdapter(TaskItemClick {
            // When todos item is clicked this block or lambda will be called by DevByteAdapter
            // context is not around, we can safely discard this click since the Fragment is no
            // longer on the screen

        })

        _binding!!.recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rowItemAdapter
        }
        //
        return view
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
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /**
     * Click listener for Row Items. By giving the block a name it helps a reader understand what it does.
     *
     */
    class TaskItemClick(val block: (ToDo) -> Unit) {
        /**
         * Called when a item is clicked
         *
         * @param todo item
         */
        fun onClick(video: ToDo) = block(video)
    }

}