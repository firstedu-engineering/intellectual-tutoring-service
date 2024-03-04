package com.firsteducation.marsladder.its.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "student_knowledge_ability")
data class StudentKnowledgeAbilityEntity(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String? = null,
    val studentId: String,
    val subContentId: String,
    val currentScore: Double,
    val targetScore: Double,
    val baseScore: Double,
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,
)