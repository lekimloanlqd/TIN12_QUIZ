package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.VolumeOff
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.PracticeMode
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionType
import com.example.data.model.TrueFalseStatement
import com.example.ui.theme.QuizError
import com.example.ui.theme.QuizOptionBlue
import com.example.ui.theme.QuizOptionGreen
import com.example.ui.theme.QuizOptionRed
import com.example.ui.theme.QuizOptionYellow
import com.example.ui.theme.QuizPurpleDark
import com.example.ui.theme.QuizPurplePrimary
import com.example.ui.theme.QuizSuccess
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val session by viewModel.sessionState.collectAsState()
    var showExitDialog by remember { mutableStateOf(false) }
    var showSubmitConfirmDialog by remember { mutableStateOf(false) }
    var showQuestionNavigatorSheet by remember { mutableStateOf(false) }
    var isSoundOn by remember { mutableStateOf(viewModel.soundHelper.isSoundEnabled) }

    // Intercept back button
    BackHandler {
        showExitDialog = true
    }

    val currentQuestion = session.questions.getOrNull(session.currentQuestionIndex)
    val totalCount = session.questions.size
    val progress = if (totalCount > 0) (session.currentQuestionIndex + 1).toFloat() / totalCount.toFloat() else 0f

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (session.practiceMode == PracticeMode.INSTANT_PRACTICE && session.isAnswerCheckedForCurrent) 120.dp else 80.dp)
        ) {
            // Top Bar
            QuizTopBar(
                current = session.currentQuestionIndex + 1,
                total = totalCount,
                progress = progress,
                streak = session.streak,
                timeSeconds = session.timeRemainingSeconds,
                isTimerActive = session.isTimerActive,
                isSoundOn = isSoundOn,
                onSoundToggle = {
                    viewModel.toggleSound()
                    isSoundOn = viewModel.soundHelper.isSoundEnabled
                },
                onExitClick = { showExitDialog = true },
                onGridClick = { showQuestionNavigatorSheet = true }
            )

            // Content Area
            if (currentQuestion != null) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    item {
                        QuestionHeaderCard(
                            question = currentQuestion,
                            index = session.currentQuestionIndex + 1,
                            total = totalCount,
                            studentName = session.studentName,
                            className = session.className
                        )
                    }

                    if (currentQuestion.type == QuestionType.MULTIPLE_CHOICE) {
                        // Part 1: Multiple Choice Options A, B, C, D
                        item {
                            MultipleChoiceSection(
                                question = currentQuestion,
                                selectedIndex = session.selectedOptions[currentQuestion.id],
                                isChecked = session.isAnswerCheckedForCurrent && session.practiceMode == PracticeMode.INSTANT_PRACTICE,
                                onOptionSelected = { optIndex ->
                                    viewModel.selectOption(currentQuestion.id, optIndex)
                                }
                            )
                        }
                    } else {
                        // Part 2: True / False 4 statements
                        item {
                            TrueFalseContextSection(
                                question = currentQuestion,
                                userAnswers = session.trueFalseSelections[currentQuestion.id] ?: emptyMap(),
                                isChecked = session.isAnswerCheckedForCurrent && session.practiceMode == PracticeMode.INSTANT_PRACTICE,
                                onStatementAnswer = { key, isTrue ->
                                    viewModel.selectTrueFalse(currentQuestion.id, key, isTrue)
                                },
                                onCheckClick = {
                                    viewModel.checkCurrentAnswer()
                                }
                            )
                        }
                    }

                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }
                }
            }
        }

        // Bottom Controls Bar / Instant Feedback Drawer
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
        ) {
            if (session.practiceMode == PracticeMode.INSTANT_PRACTICE && session.isAnswerCheckedForCurrent) {
                // Quizizz-style Instant Feedback Banner
                InstantFeedbackBanner(
                    isCorrect = session.currentQuestionCorrect,
                    explanation = currentQuestion?.explanation.orEmpty(),
                    isLast = session.currentQuestionIndex == totalCount - 1,
                    onContinue = { viewModel.nextQuestion() }
                )
            } else {
                // Regular Exam / Challenge Bottom Bar
                StandardBottomBar(
                    canGoBack = session.currentQuestionIndex > 0,
                    isLastQuestion = session.currentQuestionIndex == totalCount - 1,
                    onPrevious = { viewModel.previousQuestion() },
                    onNext = {
                        if (session.currentQuestionIndex < totalCount - 1) {
                            viewModel.nextQuestion()
                        } else {
                            showSubmitConfirmDialog = true
                        }
                    },
                    onSubmitClick = { showSubmitConfirmDialog = true }
                )
            }
        }
    }

    // Question Navigator Bottom Sheet
    if (showQuestionNavigatorSheet) {
        ModalBottomSheet(
            onDismissRequest = { showQuestionNavigatorSheet = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                Text(
                    text = "Danh sách câu hỏi (${session.questions.size} câu)",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = QuizPurpleDark
                )
                Spacer(modifier = Modifier.height(14.dp))

                val rowCount = (session.questions.size + 4) / 5
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    for (r in 0 until rowCount) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            for (c in 0 until 5) {
                                val idx = r * 5 + c
                                if (idx < session.questions.size) {
                                    val q = session.questions[idx]
                                    val isAnswered = if (q.type == QuestionType.MULTIPLE_CHOICE) {
                                        session.selectedOptions.containsKey(q.id)
                                    } else {
                                        val map = session.trueFalseSelections[q.id]
                                        map != null && map.size == 4
                                    }
                                    val isCurrent = idx == session.currentQuestionIndex

                                    Surface(
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(44.dp)
                                            .clip(RoundedCornerShape(10.dp))
                                            .clickable {
                                                viewModel.jumpToQuestion(idx)
                                                showQuestionNavigatorSheet = false
                                            },
                                        color = when {
                                            isCurrent -> QuizPurplePrimary
                                            isAnswered -> QuizOptionGreen
                                            else -> Color(0xFFE2E8F0)
                                        }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = "${idx + 1}",
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = if (isCurrent || isAnswered) Color.White else Color(0xFF475569)
                                            )
                                        }
                                    }
                                } else {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        showQuestionNavigatorSheet = false
                        showSubmitConfirmDialog = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = QuizPurpleDark),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Nộp bài ngay", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Exit Confirmation Dialog
    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Thoát bài luyện tập?", fontWeight = FontWeight.Bold) },
            text = { Text("Kết quả hiện tại sẽ không được lưu nếu bạn thoát ngay lúc này. Bạn có chắc chắn muốn quay về trang chủ?") },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        viewModel.navigateTo(AppScreen.HOME)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuizError)
                ) {
                    Text("Thoát", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Làm tiếp")
                }
            }
        )
    }

    // Submit Confirmation Dialog
    if (showSubmitConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showSubmitConfirmDialog = false },
            title = { Text("Xác nhận nộp bài?", fontWeight = FontWeight.Bold, color = QuizPurpleDark) },
            text = {
                Text("Toàn bộ câu trả lời của bạn sẽ được chấm điểm và lưu vào cơ sở dữ liệu. Bạn có muốn nộp bài bây giờ?")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showSubmitConfirmDialog = false
                        viewModel.submitQuiz()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuizOptionGreen),
                    modifier = Modifier.testTag("confirm_submit_button")
                ) {
                    Text("Nộp bài ngay", fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showSubmitConfirmDialog = false }) {
                    Text("Kiểm tra lại")
                }
            }
        )
    }
}

