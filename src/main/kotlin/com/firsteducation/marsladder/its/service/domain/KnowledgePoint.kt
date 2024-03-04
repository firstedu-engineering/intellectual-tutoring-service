package com.firsteducation.marsladder.its.service.domain

data class KnowledgePoint(
    val id: String,
    val type: String,
    val name: String,
    val outlineId: String? = null,
)
