package soya.framework.json;

import com.google.gson.JsonElement;

public abstract class JsonNode {
    private JsonNode parent;
    private String name;
    private JsonElement jsonElement;

    private String path;

    protected JsonNode(JsonElement jsonElement) {
        this.jsonElement = jsonElement;
        if (jsonElement.isJsonObject()) {
            this.path = "$";

        } else if (jsonElement.isJsonArray()) {
            this.path = "*";
        }
    }

    public JsonNode(JsonNode parent, String name, JsonElement jsonElement) {
        this.parent = parent;
        this.name = name;
        this.jsonElement = jsonElement;
    }

    public JsonNode getParent() {
        return parent;
    }

    public String getName() {
        return name;
    }

    public JsonElement getJsonElement() {
        return jsonElement;
    }

    public String getPath() {
        return path;
    }

    public enum JsonNodeType {
        BOOLEAN, NUMBER, STRING, JSON_OBJECT, JSON_ARRAY;
    }
}
