package soya.framework.restruts.actions.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.OperationMapping;
import soya.framework.restruts.action.ParameterMapping;
import soya.framework.restruts.action.PayloadMapping;

import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@OperationMapping(domain = "kafka",
        name = "produce",
        path = "/kafka/produce",
        method = OperationMapping.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON)
public class ProduceAction extends KafkaAction<RecordMetadata> {

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM, required = true)
    protected String topic;

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    protected Integer partition;

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    protected String keySerializer;

    @ParameterMapping(parameterType = ParameterMapping.ParameterType.HEADER_PARAM)
    protected String valueSerializer;

    @PayloadMapping(consumes = MediaType.TEXT_PLAIN, description = "Message for produce")
    protected String message;

    @Override
    public RecordMetadata execute() throws Exception {
        return send(producer(), createProducerRecord(topic, partition, null, message, null), timeout());
    }

    private RecordMetadata send(KafkaProducer<String, byte[]> kafkaProducer, ProducerRecord<String, byte[]> record, long timeout) throws Exception {
        long timestamp = System.currentTimeMillis();

        Future<RecordMetadata> future = kafkaProducer.send(record);
        while (!future.isDone()) {
            if (System.currentTimeMillis() - timestamp > timeout) {
                throw new TimeoutException("Fail to publish message to: " + record.key() + " in " + timeout + "ms.");
            }

            Thread.sleep(100L);
        }

        kafkaProducer.close();

        return future.get();
    }
}
