package com.example.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.ThemePreferences
import com.example.data.database.AppDatabase
import com.example.data.database.PoemGroup
import com.example.data.database.PoemNote
import com.example.data.database.PoemRepository
import com.example.ui.theme.ThemePreset
import com.example.utils.BackupRestoreHelper
import com.example.utils.PdfExporter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: PoemRepository
    private val themePreferences = ThemePreferences(application)

    val currentThemePreset: StateFlow<ThemePreset> = themePreferences.themePresetFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ThemePreset.GOLDEN_CLASSIC)

    val isDarkMode: StateFlow<Boolean?> = themePreferences.darkModeFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val hiddenPasswordHash: StateFlow<String?> = themePreferences.hiddenPasswordHashFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val defaultFont: StateFlow<String> = themePreferences.defaultFontFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), "সোনার তরী")

    val selectedCategory = MutableStateFlow("সব")
    val searchQuery = MutableStateFlow("")

    val selectedNoteIds = MutableStateFlow<Set<Int>>(emptySet())
    val isSelectionMode = MutableStateFlow(false)

    private val _showThemeDialog = MutableStateFlow(false)
    val showThemeDialog: StateFlow<Boolean> = _showThemeDialog.asStateFlow()

    val poemNotes: StateFlow<List<PoemNote>>
    val pinnedNotes: StateFlow<List<PoemNote>>
    val hiddenNotes: StateFlow<List<PoemNote>>
    val trashNotes: StateFlow<List<PoemNote>>
    val allGroups: StateFlow<List<PoemGroup>>

    init {
        val database = AppDatabase.getInstance(application)
        repository = PoemRepository(database.poemDao())

        pinnedNotes = repository.pinnedNotes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        hiddenNotes = repository.hiddenNotes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        trashNotes = repository.trashNotes.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
        allGroups = repository.allGroups.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        poemNotes = combine(selectedCategory, searchQuery) { category, query ->
            Pair(category, query)
        }.flatMapLatest { (category, query) ->
            if (query.isNotBlank()) {
                repository.searchNotes(query)
            } else {
                repository.getNotesByCategory(category)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

        // Remove sample notes if present
        viewModelScope.launch {
            val sampleTitles = setOf("বিদ্রোহী সুর", "শ্রাবণের গান", "শব্দের জাদুকর")
            val allNotes = repository.getAllNonTrashNotesSync()
            allNotes.filter { it.title in sampleTitles }.forEach { note ->
                repository.deletePermanently(note)
            }
        }
    }

    fun selectCategory(category: String) {
        selectedCategory.value = category
    }

    fun updateSearchQuery(query: String) {
        searchQuery.value = query
    }

    fun toggleNoteSelection(noteId: Int) {
        val current = selectedNoteIds.value.toMutableSet()
        if (current.contains(noteId)) {
            current.remove(noteId)
        } else {
            current.add(noteId)
        }
        selectedNoteIds.value = current
        isSelectionMode.value = current.isNotEmpty()
    }

    fun clearSelection() {
        selectedNoteIds.value = emptySet()
        isSelectionMode.value = false
    }

    fun deleteSelectedNotes() {
        val ids = selectedNoteIds.value.toList()
        if (ids.isNotEmpty()) {
            viewModelScope.launch {
                repository.moveMultipleToTrash(ids)
                clearSelection()
            }
        }
    }

    fun hideSelectedNotes() {
        val ids = selectedNoteIds.value.toList()
        if (ids.isNotEmpty()) {
            viewModelScope.launch {
                repository.hideMultiple(ids)
                clearSelection()
            }
        }
    }

    fun hideNotes(ids: List<Int>) {
        viewModelScope.launch {
            repository.hideMultiple(ids)
        }
    }

    fun unhideNote(id: Int) {
        viewModelScope.launch {
            repository.unhideNote(id)
        }
    }

    fun setGroupSelectedNotes(category: String) {
        val ids = selectedNoteIds.value.toList()
        if (ids.isNotEmpty()) {
            viewModelScope.launch {
                repository.setCategoryMultiple(ids, category)
                clearSelection()
            }
        }
    }

    fun togglePin(note: PoemNote) {
        viewModelScope.launch {
            repository.togglePin(note.id, note.isPinned)
        }
    }

    fun toggleLock(note: PoemNote) {
        viewModelScope.launch {
            val updated = note.copy(isLocked = !note.isLocked)
            repository.updateNote(updated)
        }
    }

    fun toggleThemeDialog() {
        _showThemeDialog.value = !_showThemeDialog.value
    }

    fun setThemePreset(preset: ThemePreset) {
        viewModelScope.launch {
            themePreferences.setThemePreset(preset)
        }
    }

    fun setDarkMode(isDark: Boolean) {
        viewModelScope.launch {
            themePreferences.setDarkMode(isDark)
        }
    }

    fun setHiddenNotesPasswordHash(hash: String) {
        viewModelScope.launch {
            themePreferences.setHiddenNotesPasswordHash(hash)
        }
    }

    fun setDefaultFont(fontName: String) {
        viewModelScope.launch {
            themePreferences.setDefaultFont(fontName)
        }
    }

    fun saveNote(title: String, content: String, category: String) {
        if (title.isBlank() && content.isBlank()) return
        val stanzas = if (content.isBlank()) 0 else content.split("\n\n").size
        val words = if (content.isBlank()) 0 else content.trim().split("\\s+".toRegex()).size

        val newNote = PoemNote(
            title = title.ifBlank { "শিরোনামহীন কবিতা" },
            content = content,
            stanzaCount = stanzas,
            wordCount = words,
            category = category,
            fontFamilyName = defaultFont.value
        )
        viewModelScope.launch {
            repository.insertNote(newNote)
        }
    }

    fun updateNote(note: PoemNote) {
        val stanzas = if (note.content.isBlank()) 0 else note.content.split("\n\n").size
        val words = if (note.content.isBlank()) 0 else note.content.trim().split("\\s+".toRegex()).size
        val updated = note.copy(
            stanzaCount = stanzas,
            wordCount = words,
            updatedAt = System.currentTimeMillis()
        )
        viewModelScope.launch {
            repository.updateNote(updated)
        }
    }

    fun moveToTrash(noteId: Int) {
        viewModelScope.launch {
            repository.moveToTrash(noteId)
        }
    }

    fun restoreFromTrash(noteId: Int) {
        viewModelScope.launch {
            repository.restoreFromTrash(noteId)
        }
    }

    fun deletePermanently(note: PoemNote) {
        viewModelScope.launch {
            repository.deletePermanently(note)
        }
    }

    fun emptyTrash() {
        viewModelScope.launch {
            repository.emptyTrash()
        }
    }

    // ================= GROUP OPERATIONS =================
    fun getNotesByGroup(groupId: Int): Flow<List<PoemNote>> = repository.getNotesByGroup(groupId)

    fun createGroup(name: String) {
        viewModelScope.launch {
            repository.createGroup(name)
        }
    }

    fun updateGroup(group: PoemGroup) {
        viewModelScope.launch {
            repository.updateGroup(group)
        }
    }

    fun deleteGroup(group: PoemGroup) {
        viewModelScope.launch {
            repository.deleteGroup(group)
        }
    }

    fun removeNoteFromGroup(noteId: Int) {
        viewModelScope.launch {
            repository.removeNoteFromGroup(noteId)
        }
    }

    fun addNotesToGroup(noteIds: List<Int>, groupId: Int) {
        viewModelScope.launch {
            repository.addNotesToGroup(noteIds, groupId)
        }
    }

    // ================= PDF & BACKUP EXPORTS =================
    fun exportAllPdf(context: Context) {
        viewModelScope.launch {
            val allNotes = repository.getAllNonTrashNotesSync()
            PdfExporter.exportAllNotesPdf(context, allNotes)
        }
    }

    fun exportSinglePdf(context: Context, note: PoemNote) {
        PdfExporter.exportSingleNotePdf(context, note)
    }

    fun exportBackupJson(context: Context) {
        viewModelScope.launch {
            val allNotes = repository.getAllNonTrashNotesSync()
            BackupRestoreHelper.exportToJson(context, allNotes)
        }
    }

    fun importBackupJson(jsonString: String, onComplete: (Int) -> Unit) {
        viewModelScope.launch {
            val notes = BackupRestoreHelper.parseJsonToNotes(jsonString)
            if (notes.isNotEmpty()) {
                repository.insertAllNotes(notes)
            }
            onComplete(notes.size)
        }
    }
}
