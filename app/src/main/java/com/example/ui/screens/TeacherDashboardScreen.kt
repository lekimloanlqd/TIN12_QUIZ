package com.example.ui.screens

import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
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
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults.SecondaryIndicator
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.local.DefaultQuizData
import com.example.data.model.ParticipationType
import com.example.data.model.QuizAttempt
import com.example.ui.theme.QuizError
import com.example.ui.theme.QuizOptionBlue
import com.example.ui.theme.QuizOptionGreen
import com.example.ui.theme.QuizOptionYellow
import com.example.ui.theme.QuizPurpleDark
import com.example.ui.theme.QuizPurplePrimary
import com.example.ui.theme.QuizSuccess
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.QuizViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TeacherDashboardScreen(
    viewModel: QuizViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current
    val attempts by viewModel.allAttempts.collectAsState()

    var selectedTabIndex by remember { mutableIntStateOf(0) }
    var selectedClass by remember { mutableStateOf("Tất cả") }
    var selectedType by remember { mutableStateOf("Tất cả") } // "Tất cả", "GUEST", "ACCOUNT"
    var searchQuery by remember { mutableStateOf("") }
    var detailAttempt by remember { mutableStateOf<QuizAttempt?>(null) }
    var showClearConfirmDialog by remember { mutableStateOf(false) }

    // Quizzes settings state
    var pinCode by remember { mutableStateOf("789123") }
    var isQuizOpen by remember { mutableStateOf(true) }
    var allowRetake by remember { mutableStateOf(true) }

    BackHandler {
        viewModel.navigateTo(AppScreen.HOME)
    }

    // Filter attempts
    val filteredAttempts = attempts.filter { att ->
        val matchesClass = selectedClass == "Tất cả" || att.className == selectedClass
        val matchesType = selectedType == "Tất cả" || att.participationType == selectedType
        val matchesSearch = att.studentName.contains(searchQuery, ignoreCase = true) ||
                att.className.contains(searchQuery, ignoreCase = true)
        matchesClass && matchesType && matchesSearch
    }

    val totalStudents = attempts.map { it.studentName }.distinct().size
    val totalAttempts = attempts.size
    val avgScore = if (attempts.isNotEmpty()) {
        String.format("%.1f", attempts.map { it.score }.average()).toDoubleOrNull() ?: 0.0
    } else 0.0
    val avgAccuracy = if (attempts.isNotEmpty()) {
        String.format("%.1f", attempts.map { it.accuracyRate }.average()).toDoubleOrNull() ?: 0.0
    } else 0.0

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FE))
    ) {
        // Teacher Dashboard Top Bar
        Surface(
            color = Color.White,
            shadowElevation = 4.dp
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(onClick = { viewModel.navigateTo(AppScreen.HOME) }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Trở về")
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Column {
                            Text(
                                text = "BẢNG ĐIỀU KHIỂN GIÁO VIÊN",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Black,
                                color = QuizPurpleDark
                            )
                            Text(
                                text = "Quản lý kết quả học sinh & bài tập",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            viewModel.isTeacherLoggedIn.value = false
                            viewModel.navigateTo(AppScreen.HOME)
                        }
                    ) {
                        Icon(
                            Icons.Default.ExitToApp,
                            contentDescription = "Đăng xuất",
                            tint = Color(0xFF64748B)
                        )
                    }
                }

                // Tab Navigation
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = Color.White,
                    contentColor = QuizPurplePrimary,
                    indicator = { tabPositions ->
                        SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = QuizPurplePrimary
                        )
                    }
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Analytics, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Tổng quan", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.TableChart, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Bảng điểm (${filteredAttempts.size})", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Assignment, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Bài tập & PIN", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    )
                }
            }
        }

        // Tab Content
        when (selectedTabIndex) {
            0 -> OverviewTabContent(
                totalStudents = totalStudents,
                totalAttempts = totalAttempts,
                avgScore = avgScore,
                avgAccuracy = avgAccuracy,
                attempts = attempts
            )
            1 -> ResultsTableTabContent(
                attempts = filteredAttempts,
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it },
                selectedClass = selectedClass,
                onClassChange = { selectedClass = it },
                selectedType = selectedType,
                onTypeChange = { selectedType = it },
                onDetailClick = { detailAttempt = it },
                onDeleteClick = { viewModel.deleteAttempt(it.attemptId) },
                onExportCsv = {
                    val csv = viewModel.getExportCsv()
                    val sendIntent = Intent().apply {
                        action = Intent.ACTION_SEND
                        putExtra(Intent.EXTRA_TEXT, csv)
                        putExtra(Intent.EXTRA_SUBJECT, "Bang_diem_Quiz_Smart.csv")
                        type = "text/csv"
                    }
                    context.startActivity(Intent.createChooser(sendIntent, "Xuất kết quả bảng điểm"))
                },
                onClearAll = { showClearConfirmDialog = true }
            )
            2 -> QuizSettingsTabContent(
                pinCode = pinCode,
                onPinChange = { pinCode = it },
                isQuizOpen = isQuizOpen,
                onQuizOpenToggle = { isQuizOpen = it },
                allowRetake = allowRetake,
                onAllowRetakeToggle = { allowRetake = it },
                onCopyLink = {
                    clipboardManager.setText(AnnotatedString("https://quizsmart.vercel.app/quiz/bai-7-html"))
                    Toast.makeText(context, "Đã sao chép link bài tập!", Toast.LENGTH_SHORT).show()
                },
                onCopyPin = {
                    clipboardManager.setText(AnnotatedString(pinCode))
                    Toast.makeText(context, "Đã sao chép mã PIN $pinCode!", Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    // Attempt Detail Dialog
    if (detailAttempt != null) {
        val att = detailAttempt!!
        val sdf = SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault())
        AlertDialog(
            onDismissRequest = { detailAttempt = null },
            title = {
                Column {
                    Text(
                        text = "Chi tiết bài làm: ${att.studentName}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp,
                        color = QuizPurpleDark
                    )
                    Text(
                        text = "Lớp ${att.className} • ${att.quizTitle}",
                        fontSize = 12.5.sp,
                        color = Color(0xFF64748B)
                    )
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = Color(0xFFEDE9FE),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Điểm số: ${att.score} / 10.0 điểm",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuizPurpleDark
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Số ý đúng: ${att.correctCount} / ${att.totalPoints} ý (${att.accuracyRate}%)",
                                fontSize = 13.sp,
                                color = Color(0xFF475569)
                            )
                            Text(
                                text = "Hình thức: ${if (att.participationType == ParticipationType.GUEST.name) "Khách (Guest)" else "Tài khoản học sinh"}",
                                fontSize = 13.sp,
                                color = Color(0xFF475569)
                            )
                            Text(
                                text = "Thời gian làm bài: ${att.durationSeconds} giây",
                                fontSize = 13.sp,
                                color = Color(0xFF475569)
                            )
                            Text(
                                text = "Thời điểm nộp: ${sdf.format(Date(att.submittedAt))}",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                            Text(
                                text = "Mã lượt làm bài: ${att.attemptId}",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )
                        }
                    }

                    Text(
                        text = "Tất cả 26 ý đánh giá đã được hệ thống ghi nhận chính xác theo đề thi Bài 7 Tin học 12.",
                        fontSize = 12.sp,
                        color = Color(0xFF334155)
                    )
                }
            },
            confirmButton = {
                Button(onClick = { detailAttempt = null }) {
                    Text("Đóng")
                }
            }
        )
    }

    // Clear All Attempts Dialog
    if (showClearConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showClearConfirmDialog = false },
            title = { Text("Xóa toàn bộ kết quả?", fontWeight = FontWeight.Bold) },
            text = { Text("Thao tác này sẽ xóa toàn bộ lịch sử làm bài của tất cả học sinh trong cơ sở dữ liệu. Bạn có chắc chắn không?") },
            confirmButton = {
                Button(
                    onClick = {
                        showClearConfirmDialog = false
                        viewModel.clearAllAttempts()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = QuizError)
                ) {
                    Text("Xóa tất cả")
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearConfirmDialog = false }) {
                    Text("Hủy")
                }
            }
        )
    }
}

