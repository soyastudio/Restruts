package soya.framework.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.PipelineResult;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.metrics.MetricNameFilter;
import org.apache.beam.sdk.metrics.MetricQueryResults;
import org.apache.beam.sdk.metrics.MetricResult;
import org.apache.beam.sdk.metrics.MetricsFilter;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.transforms.PTransform;
import org.apache.beam.sdk.values.PCollection;
import org.apache.beam.sdk.values.POutput;

import java.time.Instant;

public class SoyaBeam {
    public static void main(String[] args) {
        PipelineOptions options = PipelineOptionsFactory.fromArgs(args).create();

        //create a Pipeline object
        Pipeline pipeline = Pipeline.create(options);
        //creating a PCollection object which represents a distributed data set
        //and reading a text file (.txt)
        PCollection<String> output = pipeline.apply(
                TextIO.read().from("C:\\github\\Restruts\\io\\sample-text-file-input.txt")
        );

        //converting the text file into a word document
        output.apply(new PTransform<PCollection<String>, POutput>() {
            @Override
            public POutput expand(PCollection<String> input) {
                System.out.println("---------------------- !!!");
                return input;
            }
        });

        output.apply(
                TextIO.write().to("C:\\github\\Restruts\\io\\sample-text-file-output.md")
                        //if wont use withNumShards , as I told, PCollection is a distibuted set
                        //it will output multiple files instead of 1 single file
                        .withNumShards(1)
                        //to generate a file with extension .docx
                        .withSuffix(".md")
        );
        pipeline.run().waitUntilFinish();
    }

    public static void queryMetrics(PipelineResult result) {
        MetricQueryResults metrics =
                result
                        .metrics()
                        .queryMetrics(
                                MetricsFilter.builder()
                                        .addNameFilter(MetricNameFilter.inNamespace("PollingExample"))
                                        .build());
        Iterable<MetricResult<Long>> counters = metrics.getCounters();
        for (MetricResult<Long> counter : counters) {
            System.out.println(
                    counter.getName().getName() + " : " + counter.getAttempted() + " " + Instant.now());
        }
    }
}
