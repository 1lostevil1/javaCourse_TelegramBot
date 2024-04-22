package edu.java.clients;

import edu.java.Request.LinkUpdate;
import edu.java.services.interfaces.LinkUpdateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;

@Slf4j
public class ScrapperQueueProducer implements LinkUpdateService {
    private final String topicName;
    private final KafkaTemplate<String, LinkUpdate> kafkaTemplate;

    public ScrapperQueueProducer(KafkaTemplate<String, LinkUpdate> kafkaTemplate, String topicName) {
        this.topicName = topicName;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void sendUpdate(LinkUpdate request) {
        kafkaTemplate.send(topicName, request).whenComplete((result, ex) -> {
            if (ex == null) {
                log.info("massage was sent offset:{}", result.getRecordMetadata());
            } else {
                log.info("send error");
            }
        });
    }
}
