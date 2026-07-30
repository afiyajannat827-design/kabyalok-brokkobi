package com.example.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface PoemDao {

    @Query("SELECT * FROM poem_notes WHERE isTrash = 0 AND isHidden = 0 AND isArchived = 0 ORDER BY isPinned DESC, updatedAt DESC")
    fun getAllActiveNotes(): Flow<List<PoemNote>>

    @Query("SELECT * FROM poem_notes WHERE isTrash = 0 AND isHidden = 0 ORDER BY updatedAt DESC")
    suspend fun getAllNonTrashNotesSync(): List<PoemNote>

    @Query("SELECT * FROM poem_notes WHERE isTrash = 0 AND isHidden = 0 AND isPinned = 1 ORDER BY updatedAt DESC")
    fun getPinnedNotes(): Flow<List<PoemNote>>

    @Query("SELECT * FROM poem_notes WHERE isTrash = 0 AND isHidden = 1 ORDER BY updatedAt DESC")
    fun getHiddenNotes(): Flow<List<PoemNote>>

    @Query("SELECT * FROM poem_notes WHERE isTrash = 0 AND isHidden = 0 AND category = :category ORDER BY isPinned DESC, updatedAt DESC")
    fun getNotesByCategory(category: String): Flow<List<PoemNote>>

    @Query("SELECT * FROM poem_notes WHERE isArchived = 1 AND isTrash = 0 ORDER BY updatedAt DESC")
    fun getArchivedNotes(): Flow<List<PoemNote>>

    @Query("SELECT * FROM poem_notes WHERE isTrash = 1 ORDER BY updatedAt DESC")
    fun getTrashNotes(): Flow<List<PoemNote>>

    @Query("SELECT * FROM poem_notes WHERE id = :id LIMIT 1")
    suspend fun getNoteById(id: Int): PoemNote?

    @Query("SELECT * FROM poem_notes WHERE isTrash = 0 AND isHidden = 0 AND (title LIKE '%' || :query || '%' OR content LIKE '%' || :query || '%') ORDER BY isPinned DESC, updatedAt DESC")
    fun searchNotes(query: String): Flow<List<PoemNote>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: PoemNote): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAllNotes(notes: List<PoemNote>)

    @Update
    suspend fun updateNote(note: PoemNote)

    @Query("UPDATE poem_notes SET isPinned = :isPinned WHERE id = :id")
    suspend fun setPinned(id: Int, isPinned: Boolean)

    @Query("UPDATE poem_notes SET isArchived = :isArchived WHERE id = :id")
    suspend fun setArchived(id: Int, isArchived: Boolean)

    @Query("UPDATE poem_notes SET isTrash = 1 WHERE id IN (:ids)")
    suspend fun moveMultipleToTrash(ids: List<Int>)

    @Query("UPDATE poem_notes SET isHidden = 1 WHERE id IN (:ids)")
    suspend fun hideMultiple(ids: List<Int>)

    @Query("UPDATE poem_notes SET isHidden = 0 WHERE id = :id")
    suspend fun unhideNote(id: Int)

    @Query("UPDATE poem_notes SET category = :category WHERE id IN (:ids)")
    suspend fun setCategoryMultiple(ids: List<Int>, category: String)

    @Query("UPDATE poem_notes SET isTrash = :isTrash WHERE id = :id")
    suspend fun setTrash(id: Int, isTrash: Boolean)

    @Delete
    suspend fun deleteNotePermanently(note: PoemNote)

    @Query("DELETE FROM poem_notes WHERE id = :id")
    suspend fun deleteNotePermanentlyById(id: Int)

    @Query("DELETE FROM poem_notes WHERE isTrash = 1")
    suspend fun emptyTrash()

    @Query("SELECT COUNT(*) FROM poem_notes WHERE isTrash = 0 AND isHidden = 0")
    fun getActiveNoteCount(): Flow<Int>

    // ================= GROUP QUERIES =================
    @Query("SELECT * FROM poem_groups ORDER BY name ASC")
    fun getAllGroups(): Flow<List<PoemGroup>>

    @Query("SELECT * FROM poem_groups ORDER BY name ASC")
    suspend fun getAllGroupsSync(): List<PoemGroup>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGroup(group: PoemGroup): Long

    @Update
    suspend fun updateGroup(group: PoemGroup)

    @Delete
    suspend fun deleteGroup(group: PoemGroup)

    @Query("SELECT * FROM poem_notes WHERE groupId = :groupId AND isTrash = 0 ORDER BY updatedAt DESC")
    fun getNotesByGroup(groupId: Int): Flow<List<PoemNote>>

    @Query("SELECT COUNT(*) FROM poem_notes WHERE groupId = :groupId AND isTrash = 0")
    fun getNoteCountForGroup(groupId: Int): Flow<Int>

    @Query("UPDATE poem_notes SET groupId = NULL WHERE id = :noteId")
    suspend fun removeNoteFromGroup(noteId: Int)

    @Query("UPDATE poem_notes SET groupId = NULL WHERE groupId = :groupId")
    suspend fun clearGroupNotes(groupId: Int)

    @Query("UPDATE poem_notes SET groupId = :groupId WHERE id IN (:noteIds)")
    suspend fun addNotesToGroup(noteIds: List<Int>, groupId: Int)
}
