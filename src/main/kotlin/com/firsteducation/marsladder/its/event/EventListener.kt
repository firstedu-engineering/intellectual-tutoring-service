package com.firsteducation.marsladder.its.event

import com.firsteducation.marsladder.its.service.Service
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionalEventListener

@Component
class EventListener(
    private val service: Service
) {
    companion object {
        private val logger = LoggerFactory.getLogger(EventListener::class.java)
    }
    @TransactionalEventListener
    fun handlePracticeFinishedEvent(event: PracticeSubmittedEvent) {
        logger.info(
            "[PracticeSubmittedEvent Received]: practiceId: [${event.practiceId}]",
        )
        service.adjustKnowledgeAbility(event.practiceId)
        service.adjustFocus()
    }

}
