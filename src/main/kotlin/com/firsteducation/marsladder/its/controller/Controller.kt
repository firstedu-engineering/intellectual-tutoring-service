package com.firsteducation.marsladder.its.controller

import com.firsteducation.marsladder.its.controller.dto.PracticeResponse
import com.firsteducation.marsladder.its.controller.dto.SubmitPracticeRequest
import com.firsteducation.marsladder.its.service.Service
import com.firsteducation.marsladder.its.service.domain.KnowledgePoint
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
class Controller(private val service: Service) {
    private val studentId = "29895382-d9da-11ee-bc41-0242ac190004"

    @GetMapping("focus")
    fun getFocus(): ResponseEntity<KnowledgePoint> {
        return ResponseEntity.status(HttpStatus.OK).body(
            service.getFocusKnowledgePoint(studentId),
        )
    }

    @PostMapping("init-knowledge-ability/{studentId}")
    fun initKnowledgeAbility(
        @PathVariable studentId: String,
    ): ResponseEntity<String> {
        service.initKnowledgeAbility(studentId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("practice")
    fun practice(): ResponseEntity<PracticeResponse> {
        val practice = service.generatePractice(studentId)
        return ResponseEntity.status(HttpStatus.OK).body(
            PracticeResponse.from(practice)
        )
    }

    @PostMapping("submit-practice")
    fun submitPractice(@RequestBody submitPracticeRequest: SubmitPracticeRequest): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body(
            service.submitPractice(studentId = studentId, practiceId = submitPracticeRequest.practiceId, optionId = submitPracticeRequest.optionId)
        )
    }
}
