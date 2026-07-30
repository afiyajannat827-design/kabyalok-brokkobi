package com.example.ui.home

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.database.PoemNote
import com.example.ui.components.AsymmetricHamburgerIcon
import com.example.ui.components.DeleteConfirmationDialog
import com.example.ui.components.GlassCard
import com.example.ui.components.TiltedThumbtackIcon
import com.example.ui.components.glassSurface
import com.example.ui.drawer.AppDrawerContent
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.ThemePreset
import com.example.utils.formatBengaliDate
import com.example.utils.formatBengaliDateTime
import com.example.utils.toBengaliNumerals
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    onCreateNewClick: () -> Unit,
    onNoteClick: (Int) -> Unit,
    onNavigatePlaceholder: (String) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    val poemNotes by viewModel.poemNotes.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    val showThemeDialog by viewModel.showThemeDialog.collectAsState()
    val currentThemePreset by viewModel.currentThemePreset.collectAsState()
    val isDarkModePref by viewModel.isDarkMode.collectAsState()

    val selectedNoteIds by viewModel.selectedNoteIds.collectAsState()
    val isSelectionMode by viewModel.isSelectionMode.collectAsState()

    val systemInDark = isSystemInDarkTheme()
    val isDark = isDarkModePref ?: systemInDark

    var showDeleteConfirmDialog by remember { mutableStateOf(false) }
    var singleDeleteNoteId by remember { mutableStateOf<Int?>(null) }

    val bgBrush = if (isDark) {
        Brush.radialGradient(
            colors = listOf(
                Color(0xFF161C26),
                DarkBackground,
                Color(0xFF080B10)
            ),
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

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawerContent(
                currentRoute = "home",
                onMenuItemClick = { route ->
                    scope.launch { drawerState.close() }
                    if (route != "home") {
                        onNavigatePlaceholder(route)
                    }
                }
            )
        }
    ) {
        Scaffold(
            topBar = {
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        if (isSelectionMode) {
                            // Multi-Selection Mode Top Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    IconButton(onClick = { viewModel.clearSelection() }) {
                                        Icon(Icons.Default.Close, contentDescription = "Cancel", tint = GoldPrimary)
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${selectedNoteIds.size.toBengaliNumerals()} টি নির্বাচিত",
                                        style = TextStyle(
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 18.sp,
                                            color = GoldPrimary
                                        )
                                    )
                                }

                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    // Add to Group
                                    IconButton(onClick = {
                                        viewModel.setGroupSelectedNotes("পছন্দনীয়")
                                        Toast.makeText(context, "গ্রুপে যুক্ত করা হয়েছে", Toast.LENGTH_SHORT).show()
                                    }) {
                                        Icon(Icons.Default.Category, contentDescription = "Group", tint = GoldPrimary)
                                    }

                                    // Hide
                                    IconButton(onClick = {
                                        viewModel.hideSelectedNotes()
                                        Toast.makeText(context, "হাইড করা হয়েছে", Toast.LENGTH_SHORT).show()
                                    }) {
                                        Icon(Icons.Default.Lock, contentDescription = "Hide", tint = GoldPrimary)
                                    }

                                    // Delete
                                    IconButton(onClick = {
                                        singleDeleteNoteId = null
                                        showDeleteConfirmDialog = true
                                    }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(alpha = 0.8f))
                                    }
                                }
                            }
                        } else {
                            // Standard Top Bar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                IconButton(onClick = { scope.launch { drawerState.open() } }) {
                                    AsymmetricHamburgerIcon(tint = GoldPrimary)
                                }

                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = stringResource(R.string.app_name),
                                        style = TextStyle(
                                            fontFamily = FontFamily.Serif,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 20.sp,
                                            brush = Brush.horizontalGradient(
                                                listOf(GoldLight, GoldPrimary, GoldDark)
                                            )
                                        )
                                    )
                                    Text(
                                        text = stringResource(R.string.app_subtitle),
                                        style = TextStyle(
                                            fontFamily = FontFamily.Serif,
                                            fontStyle = FontStyle.Italic,
                                            fontSize = 11.sp,
                                            color = SoftLavender.copy(alpha = 0.85f),
                                            letterSpacing = 1.2.sp
                                        )
                                    )
                                }

                                 Spacer(modifier = Modifier.width(48.dp))
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // Info Bar
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Description,
                                    contentDescription = "Note Count",
                                    tint = SoftLavender,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${poemNotes.size.toBengaliNumerals()} টি নোট",
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SoftLavender
                                    )
                                )
                            }

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Schedule,
                                    contentDescription = "Date Time",
                                    tint = SoftLavender,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = formatBengaliDateTime(System.currentTimeMillis()),
                                    style = TextStyle(
                                        fontSize = 12.sp,
                                        color = SoftLavender
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Full-width Pill-shaped Search Bar ("নোট খুঁজুন...")
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { viewModel.updateSearchQuery(it) },
                            placeholder = {
                                Text(
                                    text = "নোট খুঁজুন...",
                                    style = TextStyle(
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                                    )
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = GoldPrimary
                                )
                            },
                            trailingIcon = {
                                if (searchQuery.isNotEmpty()) {
                                    IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Clear",
                                            tint = GoldPrimary
                                        )
                                    }
                                }
                            },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = CircleShape,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldPrimary,
                                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f)
                            )
                        )
                    }
                }
            },
            floatingActionButton = {
                FloatingActionButton(
                    onClick = onCreateNewClick,
                    containerColor = GoldPrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(12.dp),
                    modifier = Modifier.size(62.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "New Note",
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(bgBrush)
                    .padding(innerPadding)
            ) {
                // Ambient Background Orbs
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawCircle(
                        color = SoftLavender.copy(alpha = 0.08f),
                        radius = 350f,
                        center = Offset(100f, 150f)
                    )
                    drawCircle(
                        color = GoldPrimary.copy(alpha = 0.08f),
                        radius = 450f,
                        center = Offset(size.width - 100f, size.height - 200f)
                    )
                }

                if (poemNotes.isEmpty()) {
                    // Empty State
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .glassSurface(CircleShape, isDark),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.MenuBook,
                                contentDescription = "Empty",
                                tint = GoldPrimary,
                                modifier = Modifier.size(44.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "কোনো নোট নেই",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                color = GoldPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "+ বাটনে ট্যাপ করুন নতুন কবিতা বা নোট লিখতে",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 14.sp
                            )
                        )
                    }
                } else {
                    // Note Cards List
                    LazyColumn(
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        items(poemNotes, key = { it.id }) { note ->
                            val isSelected = selectedNoteIds.contains(note.id)

                            PoemNoteItemCard(
                                note = note,
                                isSelected = isSelected,
                                onClick = {
                                    if (isSelectionMode) {
                                        viewModel.toggleNoteSelection(note.id)
                                    } else {
                                        onNoteClick(note.id)
                                    }
                                },
                                onLongClick = {
                                    viewModel.toggleNoteSelection(note.id)
                                },
                                onTogglePin = { viewModel.togglePin(note) },
                                onToggleLock = { viewModel.toggleLock(note) },
                                onDelete = {
                                    singleDeleteNoteId = note.id
                                    showDeleteConfirmDialog = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    // Delete Confirmation Dialog
    if (showDeleteConfirmDialog) {
        val deleteCount = if (singleDeleteNoteId != null) 1 else selectedNoteIds.size
        DeleteConfirmationDialog(
            count = deleteCount,
            onConfirm = {
                if (singleDeleteNoteId != null) {
                    viewModel.moveToTrash(singleDeleteNoteId!!)
                    singleDeleteNoteId = null
                } else {
                    viewModel.deleteSelectedNotes()
                }
                showDeleteConfirmDialog = false
                Toast.makeText(context, "ট্র্যাশে পাঠানো হয়েছে", Toast.LENGTH_SHORT).show()
            },
            onDismiss = {
                showDeleteConfirmDialog = false
                singleDeleteNoteId = null
            }
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun PoemNoteItemCard(
    note: PoemNote,
    isSelected: Boolean,
    onClick: () -> Unit,
    onLongClick: () -> Unit,
    onTogglePin: () -> Unit,
    onToggleLock: () -> Unit,
    onDelete: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isSelected) 1.02f else 1f,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow)
    )

    val elevation = if (isSelected) 10.dp else 2.dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
    ) {
        GlassCard(
            modifier = Modifier
                .fillMaxWidth()
                .combinedClickable(
                    onClick = onClick,
                    onLongClick = onLongClick
                ),
            shape = RoundedCornerShape(20.dp),
            borderWidth = if (isSelected) 2.5.dp else 1.dp
        ) {
            Box(modifier = Modifier.fillMaxWidth()) {
                // Left Accent Stripe if LOCKED
                if (note.isLocked) {
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterStart)
                            .background(Color(0xFFE53935))
                    )
                }

                // Right Accent Stripe if PINNED
                if (note.isPinned) {
                    Box(
                        modifier = Modifier
                            .width(5.dp)
                            .fillMaxHeight()
                            .align(Alignment.CenterEnd)
                            .background(GoldPrimary)
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Title
                        Text(
                            text = if (note.title.isBlank()) "শিরোনামহীন" else note.title,
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                color = MaterialTheme.colorScheme.onSurface
                            ),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        // Rounded pill-shaped background containing 3 icons: pin, lock, delete
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                // Pin Icon
                                Icon(
                                    imageVector = Icons.Default.PushPin,
                                    contentDescription = "Pin",
                                    tint = if (note.isPinned) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { onTogglePin() }
                                )

                                // Lock Icon
                                Icon(
                                    imageVector = if (note.isLocked) Icons.Default.Lock else Icons.Default.Lock,
                                    contentDescription = "Lock",
                                    tint = if (note.isLocked) Color(0xFFE53935) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { onToggleLock() }
                                )

                                // Delete Icon
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
                                    modifier = Modifier
                                        .size(18.dp)
                                        .clickable { onDelete() }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // 2 Lines Note Content Preview
                    val previewText = when {
                        note.isLocked -> "🔒 গোপন নোট (পাসওয়ার্ড দিয়ে সুরক্ষিত)"
                        note.content.isBlank() -> "লেখা শূন্য..."
                        else -> note.content
                    }
                    Text(
                        text = previewText,
                        style = TextStyle(
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            lineHeight = 20.sp
                        ),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Date & Time
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Clock",
                            tint = GoldPrimary.copy(alpha = 0.8f),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formatBengaliDateTime(note.updatedAt),
                            style = TextStyle(
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )
                        )
                    }
                }
            }
        }
    }
}
