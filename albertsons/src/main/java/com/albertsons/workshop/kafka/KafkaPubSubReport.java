package com.albertsons.workshop.kafka;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;

import java.io.Serializable;
import java.util.Map;

public final class KafkaPubSubReport implements Serializable {
    private final KafkaPubSubTestCase testCase;
    private final ProducerRecord<String, byte[]> producerRecord;
    private RecordMetadata recordMetadata;

    private ConsumerRecord<String, byte[]> consumerRecord;
    private Exception exception;

    public KafkaPubSubReport(KafkaPubSubTestCase testCase, ProducerRecord<String, byte[]> producerRecord) {
        this.testCase = testCase;
        this.producerRecord = producerRecord;
    }

    public KafkaPubSubReport(KafkaPubSubTestCase testCase, String value) {
        this(testCase, 0, null, value.getBytes(), null);
    }

    public KafkaPubSubReport(KafkaPubSubTestCase testCase, Integer partition, String key, byte[] value, Map<String, String> headers) {
        this.testCase = testCase;
        RecordHeaders recordHeaders = new RecordHeaders();
        if (headers != null) {
            headers.entrySet().forEach(e -> {
                recordHeaders.add(new RecordHeader(e.getKey(), e.getValue().getBytes()));
            });
        }

        this.producerRecord = new ProducerRecord<String, byte[]>(testCase.getProduceTopic(),
                partition == null ? 0 : partition,
                key,
                value,
                recordHeaders);
    }

    public String getName() {
        return testCase.getName();
    }

    public KafkaPubSubTestCase getTestCase() {
        return testCase;
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
