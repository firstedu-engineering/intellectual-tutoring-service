package com.firsteducation.marsladder.its.service

import com.firsteducation.marsladder.its.client.QuestionServiceClient
import com.firsteducation.marsladder.its.exception.BadRequestException
import com.firsteducation.marsladder.its.repository.StudentKnowledgeAbilityRepository
import com.firsteducation.marsladder.its.repository.StudentPracticeRepository
import com.firsteducation.marsladder.its.repository.entity.QuestionOption
import com.firsteducation.marsladder.its.repository.entity.StudentKnowledgeAbilityEntity
import com.firsteducation.marsladder.its.repository.entity.StudentPracticeEntity
import com.firsteducation.marsladder.its.service.domain.KnowledgePoint
import com.firsteducation.marsladder.its.service.domain.Practice
import com.firsteducation.marsladder.its.service.exception.PracticeException
import com.firsteducation.marsladder.its.service.exception.PracticeNotFoundException
import com.firsteducation.marsladder.its.service.exception.PracticeOptionNotFoundException
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class Service(
    private val studentKnowledgeAbilityRepository: StudentKnowledgeAbilityRepository,
    private val studentPracticeRepository: StudentPracticeRepository,
    private val questionServiceClient: QuestionServiceClient
) {
    private val initScore = 1.5
    private val targetScore = 2.0
    private val baseScore = 1.0

    fun getFocus(studentId: String): KnowledgePoint {
        val g = getGraphTraversalSource()
        val focus = g.V().has("student", "id", studentId).out("focus").values<String>().toList()
        return KnowledgePoint(
            name = focus[0],
            outlineId = focus[1],
            id = focus[2],
            type = focus[3],
        )
    }

    fun getPractice(studentId: String): Practice {
        val focus = getFocus(studentId)
        val latestPracticeEntity = studentPracticeRepository.findFirstByStudentIdAndSubContentIdOrderByCreatedAtDesc(studentId, focus.id)
        if (latestPracticeEntity == null || latestPracticeEntity.questionSubmitted) {
            val knowledgeAbilityEntity = studentKnowledgeAbilityRepository.findByStudentIdAndSubContentId(studentId, focus.id)
                ?: throw PracticeException("Student $studentId has no knowledge ability for sub_content: ${focus.name}")
            if (knowledgeAbilityEntity.currentScore >= knowledgeAbilityEntity.targetScore ) {
                throw PracticeException("The current ability score ${knowledgeAbilityEntity.currentScore} is greater than or equals to the target ability score ${knowledgeAbilityEntity.targetScore}.")
            } else {
                //当上述难度范围无法抽取时，扩展范围至X+1
                val minDifficulty = knowledgeAbilityEntity.currentScore
                val maxDifficulty = knowledgeAbilityEntity.currentScore + 0.5
                val question = questionServiceClient.fetchQuestion(focus.outlineId!!, minDifficulty, maxDifficulty)
                val practiceEntity = StudentPracticeEntity(
                    studentId = studentId,
                    subContentId = focus.id,
                    questionId = question.id,
                    questionBody = question.body,
                    questionOptions = question.option.map {
                        QuestionOption(
                            id = UUID.randomUUID().toString(),
                            value = it.value,
                            correct = it.correct
                        )
                    },
                    questionRecommendedReason = "Fetch a question from content: ${focus.name}, difficulty is between $minDifficulty and $maxDifficulty."
                )
                val result = studentPracticeRepository.save(practiceEntity)
                return Practice.from(result)
            }
        } else {
            return Practice.from(latestPracticeEntity)
        }
    }

    fun submitPractice(studentId: String, practiceId: String, optionId: String) {
        val practiceEntity = studentPracticeRepository.findByStudentIdAndId(studentId, practiceId)
            ?: throw PracticeNotFoundException("Practice not found: practiceId: $practiceId, studentId: $studentId")
        if (!practiceEntity.questionOptions.map { it.id }.contains(optionId)) {
            throw PracticeOptionNotFoundException("No such option id $optionId in practice $practiceId.")
        }
        if (practiceEntity.questionSubmitted) {
            throw BadRequestException("The practice $practiceId has been submitted.")
        }
        practiceEntity.questionSubmitted = true
        practiceEntity.questionCorrect = practiceEntity.questionOptions.first { it.id == optionId }.correct
        practiceEntity.questionSelectedOptionId = optionId
        studentPracticeRepository.save(practiceEntity)
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
