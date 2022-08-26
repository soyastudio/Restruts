package soya.framework.restruts.action.io;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import soya.framework.restruts.action.MediaType;
import soya.framework.restruts.action.ServletStreamHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DefaultServletStreamHandler implements ServletStreamHandler {
    private Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private static Map<String, ServletStreamHandler> defaultHandlers = new ConcurrentHashMap<>();
    private Map<String, ServletStreamHandler> handlers = new ConcurrentHashMap<>();

    static {
        defaultHandlers.put(MediaType.TEXT_PLAIN, new TexPlainStreamHandler());
        defaultHandlers.put(MediaType.APPLICATION_JSON, new JsonStreamHandler());

    }

    @Override
    public <T> T read(HttpServletRequest request, Class<T> type) throws IOException {
        return null;
    }

    @Override
    public void write(Object object, HttpServletRequest request, HttpServletResponse response) throws IOException {
        String contentType = contentType(request.getHeader("Accept"));
        response.setHeader("Content-Type", contentType);

        OutputStream outputStream = response.getOutputStream();

        if (object instanceof String) {
            outputStream.write(((String) object).getBytes(StandardCharsets.UTF_8));

        } else {
            if (MediaType.TEXT_PLAIN.equals(contentType)) {
                outputStream.write(object.toString().getBytes(StandardCharsets.UTF_8));

            } else if (MediaType.APPLICATION_JSON.equalsIgnoreCase(contentType)) {
                outputStream.write(GSON.toJson(object).getBytes(StandardCharsets.UTF_8));

            }
        }

    }

    private String contentType(String accept) {
        String contentType = MediaType.APPLICATION_JSON;
        if (accept != null) {
            String token = accept.indexOf(";") > 0 ? accept.substring(0, accept.indexOf(";")) : accept;
            String[] accepts = token.split(",");
            if (accepts.length > 0) {
                contentType = accepts[0];
            }

        }

        return contentType;
    }

    private ServletStreamHandler getHandler(String contentType) {
        String key = contentType.toLowerCase();
        if(handlers.containsKey(key)) {
            return handlers.get(key);

        } else if(defaultHandlers.containsKey(key)) {
            return defaultHandlers.get(key);

        } else {
            throw new IllegalArgumentException("Cannot find handler for content type: " + contentType);

        }
    }
}
