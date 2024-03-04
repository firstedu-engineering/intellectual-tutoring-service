package com.firsteducation.marsladder.its.repository

import com.firsteducation.marsladder.its.repository.entity.StudentKnowledgeAbilityEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentKnowledgeAbilityRepository : JpaRepository<StudentKnowledgeAbilityEntity, String> {
    fun deleteByStudentId(studentId: String): List<StudentKnowledgeAbilityEntity>
}