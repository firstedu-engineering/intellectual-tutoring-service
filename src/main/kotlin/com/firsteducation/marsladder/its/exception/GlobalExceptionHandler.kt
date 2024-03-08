package com.firsteducation.marsladder.its.exception

import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.BindException
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.LocalDateTime

@RestControllerAdvice
class GlobalExceptionHandler {
    private val logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(NotFoundException::class)
    fun handleException(exception: NotFoundException): ResponseEntity<Error> {
        val error: Error = buildError(exception.message)
        logger.error("Not found error: {}", error, exception)
        return ResponseEntity(error, HttpStatus.NOT_FOUND)
    }


    @ExceptionHandler(BadRequestException::class)
    fun handleException(exception: BadRequestException): ResponseEntity<Error> {
        val error: Error = buildError(exception.message)
        logger.error("Bad request error: {}", error, exception)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(BindException::class)
    fun handleException(exception: BindException): ResponseEntity<Error> {
        val messages = arrayListOf<String>()
        for (item in exception.allErrors) {
            if (item is FieldError) {
                messages.add("${item.field}: ${item.defaultMessage}")
            }
        }
        val error: Error = buildError(messages)
        logger.error("Data bind error: {}", error, exception)
        return ResponseEntity(error, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InternalServerErrorException::class)
    fun handleException(exception: InternalServerErrorException): ResponseEntity<Error> {
        val error: Error = buildError(exception.message)
        logger.error("Internal server error: {}", error, exception)
        return ResponseEntity(error, HttpStatus.INTERNAL_SERVER_ERROR)
    }

    private fun buildError(message: String): Error {
        return Error(message = listOf(message), dateTime = LocalDateTime.now())
    }

    private fun buildError(messages: List<String>): Error {
        return Error(message = messages, dateTime = LocalDateTime.now())
    }
}

data class Error(
    var message: List<String>,
    var dateTime: LocalDateTime,
)
