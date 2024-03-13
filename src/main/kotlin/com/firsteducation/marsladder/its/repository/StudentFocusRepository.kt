package com.firsteducation.marsladder.its.repository

import com.firsteducation.marsladder.its.repository.entity.StudentFocusEntity
import org.springframework.data.jpa.repository.JpaRepository

interface StudentFocusRepository : JpaRepository<StudentFocusEntity, String> {
    fun findByStudentId(studentId: String): StudentFocusEntity?
}
