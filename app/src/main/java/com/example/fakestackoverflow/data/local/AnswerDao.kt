package com.example.fakestackoverflow.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnswerDao {
    @Query("SELECT * FROM answers WHERE questionId = :questionId ORDER BY score DESC")
    suspend fun getByQuestion(questionId: Long): List<AnswerEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<AnswerEntity>)

    @Query("DELETE FROM answers WHERE questionId = :questionId")
    suspend fun deleteByQuestion(questionId: Long)
}
