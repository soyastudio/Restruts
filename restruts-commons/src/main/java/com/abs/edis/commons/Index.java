package com.abs.edis.commons;

import org.w3c.dom.Document;
import soya.framework.util.StreamUtils;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class Index {
    public static void main(String[] args) throws Exception {
        Map<String, String> rules = new LinkedHashMap<>();

        InputStream inputStream = Index.class.getClassLoader().getResourceAsStream("rule.txt");
        String str = StreamUtils.copyToString(inputStream, Charset.defaultCharset());
        Arrays.stream(str.split(";")).forEach(exp -> {
            int begin = exp.indexOf("(");
            int end = exp.indexOf(")");
            String xpath = exp.substring(begin + 1, end);
            String mapping = exp.substring(0, begin);
            rules.put(xpath, mapping);

        });

        //XmlToJsonConverter converter = new XmlToJsonConverter(rules);

        XmlToJsonConverter2 converter = new XmlToJsonConverter2(rules);

        InputStream data = Index.class.getClassLoader().getResourceAsStream("data.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();

        Document document = builder.parse(data);

        String json = converter.convert(document.getDocumentElement());

        System.out.println(json);

    }

}
