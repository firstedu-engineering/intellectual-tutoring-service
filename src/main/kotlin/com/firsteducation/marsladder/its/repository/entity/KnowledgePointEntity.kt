package com.firsteducation.marsladder.its.repository.entity

import jakarta.persistence.*
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp
import java.time.LocalDateTime

@Entity(name = "knowledge_point")
data class KnowledgePointEntity(
    @Id
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid2")
    var id: String? = null,
    val type: String,
    val name: String,

    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.REFRESH])
    @JoinColumn(name = "parent_id")
    val parentKnowledge: KnowledgePointEntity?,

    @OneToMany(
        mappedBy = "parentKnowledge",
        fetch = FetchType.LAZY,
        cascade = [CascadeType.ALL],
        orphanRemoval = true,
    )
    @OrderBy("created_at ASC")
    val childrenKnowledge: List<KnowledgePointEntity>,

    @Column(name = "parent_id", insertable = false, updatable = false)
    val parentId: String?,
    val outlineId: String?,
    @CreationTimestamp
    val createdAt: LocalDateTime? = null,
    @UpdateTimestamp
    val updatedAt: LocalDateTime? = null,
)
