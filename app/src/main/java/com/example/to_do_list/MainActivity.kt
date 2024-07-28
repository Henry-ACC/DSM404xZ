package com.example.to_do_list



import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var editTextTask: EditText
    private lateinit var buttonAddTask: Button
    private lateinit var listViewTasks: ListView
    private lateinit var taskAdapter: TaskAdapter
    private val tasksList = mutableListOf<String>()
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextTask = findViewById(R.id.editTextTask)
        buttonAddTask = findViewById(R.id.buttonAddTask)
        listViewTasks = findViewById(R.id.listViewTasks)

        // Cargar tareas guardadas
        sharedPreferences = getSharedPreferences("TASKS_PREFS", Context.MODE_PRIVATE)
        val savedTasks = sharedPreferences.getStringSet("TASKS", null)
        savedTasks?.let { tasksList.addAll(it) }

        taskAdapter = TaskAdapter(this, tasksList)
        listViewTasks.adapter = taskAdapter

        buttonAddTask.setOnClickListener {
            val task = editTextTask.text.toString()
            if (task.isNotEmpty()) {
                tasksList.add(task)
                taskAdapter.notifyDataSetChanged()
                editTextTask.text.clear()

                // Guardar tareas
                saveTasks()
            }
        }
    }

    fun saveTasks() {
        val editor = sharedPreferences.edit()
        editor.putStringSet("TASKS", tasksList.toSet())
        editor.apply()
    }
}
