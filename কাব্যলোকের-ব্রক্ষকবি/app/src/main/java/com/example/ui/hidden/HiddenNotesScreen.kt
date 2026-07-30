package com.example.ui.hidden

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.database.PoemNote
import com.example.ui.components.GlassCard
import com.example.ui.home.HomeViewModel
import com.example.ui.home.PoemNoteItemCard
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SoftLavender
import java.security.MessageDigest

fun hashPassword(password: String): String {
    val bytes = MessageDigest.getInstance("SHA-256").digest(password.toByteArray())
    return bytes.joinToString("") { "%02x".format(it) }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HiddenNotesScreen(
    viewModel: HomeViewModel,
    onNoteClick: (Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val isDark = viewModel.isDarkMode.collectAsState().value ?: isSystemInDarkTheme()

    val hiddenNotes by viewModel.hiddenNotes.collectAsState()
    val activeNotes by viewModel.poemNotes.collectAsState()
    val storedPasswordHash by viewModel.hiddenPasswordHash.collectAsState()

    var isAuthenticated by remember { mutableStateOf(false) }
    var passwordInput by remember { mutableStateOf("") }
    var showPasswordDialog by remember { mutableStateOf(true) }
    var showSelectPickerSheet by remember { mutableStateOf(false) }

    val selectedToHideIds = remember { mutableStateListOf<Int>() }

    LaunchedEffect(storedPasswordHash) {
        if (storedPasswordHash.isNullOrBlank()) {
            // First time setup
            showPasswordDialog = true
        }
    }

    val bgBrush = if (isDark) {
        Brush.radialGradient(
            colors = listOf(Color(0xFF161C26), DarkBackground, Color(0xFF080B10)),
            center = Offset(300f, 400f),
            radius = 1200f
        )
    } else {
        Brush.verticalGradient(
            colors = listOf(
                MaterialTheme.colorScheme.background,
                MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
            )
        )
    }

    Scaffold(
        topBar = {
            GlassCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = GoldPrimary
                        )
                    }

                    Text(
                        text = "হাইডেন নোটস",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = GoldPrimary
                        )
                    )

                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = GoldPrimary,
                        modifier = Modifier.padding(end = 12.dp)
                    )
                }
            }
        },
        floatingActionButton = {
            if (isAuthenticated) {
                FloatingActionButton(
                    onClick = {
                        selectedToHideIds.clear()
                        showSelectPickerSheet = true
                    },
                    containerColor = GoldPrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Hidden Notes",
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(bgBrush)
                .padding(innerPadding)
        ) {
            if (isAuthenticated) {
                if (hiddenNotes.isEmpty()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "No Hidden Notes",
                            tint = GoldPrimary,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "কোনো হাইডেন নোট নেই",
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = GoldPrimary
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "+ বাটনে চাপ দিয়ে হোম স্ক্রিন থেকে নোট হাইড করুন",
                            style = TextStyle(fontSize = 14.sp, color = SoftLavender)
                        )
                    }
                } else {
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(hiddenNotes, key = { it.id }) { note ->
                            Box(modifier = Modifier.fillMaxWidth()) {
                                PoemNoteItemCard(
                                    note = note,
                                    isSelected = false,
                                    onClick = { onNoteClick(note.id) },
                                    onLongClick = {},
                                    onTogglePin = { viewModel.togglePin(note) },
                                    onToggleLock = { viewModel.toggleLock(note) },
                                    onDelete = { viewModel.moveToTrash(note.id) }
                                )

                                // Unhide Icon overlay
                                IconButton(
                                    onClick = {
                                        viewModel.unhideNote(note.id)
                                        Toast.makeText(context, "আনহাইড করা হয়েছে", Toast.LENGTH_SHORT).show()
                                    },
                                    modifier = Modifier
                                        .align(Alignment.TopEnd)
                                        .padding(8.dp)
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(GoldPrimary.copy(alpha = 0.9f))
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.LockOpen,
                                        contentDescription = "Unhide",
                                        tint = MaterialTheme.colorScheme.onPrimary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    // Password Prompt / Setup Dialog
    if (showPasswordDialog && !isAuthenticated) {
        val isFirstTime = storedPasswordHash.isNullOrBlank()

        Dialog(onDismissRequest = { onBack() }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Lock",
                        tint = GoldPrimary,
                        modifier = Modifier.size(48.dp)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = if (isFirstTime) "হাইডেন নোটস পাসওয়ার্ড সেট করুন" else "পাসওয়ার্ড দিন",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = GoldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = passwordInput,
                        onValueChange = { passwordInput = it },
                        placeholder = { Text(if (isFirstTime) "নতুন পাসওয়ার্ড লিখুন" else "পাসওয়ার্ড প্রবেশ করুন") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                                .clickable { onBack() }
                                .padding(horizontal = 16.dp, vertical = 10.dp)
                        ) {
                            Text("বাতিল", style = TextStyle(color = MaterialTheme.colorScheme.onSurfaceVariant))
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldPrimary)
                                .clickable {
                                    if (passwordInput.isNotBlank()) {
                                        val inputHash = hashPassword(passwordInput.trim())
                                        if (isFirstTime) {
                                            viewModel.setHiddenNotesPasswordHash(inputHash)
                                            isAuthenticated = true
                                            showPasswordDialog = false
                                            Toast.makeText(context, "পাসওয়ার্ড সেট হয়েছে", Toast.LENGTH_SHORT).show()
                                        } else {
                                            if (inputHash == storedPasswordHash) {
                                                isAuthenticated = true
                                                showPasswordDialog = false
                                            } else {
                                                Toast.makeText(context, "ভুল পাসওয়ার্ড!", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    }
                                }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "প্রবেশ করুন",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }
        }
    }

    // Multi-Select Picker Dialog to Choose Notes to Hide from Home
    if (showSelectPickerSheet) {
        Dialog(onDismissRequest = { showSelectPickerSheet = false }) {
            Surface(
                shape = RoundedCornerShape(24.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp,
                modifier = Modifier.padding(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "হাইড করার জন্য নোট সিলেক্ট করুন",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = GoldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    if (activeNotes.isEmpty()) {
                        Text(
                            text = "হাইড করার মতো কোনো অ্যাক্টিভ নোট নেই",
                            style = TextStyle(fontSize = 14.sp, color = SoftLavender),
                            modifier = Modifier.padding(vertical = 24.dp)
                        )
                    } else {
                        LazyColumn(
                            modifier = Modifier.height(300.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(activeNotes, key = { it.id }) { note ->
                                val isChecked = selectedToHideIds.contains(note.id)
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(
                                            if (isChecked) GoldPrimary.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                                        )
                                        .clickable {
                                            if (isChecked) selectedToHideIds.remove(note.id)
                                            else selectedToHideIds.add(note.id)
                                        }
                                        .padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Checkbox(
                                        checked = isChecked,
                                        onCheckedChange = {
                                            if (it == true) selectedToHideIds.add(note.id)
                                            else selectedToHideIds.remove(note.id)
                                        },
                                        colors = CheckboxDefaults.colors(checkedColor = GoldPrimary)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = note.title.ifBlank { "শিরোনামহীন" },
                                        style = TextStyle(
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Medium,
                                            fontSize = 15.sp,
                                            color = MaterialTheme.colorScheme.onSurface
                                        ),
                                        modifier = Modifier.weight(1f)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldPrimary)
                                .clickable {
                                    if (selectedToHideIds.isNotEmpty()) {
                                        viewModel.hideNotes(selectedToHideIds.toList())
                                        Toast.makeText(context, "নোট হাইড করা হয়েছে", Toast.LENGTH_SHORT).show()
                                    }
                                    showSelectPickerSheet = false
                                }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "হাইড করুন",
                                style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onPrimary
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

