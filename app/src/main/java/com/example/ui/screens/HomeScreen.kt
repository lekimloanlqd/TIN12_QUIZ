package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DefaultQuizData
import com.example.data.model.PracticeMode
import com.example.data.model.Quiz
import com.example.ui.theme.QuizBackground
import com.example.ui.theme.QuizOptionBlue
import com.example.ui.theme.QuizOptionGreen
import com.example.ui.theme.QuizOptionYellow
import com.example.ui.theme.QuizPurpleDark
import com.example.ui.theme.QuizPurpleLight
import com.example.ui.theme.QuizPurplePrimary
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var showJoinDialogForQuiz by remember { mutableStateOf<Quiz?>(null) }
    var showPinDialog by remember { mutableStateOf(false) }
    var showTeacherLoginDialog by remember { mutableStateOf(false) }

    val quizzes = remember { DefaultQuizData.sampleQuizzes }
    val filteredQuizzes = quizzes.filter {
        it.title.contains(searchQuery, ignoreCase = true) ||
                it.subject.contains(searchQuery, ignoreCase = true) ||
                it.pinCode.contains(searchQuery)
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(QuizBackground)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                Brush.linearGradient(
                                    listOf(QuizPurplePrimary, QuizPurpleDark)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = "Quiz Smart Logo",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "QUIZ SMART",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = QuizPurpleDark,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = "Học vui – Luyện tập thông minh",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF64748B)
                        )
                    }
                }

                // Teacher Dashboard Button
                IconButton(
                    onClick = {
                        if (viewModel.isTeacherLoggedIn.value) {
                            viewModel.navigateTo(AppScreen.TEACHER_DASHBOARD)
                        } else {
                            showTeacherLoginDialog = true
                        }
                    },
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFEDE9FE))
                        .testTag("teacher_portal_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = "Trang Giáo Viên",
                        tint = QuizPurplePrimary
                    )
                }
            }
        }

        // Hero Welcome Card (Quizizz Gamified Aesthetic)
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
                                listOf(QuizPurplePrimary, QuizPurpleDark, Color(0xFF3B0764))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                color = QuizOptionYellow,
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "TIN HỌC 12 • THPT",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Surface(
                                color = Color.White.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "26 Ý ĐÁNH GIÁ",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.White
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Ôn tập Bài 7: HTML & Cấu trúc trang web",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 28.sp
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Tham gia ngay không cần đăng nhập tài khoản. Có chấm điểm, xếp hạng và giải thích chi tiết từng câu!",
                            color = Color.White.copy(alpha = 0.85f),
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )

                        Spacer(modifier = Modifier.height(18.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            // Quick start button for Bai 7
                            Button(
                                onClick = { showJoinDialogForQuiz = DefaultQuizData.defaultQuiz },
                                modifier = Modifier
                                    .weight(1.3f)
                                    .testTag("quick_start_button"),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = QuizOptionGreen,
                                    contentColor = Color.White
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    Icons.Default.PlayArrow,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Vào làm bài",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }

                            // Enter PIN button
                            OutlinedButton(
                                onClick = { showPinDialog = true },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("enter_pin_button"),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    contentColor = Color.White
                                ),
                                border = ButtonDefaults.outlinedButtonBorder.copy(
                                    brush = Brush.linearGradient(listOf(Color.White, Color.White.copy(alpha = 0.5f)))
                                ),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                Icon(
                                    Icons.Default.Key,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Mã PIN",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 13.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Search Bar
        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("search_quiz_input"),
                placeholder = { Text("Tìm kiếm bài tập, mã PIN...", color = Color.Gray) },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = null, tint = QuizPurplePrimary)
                },
                singleLine = true,
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = QuizPurplePrimary,
                    unfocusedBorderColor = Color(0xFFE2E8F0),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
        }

        // Section Title: Danh sách bài ôn tập
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Bài Luyện Tập Trắc Nghiệm",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = Color(0xFF1E293B)
                )
                Text(
                    text = "${filteredQuizzes.size} bài",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = QuizPurplePrimary
                )
            }
        }

        // List of Quizzes
        items(filteredQuizzes) { quiz ->
            QuizCard(
                quiz = quiz,
                onSelect = { showJoinDialogForQuiz = quiz }
            )
        }

        item {
            Spacer(modifier = Modifier.height(24.dp))
        }
    }

    // Join Modal / Dialog (Guest Mode: Name + Class)
    if (showJoinDialogForQuiz != null) {
        val targetQuiz = showJoinDialogForQuiz!!
        GuestJoinModal(
            quiz = targetQuiz,
            onDismiss = { showJoinDialogForQuiz = null },
            onConfirm = { name, className, mode ->
                showJoinDialogForQuiz = null
                viewModel.startQuizSession(
                    quiz = targetQuiz,
                    studentName = name,
                    className = className,
                    isGuest = true,
                    mode = mode
                )
            }
        )
    }

    // Enter PIN Dialog
    if (showPinDialog) {
        PinJoinModal(
            onDismiss = { showPinDialog = false },
            onPinSubmitted = { enteredPin ->
                val matchedQuiz = quizzes.find { it.pinCode.trim() == enteredPin.trim() }
                if (matchedQuiz != null) {
                    showPinDialog = false
                    showJoinDialogForQuiz = matchedQuiz
                }
            }
        )
    }

    // Teacher Login Dialog
    if (showTeacherLoginDialog) {
        TeacherLoginModal(
            onDismiss = { showTeacherLoginDialog = false },
            onSuccess = {
                viewModel.isTeacherLoggedIn.value = true
                showTeacherLoginDialog = false
                viewModel.navigateTo(AppScreen.TEACHER_DASHBOARD)
            }
        )
    }
}

