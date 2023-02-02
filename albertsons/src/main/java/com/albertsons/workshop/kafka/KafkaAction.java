package com.albertsons.workshop.kafka;

import org.apache.kafka.clients.admin.AdminClient;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;
import org.apache.kafka.common.config.ConfigException;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.apache.kafka.common.header.internals.RecordHeaders;
import soya.framework.action.Action;
import soya.framework.action.ActionParameterType;
import soya.framework.action.ActionProperty;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

public abstract class KafkaAction<T> extends Action<T> {

    public static final long DEFAULT_TIMEOUT = 30000l;
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM)
    protected Long timeout;

    @ActionProperty(parameterType = ActionParameterType.HEADER_PARAM, required = true)
    protected String configuration;

    protected AdminClient adminClient() throws IOException {
        return kafkaClient().adminClient();
    }

    protected KafkaProducer producer() throws IOException {
        return kafkaClient().producer();
    }

    protected KafkaConsumer consumer() throws IOException {
        return kafkaClient().consumer();
    }

    protected KafkaClient kafkaClient() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(configuration + ".properties");
        if (inputStream == null) {
            throw new ConfigException("Configuration does not exist: " + configuration + ".properties");
        }

        Properties properties = new Properties();
        properties.load(inputStream);

        return KafkaClient.create(properties);
    }

    protected Collection<TopicPartition> partitions(String topicName) throws IOException {
        List<PartitionInfo> partitionInfoSet = consumer().partitionsFor(topicName);
        Collection<TopicPartition> partitions = partitionInfoSet.stream()
                .map(partitionInfo -> new TopicPartition(partitionInfo.topic(),
                        partitionInfo.partition()))
                .collect(Collectors.toList());
        return partitions;
    }

    protected long timeout() {
        return timeout == null ? DEFAULT_TIMEOUT : timeout;
    }

    protected boolean isTimeout(long startTime) {
        return (System.currentTimeMillis() - startTime) > timeout();
    }

    protected ProducerRecord<String, byte[]> createProducerRecord(String topicName, Integer partition, String key, String value, Map<String, String> headers) {

        RecordHeaders recordHeaders = new RecordHeaders();
        if (headers != null) {
            headers.entrySet().forEach(e -> {
                recordHeaders.add(new RecordHeader(e.getKey(), e.getValue().getBytes()));
            });
        }

        return new ProducerRecord<String, byte[]>(topicName,
                partition == null ? 0 : partition,
                key,
                value.getBytes(StandardCharsets.UTF_8),
                recordHeaders);
    }
}
