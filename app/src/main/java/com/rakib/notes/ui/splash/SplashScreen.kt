package com.rakib.notes.ui.splash

import android.Manifest
import android.os.Build
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(MaterialTheme.colorScheme.primaryContainer, MaterialTheme.colorScheme.surface)
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AnimatedContent(
                targetState = currentPage,
                transitionSpec = {
                    slideInHorizontally { it } + fadeIn() togetherWith slideOutHorizontally { -it } + fadeOut()
                },
                label = "SplashContent"
            ) { page ->
                when (page) {
                    1 -> SplashPage(
                        title = "Rakib Notes",
                        description = "Your professional companion for capturing ideas and organizing your life.",
                        icon = Icons.Default.EditNote,
                        onNext = { currentPage = 2 }
                    )
                    2 -> SplashPage(
                        title = "Meet the Developer",
                        description = "Developed by Rakibul Islam\nKhulna, Bangladesh\nmr4425390@gmail.com\nProfessional App Developer",
                        icon = Icons.Default.Person,
                        onNext = { currentPage = 3 }
                    )
                    3 -> SplashPage(
                        title = "Get Started",
                        description = "Grant necessary permissions to unlock the full potential of secure note-taking and cloud sync.",
                        icon = Icons.Default.Security,
                        buttonText = if (permissionState.allPermissionsGranted) "Launch App" else "Grant Permissions",
                        onNext = {
                            if (permissionState.allPermissionsGranted) {
                                onFinished()
                            } else {
                                permissionState.launchMultiplePermissionRequest()
                            }
                        }
                    )
                }
            }
        }
        
        // Page Indicators
        Row(
            modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 64.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            repeat(3) { index ->
                val active = index + 1 == currentPage
                Box(
                    modifier = Modifier
                        .size(if (active) 12.dp else 8.dp)
                        .clip(CircleShape)
                        .background(if (active) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant)
                )
            }
        }
    }
}

@Composable
fun SplashPage(
    title: String,
    description: String,
    icon: ImageVector,
    buttonText: String = "Next",
    onNext: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = description,
            textAlign = TextAlign.Center,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(48.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Text(buttonText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }
    }
}
