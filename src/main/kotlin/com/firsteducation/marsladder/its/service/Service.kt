package com.firsteducation.marsladder.its.service

import com.firsteducation.marsladder.its.client.QuestionServiceClient
import com.firsteducation.marsladder.its.client.domain.Question
import com.firsteducation.marsladder.its.event.PracticeSubmittedEvent
import com.firsteducation.marsladder.its.exception.BadRequestException
import com.firsteducation.marsladder.its.repository.*
import com.firsteducation.marsladder.its.repository.entity.*
import com.firsteducation.marsladder.its.service.domain.KnowledgePoint
import com.firsteducation.marsladder.its.service.domain.Practice
import com.firsteducation.marsladder.its.service.exception.PracticeException
import com.firsteducation.marsladder.its.service.exception.PracticeNotFoundException
import com.firsteducation.marsladder.its.service.exception.PracticeOptionNotFoundException
import org.apache.tinkerpop.gremlin.driver.Cluster
import org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection
import org.apache.tinkerpop.gremlin.process.traversal.AnonymousTraversalSource
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import kotlin.math.abs

@Service
class Service(
    private val studentKnowledgeAbilityRepository: StudentKnowledgeAbilityRepository,
    private val studentPracticeRepository: StudentPracticeRepository,
    private val questionServiceClient: QuestionServiceClient,
    private val publisher: ApplicationEventPublisher,
    private val studentKnowledgeAbilityAdjustmentRepository: StudentKnowledgeAbilityAdjustmentRepository,
    private val studentKnowledgeAbilityAdjustmentDetailRepository: StudentKnowledgeAbilityAdjustmentDetailRepository,
    private val studentFocusRepository: StudentFocusRepository,
    private val knowledgePointRepository: KnowledgePointRepository,
    private val preKnowledgeRelationRepository: PreKnowledgeRelationRepository
) {
    private val initScore = 1.5
    private val targetScore = 2.0
    private val baseScore = 1.0

    fun getFocusKnowledgePoint(studentId: String): KnowledgePoint {
        val studentFocusEntity = studentFocusRepository.findByStudentId(studentId)
            ?: throw PracticeException("Student $studentId do not focus on any knowledge point.")
        return knowledgePointRepository.findById(studentFocusEntity.knowledgePointId)
            .map { KnowledgePoint.from(it) }
            .orElseThrow { PracticeException("No such knowledge point ${studentFocusEntity.knowledgePointId}") }
    }

    fun getPractice(studentId: String): Practice {
        val focus = getFocusKnowledgePoint(studentId)
        val latestPracticeEntity = studentPracticeRepository.findFirstByStudentIdAndSubContentIdOrderByCreatedAtDesc(studentId, focus.id)
        if (latestPracticeEntity != null && !latestPracticeEntity.questionSubmitted) {
            return Practice.from(latestPracticeEntity)
        } else {
            val knowledgeAbilityEntity = getStudentKnowledgeAbility(studentId, focus.id)
            val question = generateQuestion(focus, latestPracticeEntity, knowledgeAbilityEntity)
            val isComprehensive = knowledgeAbilityEntity.currentScore >= knowledgeAbilityEntity.targetScore
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
                questionDifficulty = question.difficulty,
                isComprehensive = isComprehensive
            )
            val result = studentPracticeRepository.save(practiceEntity)
            return Practice.from(result)
        }
    }

    private fun generateQuestion(focus: KnowledgePoint, latestPracticeEntity: StudentPracticeEntity?, knowledgeAbilityEntity: StudentKnowledgeAbilityEntity): Question {
        val previousPracticeCorrect = latestPracticeEntity == null || latestPracticeEntity.questionCorrect!!
        if (knowledgeAbilityEntity.currentScore >= knowledgeAbilityEntity.targetScore) {
            //综合知识
            val minDifficulty = knowledgeAbilityEntity.currentScore
            val maxDifficulty = knowledgeAbilityEntity.currentScore + 0.5
            val question = questionServiceClient.fetchComprehensiveQuestion(focus.name, minDifficulty, maxDifficulty)
            return question
        }

        //单个content题目
        if (previousPracticeCorrect) {
            //当上述难度范围无法抽取时，扩展范围至X+1
            val minDifficulty = knowledgeAbilityEntity.currentScore
            val maxDifficulty = knowledgeAbilityEntity.currentScore + 0.5
            val question = questionServiceClient.fetchSingleContentQuestion(focus.name, minDifficulty, maxDifficulty)
            return question
        } else {
            if (knowledgeAbilityEntity.currentScore < knowledgeAbilityEntity.baseScore ) {
                throw PracticeException("The current ability score ${knowledgeAbilityEntity.currentScore} is less than the base ability score ${knowledgeAbilityEntity.baseScore}.")
            }
            //当上述难度范围无法抽取时，扩展范围至X+1
            val minDifficulty = knowledgeAbilityEntity.currentScore - 0.5
            val maxDifficulty = knowledgeAbilityEntity.currentScore
            val question = questionServiceClient.fetchSingleContentQuestion(focus.name, minDifficulty, maxDifficulty)
            return question
        }
    }

    @Transactional
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

        publisher.publishEvent(
            PracticeSubmittedEvent(
                source = this,
                practiceId = practiceEntity.id!!,
            ),
        )
    }

    @Transactional
    fun initKnowledgeAbility(studentId: String) {
        val subContentEntities = knowledgePointRepository.findByType("sub_content")
        studentKnowledgeAbilityRepository.deleteByStudentId(studentId)
        val studentKnowledgeAbilityEntities =
            subContentEntities.map {
                StudentKnowledgeAbilityEntity(
                    studentId = studentId,
                    subContentId = it.id!!,
                    currentScore = initScore,
                    targetScore = targetScore,
                    baseScore = baseScore,
                )
            }
        studentKnowledgeAbilityRepository.saveAll(studentKnowledgeAbilityEntities)
    }

    @Transactional
    fun adjustKnowledgeAbility(practiceId: String) {
        val practice = studentPracticeRepository.findById(practiceId).orElseThrow{ PracticeNotFoundException("No such practice $practiceId") }
        val focus = getFocusKnowledgePoint(practice.studentId)
        val knowledgeAbilityEntity = getStudentKnowledgeAbility(practice.studentId, focus.id)
        val focusKnowledgePointAdjustmentScore = if (practice.questionDifficulty >= knowledgeAbilityEntity.currentScore && practice.questionCorrect!!) {
            maxOf((practice.questionDifficulty - knowledgeAbilityEntity.currentScore) * 0.7, 0.1)
        } else if (practice.questionDifficulty < knowledgeAbilityEntity.currentScore && practice.questionCorrect!!) {
            0.1
        } else if (practice.questionDifficulty >= knowledgeAbilityEntity.currentScore && !practice.questionCorrect!!) {
            -0.1
        } else {
            minOf((practice.questionDifficulty - knowledgeAbilityEntity.currentScore) * 0.7, -0.1)
        }

        val factor = 0.5
        val adjustmentPlanMap = mutableMapOf<String, Double>() // key: sub_content_id, value: adjustment score
        adjustmentPlanMap[focus.id] = focusKnowledgePointAdjustmentScore

        var preKnowledgeRelations = preKnowledgeRelationRepository.findByKnowledgePointId(focus.id)

        while(preKnowledgeRelations.isNotEmpty()) {
            val needAdjustmentKnowledgePointIds = mutableListOf<String>()
            preKnowledgeRelations.forEach { preKnowledgeRelation ->
                val followingKnowledgePointAdjustmentScore = adjustmentPlanMap[preKnowledgeRelation.knowledgePointId]!!
                val adjustmentScore = if (followingKnowledgePointAdjustmentScore < 0)
                    followingKnowledgePointAdjustmentScore * preKnowledgeRelation.relevance * factor
                else
                    followingKnowledgePointAdjustmentScore * preKnowledgeRelation.relevance
                if (adjustmentPlanMap[preKnowledgeRelation.preKnowledgePointId] == null && abs(adjustmentScore) >= 0.1) {
                    adjustmentPlanMap[preKnowledgeRelation.preKnowledgePointId] = adjustmentScore
                    needAdjustmentKnowledgePointIds.add(preKnowledgeRelation.preKnowledgePointId)
                }
            }
            preKnowledgeRelations = preKnowledgeRelationRepository.findByKnowledgePointIdIn(needAdjustmentKnowledgePointIds)
        }

        var followingKnowledgeRelations = preKnowledgeRelationRepository.findByPreKnowledgePointId(focus.id)

        while(followingKnowledgeRelations.isNotEmpty()) {
            val needAdjustmentKnowledgePointIds = mutableListOf<String>()
            followingKnowledgeRelations.forEach { followingKnowledgeRelation ->
                val preKnowledgePointAdjustmentScore = adjustmentPlanMap[followingKnowledgeRelation.preKnowledgePointId]!!
                val adjustmentScore = if (preKnowledgePointAdjustmentScore < 0)
                    preKnowledgePointAdjustmentScore * followingKnowledgeRelation.relevance
                else
                    preKnowledgePointAdjustmentScore * followingKnowledgeRelation.relevance * factor
                if (adjustmentPlanMap[followingKnowledgeRelation.knowledgePointId] == null && abs(adjustmentScore) >= 0.1) {
                    adjustmentPlanMap[followingKnowledgeRelation.knowledgePointId] = adjustmentScore
                    needAdjustmentKnowledgePointIds.add(followingKnowledgeRelation.knowledgePointId)
                }
            }
            followingKnowledgeRelations = preKnowledgeRelationRepository.findByPreKnowledgePointIdIn(needAdjustmentKnowledgePointIds)
        }

        val adjustment = studentKnowledgeAbilityAdjustmentRepository.save(
            StudentKnowledgeAbilityAdjustmentEntity(
                studentId = practice.studentId,
                practiceId = practice.id!!,
                adjusted = true
            )
        )

        adjustmentPlanMap.keys.forEach { subContentId ->
            val studentKnowledgeAbilityEntity = getStudentKnowledgeAbility(practice.studentId, subContentId)
            val before = studentKnowledgeAbilityEntity.currentScore
            val after = before + adjustmentPlanMap[subContentId]!!

            studentKnowledgeAbilityEntity.currentScore = after
            studentKnowledgeAbilityRepository.save(studentKnowledgeAbilityEntity)

            studentKnowledgeAbilityAdjustmentDetailRepository.save(
                StudentKnowledgeAbilityAdjustmentDetailEntity(
                    adjustmentId = adjustment.id!!,
                    subContentId = subContentId,
                    before = before,
                    after = after
                )
            )
        }
    }

    @Transactional
    fun adjustFocus(practiceId: String) {

    }

    private fun getStudentKnowledgeAbility(studentId: String, knowledgePointId: String): StudentKnowledgeAbilityEntity =
        studentKnowledgeAbilityRepository.findByStudentIdAndSubContentId(studentId, knowledgePointId)
            ?: throw PracticeException("Student $studentId has no knowledge ability for sub_content: $knowledgePointId")

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
