package com.albertsons.workshop.beam;

import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.PipelineOptions;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.MediaType;

@ActionDefinition(domain = "beam",
        name = "beam",
        path = "/beam",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN)
public class BeamAction extends Action<String> {

    @Override
    public String execute() throws Exception {
        PipelineOptions options = PipelineOptionsFactory.create();
        new PipelineOptions.DirectRunner().create(options);

        Pipeline p = Pipeline.create(options);

        p.apply(TextIO.read().from(""));

        p.run().waitUntilFinish();

        return "successful";
    }
}
