package com.firsteducation.marsladder.its.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.JdbcTypeCode
import org.hibernate.annotations.UpdateTimestamp
import org.hibernate.type.SqlTypes
import java.time.LocalDateTime

@Entity(name = "student_practice")
data class StudentPracticeEntity(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String? = null,
    val studentId: String,
    val subContentId: String,
    val questionId: String,
    val questionBody: String,
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "json")
    val questionOptions: List<QuestionOption>,
    val questionDifficulty: Double,
    var questionSelectedOptionId: String? = null,
    var questionCorrect: Boolean? = null,
    var questionSubmitted: Boolean = false,
    var isComprehensive: Boolean = false,
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,
)

data class QuestionOption(
    val id: String,
    val value: String,
    val correct: Boolean,
)