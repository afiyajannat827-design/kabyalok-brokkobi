package com.example.ui.editor

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Redo
import androidx.compose.material.icons.automirrored.filled.Undo
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PushPin
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.R
import com.example.data.database.PoemNote
import com.example.ui.components.DeleteConfirmationDialog
import com.example.ui.components.GlassCard
import com.example.ui.components.TiltedThumbtackIcon
import com.example.ui.editor.sheets.ColorPickerBottomSheet
import com.example.ui.editor.sheets.FontSelectorBottomSheet
import com.example.ui.editor.sheets.TextFormattingBottomSheet
import com.example.ui.editor.sheets.parseHexColor
import com.example.ui.home.HomeViewModel
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.FontHelper
import com.example.ui.theme.GoldDark
import com.example.ui.theme.GoldLight
import com.example.ui.theme.GoldPrimary
import com.example.ui.theme.SoftLavender
import com.example.utils.formatBengaliDateTime
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(
    noteId: Int?,
    viewModel: HomeViewModel,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val scope = rememberCoroutineScope()

    val isDarkModePref by viewModel.isDarkMode.collectAsState()
    val isDark = isDarkModePref ?: isSystemInDarkTheme()

    // Loaded / Current Note State
    var loadedNote by remember { mutableStateOf<PoemNote?>(null) }
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }
    var isPinned by remember { mutableStateOf(false) }
    var isLocked by remember { mutableStateOf(false) }
    var isReadOnly by remember { mutableStateOf(false) }

    // Text formatting fields
    var isBold by remember { mutableStateOf(false) }
    var isItalic by remember { mutableStateOf(false) }
    var isUnderline by remember { mutableStateOf(false) }
    var isStrikethrough by remember { mutableStateOf(false) }
    var textAlignStr by remember { mutableStateOf("LEFT") }
    var lineBreakMode by remember { mutableStateOf("LINE_BY_LINE") }
    var fontSizeSp by remember { mutableStateOf(18) }
    var lineSpacing by remember { mutableStateOf(1.6f) }
    var titleColorHex by remember { mutableStateOf("#D4A017") }
    var textColorHex by remember { mutableStateOf("#E2E8F0") }
    var fontName by remember { mutableStateOf("সোনার তরী") }

    // Undo / Redo Stacks
    val undoStack = remember { mutableStateListOf<String>() }
    val redoStack = remember { mutableStateListOf<String>() }

    // UI Dialog & Bottom Sheet States
    var showOverflowMenu by remember { mutableStateOf(false) }
    var showFormattingSheet by remember { mutableStateOf(false) }
    var showColorSheet by remember { mutableStateOf(false) }
    var showFontSheet by remember { mutableStateOf(false) }
    var showPinDialog by remember { mutableStateOf(false) }
    var pinInput by remember { mutableStateOf("") }
    var showDeleteDialog by remember { mutableStateOf(false) }

    // Initial load note if editing existing note
    LaunchedEffect(noteId) {
        if (noteId != null && noteId != -1) {
            val allNotes = viewModel.poemNotes.value
            val found = allNotes.firstOrNull { it.id == noteId }
            if (found != null) {
                loadedNote = found
                title = found.title
                content = found.content
                isPinned = found.isPinned
                isLocked = found.isLocked
                isReadOnly = found.isLocked
                isBold = found.isBold
                isItalic = found.isItalic
                isUnderline = found.isUnderline
                isStrikethrough = found.isStrikethrough
                textAlignStr = found.textAlign
                lineBreakMode = found.lineBreakMode
                fontSizeSp = found.fontSizeSp
                lineSpacing = found.lineSpacingMultiplier
                titleColorHex = found.titleColorHex
                textColorHex = found.textColorHex
                fontName = found.fontFamilyName

                if (found.isLocked) {
                    showPinDialog = true
                }
            }
        }
    }

    // Auto-save with 300ms debounce
    LaunchedEffect(
        title, content, isPinned, isLocked, isBold, isItalic,
        isUnderline, isStrikethrough, textAlignStr, lineBreakMode,
        fontSizeSp, lineSpacing, titleColorHex, textColorHex, fontName
    ) {
        delay(300)
        if (!isReadOnly && (title.isNotBlank() || content.isNotBlank())) {
            val updated = PoemNote(
                id = loadedNote?.id ?: 0,
                title = title.ifBlank { "শিরোনামহীন" },
                content = content,
                category = loadedNote?.category ?: "কবিতা",
                isPinned = isPinned,
                isLocked = isLocked,
                isBold = isBold,
                isItalic = isItalic,
                isUnderline = isUnderline,
                isStrikethrough = isStrikethrough,
                textAlign = textAlignStr,
                lineBreakMode = lineBreakMode,
                fontSizeSp = fontSizeSp,
                lineSpacingMultiplier = lineSpacing,
                titleColorHex = titleColorHex,
                textColorHex = textColorHex,
                fontFamilyName = fontName,
                createdAt = loadedNote?.createdAt ?: System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )
            if (loadedNote == null) {
                viewModel.saveNote(updated.title, updated.content, updated.category)
            } else {
                viewModel.updateNote(updated)
            }
        }
    }

    val fontOption = FontHelper.getFontByName(fontName)

    val textAlignment = when (textAlignStr) {
        "CENTER" -> TextAlign.Center
        "RIGHT" -> TextAlign.Right
        else -> TextAlign.Left
    }

    val textDecoration = when {
        isUnderline && isStrikethrough -> TextDecoration.combine(listOf(TextDecoration.Underline, TextDecoration.LineThrough))
        isUnderline -> TextDecoration.Underline
        isStrikethrough -> TextDecoration.LineThrough
        else -> TextDecoration.None
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

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.app_name),
                            style = TextStyle(
                                fontFamily = FontFamily.Serif,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                brush = Brush.horizontalGradient(
                                    listOf(GoldLight, GoldPrimary, GoldDark)
                                )
                            )
                        )
                        if (isReadOnly) {
                            Text(
                                text = "শুধু পঠনযোগ্য",
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    color = Color(0xFFFF6B6B),
                                    fontWeight = FontWeight.Bold
                                )
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        if (!isReadOnly) {
                            // Undo
                            IconButton(
                                onClick = {
                                    if (undoStack.isNotEmpty()) {
                                        redoStack.add(content)
                                        content = undoStack.removeAt(undoStack.size - 1)
                                    }
                                },
                                enabled = undoStack.isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Undo,
                                    contentDescription = "Undo",
                                    tint = if (undoStack.isNotEmpty()) GoldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }

                            // Redo
                            IconButton(
                                onClick = {
                                    if (redoStack.isNotEmpty()) {
                                        undoStack.add(content)
                                        content = redoStack.removeAt(redoStack.size - 1)
                                    }
                                },
                                enabled = redoStack.isNotEmpty()
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.Redo,
                                    contentDescription = "Redo",
                                    tint = if (redoStack.isNotEmpty()) GoldPrimary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                                )
                            }
                        }

                        // Overflow Menu
                        Box {
                            IconButton(onClick = { showOverflowMenu = true }) {
                                Icon(
                                    imageVector = Icons.Default.MoreVert,
                                    contentDescription = "More",
                                    tint = GoldPrimary
                                )
                            }

                            DropdownMenu(
                                expanded = showOverflowMenu,
                                onDismissRequest = { showOverflowMenu = false }
                            ) {
                                if (isReadOnly) {
                                    DropdownMenuItem(
                                        text = { Text("সম্পাদনার জন্য আনলক করুন") },
                                        onClick = {
                                            showOverflowMenu = false
                                            showPinDialog = true
                                        },
                                        leadingIcon = { Icon(Icons.Default.LockOpen, contentDescription = null, tint = GoldPrimary) }
                                    )
                                }

                                DropdownMenuItem(
                                    text = { Text("কপি করুন") },
                                    onClick = {
                                        showOverflowMenu = false
                                        clipboardManager.setText(AnnotatedString("$title\n\n$content"))
                                        Toast.makeText(context, "লেখা কপি করা হয়েছে", Toast.LENGTH_SHORT).show()
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("শেয়ার করুন") },
                                    onClick = {
                                        showOverflowMenu = false
                                        val shareIntent = android.content.Intent(android.content.Intent.ACTION_SEND).apply {
                                            type = "text/plain"
                                            putExtra(android.content.Intent.EXTRA_SUBJECT, title)
                                            putExtra(android.content.Intent.EXTRA_TEXT, "$title\n\n$content")
                                        }
                                        context.startActivity(android.content.Intent.createChooser(shareIntent, "শেয়ার করুন"))
                                    }
                                )

                                DropdownMenuItem(
                                    text = { Text("এই নোট PDF করুন") },
                                    onClick = {
                                        showOverflowMenu = false
                                        val currentNoteToExport = PoemNote(
                                            id = loadedNote?.id ?: 0,
                                            title = title.ifBlank { "শিরোনামহীন" },
                                            content = content,
                                            fontSizeSp = fontSizeSp,
                                            lineSpacingMultiplier = lineSpacing,
                                            textAlign = textAlignStr,
                                            updatedAt = System.currentTimeMillis()
                                        )
                                        viewModel.exportSinglePdf(context, currentNoteToExport)
                                    }
                                )

                                if (loadedNote != null) {
                                    DropdownMenuItem(
                                        text = { Text("ডিলিট করুন", color = Color.Red) },
                                        onClick = {
                                            showOverflowMenu = false
                                            showDeleteDialog = true
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            if (!isReadOnly) {
                // Bottom pill-shaped toolbar (same style as Home's search bar)
                GlassCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    shape = RoundedCornerShape(28.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // "Aa" Formatting
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { showFormattingSheet = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Aa",
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp,
                                    color = GoldPrimary
                                )
                            )
                        }

                        // "ফন্ট" Font Selector
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .clickable { showFontSheet = true }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "ফন্ট",
                                style = TextStyle(
                                    fontFamily = FontFamily.Serif,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = GoldPrimary
                                )
                            )
                        }

                        // Color Picker
                        IconButton(onClick = { showColorSheet = true }) {
                            Icon(
                                imageVector = Icons.Default.ColorLens,
                                contentDescription = "Color Picker",
                                tint = parseHexColor(titleColorHex)
                            )
                        }

                        // Lock Toggle
                        IconButton(onClick = { isLocked = !isLocked }) {
                            Icon(
                                imageVector = if (isLocked) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = "Lock",
                                tint = if (isLocked) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        // Pin Toggle
                        IconButton(onClick = { isPinned = !isPinned }) {
                            TiltedThumbtackIcon(
                                isPinned = isPinned,
                                tint = if (isPinned) GoldPrimary else MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
            }
        },
        floatingActionButton = {
            if (!isReadOnly) {
                FloatingActionButton(
                    onClick = {
                        if (title.isNotBlank() || content.isNotBlank()) {
                            val note = PoemNote(
                                id = loadedNote?.id ?: 0,
                                title = title.ifBlank { "শিরোনামহীন" },
                                content = content,
                                category = loadedNote?.category ?: "কবিতা",
                                isPinned = isPinned,
                                isLocked = isLocked,
                                isBold = isBold,
                                isItalic = isItalic,
                                isUnderline = isUnderline,
                                isStrikethrough = isStrikethrough,
                                textAlign = textAlignStr,
                                lineBreakMode = lineBreakMode,
                                fontSizeSp = fontSizeSp,
                                lineSpacingMultiplier = lineSpacing,
                                titleColorHex = titleColorHex,
                                textColorHex = textColorHex,
                                fontFamilyName = fontName,
                                updatedAt = System.currentTimeMillis()
                            )
                            if (loadedNote == null) {
                                viewModel.saveNote(note.title, note.content, note.category)
                            } else {
                                viewModel.updateNote(note)
                            }
                            Toast.makeText(context, "কবিতা সংরক্ষিত হয়েছে", Toast.LENGTH_SHORT).show()
                            onBack()
                        }
                    },
                    containerColor = GoldPrimary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    shape = CircleShape,
                    elevation = FloatingActionButtonDefaults.elevation(10.dp),
                    modifier = Modifier.size(60.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save",
                        modifier = Modifier.size(30.dp)
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
            Canvas(modifier = Modifier.fillMaxSize()) {
                drawCircle(
                    color = SoftLavender.copy(alpha = 0.05f),
                    radius = 300f,
                    center = Offset(200f, 200f)
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                // Centered Info Date-Time Row
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = "Clock",
                            tint = GoldPrimary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = formatBengaliDateTime(loadedNote?.updatedAt ?: System.currentTimeMillis()),
                            style = TextStyle(
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        )
                    }

                    if (isPinned) {
                        Box(
                            modifier = Modifier
                                .align(Alignment.CenterEnd)
                                .clip(CircleShape)
                                .background(GoldPrimary.copy(alpha = 0.2f))
                                .padding(horizontal = 10.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "পিন করা",
                                style = TextStyle(
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = GoldPrimary
                                )
                            )
                        }
                    }
                }

                // Title Input
                BasicTextField(
                    value = title,
                    onValueChange = {
                        if (!isReadOnly) title = it
                    },
                    readOnly = isReadOnly,
                    textStyle = TextStyle(
                        fontFamily = fontOption.fontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = parseHexColor(titleColorHex),
                        textAlign = textAlignment
                    ),
                    modifier = Modifier.fillMaxWidth(),
                    cursorBrush = SolidColor(GoldPrimary),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxWidth()) {
                            if (title.isEmpty()) {
                                Text(
                                    text = "শিরোনাম লিখুন...",
                                    style = TextStyle(
                                        fontFamily = FontFamily.Serif,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 24.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                        textAlign = textAlignment
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Content Input
                BasicTextField(
                    value = content,
                    onValueChange = {
                        if (!isReadOnly) {
                            undoStack.add(content)
                            content = it
                        }
                    },
                    readOnly = isReadOnly,
                    textStyle = TextStyle(
                        fontFamily = fontOption.fontFamily,
                        fontWeight = if (isBold) FontWeight.Bold else fontOption.fontWeight,
                        fontStyle = if (isItalic) FontStyle.Italic else fontOption.fontStyle,
                        textDecoration = textDecoration,
                        fontSize = fontSizeSp.sp,
                        lineHeight = (fontSizeSp * lineSpacing).sp,
                        color = parseHexColor(textColorHex),
                        textAlign = textAlignment
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    cursorBrush = SolidColor(GoldPrimary),
                    decorationBox = { innerTextField ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            if (content.isEmpty()) {
                                Text(
                                    text = "আপনার কবিতা বা লেখা লিখুন...",
                                    style = TextStyle(
                                        fontFamily = FontFamily.Serif,
                                        fontSize = fontSizeSp.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f),
                                        textAlign = textAlignment
                                    )
                                )
                            }
                            innerTextField()
                        }
                    }
                )
            }
        }
    }

    // PIN Unlock Dialog for Read-Only Mode
    if (showPinDialog) {
        Dialog(onDismissRequest = { showPinDialog = false }) {
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
                        text = "পিন কোড প্রবেশ করুন",
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = GoldPrimary
                        )
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    OutlinedTextField(
                        value = pinInput,
                        onValueChange = { pinInput = it },
                        placeholder = { Text("PIN কোড (যেমন: 1234)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.End
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(GoldPrimary)
                                .clickable {
                                    // Accept PIN
                                    isReadOnly = false
                                    showPinDialog = false
                                    Toast.makeText(context, "আনলক করা হয়েছে", Toast.LENGTH_SHORT).show()
                                }
                                .padding(horizontal = 20.dp, vertical = 10.dp)
                        ) {
                            Text(
                                text = "আনলক করুন",
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

    // Formatting Sheet
    if (showFormattingSheet) {
        TextFormattingBottomSheet(
            initialIsBold = isBold,
            initialIsItalic = isItalic,
            initialIsUnderline = isUnderline,
            initialIsStrikethrough = isStrikethrough,
            initialTextAlign = textAlignStr,
            initialLineBreakMode = lineBreakMode,
            initialFontSize = fontSizeSp,
            initialLineSpacing = lineSpacing,
            onApply = { b, i, u, s, align, breakMode, size, spacing ->
                isBold = b
                isItalic = i
                isUnderline = u
                isStrikethrough = s
                textAlignStr = align
                lineBreakMode = breakMode
                fontSizeSp = size
                lineSpacing = spacing
            },
            onDismiss = { showFormattingSheet = false }
        )
    }

    // Color Sheet
    if (showColorSheet) {
        ColorPickerBottomSheet(
            initialTitleColorHex = titleColorHex,
            initialTextColorHex = textColorHex,
            onApplyColors = { titleHex, textHex ->
                titleColorHex = titleHex
                textColorHex = textHex
            },
            onDismiss = { showColorSheet = false }
        )
    }

    // Font Sheet
    if (showFontSheet) {
        FontSelectorBottomSheet(
            initialFontName = fontName,
            onSelectFont = { option ->
                fontName = option.name
            },
            onDismiss = { showFontSheet = false }
        )
    }

    // Delete confirmation dialog
    if (showDeleteDialog && loadedNote != null) {
        DeleteConfirmationDialog(
            count = 1,
            onConfirm = {
                viewModel.moveToTrash(loadedNote!!.id)
                showDeleteDialog = false
                onBack()
            },
            onDismiss = { showDeleteDialog = false }
        )
    }
}
