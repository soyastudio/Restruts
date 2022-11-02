package soya.framework.action.orchestration.pipeline;

import soya.framework.action.orchestration.Pipeline;
import soya.framework.commons.util.StreamUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class PipelineContainer {
    private static Logger logger = Logger.getLogger(PipelineContainer.class.getName());

    private URI home;
    private Map<URI, Deployment> deployments = new ConcurrentHashMap<>();
    private Map<String, Deployment> pipelines = new ConcurrentHashMap<>();

    public PipelineContainer(File home) {
        this.home = home.toURI();
    }

    public PipelineContainer(File home, long heartbeat, long delay) {
        this(home);
        if (heartbeat > 0) {
            long delayMs = Math.max(1000l, delay);
            new Timer().schedule(new TimerTask() {
                                     @Override
                                     public void run() {
                                         scan();
                                     }
                                 },
                    delayMs,
                    heartbeat);
        }
    }

    public String[] pipelines() {
        List<String> list = new ArrayList<>(pipelines.keySet());
        Collections.sort(list);
        return list.toArray(new String[list.size()]);
    }

    public String pipelineDetails(String name) {
        if (!pipelines.containsKey(name)) {
            throw new IllegalArgumentException("Pipeline is not found: " + name);
        }

        return pipelines.get(name).pipeline.toString();
    }

    public synchronized void undeploy(String name) throws IOException {
        if (pipelines.containsKey(name)) {
            Deployment deployment = pipelines.get(name);
            URI uri = deployment.uri;
            Files.delete(Paths.get(uri));
        }
    }

    public synchronized void scan() {
        forUndeploy();
        try {
            forDeploy();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void forUndeploy() {
        deployments.keySet().forEach(e -> {
            File file = Paths.get(e).toFile();
            if (!file.exists() || file.lastModified() > deployments.get(e).deployTimestamp) {
                Deployment deployment = deployments.get(e);
                logger.info("File removed: " + file.getName());
                if (deployment.pipeline != null) {
                    pipelines.remove(deployment.pipeline.getName());
                    logger.info("Pipeline undeployed: " + deployment.pipeline.getName());
                }
                deployments.remove(e);
            }
        });
    }

    private void forDeploy() throws IOException {
        Files.walk(Paths.get(home)).forEach(path -> {
            File file = path.toFile();
            if (file.isFile()) {
                URI uri = file.toURI();
                if (!deployments.containsKey(uri)) {
                    logger.info("Deploying file " + file.getPath());
                    Deployment deployment = new Deployment(file);
                    deployments.put(deployment.uri, deployment);
                    if (deployment.pipeline != null && deployment.pipeline.getName() != null) {
                        Pipeline pipeline = deployment.pipeline;
                        if (!pipelines.containsKey(pipeline.getName())) {
                            pipelines.put(pipeline.getName(), deployment);
                            logger.info("Pipeline deployed: " + pipeline.getName());
                        }
                    }
                }
            }
        });
    }

    static class Deployment {
        private long deployTimestamp;
        private URI uri;
        private Pipeline pipeline;

        public Deployment(File file) {
            this.deployTimestamp = System.currentTimeMillis();
            this.uri = file.toURI();

            try {
                InputStream inputStream = new FileInputStream(file);
                byte[] bin = StreamUtils.copyToByteArray(inputStream);
                String contents = new String(bin);

                inputStream.close();

                try {
                    pipeline = PipelineParser.fromJson(contents);
                } catch (Exception e) {

                }

                if (pipeline == null) {
                    pipeline = PipelineParser.fromJson(contents);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}
