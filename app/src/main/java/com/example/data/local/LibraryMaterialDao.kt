package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryMaterialDao {

    @Query("SELECT * FROM library_materials ORDER BY timestamp DESC")
    fun getAllMaterials(): Flow<List<LibraryMaterialEntity>>

    @Query("SELECT * FROM library_materials WHERE isBookmarked = 1 ORDER BY timestamp DESC")
    fun getBookmarkedMaterials(): Flow<List<LibraryMaterialEntity>>

    @Query("""
        SELECT * FROM library_materials 
        WHERE (title LIKE '%' || :query || '%' 
           OR description LIKE '%' || :query || '%' 
           OR content LIKE '%' || :query || '%'
           OR tags LIKE '%' || :query || '%' 
           OR subject LIKE '%' || :query || '%'
           OR targetExam LIKE '%' || :query || '%'
           OR author LIKE '%' || :query || '%')
        ORDER BY timestamp DESC
    """)
    fun searchMaterials(query: String): Flow<List<LibraryMaterialEntity>>

    @Query("SELECT * FROM library_materials WHERE id = :id")
    suspend fun getMaterialById(id: String): LibraryMaterialEntity?

    @Query("SELECT COUNT(*) FROM library_materials")
    suspend fun getCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterial(material: LibraryMaterialEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMaterials(materials: List<LibraryMaterialEntity>)

    @Query("UPDATE library_materials SET isBookmarked = :isBookmarked WHERE id = :id")
    suspend fun updateBookmark(id: String, isBookmarked: Boolean)

    @Query("SELECT * FROM library_materials")
    suspend fun getAllMaterialsList(): List<LibraryMaterialEntity>

    @Query("UPDATE library_materials SET imageUrl = :newUrl WHERE id = :id")
    suspend fun updateImageUrl(id: String, newUrl: String)

    @Query("DELETE FROM library_materials WHERE id = :id")
    suspend fun deleteMaterial(id: String)
}
