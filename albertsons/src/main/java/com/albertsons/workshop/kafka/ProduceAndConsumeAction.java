package com.albertsons.workshop.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@ActionDefinition(domain = "kafka",
        name = "produce-consume",
        path = "/produce-consume",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class ProduceAndConsumeAction extends KafkaAction<String> {
    public static final long DEFAULT_POLL_DURATION = 1000l;

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            option = "p",
            required = true,
            displayOrder = 3
    )
    protected String produceTopic;

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            option = "c",
            required = true,
            displayOrder = 4
    )
    protected String consumeTopic;

    @ActionProperty(
            parameterType = ActionParameterType.HEADER_PARAM,
            option = "d"
    )
    protected Long pollDuration = 300l;

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM)
    protected Integer partition;

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM)
    protected String keySerializer;

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM)
    protected String valueSerializer;

    @ActionProperty(parameterType = ActionParameterType.PAYLOAD, description = "Message for produce", required = true)
    protected String message;

    @Override
    public String execute() throws Exception {

        long timestamp = System.currentTimeMillis();

        KafkaProducer<String, byte[]> producer = producer();
        KafkaConsumer<String, byte[]> kafkaConsumer = consumer();

        Report report = new Report(produceTopic, partition, UUID.randomUUID().toString(), message, null);

        producer.send(report.producerRecord, (recordMetadata, e) -> {
            if (e != null) {
                report.setException(e);

            } else {
                report.setRecordMetadata(recordMetadata);

                List<PartitionInfo> partitionInfoSet = kafkaConsumer.partitionsFor(consumeTopic);
                Collection<TopicPartition> partitions = partitionInfoSet.stream()
                        .map(partitionInfo -> new TopicPartition(partitionInfo.topic(),
                                partitionInfo.partition()))
                        .collect(Collectors.toList());
                Map<TopicPartition, Long> latestOffsets = kafkaConsumer.endOffsets(partitions);

                if (isTimeout(timestamp)) {
                    report.setException(new TimeoutException());

                } else {
                    try {
                        Thread.sleep(500l);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    for (TopicPartition partition : partitions) {
                        List<TopicPartition> assignments = new ArrayList<>();
                        assignments.add(partition);
                        kafkaConsumer.assign(assignments);

                        Long latestOffset = Math.max(0, latestOffsets.get(partition) - 1);
                        kafkaConsumer.seek(partition, Math.max(0, latestOffset));
                        ConsumerRecords<String, byte[]> polled = kafkaConsumer.poll(pollDuration());

                        for (ConsumerRecord<String, byte[]> rc : polled) {
                            if (rc.timestamp() > recordMetadata.timestamp()) {
                                report.setConsumerRecord(rc);
                                break;
                            }
                        }
                    }
                }
            }
        });

        while (!report.isDone()) {
            if (isTimeout(timestamp)) {
                report.setException(new Exception("Timeout"));

            } else {
                Thread.sleep(300l);
            }
        }

        if (report.getConsumerRecord() != null) {
            return new String(report.getConsumerRecord().value());

        } else {
            return report.getException().getMessage();
        }
    }

    private Duration pollDuration() {
        return Duration.ofMillis(pollDuration == null ? DEFAULT_POLL_DURATION : pollDuration);
    }

    static class Report {
        private final ProducerRecord<String, byte[]> producerRecord;
        private RecordMetadata recordMetadata;

        private ConsumerRecord<String, byte[]> consumerRecord;
        private Exception exception;

        public Report(ProducerRecord<String, byte[]> producerRecord) {
            this.producerRecord = producerRecord;
        }

        public Report(String topicName, String value) {
            this(topicName, 0, null, value, null);
        }

        public Report(String topicName, Integer partition, String key, String value, Map<String, String> headers) {
            RecordHeaders recordHeaders = new RecordHeaders();
            if (headers != null) {
                headers.entrySet().forEach(e -> {
                    recordHeaders.add(new RecordHeader(e.getKey(), e.getValue().getBytes()));
                });
            }

            this.producerRecord = new ProducerRecord<String, byte[]>(topicName,
                    partition == null ? 0 : partition,
                    key,
                    value.getBytes(StandardCharsets.UTF_8),
                    recordHeaders);
        }

        public ProducerRecord<String, byte[]> getProducerRecord() {
            return producerRecord;
        }

        public RecordMetadata getRecordMetadata() {
            return recordMetadata;
        }

        public void setRecordMetadata(RecordMetadata recordMetadata) {
            this.recordMetadata = recordMetadata;
        }

        public ConsumerRecord<String, byte[]> getConsumerRecord() {
            return consumerRecord;
        }

        public void setConsumerRecord(ConsumerRecord<String, byte[]> consumerRecord) {
            this.consumerRecord = consumerRecord;
        }

        public Exception getException() {
            return exception;
        }

        public void setException(Exception exception) {
            this.exception = exception;
        }

        public boolean isDone() {
            return exception != null || consumerRecord != null;
        }
    }
}
