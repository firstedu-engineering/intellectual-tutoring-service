package com.firsteducation.marsladder.its.controller

import com.firsteducation.marsladder.its.service.Service
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller(private val service: Service) {

    @GetMapping("focus")
    fun getQuestions(
    ): ResponseEntity<String> {
        return ResponseEntity.status(HttpStatus.OK).body(
            service.getFocus()
        )
    }
}