@Composable
fun OverviewTabContent(
    totalStudents: Int,
    totalAttempts: Int,
    avgScore: Double,
    avgAccuracy: Double,
    attempts: List<QuizAttempt>
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // KPI Grid
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KpiCard(
                    title = "Tổng học sinh",
                    value = "$totalStudents",
                    color = QuizPurplePrimary,
                    icon = Icons.Default.Groups,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Lượt làm bài",
                    value = "$totalAttempts",
                    color = QuizOptionBlue,
                    icon = Icons.Default.Assignment,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                KpiCard(
                    title = "Điểm trung bình",
                    value = "$avgScore",
                    color = QuizOptionGreen,
                    icon = Icons.Default.CheckCircle,
                    modifier = Modifier.weight(1f)
                )
                KpiCard(
                    title = "Tỷ lệ đúng TB",
                    value = "$avgAccuracy%",
                    color = QuizOptionYellow,
                    icon = Icons.Default.Analytics,
                    modifier = Modifier.weight(1f)
                )
            }
        }

        // Section: Phân bố điểm số
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Phân Bố Điểm Số",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    val gioicount = attempts.count { it.score >= 8.5 }
                    val khaCount = attempts.count { it.score in 7.0..8.4 }
                    val tbCount = attempts.count { it.score in 5.0..6.9 }
                    val yeuCount = attempts.count { it.score < 5.0 }
                    val total = (attempts.size).coerceAtLeast(1)

                    ScoreDistRow(label = "Giỏi (8.5 - 10.0)", count = gioicount, total = total, color = QuizSuccess)
                    Spacer(modifier = Modifier.height(8.dp))
                    ScoreDistRow(label = "Khá (7.0 - 8.4)", count = khaCount, total = total, color = QuizOptionBlue)
                    Spacer(modifier = Modifier.height(8.dp))
                    ScoreDistRow(label = "Trung bình (5.0 - 6.9)", count = tbCount, total = total, color = QuizOptionYellow)
                    Spacer(modifier = Modifier.height(8.dp))
                    ScoreDistRow(label = "Chưa đạt (< 5.0)", count = yeuCount, total = total, color = QuizError)
                }
            }
        }

        // Section: Hình thức tham gia (Guest vs Account)
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Tỷ Lệ Hình Thức Tham Gia",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF1E293B)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    val guestCount = attempts.count { it.participationType == ParticipationType.GUEST.name }
                    val accountCount = attempts.count { it.participationType == ParticipationType.ACCOUNT.name }
                    val total = (attempts.size).coerceAtLeast(1)

                    ScoreDistRow(
                        label = "Khách (Guest - Không cần đăng nhập)",
                        count = guestCount,
                        total = total,
                        color = QuizPurplePrimary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    ScoreDistRow(
                        label = "Tài khoản học sinh",
                        count = accountCount,
                        total = total,
                        color = QuizOptionGreen
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun ScoreDistRow(
    label: String,
    count: Int,
    total: Int,
    color: Color
) {
    val pct = (count.toFloat() / total.toFloat()).coerceIn(0f, 1f)
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = label, fontSize = 12.5.sp, color = Color(0xFF334155))
            Text(
                text = "$count lượt (${(pct * 100).toInt()}%)",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { pct },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .clip(CircleShape),
            color = color,
            trackColor = Color(0xFFF1F5F9)
        )
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    color: Color,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = title, fontSize = 12.sp, color = Color(0xFF64748B))
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = value,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = color
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultsTableTabContent(
    attempts: List<QuizAttempt>,
    searchQuery: String,
    onSearchChange: (String) -> Unit,
    selectedClass: String,
    onClassChange: (String) -> Unit,
    selectedType: String,
    onTypeChange: (String) -> Unit,
    onDetailClick: (QuizAttempt) -> Unit,
    onDeleteClick: (QuizAttempt) -> Unit,
    onExportCsv: () -> Unit,
    onClearAll: () -> Unit
) {
    val classes = listOf("Tất cả") + DefaultQuizData.defaultClasses
    var isClassDropdownOpen by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Controls: Search & CSV Export
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = onSearchChange,
                    modifier = Modifier
                        .weight(1f)
                        .testTag("teacher_search_input"),
                    placeholder = { Text("Tìm theo họ tên...", fontSize = 13.sp) },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, modifier = Modifier.size(18.dp)) },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )

                Button(
                    onClick = onExportCsv,
                    colors = ButtonDefaults.buttonColors(containerColor = QuizOptionGreen),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("export_csv_button")
                ) {
                    Icon(Icons.Default.Download, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Excel/CSV", fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }

        // Filters Row
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Class filter
                ExposedDropdownMenuBox(
                    expanded = isClassDropdownOpen,
                    onExpandedChange = { isClassDropdownOpen = !isClassDropdownOpen },
                    modifier = Modifier.weight(1f)
                ) {
                    OutlinedTextField(
                        value = "Lớp: $selectedClass",
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isClassDropdownOpen) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedContainerColor = Color.White,
                            unfocusedContainerColor = Color.White
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = isClassDropdownOpen,
                        onDismissRequest = { isClassDropdownOpen = false }
                    ) {
                        classes.forEach { c ->
                            DropdownMenuItem(
                                text = { Text(c) },
                                onClick = {
                                    onClassChange(c)
                                    isClassDropdownOpen = false
                                }
                            )
                        }
                    }
                }

                // Type filter chips
                FilterChip(
                    selected = selectedType == "GUEST",
                    onClick = { onTypeChange(if (selectedType == "GUEST") "Tất cả" else "GUEST") },
                    label = { Text("Khách (Guest)") }
                )
            }
        }

        // Section Title
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Danh sách kết quả (${attempts.size} lượt)",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1E293B)
                )

                if (attempts.isNotEmpty()) {
                    TextButton(onClick = onClearAll) {
                        Text("Xóa tất cả", color = QuizError, fontSize = 12.sp)
                    }
                }
            }
        }

        if (attempts.isEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(40.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Không có kết quả nào phù hợp với bộ lọc.", color = Color.Gray)
                }
            }
        }

        itemsIndexed(attempts) { index, att ->
            StudentAttemptCard(
                stt = index + 1,
                attempt = att,
                onDetailClick = { onDetailClick(att) },
                onDeleteClick = { onDeleteClick(att) }
            )
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}

