package com.example.springbootartemis.util;

import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QueueSender {

    private static final String routingQueue = "queue.to.routing.01";
    private static final String trackingQueue = "queue.to.tracking.01";
    private static final String errorQueue = "queue.to.error.01";
    public static final String SOURCE_UID_KEY = "vt_srcUid";
    public static final String ERROR_MSG_KEY = "vt_errorMessage";
    private final JmsTemplate template;


    public void sendToError(String payload, String sourceUid, String errorMsg) {
        sendMessage(errorQueue, payload, sourceUid, errorMsg);
    }

    public void sendToRouting(String payload, String sourceUid) {
        sendMessage(routingQueue, payload, sourceUid, null);
        sendMessage(trackingQueue, payload, sourceUid, null);
    }

    private void sendMessage(String queue, String payload, String sourceUid, String errorMsg) {
        template.convertAndSend(queue, payload, m -> {
            m.setStringProperty(SOURCE_UID_KEY, sourceUid);
            if (errorMsg != null) {
                m.setStringProperty(ERROR_MSG_KEY, errorMsg);
            }
            return m;
        });
    }
}
