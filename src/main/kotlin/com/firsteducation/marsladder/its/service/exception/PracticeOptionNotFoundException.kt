package com.firsteducation.marsladder.its.service.exception

import com.firsteducation.marsladder.its.exception.NotFoundException

open class PracticeOptionNotFoundException(override val message: String) : NotFoundException(message)