@Composable
fun QuizCard(
    quiz: Quiz,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .clickable { onSelect() }
            .testTag("quiz_card_${quiz.id}"),
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
                    color = if (quiz.id == "bai-7-html") Color(0xFFEDE9FE) else Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = quiz.subject,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = if (quiz.id == "bai-7-html") QuizPurplePrimary else Color(0xFF475569)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Key,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = QuizOptionYellow
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "PIN: ${quiz.pinCode}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1E293B)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = quiz.title,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF0F172A)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Giáo viên: ${quiz.teacherName}",
                fontSize = 12.sp,
                color = Color(0xFF64748B)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = QuizOptionGreen,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${quiz.totalEvaluationPoints} ý đánh giá",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Timer,
                            contentDescription = null,
                            tint = QuizOptionBlue,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "${quiz.durationMinutes} phút",
                            fontSize = 12.sp,
                            color = Color(0xFF475569)
                        )
                    }
                }

                Button(
                    onClick = onSelect,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = QuizPurplePrimary
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(36.dp)
                ) {
                    Text(
                        text = "Làm bài",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GuestJoinModal(
    quiz: Quiz,
    onDismiss: () -> Unit,
    onConfirm: (name: String, className: String, mode: PracticeMode) -> Unit
) {
    var studentName by remember { mutableStateOf("") }
    var className by remember { mutableStateOf("12/1B") }
    var isClassDropdownExpanded by remember { mutableStateOf(false) }
    var selectedMode by remember { mutableStateOf(PracticeMode.INSTANT_PRACTICE) }
    var errorMessage by remember { mutableStateOf("") }

    val classes = remember { DefaultQuizData.defaultClasses }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Column {
                Text(
                    text = "QUIZ SMART – THAM GIA BÀI TẬP",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = QuizPurpleDark
                )
                Text(
                    text = quiz.title,
                    fontSize = 13.sp,
                    color = Color(0xFF64748B),
                    fontWeight = FontWeight.Medium
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                // Info banner: Guest mode active
                Surface(
                    color = Color(0xFFDCFCE7),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF16A34A),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Chế độ Khách (Guest): Không cần tạo tài khoản hay mật khẩu!",
                            fontSize = 11.5.sp,
                            color = Color(0xFF166534),
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                // Họ và tên Input
                OutlinedTextField(
                    value = studentName,
                    onValueChange = {
                        studentName = it
                        errorMessage = ""
                    },
                    label = { Text("Họ và tên học sinh *") },
                    placeholder = { Text("Ví dụ: Nguyễn Mai Anh") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("student_name_input"),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp)
                )

                // Lớp Dropdown
                ExposedDropdownMenuBox(
                    expanded = isClassDropdownExpanded,
                    onExpandedChange = { isClassDropdownExpanded = !isClassDropdownExpanded }
                ) {
                    OutlinedTextField(
                        value = className,
                        onValueChange = { className = it },
                        label = { Text("Lớp học *") },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(expanded = isClassDropdownExpanded)
                        },
                        readOnly = false,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .testTag("class_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = isClassDropdownExpanded,
                        onDismissRequest = { isClassDropdownExpanded = false }
                    ) {
                        classes.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(c) },
                                onClick = {
                                    className = c
                                    isClassDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                // Choose Practice Mode
                Text(
                    text = "Chọn chế độ ôn tập:",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                PracticeMode.values().forEach { mode ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .border(
                                width = if (selectedMode == mode) 2.dp else 1.dp,
                                color = if (selectedMode == mode) QuizPurplePrimary else Color(0xFFE2E8F0),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedMode = mode },
                        color = if (selectedMode == mode) Color(0xFFF5F3FF) else Color.White
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = selectedMode == mode,
                                onClick = { selectedMode = mode },
                                colors = RadioButtonDefaults.colors(selectedColor = QuizPurplePrimary)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Column {
                                Text(
                                    text = mode.displayName,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (selectedMode == mode) QuizPurpleDark else Color(0xFF1E293B)
                                )
                                Text(
                                    text = mode.description,
                                    fontSize = 11.sp,
                                    color = Color(0xFF64748B)
                                )
                            }
                        }
                    }
                }

                if (errorMessage.isNotEmpty()) {
                    Text(
                        text = errorMessage,
                        color = Color.Red,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (studentName.trim().isEmpty()) {
                        errorMessage = "Vui lòng nhập Họ và tên để bắt đầu làm bài!"
                    } else if (className.trim().isEmpty()) {
                        errorMessage = "Vui lòng chọn hoặc nhập Lớp học!"
                    } else {
                        onConfirm(studentName.trim(), className.trim(), selectedMode)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuizPurplePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("start_quiz_button")
            ) {
                Text("BẮT ĐẦU LÀM BÀI", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Đóng", color = Color(0xFF64748B))
            }
        }
    )
}

@Composable
fun PinJoinModal(
    onDismiss: () -> Unit,
    onPinSubmitted: (String) -> Unit
) {
    var pin by remember { mutableStateOf("") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Tham gia bằng mã PIN",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = QuizPurpleDark
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Nhập mã PIN 6 chữ số do giáo viên cung cấp để vào bài tập ngay:",
                    fontSize = 13.sp,
                    color = Color(0xFF475569)
                )

                OutlinedTextField(
                    value = pin,
                    onValueChange = {
                        if (it.length <= 6) {
                            pin = it
                            error = ""
                        }
                    },
                    placeholder = { Text("Ví dụ: 789123") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("pin_code_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Surface(
                    color = Color(0xFFF1F5F9),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Gợi ý: Mã PIN bài 7 là 789123",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 11.5.sp,
                        color = Color(0xFF475569)
                    )
                }

                if (error.isNotEmpty()) {
                    Text(text = error, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (pin.trim().isEmpty()) {
                        error = "Vui lòng nhập mã PIN!"
                    } else if (DefaultQuizData.sampleQuizzes.none { it.pinCode.trim() == pin.trim() }) {
                        error = "Không tìm thấy bài tập nào với mã PIN này!"
                    } else {
                        onPinSubmitted(pin.trim())
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuizPurplePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("submit_pin_button")
            ) {
                Text("Vào bài tập", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color(0xFF64748B))
            }
        }
    )
}

@Composable
fun TeacherLoginModal(
    onDismiss: () -> Unit,
    onSuccess: () -> Unit
) {
    var username by remember { mutableStateOf("giaovien") }
    var password by remember { mutableStateOf("123456") }
    var error by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.AdminPanelSettings,
                    contentDescription = null,
                    tint = QuizPurplePrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Đăng Nhập Giáo Viên",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = QuizPurpleDark
                )
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Dành cho giáo viên quản lý học sinh, bài tập và xem bảng điểm thống kê.",
                    fontSize = 12.5.sp,
                    color = Color(0xFF64748B)
                )

                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it },
                    label = { Text("Tên tài khoản") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_username_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Mật khẩu") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("teacher_password_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Surface(
                    color = Color(0xFFEDE9FE),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "Tài khoản mẫu: giaovien / 123456",
                        modifier = Modifier.padding(8.dp),
                        fontSize = 11.5.sp,
                        color = QuizPurplePrimary,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                if (error.isNotEmpty()) {
                    Text(text = error, color = Color.Red, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (username.isNotEmpty() && password.isNotEmpty()) {
                        onSuccess()
                    } else {
                        error = "Vui lòng nhập tài khoản và mật khẩu!"
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = QuizPurplePrimary),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("teacher_login_submit_button")
            ) {
                Text("Đăng nhập", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy", color = Color(0xFF64748B))
            }
        }
    )
}
