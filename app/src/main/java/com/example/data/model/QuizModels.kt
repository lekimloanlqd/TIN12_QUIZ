package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

enum class PracticeMode(val displayName: String, val description: String) {
    INSTANT_PRACTICE(
        "Luyện tập từng câu",
        "Xem kết quả và lời giải chi tiết ngay sau mỗi câu trả lời."
    ),
    EXAM(
        "Kiểm tra tính điểm",
        "Làm toàn bộ bài kiểm tra, chuyển đổi qua lại và nộp bài để xem điểm số."
    ),
    CHALLENGE(
        "Thử thách Quizizz",
        "Đồng hồ đếm ngược, chuỗi Streak combo điểm thưởng và xáo trộn câu hỏi!"
    )
}

enum class ParticipationType(val displayName: String) {
    GUEST("Khách (Không đăng nhập)"),
    ACCOUNT("Tài khoản học sinh")
}

data class Quiz(
    val id: String = "bai-7-html",
    val title: String = "Bài 7 – HTML và Cấu trúc trang web",
    val subject: String = "Tin học 12",
    val grade: String = "Khối 12",
    val teacherName: String = "Thầy/Cô Giáo Tin Học",
    val pinCode: String = "789123",
    val durationMinutes: Int = 20, // 0 means no time limit
    val isOpen: Boolean = true,
    val allowRetake: Boolean = true,
    val totalMultipleChoice: Int = 10,
    val totalTrueFalseContexts: Int = 4,
    val totalEvaluationPoints: Int = 26
)

enum class QuestionType {
    MULTIPLE_CHOICE,
    TRUE_FALSE_CONTEXT
}

data class TrueFalseStatement(
    val key: String, // "a", "b", "c", "d"
    val level: String, // "Biết", "Hiểu", "Vận dụng"
    val text: String,
    val isTrue: Boolean,
    val explanation: String
)

data class QuestionItem(
    val id: Int,
    val part: Int, // 1: Multiple choice, 2: True/False context
    val questionNumber: Int,
    val type: QuestionType,
    val level: String, // "Nhận biết", "Thông hiểu", "Vận dụng thực tế"
    val prompt: String,
    val contextTitle: String = "",
    val contextDescription: String = "",
    val options: List<String> = emptyList(), // ["A. ...", "B. ...", "C. ...", "D. ..."]
    val correctOptionIndex: Int = -1, // 0 = A, 1 = B, 2 = C, 3 = D
    val statements: List<TrueFalseStatement> = emptyList(),
    val explanation: String = ""
)

@Entity(tableName = "quiz_attempts")
data class QuizAttempt(
    @PrimaryKey
    val attemptId: String = UUID.randomUUID().toString(),
    val quizId: String = "bai-7-html",
    val quizTitle: String = "Bài 7 – HTML và Cấu trúc trang web",
    val studentName: String,
    val className: String,
    val participationType: String = ParticipationType.GUEST.name,
    val practiceMode: String = PracticeMode.INSTANT_PRACTICE.name,
    val startedAt: Long = System.currentTimeMillis(),
    val submittedAt: Long = System.currentTimeMillis(),
    val score: Double = 0.0, // Scale 10.0
    val correctCount: Int = 0,
    val incorrectCount: Int = 0,
    val totalPoints: Int = 26,
    val accuracyRate: Double = 0.0,
    val durationSeconds: Long = 0,
    val completionStatus: String = "completed", // "completed", "in_progress"
    val answersJson: String = "" // Serialized answer details
)

data class StudentUser(
    val username: String,
    val fullName: String,
    val className: String,
    val role: String // "student" or "teacher"
)
