package com.albertsons.workshop.kafka;

import com.albertsons.workshop.configuration.Workspace;
import org.apache.commons.io.FileUtils;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;

import javax.xml.XMLConstants;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class KafkaPubSubTestRunner {

    private static ExecutorService executorService = Executors.newFixedThreadPool(30);

    private KafkaService kafkaService;
    private Workspace workspace;

    public KafkaPubSubTestRunner(KafkaService kafkaService, Workspace workspace) {
        this.kafkaService = kafkaService;
        this.workspace = workspace;
    }

    public String execute(KafkaPubSubTestCase testCase, String env) {
        KafkaClient kafkaClient = kafkaService.get(env);
        KafkaPubSubTask task = new KafkaPubSubTask(testCase, kafkaClient.producer(), kafkaClient.consumer(), workspace.getHome());
        Future<KafkaPubSubReport> future = executorService.submit(task);

        while(!future.isDone()) {
            try {
                Thread.sleep(300l);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        StringBuilder builder = new StringBuilder();
        try {
            printReport(future.get(), builder);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return builder.toString();
    }

    public void runTest(String name, List<KafkaPubSubTestCase> testCases, String env) {
        KafkaClient kafkaClient = kafkaService.get(env);
        File dir = new File(workspace.getHome(), "Test");
        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(dir, name + ".md");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        executorService.execute(new TestRunner(kafkaClient.producer(), kafkaClient.consumer(), testCases, workspace.getHome(), file));
    }

    private static void printReport(KafkaPubSubReport report, StringBuilder builder) {
        builder.append("## ").append(report.getTestCase().getBod()).append("_").append(report.getName()).append("\n");

        builder.append("### Test Case").append("\n");
        builder.append("- Business Object: ").append(report.getTestCase().getBod()).append("\n");
        builder.append("- Produce Topic: ").append(report.getTestCase().getProduceTopic()).append("\n");
        builder.append("- Consume Topic: ").append(report.getTestCase().getConsumeTopic()).append("\n");

        if (report.getRecordMetadata() != null) {
            builder.append("- Message Produced: ").append(report.getRecordMetadata().partition()).append("-").append(report.getRecordMetadata().offset()).append("\n");
        }

        if (report.getException() != null) {
            builder.append("- Failure: ").append(report.getException().getMessage()).append("\n");
        } else {
            builder.append("- Success: in ").append(report.getConsumerRecord().timestamp() - report.getRecordMetadata().timestamp()).append("ms\n");
        }

        builder.append("### Input Message").append("\n");
        builder.append("```").append("\n");

        String in = new String(report.getProducerRecord().value());
        builder.append(in);

        builder.append("\n").append("```").append("\n");
        builder.append("\n");

        builder.append("### Output Message").append("\n");
        builder.append("```").append("\n");

        String out = new String(report.getConsumerRecord().value());
        out = format(out);
        builder.append(out);

        builder.append("\n").append("```").append("\n");
        builder.append("\n");
    }

    private static String format(String src) {
        if(src.startsWith("<") && src.endsWith(">")) {
            try {
                return formatXml(src);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            return src;
        }
    }

    private static String formatXml(String src) throws Exception {
        Source xmlInput = new StreamSource(new StringReader(src));
        StringWriter stringWriter = new StringWriter();
        StreamResult xmlOutput = new StreamResult(stringWriter);
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        transformerFactory.setAttribute("indent-number", 2);
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_DTD, "");
        transformerFactory.setAttribute(XMLConstants.ACCESS_EXTERNAL_STYLESHEET, "");
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.transform(xmlInput, xmlOutput);
        return xmlOutput.getWriter().toString();
    }

    static class TestRunner implements Runnable {

        private final Producer<String, byte[]> producer;
        private final Consumer<String, byte[]> consumer;
        private List<KafkaPubSubTestCase> testCases;
        private File base;
        private File file;

        public TestRunner(Producer<String, byte[]> producer,
                          Consumer<String, byte[]> consumer,
                          List<KafkaPubSubTestCase> testCases,
                          File base,
                          File file) {
            this.producer = producer;
            this.consumer = consumer;
            this.testCases = testCases;
            this.base = base;
            this.file = file;
        }

        @Override
        public void run() {
            long timestamp = System.currentTimeMillis();
            List<Future<KafkaPubSubReport>> futures = new ArrayList<>();
            testCases.forEach(e -> {
                futures.add(executorService.submit(new KafkaPubSubTask(e, producer, consumer, base)));
            });

            while (!isDone(futures)) {
                if (System.currentTimeMillis() - timestamp > 600000l) {
                    throw new RuntimeException("Timeout");

                } else {
                    try {
                        Thread.sleep(1000l);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }

            StringBuilder builder = new StringBuilder();
            futures.forEach(e -> {
                try {
                    KafkaPubSubReport report = e.get();
                    printReport(report, builder);

                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                } catch (ExecutionException ex) {
                    ex.printStackTrace();
                }
            });

            try {
                FileUtils.write(file, builder.toString(), Charset.defaultCharset());
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        private boolean isDone(List<Future<KafkaPubSubReport>> futures) {
            for (Future<KafkaPubSubReport> future : futures) {
                if (!future.isDone()) {
                    return false;
                }
            }

            return true;
        }
    }
}