@Composable
fun QuizTopBar(
    current: Int,
    total: Int,
    progress: Float,
    streak: Int,
    timeSeconds: Int,
    isTimerActive: Boolean,
    isSoundOn: Boolean,
    onSoundToggle: () -> Unit,
    onExitClick: () -> Unit,
    onGridClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(top = 10.dp, start = 12.dp, end = 12.dp, bottom = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onExitClick) {
                Icon(Icons.Default.Close, contentDescription = "Thoát", tint = Color(0xFF64748B))
            }

            // Streak or Timer center pill
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (streak > 1) {
                    Surface(
                        color = Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = Color(0xFFD97706),
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "Streak ${streak}x",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Color(0xFFB45309)
                            )
                        }
                    }
                }

                if (isTimerActive) {
                    val minutes = timeSeconds / 60
                    val secs = timeSeconds % 60
                    Surface(
                        color = if (timeSeconds < 120) Color(0xFFFEE2E2) else Color(0xFFEDE9FE),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = String.format("%02d:%02d", minutes, secs),
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (timeSeconds < 120) Color(0xFFDC2626) else QuizPurplePrimary
                        )
                    }
                }

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(
                        text = "Câu $current / $total",
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }
            }

            Row {
                IconButton(onClick = onSoundToggle) {
                    Icon(
                        imageVector = if (isSoundOn) Icons.Default.VolumeUp else Icons.Default.VolumeOff,
                        contentDescription = "Âm thanh",
                        tint = if (isSoundOn) QuizPurplePrimary else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
                IconButton(onClick = onGridClick) {
                    Icon(Icons.Default.GridView, contentDescription = "Xem tất cả", tint = Color(0xFF64748B))
                }
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        // Progress bar
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp)
                .clip(CircleShape),
            color = QuizPurplePrimary,
            trackColor = Color(0xFFE2E8F0)
        )
    }
}

