package com.example.data.database

import kotlinx.coroutines.flow.Flow

class PoemRepository(private val poemDao: PoemDao) {

    val allActiveNotes: Flow<List<PoemNote>> = poemDao.getAllActiveNotes()
    val pinnedNotes: Flow<List<PoemNote>> = poemDao.getPinnedNotes()
    val hiddenNotes: Flow<List<PoemNote>> = poemDao.getHiddenNotes()
    val archivedNotes: Flow<List<PoemNote>> = poemDao.getArchivedNotes()
    val trashNotes: Flow<List<PoemNote>> = poemDao.getTrashNotes()
    val totalNoteCount: Flow<Int> = poemDao.getActiveNoteCount()
    val allGroups: Flow<List<PoemGroup>> = poemDao.getAllGroups()

    fun getNotesByCategory(category: String): Flow<List<PoemNote>> {
        return if (category == "সব" || category.isBlank()) {
            poemDao.getAllActiveNotes()
        } else {
            poemDao.getNotesByCategory(category)
        }
    }

    fun searchNotes(query: String): Flow<List<PoemNote>> = poemDao.searchNotes(query)

    suspend fun getNoteById(id: Int): PoemNote? = poemDao.getNoteById(id)

    suspend fun getAllNonTrashNotesSync(): List<PoemNote> = poemDao.getAllNonTrashNotesSync()

    suspend fun insertNote(note: PoemNote): Long = poemDao.insertNote(note)

    suspend fun insertAllNotes(notes: List<PoemNote>) = poemDao.insertAllNotes(notes)

    suspend fun updateNote(note: PoemNote) = poemDao.updateNote(note)

    suspend fun togglePin(id: Int, currentPinned: Boolean) = poemDao.setPinned(id, !currentPinned)

    suspend fun toggleArchive(id: Int, currentArchived: Boolean) = poemDao.setArchived(id, !currentArchived)

    suspend fun moveToTrash(id: Int) = poemDao.setTrash(id, true)

    suspend fun moveMultipleToTrash(ids: List<Int>) = poemDao.moveMultipleToTrash(ids)

    suspend fun hideMultiple(ids: List<Int>) = poemDao.hideMultiple(ids)

    suspend fun unhideNote(id: Int) = poemDao.unhideNote(id)

    suspend fun setCategoryMultiple(ids: List<Int>, category: String) = poemDao.setCategoryMultiple(ids, category)

    suspend fun restoreFromTrash(id: Int) = poemDao.setTrash(id, false)

    suspend fun deletePermanently(note: PoemNote) = poemDao.deleteNotePermanently(note)

    suspend fun deleteNotePermanentlyById(id: Int) = poemDao.deleteNotePermanentlyById(id)

    suspend fun emptyTrash() = poemDao.emptyTrash()

    // ================= GROUP METHODS =================
    suspend fun createGroup(name: String): Long = poemDao.insertGroup(PoemGroup(name = name))

    suspend fun updateGroup(group: PoemGroup) = poemDao.updateGroup(group)

    suspend fun deleteGroup(group: PoemGroup) {
        poemDao.clearGroupNotes(group.id)
        poemDao.deleteGroup(group)
    }

    fun getNotesByGroup(groupId: Int): Flow<List<PoemNote>> = poemDao.getNotesByGroup(groupId)

    fun getNoteCountForGroup(groupId: Int): Flow<Int> = poemDao.getNoteCountForGroup(groupId)

    suspend fun removeNoteFromGroup(noteId: Int) = poemDao.removeNoteFromGroup(noteId)

    suspend fun addNotesToGroup(noteIds: List<Int>, groupId: Int) = poemDao.addNotesToGroup(noteIds, groupId)
}
