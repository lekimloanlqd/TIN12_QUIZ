package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.DefaultQuizData
import com.example.data.local.QuizDatabase
import com.example.data.model.ParticipationType
import com.example.data.model.PracticeMode
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionType
import com.example.data.model.Quiz
import com.example.data.model.QuizAttempt
import com.example.data.repository.QuizRepository
import com.example.ui.sound.SoundHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.UUID

enum class AppScreen {
    HOME,
    QUIZ_ACTIVE,
    QUIZ_RESULT,
    TEACHER_DASHBOARD
}

data class QuizSessionState(
    val quiz: Quiz = DefaultQuizData.defaultQuiz,
    val studentName: String = "",
    val className: String = "",
    val participationType: ParticipationType = ParticipationType.GUEST,
    val practiceMode: PracticeMode = PracticeMode.INSTANT_PRACTICE,
    val questions: List<QuestionItem> = emptyList(),
    val currentQuestionIndex: Int = 0,
    // For part 1: questionId -> selectedOptionIndex (0..3)
    val selectedOptions: Map<Int, Int> = emptyMap(),
    // For part 2: questionId -> Map("a" -> true, "b" -> false...)
    val trueFalseSelections: Map<Int, Map<String, Boolean>> = emptyMap(),
    // Instant feedback for practice mode
    val isAnswerCheckedForCurrent: Boolean = false,
    val currentQuestionCorrect: Boolean = false,
    // Quizizz gamification
    val streak: Int = 0,
    val scorePoints: Int = 0,
    val timeRemainingSeconds: Int = 0,
    val startTime: Long = 0,
    val isTimerActive: Boolean = false
)

class QuizViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: QuizRepository
    val soundHelper: SoundHelper = SoundHelper()

    private val _currentScreen = MutableStateFlow(AppScreen.HOME)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    private val _sessionState = MutableStateFlow(QuizSessionState())
    val sessionState: StateFlow<QuizSessionState> = _sessionState.asStateFlow()

    private val _latestAttempt = MutableStateFlow<QuizAttempt?>(null)
    val latestAttempt: StateFlow<QuizAttempt?> = _latestAttempt.asStateFlow()

    // Teacher filters
    val selectedClassFilter = MutableStateFlow("Tất cả")
    val selectedQuizFilter = MutableStateFlow("Tất cả")
    val searchQuery = MutableStateFlow("")
    val selectedParticipationFilter = MutableStateFlow("Tất cả")
    val isTeacherLoggedIn = MutableStateFlow(false)

    private var timerJob: Job? = null

    init {
        val db = QuizDatabase.getDatabase(application)
        repository = QuizRepository(db.quizAttemptDao())
        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
        }
    }

    val allAttempts: StateFlow<List<QuizAttempt>> = repository.allAttempts
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    fun startQuizSession(
        quiz: Quiz,
        studentName: String,
        className: String,
        isGuest: Boolean,
        mode: PracticeMode
    ) {
        timerJob?.cancel()
        var questionList = repository.getQuestions(quiz.id)
        if (mode == PracticeMode.CHALLENGE) {
            // Shuffle questions for challenge mode, but keep options functional
            questionList = questionList.shuffled()
        }

        val timeSec = if (quiz.durationMinutes > 0) quiz.durationMinutes * 60 else 0

        _sessionState.value = QuizSessionState(
            quiz = quiz,
            studentName = studentName.trim().ifEmpty { "Học sinh Ẩn danh" },
            className = className.trim().ifEmpty { "12/1B" },
            participationType = if (isGuest) ParticipationType.GUEST else ParticipationType.ACCOUNT,
            practiceMode = mode,
            questions = questionList,
            currentQuestionIndex = 0,
            selectedOptions = emptyMap(),
            trueFalseSelections = emptyMap(),
            isAnswerCheckedForCurrent = false,
            currentQuestionCorrect = false,
            streak = 0,
            scorePoints = 0,
            timeRemainingSeconds = timeSec,
            startTime = System.currentTimeMillis(),
            isTimerActive = timeSec > 0
        )

        _currentScreen.value = AppScreen.QUIZ_ACTIVE

        if (timeSec > 0) {
            startTimer()
        }
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (_sessionState.value.timeRemainingSeconds > 0) {
                delay(1000)
                _sessionState.value = _sessionState.value.copy(
                    timeRemainingSeconds = _sessionState.value.timeRemainingSeconds - 1
                )
            }
            // Auto submit when time expires
            submitQuiz()
        }
    }

    fun selectOption(questionId: Int, optionIndex: Int) {
        val current = _sessionState.value
        // If already checked in instant practice mode, don't allow re-selection
        if (current.practiceMode == PracticeMode.INSTANT_PRACTICE && current.isAnswerCheckedForCurrent) {
            return
        }

        val updatedMap = current.selectedOptions.toMutableMap()
        updatedMap[questionId] = optionIndex
        _sessionState.value = current.copy(selectedOptions = updatedMap)

        // In instant practice mode, immediately check answer on click
        if (current.practiceMode == PracticeMode.INSTANT_PRACTICE) {
            checkCurrentAnswer()
        }
    }

    fun selectTrueFalse(questionId: Int, statementKey: String, answer: Boolean) {
        val current = _sessionState.value
        val currentTFMap = (current.trueFalseSelections[questionId] ?: emptyMap()).toMutableMap()
        currentTFMap[statementKey] = answer

        val updatedMap = current.trueFalseSelections.toMutableMap()
        updatedMap[questionId] = currentTFMap

        _sessionState.value = current.copy(trueFalseSelections = updatedMap)
    }

    fun checkCurrentAnswer() {
        val current = _sessionState.value
        val question = current.questions.getOrNull(current.currentQuestionIndex) ?: return

        var isCorrect = false
        if (question.type == QuestionType.MULTIPLE_CHOICE) {
            val selected = current.selectedOptions[question.id]
            isCorrect = (selected != null && selected == question.correctOptionIndex)
        } else {
            val userTF = current.trueFalseSelections[question.id] ?: emptyMap()
            var allMatch = true
            for (st in question.statements) {
                if (userTF[st.key] != st.isTrue) {
                    allMatch = false
                    break
                }
            }
            isCorrect = allMatch
        }

        val newStreak = if (isCorrect) current.streak + 1 else 0
        val pointsToAdd = if (isCorrect) 100 + (newStreak * 20) else 0

        if (isCorrect) {
            if (newStreak >= 3) {
                soundHelper.playStreak()
            } else {
                soundHelper.playCorrect()
            }
        } else {
            soundHelper.playIncorrect()
        }

        _sessionState.value = current.copy(
            isAnswerCheckedForCurrent = true,
            currentQuestionCorrect = isCorrect,
            streak = newStreak,
            scorePoints = current.scorePoints + pointsToAdd
        )
    }

    fun nextQuestion() {
        val current = _sessionState.value
        if (current.currentQuestionIndex < current.questions.size - 1) {
            _sessionState.value = current.copy(
                currentQuestionIndex = current.currentQuestionIndex + 1,
                isAnswerCheckedForCurrent = false,
                currentQuestionCorrect = false
            )
        } else {
            submitQuiz()
        }
    }

    fun previousQuestion() {
        val current = _sessionState.value
        if (current.currentQuestionIndex > 0) {
            _sessionState.value = current.copy(
                currentQuestionIndex = current.currentQuestionIndex - 1,
                isAnswerCheckedForCurrent = false,
                currentQuestionCorrect = false
            )
        }
    }

    fun jumpToQuestion(index: Int) {
        val current = _sessionState.value
        if (index in current.questions.indices) {
            _sessionState.value = current.copy(
                currentQuestionIndex = index,
                isAnswerCheckedForCurrent = false,
                currentQuestionCorrect = false
            )
        }
    }

    fun submitQuiz() {
        timerJob?.cancel()
        val current = _sessionState.value
        val now = System.currentTimeMillis()
        val durationSec = ((now - current.startTime) / 1000).coerceAtLeast(1)

        // Calculate evaluation points (26 total points)
        var correctPoints = 0
        val totalPoints = 26

        // Part 1: 10 MCQ questions (1 pt each = 10 pts)
        // Part 2: 4 contexts x 4 statements (1 pt each statement = 16 pts)
        for (q in current.questions) {
            if (q.type == QuestionType.MULTIPLE_CHOICE) {
                val selected = current.selectedOptions[q.id]
                if (selected != null && selected == q.correctOptionIndex) {
                    correctPoints++
                }
            } else {
                val userTF = current.trueFalseSelections[q.id] ?: emptyMap()
                for (st in q.statements) {
                    if (userTF[st.key] == st.isTrue) {
                        correctPoints++
                    }
                }
            }
        }

        val incorrectPoints = totalPoints - correctPoints
        val scoreOutOf10 = String.format("%.1f", (correctPoints.toDouble() / totalPoints) * 10.0)
            .replace(",", ".")
            .toDoubleOrNull() ?: 0.0
        val accuracyRate = String.format("%.1f", (correctPoints.toDouble() / totalPoints) * 100.0)
            .replace(",", ".")
            .toDoubleOrNull() ?: 0.0

        val attempt = QuizAttempt(
            attemptId = UUID.randomUUID().toString(),
            quizId = current.quiz.id,
            quizTitle = current.quiz.title,
            studentName = current.studentName,
            className = current.className,
            participationType = current.participationType.name,
            practiceMode = current.practiceMode.displayName,
            startedAt = current.startTime,
            submittedAt = now,
            score = scoreOutOf10,
            correctCount = correctPoints,
            incorrectCount = incorrectPoints,
            totalPoints = totalPoints,
            accuracyRate = accuracyRate,
            durationSeconds = durationSec,
            completionStatus = "completed"
        )

        viewModelScope.launch {
            repository.saveAttempt(attempt)
            _latestAttempt.value = attempt
            soundHelper.playComplete()
            _currentScreen.value = AppScreen.QUIZ_RESULT
        }
    }

    fun toggleSound() {
        soundHelper.isSoundEnabled = !soundHelper.isSoundEnabled
    }

    fun deleteAttempt(attemptId: String) {
        viewModelScope.launch {
            repository.deleteAttempt(attemptId)
        }
    }

    fun clearAllAttempts() {
        viewModelScope.launch {
            repository.clearAttempts()
        }
    }

    fun getExportCsv(): String {
        return repository.exportAttemptsToCsv(allAttempts.value)
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        soundHelper.release()
    }
}
