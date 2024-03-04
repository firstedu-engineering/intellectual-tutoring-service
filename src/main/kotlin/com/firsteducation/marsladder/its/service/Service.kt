package com.firsteducation.marsladder.its.service

import com.firsteducation.marsladder.its.repository.StudentKnowledgeAbilityRepository
import com.firsteducation.marsladder.its.repository.StudentPracticeHistoryRepository
import com.firsteducation.marsladder.its.repository.entity.StudentKnowledgeAbilityEntity
import com.firsteducation.marsladder.its.service.domain.KnowledgePoint
import com.firsteducation.marsladder.its.service.domain.Question
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class Service(
    private val studentKnowledgeAbilityRepository: StudentKnowledgeAbilityRepository,
    private val studentPracticeHistoryRepository: StudentPracticeHistoryRepository
) {
    private val initScore = 1.5
    private val targetScore = 2.0
    private val baseScore = 1.0
    private val studentId = "29895382-d9da-11ee-bc41-0242ac190004"

    fun getFocus(): KnowledgePoint {
        val g = getGraphTraversalSource()
        val focus = g.V().has("student", "id", studentId).out("focus").values<String>().toList()
        return KnowledgePoint(
            name = focus[0],
            outlineId = focus[1],
            id = focus[2],
            type = focus[3],
        )
    }

    fun getQuestion(): Question {
        val focus = getFocus()
        val latestHistoryEntity = studentPracticeHistoryRepository.findFirstByStudentIdAndSubContentIdOrderByCreatedAtDesc(studentId, focus.id)
        val knowledgeAbilityEntity = studentKnowledgeAbilityRepository.findByStudentIdAndSubContentId(studentId, focus.id)
        return Question(
            questionId = "123",
            subContentId = "123",
            reason = "123",
        )
    }

    @Transactional
    fun initKnowledgeAbility(studentId: String) {
        val g = getGraphTraversalSource()
        val subContents = g.V().has("knowledge", "type", "sub_content").values<String>("id").toList()
        studentKnowledgeAbilityRepository.deleteByStudentId(studentId)
        val studentKnowledgeAbilityEntities =
            subContents.map {
                StudentKnowledgeAbilityEntity(
                    studentId = studentId,
                    subContentId = it,
                    currentScore = initScore,
                    targetScore = targetScore,
                    baseScore = baseScore,
                )
            }
        studentKnowledgeAbilityRepository.saveAll(studentKnowledgeAbilityEntities)
    }

    private fun connectToDatabase(): Cluster {
        val builder: Cluster.Builder = Cluster.build()
        builder.addContactPoint("localhost")
        builder.port(8182)
        return builder.create()
    }

    fun getGraphTraversalSource(): GraphTraversalSource {
        val cluster = connectToDatabase()
        return AnonymousTraversalSource.traversal().withRemote(DriverRemoteConnection.using(cluster))
    }
}
