package com.example.springbootartemis.consumer;

import com.example.springbootartemis.util.QueueSender;
import com.example.springbootartemis.util.StaxMessageUtil;
import com.example.springbootartemis.util.VaiHeader;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

import javax.xml.stream.XMLStreamException;
import java.time.Duration;
import java.time.Instant;
import java.util.UUID;

import static com.example.springbootartemis.util.QueueSender.SOURCE_UID_KEY;

@Component
@Slf4j
@RequiredArgsConstructor
public class JmsConsumer {

    private final StaxMessageUtil messageUtil;
    private final QueueSender sender;

    @JmsListener(destination = "vai.test.queue.01")
    @JmsListener(destination = "fromQueue2")
    public void processMessage(Message<String> message) {
        Instant start = Instant.now();
        String payload = message.getPayload();
        //log.info("message payload {}", payload);
        String sourceUid = createSourceUid(message);
        VaiHeader header = null;
        try {
            header = messageUtil.parseVaiHeader(payload);
            log.info("parsed vai header {}", header);

            // query routing info here and add to properties
            sender.sendToRouting(payload, sourceUid);
            log.info("Successfully forwarded message. duration={}ms", Duration.between(start, Instant.now()).toMillis());
        } catch (XMLStreamException e) {
            log.warn("invalid message payload, sent to error processor!", e);
            sender.sendToError(payload, sourceUid, e.getMessage());
        }
    }

    private String createSourceUid(Message<String> message) {
        String srcUid = message.getHeaders().get(SOURCE_UID_KEY, String.class);
        return srcUid != null ? srcUid : UUID.randomUUID().toString();
    }

}
