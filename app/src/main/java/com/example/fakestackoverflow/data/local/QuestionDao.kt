package com.example.fakestackoverflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QuestionDao {
    @Query("SELECT * FROM questions ORDER BY lastActivityDate DESC")
    fun getAllFlow(): Flow<List<QuestionEntity>>

    @Query("SELECT * FROM questions ORDER BY lastActivityDate DESC")
    suspend fun getAll(): List<QuestionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(items: List<QuestionEntity>)

    @Query("DELETE FROM questions")
    suspend fun clear()
}
