package soya.framework.kafka.actions;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@ActionDefinition(domain = "kafka",
        name = "produce",
        path = "/produce",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON)
public class ProduceAction extends KafkaAction<RecordMetadata> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    protected String topic;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM)
    protected Integer partition;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM)
    protected String keySerializer;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM)
    protected String valueSerializer;

    @ActionProperty(parameterType = ActionProperty.PropertyType.PAYLOAD, description = "Message for produce", required = true)
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
