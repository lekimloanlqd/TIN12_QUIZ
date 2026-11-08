package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.QuizAttempt
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizAttemptDao {
    @Query("SELECT * FROM quiz_attempts ORDER BY submittedAt DESC")
    fun getAllAttempts(): Flow<List<QuizAttempt>>

    @Query("SELECT * FROM quiz_attempts WHERE quizId = :quizId ORDER BY submittedAt DESC")
    fun getAttemptsByQuiz(quizId: String): Flow<List<QuizAttempt>>

    @Query("SELECT * FROM quiz_attempts WHERE className = :className ORDER BY submittedAt DESC")
    fun getAttemptsByClass(className: String): Flow<List<QuizAttempt>>

    @Query("SELECT * FROM quiz_attempts WHERE attemptId = :attemptId LIMIT 1")
    suspend fun getAttemptById(attemptId: String): QuizAttempt?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAttempt(attempt: QuizAttempt)

    @Query("DELETE FROM quiz_attempts WHERE attemptId = :attemptId")
    suspend fun deleteAttempt(attemptId: String)

    @Query("DELETE FROM quiz_attempts")
    suspend fun clearAllAttempts()
}
