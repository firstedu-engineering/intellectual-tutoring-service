package com.firsteducation.marsladder.its.service

import com.firsteducation.marsladder.its.repository.StudentKnowledgeAbilityRepository
import com.firsteducation.marsladder.its.repository.entity.StudentKnowledgeAbilityEntity
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class Service(
    private val studentKnowledgeAbilityRepository: StudentKnowledgeAbilityRepository
) {
    private val initScore = 1.5
    private val targetScore = 2.0
    private val baseScore = 1.0

    fun getFocus(): String? {
        val g = getGraphTraversalSource()

        val focus = g.V().has("student", "name", "Yang Fan").out("focus").valueMap<String>().toList()
        return focus.toString()
    }

    @Transactional
    fun initKnowledgeAbility(studentId: String) {
        val g = getGraphTraversalSource()
        val subContents = g.V().has("knowledge", "type", "sub_content").values<String>("id").toList()
        studentKnowledgeAbilityRepository.deleteByStudentId(studentId)
        val studentKnowledgeAbilityEntities = subContents.map {
            StudentKnowledgeAbilityEntity (
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