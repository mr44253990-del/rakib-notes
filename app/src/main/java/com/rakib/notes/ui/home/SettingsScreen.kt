package com.rakib.notes.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Settings") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.Default.ArrowBack, null) }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Backup & Sync", style = MaterialTheme.typography.titleMedium)
            ListItem(
                headlineContent = { Text("Google Drive Sync") },
                supportingContent = { Text("Automatically backup notes to cloud") },
                trailingContent = { Switch(checked = true, onCheckedChange = {}) },
                leadingContent = { Icon(Icons.Default.CloudUpload, null) }
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Appearance", style = MaterialTheme.typography.titleMedium)
            ListItem(
                headlineContent = { Text("Dark Mode") },
                trailingContent = { Switch(checked = false, onCheckedChange = {}) },
                leadingContent = { Icon(Icons.Default.DarkMode, null) }
            )
            HorizontalDivider()
            Spacer(modifier = Modifier.height(16.dp))
            Text("About Developer", style = MaterialTheme.typography.titleMedium)
            ListItem(
                headlineContent = { Text("Rakibul Islam") },
                supportingContent = { Text("Khulna, Bangladesh") },
                leadingContent = { Icon(Icons.Default.Info, null) }
            )
        }
    }
}
