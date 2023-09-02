package com.abs.edis.commons;

import com.google.gson.*;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XmlToJsonConverter {

	private Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	private Map<String, String> mappings;

	private Map<String, JsonArray> arrayMap = new LinkedHashMap<>();

	public XmlToJsonConverter(Map<String, String> mappings) {
		this.mappings = mappings;
	}

	public String convert(Node node) {
		arrayMap.clear();
		return GSON.toJson(estimate(node));
	}

	public JsonElement estimate(Node node) {
		String path = path(node);
		if (node.getNodeType() == Node.ATTRIBUTE_NODE
				&& node.getTextContent() != null
				&& node.getTextContent().trim().length() > 0) {
			return new JsonPrimitive(node.getTextContent());

		} else if (node.getNodeType() == Node.ELEMENT_NODE) {
			checkArrayStack(path);

			if (!mappings.containsKey(path)) {
				NodeList nodeList = node.getChildNodes();
				if (nodeList.getLength() == 1
						&& node.getFirstChild().getNodeType() == Node.TEXT_NODE) {
					if(node.getTextContent() != null && node.getTextContent().trim().length() > 0) {
						return new JsonPrimitive(node.getTextContent());
						
					} else {
						// throw new RuntimeException("==================== " + node.getLocalName() + " ======================");
						return null;
					}
				} else if (node.getChildNodes().getLength() > 0) {
					
					JsonObject obj = new JsonObject();
					NamedNodeMap attributes = node.getAttributes();
					if (attributes != null) {
						for (int i = 0; i < attributes.getLength(); i++) {
							Node attr = attributes.item(i);
							String attrName = getNodeName(attr);
							if (!"Abs".equals(attrName)) {
								JsonElement sub = estimate(attr);
								if (sub != null) {
									obj.add(this.getNodeName(attr), sub);
								}
							}
						}
					}

					NodeList list = node.getChildNodes();
					for (int i = 0; i < list.getLength(); i++) {
						Node child = list.item(i);
						JsonElement sub = estimate(child);
						if (sub != null) {
							obj.add(getNodeName(child), sub);
						}
					}

					return obj;

				} else if (node.getAttributes() != null
						&& node.getAttributes().getLength() > 0) {
					JsonObject obj = new JsonObject();
					NamedNodeMap attributes = node.getAttributes();
					if (attributes != null) {
						for (int i = 0; i < attributes.getLength(); i++) {
							Node attr = attributes.item(i);
							String attrName = getNodeName(attr);
							if (!"Abs".equals(attrName)) {
								JsonElement sub = estimate(attr);
								if (sub != null) {
									obj.add(this.getNodeName(attr), sub);
								}
							}
						}
					}
					return obj;
					
				} else {
					return null;
				}

			} else {
				String type = mappings.get(path).toLowerCase();
				if (type.contains("array")) {
					JsonArray array = arrayMap.get(path);
					if (array == null) {
						array = new JsonArray();
						arrayMap.put(path, array);
					}

					if ("array".equals(type)) {
						JsonObject obj = new JsonObject();
						NamedNodeMap attributes = node.getAttributes();
						for (int i = 0; i < attributes.getLength(); i++) {
							Node attr = attributes.item(i);
							JsonElement sub = estimate(attr);
							if (sub != null) {
								obj.add(getNodeName(attr), sub);

							}
						}

						NodeList list = node.getChildNodes();
						for (int i = 0; i < list.getLength(); i++) {
							Node child = list.item(i);
							JsonElement sub = estimate(child);
							if (sub != null) {
								obj.add(this.getNodeName(child), sub);
							}
						}
						array.add(obj);

					} else if (type.endsWith("_array")) {
						String elementType = type.substring(0,
								type.lastIndexOf("_array"));
						JsonElement primitive = convert(node.getTextContent(),
								elementType);
						array.add(primitive);
					}

					return array;

				} else if (node.getTextContent() != null) {
					return convert(node.getTextContent(), type);

				} else if (node.getChildNodes().getLength() > 0) {
					JsonObject obj = new JsonObject();
					NamedNodeMap attributes = node.getAttributes();
					for (int i = 0; i < attributes.getLength(); i++) {
						Node attr = attributes.item(i);
						JsonElement sub = estimate(attr);
						if (sub != null) {
							obj.add(getNodeName(attr), sub);
						}
					}

					NodeList list = node.getChildNodes();
					for (int i = 0; i < list.getLength(); i++) {
						Node child = list.item(i);
						JsonElement sub = estimate(child);
						if (sub != null) {
							obj.add(getNodeName(child), sub);
						}
					}
					return obj;
				}
			}
		}
		
		return null;
	}

	private void checkArrayStack(String path) {
		if (arrayMap.size() > 0) {
			List<String> paths = new ArrayList<>(arrayMap.keySet());
			for (String p : paths) {
				if (!path.equals(p) && !path.startsWith(p + "/")) {
					arrayMap.remove(p);
				}
			}
		}
	}

	private JsonElement convert(String value, String type) {
		if (value == null || value.trim().length() == 0) {
			if ("string".equals(type)) {
				return new JsonPrimitive("");
			} else {
				return JsonNull.INSTANCE;
			}
		}

		if ("boolean".equals(type)) {
			if ("Y".equalsIgnoreCase(value) || "TRUE".equalsIgnoreCase(value)) {
				return new JsonPrimitive(true);
			} else {
				return new JsonPrimitive(false);
			}
		} else if ("short".equals(type)) {
			try {
				return new JsonPrimitive(Short.parseShort(value));
			} catch (Exception e) {
				return new JsonPrimitive(value);
			}
		} else if ("integer".equals(type)) {
			try {
				return new JsonPrimitive(Integer.parseInt(value));
			} catch (Exception e) {
				return new JsonPrimitive(value);
			}
		} else if ("long".equals(type)) {
			try {
				return new JsonPrimitive(Long.parseLong(value));
			} catch (Exception e) {
				return new JsonPrimitive(value);
			}
		} else if ("float".equals(type)) {
			try {
				return new JsonPrimitive(Float.parseFloat(value));
			} catch (Exception e) {
				return new JsonPrimitive(value);
			}

		} else if ("double".equals(type)) {
			try {
				return new JsonPrimitive(Double.parseDouble(value));
			} catch (Exception e) {
				return new JsonPrimitive(value);
			}
		} else if ("number".equals(type)) {
			try {
				if (value.contains(".")) {
					return new JsonPrimitive(Double.parseDouble(value));

				} else {
					return new JsonPrimitive(Long.parseLong(value));
				}
			} catch (Exception e) {
				return new JsonPrimitive(value);
			}
		} else {
			return new JsonPrimitive(value);
		}
	}

	private String path(Node node) {
		String path = getNodeName(node);
		Node parent = node.getParentNode();
		while (parent != null && !"XMLNSC".equals(getNodeName(parent))) {
			path = getNodeName(parent) + "/" + path;
			parent = parent.getParentNode();
		}

		return path;
	}

	private String getNodeName(Node node) {
		String name = node.getNodeName();
		if (name.contains(":")) {
			name = name.substring(name.lastIndexOf(":") + 1);
		}

		return name;
	}
}