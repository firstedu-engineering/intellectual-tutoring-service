package com.firsteducation.marsladder.its.controller.dto

import com.firsteducation.marsladder.its.service.domain.Practice

data class PracticeResponse(
    val practiceId: String,
    val questionId: String,
    val questionBody: String,
    val questionOptions: List<QuestionOptionResponse>,
    val subContentId: String,
    val questionRecommendedReason: String,
) {
    companion object {
        fun from(practice: Practice): PracticeResponse =
            PracticeResponse(
                practiceId = practice.practiceId,
                questionId = practice.questionId,
                questionBody = practice.questionBody,
                questionOptions = practice.questionOption.map {
                    QuestionOptionResponse(
                        id = it.id,
                        value = it.value
                    )
                },
                questionRecommendedReason = practice.questionRecommendedReason,
                subContentId = practice.subContentId
            )

    }
}

data class QuestionOptionResponse(
    val id: String,
    val value: String
)
