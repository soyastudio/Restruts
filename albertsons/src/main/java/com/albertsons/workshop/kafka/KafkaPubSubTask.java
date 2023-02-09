package com.albertsons.workshop.kafka;

import org.apache.commons.io.IOUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.TopicPartition;

import java.io.File;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

public class KafkaPubSubTask implements Callable<KafkaPubSubReport> {

    private final KafkaPubSubTestCase testCase;
    private final Producer<String, byte[]> producer;
    private final Consumer<String, byte[]> consumer;
    private final File base;

    public KafkaPubSubTask(KafkaPubSubTestCase testCase,
                           Producer<String, byte[]> producer,
                           Consumer<String, byte[]> consumer,
                           File base) {

        this.testCase = testCase;
        this.producer = producer;
        this.consumer = consumer;
        this.base = base;
    }

    @Override
    public KafkaPubSubReport call() throws Exception {
        Thread.sleep(50l);

        long timestamp = System.currentTimeMillis();
        File inputFile = new File(base, testCase.getInput());
        byte[] message = IOUtils.toByteArray(inputFile.toURI());
        KafkaPubSubReport report = new KafkaPubSubReport(testCase, 0, UUID.randomUUID().toString(), message, null);

        producer.send(report.getProducerRecord(), (recordMetadata, e) -> {
            if (e != null) {
                report.setException(e);

            } else {
                report.setRecordMetadata(recordMetadata);

                ((Runnable) () -> {
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }

                    List<PartitionInfo> partitionInfoSet = consumer.partitionsFor(testCase.getConsumeTopic());
                    Collection<TopicPartition> partitions = partitionInfoSet.stream()
                            .map(partitionInfo -> new TopicPartition(partitionInfo.topic(),
                                    partitionInfo.partition()))
                            .collect(Collectors.toList());
                    Map<TopicPartition, Long> latestOffsets = consumer.endOffsets(partitions);

                    try {
                        while (!report.isDone()) {
                            if(System.currentTimeMillis() - timestamp > 30000) {
                                report.setException(new TimeoutException());

                            } else {
                                Thread.sleep(500l);

                                for (TopicPartition partition : partitions) {
                                    List<TopicPartition> assignments = new ArrayList<>();
                                    assignments.add(partition);
                                    consumer.assign(assignments);

                                    Long latestOffset = Math.max(0, latestOffsets.get(partition) - 1);
                                    consumer.seek(partition, Math.max(0, latestOffset));
                                    ConsumerRecords<String, byte[]> polled = consumer.poll(Duration.ofMillis(1000l));
                                    for (ConsumerRecord<String, byte[]> rc : polled) {
                                        if (rc.timestamp() > recordMetadata.timestamp()) {
                                            report.setConsumerRecord(rc);
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception ex) {
                        report.setException(ex);
                    }

                }).run();               
            }
        });

        while (!report.isDone()) {
            Thread.sleep(500l);
        }
        
        return report;
    }
}