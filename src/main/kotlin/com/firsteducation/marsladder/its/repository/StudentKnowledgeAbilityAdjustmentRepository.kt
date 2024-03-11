package com.firsteducation.marsladder.its.repository

import com.firsteducation.marsladder.its.repository.entity.StudentKnowledgeAbilityAdjustmentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentKnowledgeAbilityAdjustmentRepository : JpaRepository<StudentKnowledgeAbilityAdjustmentEntity, String> {
}
