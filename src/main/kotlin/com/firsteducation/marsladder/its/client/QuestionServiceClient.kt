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

    fun fetchQuestion(contentId: String, minDifficulty: Double, maxDifficulty: Double): Question {
        return Question(
            id = UUID.randomUUID().toString(),
            body = "this is body",
            option = option
        )

    }

    fun fetchQuestionByQuestionId(questionId: String): Question {
        return Question(
            id = questionId,
            body = "this is body",
            option = option
        )
    }
}