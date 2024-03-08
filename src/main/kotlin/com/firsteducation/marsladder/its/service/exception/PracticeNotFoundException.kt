package com.firsteducation.marsladder.its.service.exception

import com.firsteducation.marsladder.its.exception.NotFoundException

open class PracticeNotFoundException(override val message: String) : NotFoundException(message)