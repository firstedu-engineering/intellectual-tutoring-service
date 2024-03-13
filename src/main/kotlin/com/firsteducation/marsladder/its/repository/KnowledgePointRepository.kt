package com.firsteducation.marsladder.its.repository

import com.firsteducation.marsladder.its.repository.entity.KnowledgePointEntity
import org.springframework.data.jpa.repository.JpaRepository

interface KnowledgePointRepository : JpaRepository<KnowledgePointEntity, String> {
    fun findByType(type: String): List<KnowledgePointEntity>
}
