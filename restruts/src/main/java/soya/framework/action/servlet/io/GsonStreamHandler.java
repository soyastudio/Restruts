package soya.framework.action.servlet.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import soya.framework.action.MediaType;
import soya.framework.action.servlet.StreamHandler;
import soya.framework.action.servlet.StreamReader;
import soya.framework.action.servlet.StreamWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

@StreamHandler(MediaType.APPLICATION_JSON)
public class GsonStreamHandler implements StreamReader, StreamWriter {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public <T> T read(InputStream inputStream, Class<T> type) throws IOException {
        return null;
    }

    @Override
    public void write(Object object, OutputStream out) throws IOException {
        out.write(GSON.toJson(object).getBytes(StandardCharsets.UTF_8));
    }
}
