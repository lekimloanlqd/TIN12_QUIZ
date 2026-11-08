package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.local.DefaultQuizData
import com.example.data.model.QuestionType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Quiz Smart", appName)
  }

  @Test
  fun `verify Bai 7 questions count and evaluation points`() {
    val questions = DefaultQuizData.questions
    // 10 multiple choice + 4 true/false contexts = 14 questions
    assertEquals(14, questions.size)

    val mcqList = questions.filter { it.type == QuestionType.MULTIPLE_CHOICE }
    assertEquals(10, mcqList.size)

    val tfList = questions.filter { it.type == QuestionType.TRUE_FALSE_CONTEXT }
    assertEquals(4, tfList.size)

    val totalStatements = tfList.sumOf { it.statements.size }
    assertEquals(16, totalStatements)

    // Total evaluation points = 10 + 16 = 26
    assertEquals(26, mcqList.size + totalStatements)
  }
}
