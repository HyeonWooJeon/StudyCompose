package com.example.project2


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp


class TodoActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // A surface container using the 'background' color from the theme
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                TopLevel()
            }

        }
    }

    @Composable
    fun TopLevel() {
        val (text, setText) = remember { mutableStateOf("") }
        val toDoList = remember { mutableStateListOf<TodoData>() }

        val onSubmit: (String) -> Unit = { text ->
            val key = (toDoList.lastOrNull()?.key ?: 0) + 1
            toDoList.add(TodoData(key, text))
            setText("")
        }
        Column(modifier = Modifier.fillMaxSize()) {
            TodoInput(text, setText, onSubmit)
        }
        LazyColumn( modifier = Modifier.fillMaxSize()){
            items(toDoList, key = { it.key }){ toDoData ->
                ToDo(todoData = toDoData)
            }
        }
    }


    @Composable
    fun TodoInput(text:String, onTextChange: (String)->Unit, onSubmit: (String)->Unit){
        Row(modifier = Modifier.fillMaxWidth()) {
            TextField(value = text, onValueChange = onTextChange, modifier = Modifier.padding(8.dp))
            Spacer(modifier =Modifier.size(8.dp))
            Button(onClick = { onSubmit(text) }) {
                Text(text = "입력 ")
            }
        }
    }

    @Composable
    fun ToDo(todoData: TodoData, onToggle: (key: Int, checked: Boolean) -> Unit = { _, _-> }) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(8.dp)
        ) {
            Checkbox(
                checked = todoData.done,
                onCheckedChange = { checked -> onToggle(todoData.key,checked)}
            )
            Text(
                text = todoData.text,
                modifier = Modifier.weight(1f)

            )
        }
    }

data class TodoData (
    val key: Int,
    val text: String,
    val done: Boolean = false
)

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
            TopLevel()
    }
}

