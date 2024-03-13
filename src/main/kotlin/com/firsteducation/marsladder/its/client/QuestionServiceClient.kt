package com.firsteducation.marsladder.its.client

import com.firsteducation.marsladder.its.client.domain.Option
import com.firsteducation.marsladder.its.client.domain.Question
import org.springframework.stereotype.Service
import java.util.*

@Service
class QuestionServiceClient {
    val option = listOf(
        Option(
            value = "A",
            correct = true
        ),
        Option(
            value = "B",
            correct = false
        ),
        Option(
            value = "C",
            correct = false
        ),
        Option(
            value = "D",
            correct = false
        ),
        Option(
            value = "D",
            correct = false
        ),
    )

    fun fetchSingleContentQuestion(contentId: String, minDifficulty: Double, maxDifficulty: Double): Question {
        val body = "单一tag题目. The content tag is $contentId. The difficulty is between $minDifficulty and $maxDifficulty."
        return Question(
            id = UUID.randomUUID().toString(),
            body = body,
            option = option,
            difficulty = generateFakeQuestionDifficulty(minDifficulty, maxDifficulty)
        )
    }

    fun fetchComprehensiveQuestion(contentId: String, minDifficulty: Double, maxDifficulty: Double): Question {
        val body = "综合题目. The content tag is $contentId. The difficulty is between $minDifficulty and $maxDifficulty."
        return Question(
            id = UUID.randomUUID().toString(),
            body = body,
            option = option,
            difficulty = generateFakeQuestionDifficulty(minDifficulty, maxDifficulty)
        )
    }

    private fun generateFakeQuestionDifficulty(minDifficulty: Double, maxDifficulty: Double): Double {
        return ((minDifficulty + (maxDifficulty - minDifficulty) * Random().nextDouble()) * 100).toInt() / 100.0
    }
}