@Composable
fun StudentAttemptCard(
    stt: Int,
    attempt: QuizAttempt,
    onDetailClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    val sdf = SimpleDateFormat("dd/MM HH:mm", Locale.getDefault())
    val isGuest = attempt.participationType == ParticipationType.GUEST.name

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable { onDetailClick() }
            .testTag("attempt_item_${attempt.attemptId}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(Color(0xFFEDE9FE)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "$stt",
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = QuizPurpleDark
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = attempt.studentName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = Color(0xFF0F172A)
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Lớp ${attempt.className} • ${sdf.format(Date(attempt.submittedAt))}",
                                fontSize = 11.5.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }

                // Score pill
                Surface(
                    color = if (attempt.score >= 8.0) Color(0xFFDCFCE7) else Color(0xFFEDE9FE),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "${attempt.score} điểm",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Black,
                        color = if (attempt.score >= 8.0) QuizSuccess else QuizPurpleDark
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Surface(
                        color = if (isGuest) Color(0xFFFEF3C7) else Color(0xFFE0E7FF),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = if (isGuest) "Khách (Guest)" else "Tài khoản",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = if (isGuest) Color(0xFFB45309) else Color(0xFF3730A3)
                        )
                    }

                    Surface(
                        color = Color(0xFFF1F5F9),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${attempt.correctCount}/${attempt.totalPoints} ý đúng (${attempt.accuracyRate}%)",
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                            fontSize = 11.sp,
                            color = Color(0xFF475569)
                        )
                    }
                }

                Row {
                    TextButton(onClick = onDetailClick) {
                        Text("Chi tiết", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                    IconButton(onClick = onDeleteClick, modifier = Modifier.size(28.dp)) {
                        Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }
    }
}

@Composable
fun QuizSettingsTabContent(
    pinCode: String,
    onPinChange: (String) -> Unit,
    isQuizOpen: Boolean,
    onQuizOpenToggle: (Boolean) -> Unit,
    allowRetake: Boolean,
    onAllowRetakeToggle: (Boolean) -> Unit,
    onCopyLink: () -> Unit,
    onCopyPin: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Link and PIN Sharing Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Đường Link & Mã PIN Chia Sẻ",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = QuizPurpleDark
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Học sinh chỉ cần bấm link hoặc nhập mã PIN là có thể làm bài ngay ở chế độ Khách (Guest) không cần đăng nhập.",
                        fontSize = 12.5.sp,
                        color = Color(0xFF64748B),
                        lineHeight = 17.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Shareable Link row
                    Surface(
                        color = Color(0xFFF8FAFC),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text("Đường link bài tập:", fontSize = 11.sp, color = Color.Gray)
                                Text(
                                    text = "https://quizsmart.vercel.app/quiz/bai-7-html",
                                    fontSize = 12.5.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = QuizPurplePrimary
                                )
                            }
                            IconButton(onClick = onCopyLink) {
                                Icon(Icons.Default.ContentCopy, contentDescription = "Sao chép link", tint = QuizPurplePrimary)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // PIN Code row
                    Surface(
                        color = Color(0xFFFEF3C7),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFFD97706))
                                Spacer(modifier = Modifier.width(8.dp))
                                Column {
                                    Text("Mã PIN tham gia:", fontSize = 11.sp, color = Color(0xFF92400E))
                                    Text(
                                        text = pinCode,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Black,
                                        color = Color(0xFF78350F)
                                    )
                                }
                            }
                            Button(
                                onClick = onCopyPin,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFD97706)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Sao chép PIN", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // Quiz Configuration Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = CardDefaults.outlinedCardBorder()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text(
                        text = "Cài Đặt Bài Ôn Tập Bài 7",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = Color(0xFF1E293B)
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Switch: Mở/Đóng bài tập
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Trạng thái bài tập", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = if (isQuizOpen) "Đang mở cho học sinh làm bài" else "Đã đóng bài tập",
                                fontSize = 12.sp,
                                color = if (isQuizOpen) QuizSuccess else QuizError
                            )
                        }
                        Switch(
                            checked = isQuizOpen,
                            onCheckedChange = onQuizOpenToggle,
                            colors = SwitchDefaults.colors(checkedThumbColor = QuizPurplePrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Switch: Cho phép làm lại
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Cho phép làm lại", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text(
                                text = "Học sinh có thể thử sức nhiều lần",
                                fontSize = 12.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                        Switch(
                            checked = allowRetake,
                            onCheckedChange = onAllowRetakeToggle,
                            colors = SwitchDefaults.colors(checkedThumbColor = QuizPurplePrimary)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Time limit info
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Thời gian làm bài", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("20 phút (Đếm ngược tự động)", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Surface(
                            color = Color(0xFFEDE9FE),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "20 phút",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = QuizPurpleDark
                            )
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(30.dp))
        }
    }
}
