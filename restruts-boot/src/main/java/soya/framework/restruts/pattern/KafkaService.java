package soya.framework.restruts.pattern;

import soya.framework.action.dispatch.ActionDispatchPattern;
import soya.framework.action.dispatch.ParamName;
import soya.framework.action.dispatch.proxy.ActionProxyPattern;

@ActionProxyPattern
public interface KafkaService {
    @ActionDispatchPattern(
            uri = "kafka://produce?configuration=param(configuration)&keySerializer=param(keySerializer)&partition=param(partition)&timeout=param(timeout)&topic=param(topic)&valueSerializer=param(valueSerializer)&message=param(message)"
    )
    Object produce(
            @ParamName("configuration") String configuration,
            @ParamName("keySerializer") String keySerializer,
            @ParamName("partition") Integer partition,
            @ParamName("timeout") Long timeout,
            @ParamName("topic") String topic,
            @ParamName("valueSerializer") String valueSerializer,
            @ParamName("message") String message
    );

    @ActionDispatchPattern(
            uri = "kafka://consume-fetch?configuration=param(configuration)&offset=param(offset)&partition=param(partition)&pollDuration=param(pollDuration)&timeout=param(timeout)&topic=param(topic)"
    )
    Object consumeFetch(
            @ParamName("configuration") String configuration,
            @ParamName("offset") Long offset,
            @ParamName("partition") Integer partition,
            @ParamName("pollDuration") Long pollDuration,
            @ParamName("timeout") Long timeout,
            @ParamName("topic") String topic
    );

    @ActionDispatchPattern(
            uri = "kafka://consume-seek-to-end?configuration=param(configuration)&pollDuration=param(pollDuration)&timeout=param(timeout)&topic=param(topic)"
    )
    Object consumeSeekToEnd(
            @ParamName("configuration") String configuration,
            @ParamName("pollDuration") Long pollDuration,
            @ParamName("timeout") Long timeout,
            @ParamName("topic") String topic
    );

    @ActionDispatchPattern(
            uri = "kafka://admin-topic-info?configuration=param(configuration)&timeout=param(timeout)&topic=param(topic)"
    )
    Object adminTopicInfo(
            @ParamName("configuration") String configuration,
            @ParamName("timeout") Long timeout,
            @ParamName("topic") String topic
    );

    @ActionDispatchPattern(
            uri = "kafka://consume-latest?configuration=param(configuration)&pollDuration=param(pollDuration)&timeout=param(timeout)&topic=param(topic)"
    )
    Object consumeLatest(
            @ParamName("configuration") String configuration,
            @ParamName("pollDuration") Long pollDuration,
            @ParamName("timeout") Long timeout,
            @ParamName("topic") String topic
    );

    @ActionDispatchPattern(
            uri = "kafka://admin-metrics?configuration=param(configuration)&timeout=param(timeout)"
    )
    Object adminMetrics(
            @ParamName("configuration") String configuration,
            @ParamName("timeout") Long timeout
    );

    @ActionDispatchPattern(
            uri = "kafka://admin-topics?configuration=param(configuration)&query=param(query)&timeout=param(timeout)"
    )
    Object adminTopics(
            @ParamName("configuration") String configuration,
            @ParamName("query") String query,
            @ParamName("timeout") Long timeout
    );

    @ActionDispatchPattern(
            uri = "kafka://consume-seek-to-begin?configuration=param(configuration)&pollDuration=param(pollDuration)&timeout=param(timeout)&topic=param(topic)"
    )
    Object consumeSeekToBegin(
            @ParamName("configuration") String configuration,
            @ParamName("pollDuration") Long pollDuration,
            @ParamName("timeout") Long timeout,
            @ParamName("topic") String topic
    );

}