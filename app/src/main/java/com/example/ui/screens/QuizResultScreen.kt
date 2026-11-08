package com.example.ui.screens

import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DefaultQuizData
import com.example.data.model.QuestionItem
import com.example.data.model.QuestionType
import com.example.data.model.TrueFalseStatement
import com.example.ui.theme.QuizError
import com.example.ui.theme.QuizOptionBlue
import com.example.ui.theme.QuizOptionGreen
import com.example.ui.theme.QuizOptionYellow
import com.example.ui.theme.QuizPurpleDark
import com.example.ui.theme.QuizPurpleLight
import com.example.ui.theme.QuizPurplePrimary
import com.example.ui.theme.QuizSuccess
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel

@Composable
fun QuizResultScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val attempt by viewModel.latestAttempt.collectAsState()
    val session by viewModel.sessionState.collectAsState()
    val context = LocalContext.current

    var selectedFilter by remember { mutableStateOf("ALL") } // "ALL", "WRONG", "CORRECT"

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    if (attempt == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không tìm thấy kết quả làm bài.")
        }
        return
    }

    val att = attempt!!
    val minutes = att.durationSeconds / 60
    val seconds = att.durationSeconds % 60

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Hero Score & Celebration Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.Transparent)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                listOf(QuizPurplePrimary, QuizPurpleDark, Color(0xFF2E1065))
                            )
                        )
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.EmojiEvents,
                                contentDescription = null,
                                tint = QuizOptionYellow,
                                modifier = Modifier.size(40.dp)
                            )
                        }

                        Text(
                            text = "HOÀN THÀNH BÀI ÔN TẬP!",
                            fontWeight = FontWeight.Black,
                            fontSize = 20.sp,
                            color = Color.White,
                            letterSpacing = 1.sp
                        )

                        Text(
                            text = "${att.studentName} • Lớp ${att.className}",
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.85f)
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // Large Score
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = String.format("%.1f", att.score),
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Text(
                                text = " / 10.0 điểm",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White.copy(alpha = 0.7f),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Surface(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text(
                                text = "Đạt ${att.correctCount} / ${att.totalPoints} ý đánh giá (${att.accuracyRate}%)",
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                fontSize = 12.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }
            }
        }

        // Stats Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Correct Count Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Số ý đúng", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${att.correctCount}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuizSuccess
                        )
                    }
                }

                // Incorrect Count Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Số ý sai", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${att.incorrectCount}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuizError
                        )
                    }
                }

                // Time Duration Card
                Card(
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Thời gian", fontSize = 12.sp, color = Color(0xFF64748B))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = String.format("%02d:%02d", minutes, seconds),
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = QuizOptionBlue
                        )
                    }
                }
            }
        }

        // Action Buttons Row (Làm lại, Chia sẻ, Về trang chủ)
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = {
                        viewModel.startQuizSession(
                            quiz = session.quiz,
                            studentName = att.studentName,
                            className = att.className,
                            isGuest = true,
                            mode = session.practiceMode
                        )
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("retry_quiz_button"),
                    colors = ButtonDefaults.buttonColors(containerColor = QuizPurplePrimary),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Làm lại", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        val shareIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(
                                Intent.EXTRA_TEXT,
                                "Kết quả Quiz Smart: ${att.studentName} (Lớp ${att.className}) đạt ${att.score}/10 điểm (${att.correctCount}/${att.totalPoints} ý đúng) môn Tin học 12 – Bài 7: HTML!"
                            )
                            type = "text/plain"
                        }
                        context.startActivity(Intent.createChooser(shareIntent, "Chia sẻ kết quả"))
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Chia sẻ", fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = { viewModel.navigateTo(AppScreen.HOME) },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(14.dp)
                ) {
                    Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Trang chủ", fontWeight = FontWeight.Bold)
                }
            }
        }

        // Section Title: Chi tiết đáp án và lời giải
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Chi Tiết Đáp Án & Lời Giải",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "Xem giải thích chi tiết trích từ tài liệu gốc cho từng câu hỏi:",
                    fontSize = 12.5.sp,
                    color = Color(0xFF64748B)
                )

                // Filter chips
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    FilterChip(
                        selected = selectedFilter == "ALL",
                        onClick = { selectedFilter = "ALL" },
                        label = { Text("Tất cả (${session.questions.size} câu)") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = QuizPurplePrimary,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = selectedFilter == "WRONG",
                        onClick = { selectedFilter = "WRONG" },
                        label = { Text("Xem câu sai") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = QuizError,
                            selectedLabelColor = Color.White
                        )
                    )
                    FilterChip(
                        selected = selectedFilter == "CORRECT",
                        onClick = { selectedFilter = "CORRECT" },
                        label = { Text("Xem câu đúng") },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = QuizSuccess,
                            selectedLabelColor = Color.White
                        )
                    )
                }
            }
        }

        // Review list of questions
        val filteredQuestions = session.questions.filter { q ->
            if (selectedFilter == "ALL") true
            else {
                val isCorrect = if (q.type == QuestionType.MULTIPLE_CHOICE) {
                    val sel = session.selectedOptions[q.id]
                    sel != null && sel == q.correctOptionIndex
                } else {
                    val tf = session.trueFalseSelections[q.id] ?: emptyMap()
                    q.statements.all { tf[it.key] == it.isTrue }
                }
                if (selectedFilter == "WRONG") !isCorrect else isCorrect
            }
        }

        itemsIndexed(filteredQuestions) { idx, q ->
            QuestionReviewCard(
                question = q,
                index = idx + 1,
                selectedOption = session.selectedOptions[q.id],
                trueFalseAnswers = session.trueFalseSelections[q.id] ?: emptyMap()
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun QuestionReviewCard(
    question: QuestionItem,
    index: Int,
    selectedOption: Int?,
    trueFalseAnswers: Map<String, Boolean>
) {
    val isPart1 = question.type == QuestionType.MULTIPLE_CHOICE
    val isOverallCorrect = if (isPart1) {
        selectedOption != null && selectedOption == question.correctOptionIndex
    } else {
        question.statements.all { trueFalseAnswers[it.key] == it.isTrue }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = CardDefaults.outlinedCardBorder()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    color = if (isOverallCorrect) Color(0xFFDCFCE7) else Color(0xFFFEE2E2),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (isOverallCorrect) Icons.Default.CheckCircle else Icons.Default.Close,
                            contentDescription = null,
                            tint = if (isOverallCorrect) QuizSuccess else QuizError,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isOverallCorrect) "ĐÚNG" else "CHƯA ĐÚNG",
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = if (isOverallCorrect) Color(0xFF166534) else Color(0xFF991B1B)
                        )
                    }
                }

                Text(
                    text = question.level,
                    fontSize = 11.sp,
                    color = Color(0xFF64748B)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (question.contextTitle.isNotEmpty()) {
                Text(
                    text = question.contextTitle,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    color = QuizPurpleDark
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text(
                text = "${question.prompt}",
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1E293B)
            )

            Spacer(modifier = Modifier.height(10.dp))

            if (isPart1) {
                // Multiple Choice Review
                val correctChar = ('A' + question.correctOptionIndex).toString()
                val selectedChar = if (selectedOption != null) ('A' + selectedOption).toString() else "Chưa chọn"

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Bạn chọn:", fontSize = 11.sp, color = Color.Gray)
                            Text(
                                text = selectedChar,
                                fontWeight = FontWeight.Bold,
                                color = if (isOverallCorrect) QuizSuccess else QuizError
                            )
                        }
                    }

                    Surface(
                        color = Color(0xFFDCFCE7),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Column(modifier = Modifier.padding(8.dp)) {
                            Text("Đáp án đúng:", fontSize = 11.sp, color = Color(0xFF166534))
                            Text(
                                text = correctChar,
                                fontWeight = FontWeight.Bold,
                                color = QuizSuccess
                            )
                        }
                    }
                }

                if (question.explanation.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = "💡 Giải thích: ${question.explanation}",
                            modifier = Modifier.padding(10.dp),
                            fontSize = 12.5.sp,
                            color = Color(0xFF334155),
                            lineHeight = 17.sp
                        )
                    }
                }
            } else {
                // Part 2: True/False Review
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    question.statements.forEach { st ->
                        val userChoice = trueFalseAnswers[st.key]
                        val isStCorrect = userChoice == st.isTrue
                        val userText = if (userChoice == null) "Chưa chọn" else if (userChoice) "Đúng" else "Sai"
                        val keyText = if (st.isTrue) "Đúng" else "Sai"

                        Surface(
                            color = Color(0xFFF8FAFC),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text(
                                    text = "${st.key}) ${st.text}",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF1E293B)
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Bạn chọn: $userText",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (isStCorrect) QuizSuccess else QuizError
                                    )
                                    Text(
                                        text = "Đáp án: $keyText",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = QuizSuccess
                                    )
                                }
                                if (st.explanation.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = "• ${st.explanation}",
                                        fontSize = 11.5.sp,
                                        color = Color(0xFF64748B)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
