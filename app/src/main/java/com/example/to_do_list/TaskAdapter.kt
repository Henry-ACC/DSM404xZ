package com.example.to_do_list




import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog

class TaskAdapter(private val context: Context, private val tasks: MutableList<String>) : BaseAdapter() {

    override fun getCount(): Int = tasks.size

    override fun getItem(position: Int): String = tasks[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View
        val viewHolder: ViewHolder

        if (convertView == null) {
            view = LayoutInflater.from(context).inflate(R.layout.task_item, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            view = convertView
            viewHolder = view.tag as ViewHolder
        }

        val task = getItem(position)
        viewHolder.textViewTask.text = task

        viewHolder.checkBoxTask.setOnCheckedChangeListener(null)
        viewHolder.checkBoxTask.isChecked = false
        viewHolder.checkBoxTask.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                viewHolder.textViewTask.paintFlags = viewHolder.textViewTask.paintFlags or android.graphics.Paint.STRIKE_THRU_TEXT_FLAG
            } else {
                viewHolder.textViewTask.paintFlags = viewHolder.textViewTask.paintFlags and android.graphics.Paint.STRIKE_THRU_TEXT_FLAG.inv()
            }
        }

        viewHolder.buttonDelete.setOnClickListener {
            tasks.removeAt(position)
            notifyDataSetChanged()
        }

        viewHolder.textViewTask.setOnClickListener {
            showEditTaskDialog(position)
        }

        return view
    }

    private fun showEditTaskDialog(position: Int) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Editar Tarea")

        val input = EditText(context)
        input.setText(tasks[position])
        builder.setView(input)

        builder.setPositiveButton("OK") { dialog, _ ->
            val editedTask = input.text.toString()
            if (editedTask.isNotEmpty()) {
                tasks[position] = editedTask
                notifyDataSetChanged()
                (context as MainActivity).saveTasks()
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("Cancelar") { dialog, _ -> dialog.cancel() }

        builder.show()
    }

    private class ViewHolder(view: View) {
        val checkBoxTask: CheckBox = view.findViewById(R.id.checkBoxTask)
        val textViewTask: TextView = view.findViewById(R.id.textViewTask)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
    }
}
