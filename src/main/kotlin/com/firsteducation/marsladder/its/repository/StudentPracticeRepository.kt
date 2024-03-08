package com.firsteducation.marsladder.its.repository

import com.firsteducation.marsladder.its.repository.entity.StudentPracticeEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentPracticeRepository : JpaRepository<StudentPracticeEntity, String> {
    fun findFirstByStudentIdAndSubContentIdOrderByCreatedAtDesc(studentId: String, subContentId: String): StudentPracticeEntity?
    fun findByStudentIdAndId(studentId: String, practiceId: String): StudentPracticeEntity?
}
