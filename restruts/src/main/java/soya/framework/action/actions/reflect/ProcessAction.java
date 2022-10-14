package soya.framework.action.actions.reflect;

import soya.framework.action.Action;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;

import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@ActionDefinition(domain = "reflect",
        name = "process",
        path = "/process",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.TEXT_PLAIN,
        displayName = "Process",
        description = "Print as markdown format.")
public class ProcessAction extends Action<String> {

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String command;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, required = true)
    private String directory;

    @ActionProperty(parameterType = ActionProperty.PropertyType.HEADER_PARAM, defaultValue = "10")
    private Integer timeoutInSecond = 10;

    @Override
    public String execute() throws Exception {
        boolean isWindows = System.getProperty("os.name")
                .toLowerCase().startsWith("windows");

        List<String> list = new ArrayList<>();
        /*if (isWindows) {
            list.add("cmd.exe");
        } else {
            list.add("sh");
        }*/

        StringTokenizer tokenizer = new StringTokenizer(command);
        while (tokenizer.hasMoreTokens()) {
            list.add(tokenizer.nextToken());
        }

        File dir = directory == null? new File(System.getProperty("user.home")) : new File(URI.create(directory).toURL().getFile());
        Process process = new ProcessBuilder()
                .command(list)
                .directory(dir)
                .start();

        StringBuilder stringBuilder = new StringBuilder();
        ProcessConsumer processConsumer =
                new ProcessConsumer(process.getInputStream(), x -> stringBuilder.append(x).append("\n"));

        Future<?> future = Executors.newSingleThreadExecutor().submit(processConsumer);
        int exitCode = process.waitFor();
        assert exitCode == 0;
        future.get(timeoutInSecond, TimeUnit.SECONDS);

        return stringBuilder.toString();
    }

    private static class ProcessConsumer implements Runnable {
        private InputStream inputStream;
        private Consumer<String> consumer;

        public ProcessConsumer(InputStream inputStream, Consumer<String> consumer) {
            this.inputStream = inputStream;
            this.consumer = consumer;
        }

        @Override
        public void run() {
            new BufferedReader(new InputStreamReader(inputStream)).lines()
                    .forEach(consumer);
        }
    }
}