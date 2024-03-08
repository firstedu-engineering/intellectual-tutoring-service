package com.firsteducation.marsladder.its.service.exception

import com.firsteducation.marsladder.its.exception.InternalServerErrorException

open class PracticeException(override val message: String) : InternalServerErrorException(message)