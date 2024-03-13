package com.firsteducation.marsladder.its.repository

import com.firsteducation.marsladder.its.repository.entity.PreKnowledgeRelationEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PreKnowledgeRelationRepository : JpaRepository<PreKnowledgeRelationEntity, String> {
    fun findByKnowledgePointId(knowledgePointId: String): List<PreKnowledgeRelationEntity>
    fun findByKnowledgePointIdIn(knowledgePointId: List<String>): List<PreKnowledgeRelationEntity>
    fun findByPreKnowledgePointId(knowledgePointId: String): List<PreKnowledgeRelationEntity>
    fun findByPreKnowledgePointIdIn(knowledgePointId: List<String>): List<PreKnowledgeRelationEntity>
}
