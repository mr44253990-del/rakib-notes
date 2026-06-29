package com.rakib.notes.ui.notes

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.rakib.notes.data.AppDatabase
import com.rakib.notes.data.Note
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class NoteViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).noteDao()
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery = _searchQuery.asStateFlow()

    val notes: StateFlow<List<Note>> = _searchQuery
        .debounce(300)
        .flatMapLatest { query ->
            if (query.isEmpty()) dao.getAllActiveNotes()
            else dao.searchNotes(query)
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun upsertNote(note: Note) = viewModelScope.launch {
        dao.insertNote(note)
    }

    fun deleteNote(note: Note) = viewModelScope.launch {
        dao.deleteNote(note)
    }
    
    fun togglePin(note: Note) = viewModelScope.launch {
        dao.insertNote(note.copy(isPinned = !note.isPinned))
    }
}
