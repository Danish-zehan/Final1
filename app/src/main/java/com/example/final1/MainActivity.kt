package com.example.final1

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val editText: EditText = findViewById(R.id.editText)
        val listView: ListView = findViewById(R.id.listView)
        val progressBar: ProgressBar = findViewById(R.id.progressBar)
        val button: Button = findViewById(R.id.button)

        val items = listOf("Item 1", "Item 2", "Item 3")
        val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, items)
        listView.adapter = adapter

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            val selectedItem = items[position]
            Snackbar.make(listView, "Selected: $selectedItem", Snackbar.LENGTH_SHORT).show()
        }

        button.setOnClickListener {
            val inputText = editText.text.toString()
            if (inputText.isNotEmpty()) {
                progressBar.visibility = View.VISIBLE
                progressBar.postDelayed({
                    progressBar.visibility = View.GONE
                    Toast.makeText(this, "Task Completed with input: $inputText", Toast.LENGTH_SHORT).show()
                }, 2000)
            } else {
                Snackbar.make(button, "Please enter text", Snackbar.LENGTH_SHORT).show()
            }
        }
    }
}