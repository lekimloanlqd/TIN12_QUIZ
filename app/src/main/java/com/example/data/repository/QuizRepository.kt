package com.example.data.repository

import com.example.data.local.DefaultQuizData
import com.example.data.local.QuizAttemptDao
import com.example.data.model.ParticipationType
import com.example.data.model.PracticeMode
import com.example.data.model.QuestionItem
import com.example.data.model.Quiz
import com.example.data.model.QuizAttempt
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class QuizRepository(private val attemptDao: QuizAttemptDao) {

    val allAttempts: Flow<List<QuizAttempt>> = attemptDao.getAllAttempts()

    suspend fun saveAttempt(attempt: QuizAttempt) {
        attemptDao.insertAttempt(attempt)
    }

    suspend fun deleteAttempt(attemptId: String) {
        attemptDao.deleteAttempt(attemptId)
    }

    suspend fun clearAttempts() {
        attemptDao.clearAllAttempts()
    }

    fun getAvailableQuizzes(): List<Quiz> = DefaultQuizData.sampleQuizzes

    fun getQuizById(quizId: String): Quiz? {
        return DefaultQuizData.sampleQuizzes.find { it.id == quizId } ?: DefaultQuizData.defaultQuiz
    }

    fun getQuizByPin(pin: String): Quiz? {
        return DefaultQuizData.sampleQuizzes.find { it.pinCode.trim() == pin.trim() }
    }

    fun getQuestions(quizId: String): List<QuestionItem> {
        // Currently Bài 7 has the rich 26-evaluation items
        return DefaultQuizData.questions
    }

    suspend fun seedInitialDataIfEmpty() {
        val current = attemptDao.getAllAttempts().first()
        if (current.isEmpty()) {
            val now = System.currentTimeMillis()
            val hourAgo = now - 3600000
            val dayAgo = now - 86400000

            val sampleAttempts = listOf(
                QuizAttempt(
                    attemptId = UUID.randomUUID().toString(),
                    quizId = "bai-7-html",
                    quizTitle = "Bài 7 – HTML và Cấu trúc trang web",
                    studentName = "Nguyễn Mai Anh",
                    className = "12/1B",
                    participationType = ParticipationType.GUEST.name,
                    practiceMode = PracticeMode.EXAM.name,
                    startedAt = dayAgo,
                    submittedAt = dayAgo + 720000,
                    score = 9.2,
                    correctCount = 24,
                    incorrectCount = 2,
                    totalPoints = 26,
                    accuracyRate = 92.3,
                    durationSeconds = 720,
                    completionStatus = "completed"
                ),
                QuizAttempt(
                    attemptId = UUID.randomUUID().toString(),
                    quizId = "bai-7-html",
                    quizTitle = "Bài 7 – HTML và Cấu trúc trang web",
                    studentName = "Trần Nam Phong",
                    className = "12/1B",
                    participationType = ParticipationType.GUEST.name,
                    practiceMode = PracticeMode.INSTANT_PRACTICE.name,
                    startedAt = hourAgo,
                    submittedAt = hourAgo + 850000,
                    score = 8.5,
                    correctCount = 22,
                    incorrectCount = 4,
                    totalPoints = 26,
                    accuracyRate = 84.6,
                    durationSeconds = 850,
                    completionStatus = "completed"
                ),
                QuizAttempt(
                    attemptId = UUID.randomUUID().toString(),
                    quizId = "bai-7-html",
                    quizTitle = "Bài 7 – HTML và Cấu trúc trang web",
                    studentName = "Lê Hoàng Lan",
                    className = "12A1",
                    participationType = ParticipationType.ACCOUNT.name,
                    practiceMode = PracticeMode.CHALLENGE.name,
                    startedAt = now - 1800000,
                    submittedAt = now - 1200000,
                    score = 10.0,
                    correctCount = 26,
                    incorrectCount = 0,
                    totalPoints = 26,
                    accuracyRate = 100.0,
                    durationSeconds = 600,
                    completionStatus = "completed"
                ),
                QuizAttempt(
                    attemptId = UUID.randomUUID().toString(),
                    quizId = "bai-7-html",
                    quizTitle = "Bài 7 – HTML và Cấu trúc trang web",
                    studentName = "Phạm Quang Huy",
                    className = "12A2",
                    participationType = ParticipationType.GUEST.name,
                    practiceMode = PracticeMode.EXAM.name,
                    startedAt = now - 600000,
                    submittedAt = now - 100000,
                    score = 7.7,
                    correctCount = 20,
                    incorrectCount = 6,
                    totalPoints = 26,
                    accuracyRate = 76.9,
                    durationSeconds = 500,
                    completionStatus = "completed"
                )
            )

            sampleAttempts.forEach { attemptDao.insertAttempt(it) }
        }
    }

    fun exportAttemptsToCsv(attempts: List<QuizAttempt>): String {
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val sb = StringBuilder()
        sb.append("STT,Họ và tên,Lớp,Hình thức,Chế độ,Bài tập,Điểm số (/10),Số câu đúng,Tổng số câu,Tỷ lệ (%),Thời gian làm (giây),Thời điểm nộp\n")
        attempts.forEachIndexed { index, att ->
            val dateStr = sdf.format(Date(att.submittedAt))
            val partTypeStr = if (att.participationType == ParticipationType.GUEST.name) "Khách (Guest)" else "Tài khoản"
            sb.append("${index + 1},\"${att.studentName}\",\"${att.className}\",\"$partTypeStr\",\"${att.practiceMode}\",\"${att.quizTitle}\",${att.score},${att.correctCount},${att.totalPoints},${att.accuracyRate},${att.durationSeconds},\"$dateStr\"\n")
        }
        return sb.toString()
    }
}
