package com.firsteducation.marsladder.its.service.domain

import com.firsteducation.marsladder.its.repository.entity.KnowledgePointEntity

data class KnowledgePoint(
    val id: String,
    val type: String,
    val name: String,
    val outlineId: String? = null,
) {
    companion object {
        fun from(knowledgePointEntity: KnowledgePointEntity): KnowledgePoint =
            KnowledgePoint(
                id = knowledgePointEntity.id!!,
                type = knowledgePointEntity.type,
                name = knowledgePointEntity.name,
                outlineId = knowledgePointEntity.outlineId
            )
    }
}
