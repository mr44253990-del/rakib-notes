package com.rakib.notes.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "notes")
data class Note(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val title: String,
    val content: String,
    val timestamp: Long = System.currentTimeMillis(),
    val reminderTime: Long? = null,
    val category: String = "General",
    val color: Int = 0xFFFFFFFF.toInt(),
    val isPinned: Boolean = false,
    val isArchived: Boolean = false
)
