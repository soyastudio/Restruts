package soya.framework.json;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

public class JsonTree {
    private static Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private JsonElement jsonElement;

    public JsonTree(JsonElement jsonElement) {
        this.jsonElement = jsonElement;


    }

    public String toString() {
        return GSON.toJson(jsonElement);
    }

    public static JsonTree from(String json) {
        JsonElement jsonElement = JsonParser.parseString(json);
        JsonTree tree = new JsonTree(jsonElement);

        return tree;
    }

    public static JsonTree from(Reader reader) {
        JsonElement jsonElement = JsonParser.parseReader(reader);
        JsonTree tree = new JsonTree(jsonElement);

        return tree;
    }

    public static void main(String[] args) throws FileNotFoundException {
        System.out.println(from(new FileReader("C:\\Albertsons\\workspace\\BOD\\OfferRequest\\input.json")));
    }
}
