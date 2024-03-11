package com.firsteducation.marsladder.its.event

import org.springframework.context.ApplicationEvent

class PracticeSubmittedEvent(source: Any, val practiceId: String) : ApplicationEvent(source)
