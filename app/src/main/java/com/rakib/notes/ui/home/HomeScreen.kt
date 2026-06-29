package com.rakib.notes.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.rakib.notes.data.AppDatabase
import com.rakib.notes.data.Note
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen() {
    val context = LocalContext.current
    val db = AppDatabase.getDatabase(context)
    val notes by db.noteDao().getAllNotes().collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    
    var showAddDialog by remember { mutableStateOf(false) }
    var noteTitle by remember { mutableStateOf("") }
    var noteContent by remember { mutableStateOf("") }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Rakib Notes") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add Note")
            }
        }
    ) { padding ->
        LazyColumn(modifier = Modifier.padding(padding).fillMaxSize()) {
            items(notes) { note ->
                NoteItem(note) {
                    scope.launch { db.noteDao().deleteNote(note) }
                }
            }
        }

        if (showAddDialog) {
            AlertDialog(
                onDismissRequest = { showAddDialog = false },
                title = { Text("New Note") },
                text = {
                    Column {
                        TextField(value = noteTitle, onValueChange = { noteTitle = it }, label = { Text("Title") })
                        Spacer(modifier = Modifier.height(8.dp))
                        TextField(value = noteContent, onValueChange = { noteContent = it }, label = { Text("Content") })
                    }
                },
                confirmButton = {
                    Button(onClick = {
                        scope.launch {
                            db.noteDao().insertNote(Note(title = noteTitle, content = noteContent))
                            showAddDialog = false
                            noteTitle = ""
                            noteContent = ""
                        }
                    }) { Text("Save") }
                }
            )
        }
    }
}

@Composable
fun NoteItem(note: Note, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(note.title, style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(4.dp))
            Text(note.content)
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = onDelete) { Text("Delete") }
        }
    }
}
