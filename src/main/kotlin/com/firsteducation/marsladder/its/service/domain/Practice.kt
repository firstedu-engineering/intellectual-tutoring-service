package com.firsteducation.marsladder.its.service.domain

import com.firsteducation.marsladder.its.repository.entity.QuestionOption
import com.firsteducation.marsladder.its.repository.entity.StudentPracticeEntity

data class Practice(
    val practiceId: String,
    val questionId: String,
    val questionBody: String,
    val questionOption: List<QuestionOption>,
    val questionDifficulty: Double,
    val subContentId: String
) {
    companion object {
        fun from(studentPracticeEntity: StudentPracticeEntity): Practice =
            Practice(
                practiceId = studentPracticeEntity.id!!,
                questionId = studentPracticeEntity.questionId,
                subContentId = studentPracticeEntity.subContentId,
                questionBody = studentPracticeEntity.questionBody,
                questionDifficulty = studentPracticeEntity.questionDifficulty,
                questionOption = studentPracticeEntity.questionOptions,
            )
    }
}
