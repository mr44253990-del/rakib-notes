package com.rakib.notes.ui.splash

import android.Manifest
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.permissions.*

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashScreen(onFinished: () -> Unit) {
    var currentPage by remember { mutableIntStateOf(1) }
    
    val permissionsToRequest = mutableListOf(
        Manifest.permission.INTERNET
    ).apply {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            add(Manifest.permission.POST_NOTIFICATIONS)
        }
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2) {
            add(Manifest.permission.READ_EXTERNAL_STORAGE)
            add(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        }
    }

    val permissionState = rememberMultiplePermissionsState(permissionsToRequest)

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        when (currentPage) {
            1 -> SplashPage1 { currentPage = 2 }
            2 -> SplashPage2 { currentPage = 3 }
            3 -> SplashPage3(permissionState) { 
                if (permissionState.allPermissionsGranted) {
                    onFinished()
                } else {
                    permissionState.launchMultiplePermissionRequest()
                }
            }
        }
    }
}

@Composable
fun SplashPage1(onNext: () -> Unit) {
    Text("Welcome to Rakib Notes", fontSize = 28.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Your personal note-taking and reminder assistant.", textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(32.dp))
    Button(onClick = onNext) { Text("Next") }
}

@Composable
fun SplashPage2(onNext: () -> Unit) {
    Text("Developer Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    Text("Name: Rakibul Islam", fontSize = 18.sp)
    Text("Address: Khulna", fontSize = 18.sp)
    Text("Email: mr4425390@gmail.com", fontSize = 18.sp)
    Spacer(modifier = Modifier.height(16.dp))
    Text("I'm a professional application developer.", textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(32.dp))
    Button(onClick = onNext) { Text("Next") }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SplashPage3(permissionState: MultiplePermissionsState, onComplete: () -> Unit) {
    Text("Permissions Required", fontSize = 24.sp, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))
    Text("To work perfectly, we need access to storage and notifications.", textAlign = TextAlign.Center)
    Spacer(modifier = Modifier.height(32.dp))
    
    if (permissionState.allPermissionsGranted) {
        Text("All permissions granted!", color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = onComplete) { Text("Get Started") }
    } else {
        Button(onClick = onComplete) { Text("Grant Permissions") }
    }
}
