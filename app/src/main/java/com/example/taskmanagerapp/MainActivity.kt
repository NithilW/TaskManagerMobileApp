package com.example.taskmanagerapp

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.inputmethod.EditorInfo
import android.widget.Button
import android.widget.ImageView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.taskmanagerapp.adapters.TaskRecyclerViewAdapter
import com.example.taskmanagerapp.databinding.ActivityMainBinding
import com.example.taskmanagerapp.models.Task
import com.example.taskmanagerapp.utils.Status
import com.example.taskmanagerapp.utils.clearEditText
import com.example.taskmanagerapp.utils.hideKeyBoard
import com.example.taskmanagerapp.utils.longToastShow
import com.example.taskmanagerapp.utils.setupDialog
import com.example.taskmanagerapp.utils.validateEditText
import com.example.taskmanagerapp.viewmodels.TaskViewModel
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date
import java.util.UUID


class MainActivity : AppCompatActivity() {


    private val mainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val addTaskDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply{
            setupDialog(R.layout.add_task_dialog)
        }
    }
    private val updateTaskDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply{
            setupDialog(R.layout.update_task_dialog)
        }
    }
    private val loadingDialog : Dialog by lazy {
        Dialog(this,R.style.DialogCustomTheme).apply{
            setupDialog(R.layout.loading_dialog)
        }
    }

    private val taskViewModel:TaskViewModel by lazy{
        ViewModelProvider(this)[TaskViewModel::class.java]
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(mainBinding.root)


        val addCloseBtn = addTaskDialog.findViewById<ImageView>(R.id.closeImg)
        val updateCloseBtn = updateTaskDialog.findViewById<ImageView>(R.id.closeImg)

        addCloseBtn.setOnClickListener{addTaskDialog.dismiss()}
        updateCloseBtn.setOnClickListener{updateTaskDialog.dismiss()}

        val addEditTitle = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val addEditTitleL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        addEditTitle.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0:CharSequence?,p1:Int,p2:Int,p3:Int){}
            override fun onTextChanged(p0:CharSequence?,p1:Int,p2:Int,p3:Int){}
            override fun afterTextChanged(s:Editable){
                validateEditText(addEditTitle,addEditTitleL)
            }
        })

        val addEDDesc = addTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val addEDDescL = addTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        addEDDesc.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0:CharSequence?,p1:Int,p2:Int,p3:Int){}
            override fun onTextChanged(p0:CharSequence?,p1:Int,p2:Int,p3:Int){}
            override fun afterTextChanged(s:Editable){
                validateEditText(addEDDesc,addEDDescL)
            }
        })

        val updateEditTitle = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskTitle)
        val updateEditTitleL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskTitleL)

        addEditTitle.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0:CharSequence?,p1:Int,p2:Int,p3:Int){}
            override fun onTextChanged(p0:CharSequence?,p1:Int,p2:Int,p3:Int){}
            override fun afterTextChanged(s:Editable){
                validateEditText(updateEditTitle,updateEditTitleL)
            }
        })

        val updateEDDesc = updateTaskDialog.findViewById<TextInputEditText>(R.id.edTaskDesc)
        val updateEDDescL = updateTaskDialog.findViewById<TextInputLayout>(R.id.edTaskDescL)

        addEDDesc.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(p0:CharSequence?,p1:Int,p2:Int,p3:Int){}
            override fun onTextChanged(p0:CharSequence?,p1:Int,p2:Int,p3:Int){}
            override fun afterTextChanged(s:Editable){
                validateEditText(updateEDDesc,updateEDDescL)
            }
        })

        mainBinding.addTaskFABtn.setOnClickListener {
            clearEditText(addEditTitle,addEditTitleL)
            clearEditText(addEDDesc,addEDDescL)
            addTaskDialog.show()
        }

        val saveTaskBtn = addTaskDialog.findViewById<Button>(R.id.saveTaskBtn)
        saveTaskBtn.setOnClickListener {
            if( validateEditText(addEditTitle,addEditTitleL) &&  validateEditText(addEDDesc,addEDDescL)){
                addTaskDialog.dismiss()
                val newTask = Task(
                    UUID.randomUUID().toString(),
                    addEditTitle.text.toString().trim(),
                    addEDDesc.text.toString().trim(),
                    Date()
                )
                taskViewModel.inserTask(newTask).observe(this){
                    when(it.status){
                        Status.LOADING ->{
                            loadingDialog.show()
                        }
                        Status.SUCCESS->{
                            loadingDialog.dismiss()
                            if(it.data?.toInt() != -1){
                                longToastShow("Task Added")
                            }
                        }
                        Status.ERROR ->{
                            loadingDialog.dismiss()
                            it.message?.let{it1 -> longToastShow(it1)}
                        }
                    }

                }
            }
        }

        val updateTaskBtn = updateTaskDialog.findViewById<Button>(R.id.updateTaskBtn)


        val taskRecyclerViewAdapter = TaskRecyclerViewAdapter{type,position, task ->
            if(type == "delete"){
            taskViewModel.deleteTask(task).observe(this){
                when(it.status){
                    Status.LOADING ->{
                        loadingDialog.show()
                    }
                    Status.SUCCESS->{
                        loadingDialog.dismiss()
                        if(it.data != -1){
                            longToastShow("Task deleted")
                        }
                    }
                    Status.ERROR ->{
                        loadingDialog.dismiss()
                        it.message?.let{it1 -> longToastShow(it1)}
                    }
                }
            }
            }else if(type == "update"){
                updateEditTitle.setText(task.title)
                updateEDDesc.setText(task.description)
                updateTaskBtn.setOnClickListener {
                    if( validateEditText(updateEditTitle,updateEditTitleL) &&  validateEditText(updateEDDesc,updateEDDescL)){
                        val updateTask = Task(
                            task.id,
                            updateEditTitle.text.toString().trim(),
                            updateEDDesc.text.toString().trim(),
                            Date()
                        )
                        updateTaskDialog.dismiss()
                        loadingDialog.show()
                        taskViewModel.updateTask(updateTask).observe(this){
                            when(it.status){
                                Status.LOADING ->{
                                    loadingDialog.show()
                                }
                                Status.SUCCESS->{
                                    loadingDialog.dismiss()
                                    if(it.data != -1){
                                        longToastShow("Task updated")
                                    }
                                }
                                Status.ERROR ->{
                                    loadingDialog.dismiss()
                                    it.message?.let{it1 -> longToastShow(it1)}
                                }
                            }
                        }
                    }
                }
                updateTaskDialog.show()
            }
        }
        mainBinding.taskRV.adapter = taskRecyclerViewAdapter
        taskRecyclerViewAdapter.registerAdapterDataObserver(object :
            RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                super.onItemRangeInserted(positionStart, itemCount)
              mainBinding.taskRV.smoothScrollToPosition(positionStart)
            }
        })
        callGetTaskList(taskRecyclerViewAdapter)

        callSearch()

    }
    private fun callSearch(){
        mainBinding.edSearch.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(query: Editable) {
                if(query.toString().isNotEmpty()){
                    taskViewModel.searchTaskList(query.toString())
                }
                else{
                    taskViewModel.getTaskList()
                }
            }

        })
        mainBinding.edSearch.setOnEditorActionListener{v,actionId,event ->
            if(actionId == EditorInfo.IME_ACTION_SEARCH) {
                hideKeyBoard(v)
                return@setOnEditorActionListener true
            }
            false
        }
    }

    private fun callGetTaskList(taskRecyclerViewAdapter:TaskRecyclerViewAdapter){
        CoroutineScope(Dispatchers.Main).launch {
            taskViewModel.getTaskList().collect {
                when (it.status) {
                    Status.LOADING -> {
                        loadingDialog.show()
                    }

                    Status.SUCCESS -> {
                        it.data?.collect {taskList->
                            loadingDialog.dismiss()
                            taskRecyclerViewAdapter.addAllTask(taskList)
                        }
                    }

                    Status.ERROR -> {
                        loadingDialog.dismiss()
                        it.message?.let { it1 -> longToastShow(it1) }
                    }
                }

            }
        }
    }
}