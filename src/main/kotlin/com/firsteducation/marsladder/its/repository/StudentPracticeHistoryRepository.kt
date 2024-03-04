package com.firsteducation.marsladder.its.repository

import com.firsteducation.marsladder.its.repository.entity.StudentPracticeHistoryEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentPracticeHistoryRepository : JpaRepository<StudentPracticeHistoryEntity, String> {
    fun findFirstByStudentIdAndSubContentIdOrderByCreatedAtDesc(studentId: String, subContentId: String): StudentPracticeHistoryEntity?
}
