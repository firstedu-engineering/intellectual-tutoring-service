package com.firsteducation.marsladder.its.controller

import com.firsteducation.marsladder.its.service.Service
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val service: Service) {

    @GetMapping("focus")
    fun getFocus(
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body(
            service.getFocus()
        )
    }

    @PostMapping("init-knowledge-ability/{studentId}")
    fun initKnowledgeAbility(
        @PathVariable studentId: String,
    ): ResponseEntity<String> {
        service.initKnowledgeAbility(studentId)
        return ResponseEntity.ok().build()
    }

    @GetMapping("question")
    fun generateQuestion(
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body(
            service.getFocus()
        )
    }
}
