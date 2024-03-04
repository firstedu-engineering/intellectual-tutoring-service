package com.firsteducation.marsladder.its.service

import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.springframework.stereotype.Service

@Service
class Service {
    private val initScore = 1.5
    private val targetScore = 2
    private val baseScore = 1

    fun getFocus(): String? {
        val cluster = connectToDatabase()
        println("Using cluster connection: $cluster")
        val g = getGraphTraversalSource(cluster)
        println("Using traversal source: $g")

        val focus = g.V().has("student", "name", "Yang Fan").out("focus").valueMap<String>().toList()
        return focus.toString()
    }

    fun initKnowledgeAbility(studentId: String) {

    }

    private fun connectToDatabase(): Cluster {
        val builder: Cluster.Builder = Cluster.build()
        builder.addContactPoint("localhost")
        builder.port(8182)
        return builder.create()
    }

    fun getGraphTraversalSource(cluster: Cluster?): GraphTraversalSource {
        return AnonymousTraversalSource.traversal().withRemote(DriverRemoteConnection.using(cluster))
    }
}