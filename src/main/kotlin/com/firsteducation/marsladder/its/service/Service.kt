package com.firsteducation.marsladder.its.service

import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.springframework.stereotype.Service

@Service
class Service {
    fun getFocus(): String? {
        val cluster = connectToDatabase()
        println("Using cluster connection: $cluster")
        val g = getGraphTraversalSource(cluster)
        println("Using traversal source: $g")

        val focus = g.V().has("student", "name", "Yang Fan").out("focus").valueMap<String>().toList()
        return focus.toString()
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