@Composable
fun QuestionHeaderCard(
    question: QuestionItem,
    index: Int,
    total: Int,
    studentName: String,
    className: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (question.part == 1) Color(0xFFEDE9FE) else Color(0xFFFEF3C7),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = if (question.part == 1) "PHẦN 1: TRẮC NGHIỆM ĐƠN" else "PHẦN 2: ĐÚNG / SAI",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (question.part == 1) QuizPurpleDark else Color(0xFFB45309)
                    )
                }

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = question.level,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF475569)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            if (question.contextTitle.isNotEmpty()) {
                Surface(
                    color = Color(0xFFF8FAFC),
                    shape = RoundedCornerShape(12.dp),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(
                            text = question.contextTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = QuizPurpleDark
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = question.contextDescription,
                            fontSize = 13.sp,
                            color = Color(0xFF334155),
                            lineHeight = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = "Câu $index: ${question.prompt}",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A),
                lineHeight = 22.sp
            )
        }
    }
}

@Composable
fun MultipleChoiceSection(
    question: QuestionItem,
    selectedIndex: Int?,
    isChecked: Boolean,
    onOptionSelected: (Int) -> Unit
) {
    val optionColors = listOf(QuizOptionRed, QuizOptionBlue, QuizOptionYellow, QuizOptionGreen)

    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        question.options.forEachIndexed { idx, optText ->
            val isSelected = selectedIndex == idx
            val isCorrect = idx == question.correctOptionIndex

            val backgroundColor = when {
                isChecked && isCorrect -> Color(0xFFDCFCE7)
                isChecked && isSelected && !isCorrect -> Color(0xFFFEE2E2)
                isSelected -> optionColors[idx % 4].copy(alpha = 0.15f)
                else -> Color.White
            }

            val borderColor = when {
                isChecked && isCorrect -> QuizSuccess
                isChecked && isSelected && !isCorrect -> QuizError
                isSelected -> optionColors[idx % 4]
                else -> Color(0xFFE2E8F0)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .border(
                        width = if (isSelected || (isChecked && isCorrect)) 2.5.dp else 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .clickable { onOptionSelected(idx) }
                    .testTag("option_${question.id}_$idx"),
                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(optionColors[idx % 4]),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = ('A' + idx).toString(),
                            fontWeight = FontWeight.Black,
                            color = Color.White,
                            fontSize = 16.sp
                        )
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    Text(
                        text = optText.substringAfter(". "),
                        fontSize = 14.5.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = Color(0xFF1E293B),
                        modifier = Modifier.weight(1f)
                    )

                    if (isChecked && isCorrect) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Đúng",
                            tint = QuizSuccess,
                            modifier = Modifier.size(24.dp)
                        )
                    } else if (isChecked && isSelected && !isCorrect) {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = "Sai",
                            tint = QuizError,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TrueFalseContextSection(
    question: QuestionItem,
    userAnswers: Map<String, Boolean>,
    isChecked: Boolean,
    onStatementAnswer: (String, Boolean) -> Unit,
    onCheckClick: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        question.statements.forEach { statement ->
            val userChoice = userAnswers[statement.key]
            val hasAnswered = userChoice != null

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = Color(0xFFEDE9FE),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "Ý ${statement.key}) [${statement.level}]",
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuizPurpleDark
                            )
                        }

                        if (isChecked && hasAnswered) {
                            val isCorrect = userChoice == statement.isTrue
                            Surface(
                                color = if (isCorrect) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                                shape = RoundedCornerShape(6.dp)
                            ) {
                                Text(
                                    text = if (isCorrect) "CHÍNH XÁC" else "CHƯA ĐÚNG",
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCorrect) QuizSuccess else QuizError
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = statement.text,
                        fontSize = 13.5.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1E293B),
                        lineHeight = 19.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        // "ĐÚNG" Button
                        TrueFalseButton(
                            text = "ĐÚNG",
                            isSelected = userChoice == true,
                            isCorrectAnswer = isChecked && statement.isTrue,
                            activeColor = QuizSuccess,
                            onClick = { onStatementAnswer(statement.key, true) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("tf_${statement.key}_true")
                        )

                        // "SAI" Button
                        TrueFalseButton(
                            text = "SAI",
                            isSelected = userChoice == false,
                            isCorrectAnswer = isChecked && !statement.isTrue,
                            activeColor = QuizError,
                            onClick = { onStatementAnswer(statement.key, false) },
                            modifier = Modifier
                                .weight(1f)
                                .testTag("tf_${statement.key}_false")
                        )
                    }

                    // Explanation in practice mode
                    if (isChecked && statement.explanation.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Surface(
                            color = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "💡 Giải thích: ${statement.explanation}",
                                modifier = Modifier.padding(8.dp),
                                fontSize = 12.sp,
                                color = Color(0xFF475569),
                                lineHeight = 16.sp
                            )
                        }
                    }
                }
            }
        }

        // In practice mode, if all 4 are selected but not checked yet, show "Kiểm tra đáp án"
        if (!isChecked && userAnswers.size == 4) {
            Button(
                onClick = onCheckClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = QuizPurpleDark),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Kiểm tra 4 ý này", fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TrueFalseButton(
    text: String,
    isSelected: Boolean,
    isCorrectAnswer: Boolean,
    activeColor: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .height(40.dp)
            .clip(RoundedCornerShape(10.dp))
            .border(
                width = if (isSelected || isCorrectAnswer) 2.dp else 1.dp,
                color = when {
                    isCorrectAnswer -> QuizSuccess
                    isSelected -> activeColor
                    else -> Color(0xFFCBD5E1)
                },
                shape = RoundedCornerShape(10.dp)
            )
            .clickable { onClick() },
        color = when {
            isCorrectAnswer -> Color(0xFFDCFCE7)
            isSelected -> activeColor.copy(alpha = 0.15f)
            else -> Color.White
        }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = text,
                fontWeight = if (isSelected || isCorrectAnswer) FontWeight.Black else FontWeight.Bold,
                fontSize = 13.sp,
                color = when {
                    isCorrectAnswer -> QuizSuccess
                    isSelected -> activeColor
                    else -> Color(0xFF475569)
                }
            )
        }
    }
}

@Composable
fun InstantFeedbackBanner(
    isCorrect: Boolean,
    explanation: String,
    isLast: Boolean,
    onContinue: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = if (isCorrect) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
        shape = RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp),
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                        contentDescription = null,
                        tint = if (isCorrect) QuizSuccess else QuizError,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = if (isCorrect) "CHÍNH XÁC! 🎉" else "CHƯA CHÍNH XÁC! 💡",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isCorrect) Color(0xFF166534) else Color(0xFF991B1B)
                    )
                }

                Button(
                    onClick = onContinue,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isCorrect) QuizSuccess else QuizPurpleDark
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("continue_button")
                ) {
                    Text(
                        text = if (isLast) "Xem Kết Quả" else "Tiếp Tục",
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }

            if (explanation.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Giải thích: $explanation",
                    fontSize = 12.5.sp,
                    color = Color(0xFF334155),
                    lineHeight = 17.sp
                )
            }
        }
    }
}

@Composable
fun StandardBottomBar(
    canGoBack: Boolean,
    isLastQuestion: Boolean,
    onPrevious: () -> Unit,
    onNext: () -> Unit,
    onSubmitClick: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = Color.White,
        shadowElevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedButton(
                onClick = onPrevious,
                enabled = canGoBack,
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text("Câu trước")
            }

            if (isLastQuestion) {
                Button(
                    onClick = onSubmitClick,
                    colors = ButtonDefaults.buttonColors(containerColor = QuizOptionGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("submit_quiz_button")
                ) {
                    Text("Nộp bài ngay", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            } else {
                Button(
                    onClick = onNext,
                    colors = ButtonDefaults.buttonColors(containerColor = QuizPurplePrimary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("next_button")
                ) {
                    Text("Tiếp theo", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}
