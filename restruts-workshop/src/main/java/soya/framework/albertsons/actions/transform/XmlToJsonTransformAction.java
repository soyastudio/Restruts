package soya.framework.albertsons.actions.transform;

import org.w3c.dom.Document;
import soya.framework.action.ActionDefinition;
import soya.framework.action.ActionProperty;
import soya.framework.action.MediaType;
import soya.framework.action.ParameterType;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.LinkedHashMap;
import java.util.Map;

@ActionDefinition(domain = "albertsons",
        name = "xml-to-json-transform",
        path = "/workshop/transform/xml-to-json",
        method = ActionDefinition.HttpMethod.POST,
        produces = MediaType.APPLICATION_JSON,
        displayName = "EDM Table Mapping",
        description = "EDM Table Mapping.")
public class XmlToJsonTransformAction extends TransformAction {

    @ActionProperty(
            parameterType = ParameterType.HEADER_PARAM,
            option = "r",
            required = true)
    private String rule;

    @ActionProperty(parameterType = ParameterType.HEADER_PARAM,
            description = "Message for converting",
            option = "r",
            required = true)
    private String message;

    @Override
    public String execute() throws Exception {

        System.out.println(message);


        Map<String, String> properties = new LinkedHashMap<String, String>();
        String[] arr = rule.split(";");
        for (String exp : arr) {
            int begin = exp.indexOf("(");
            int end = exp.indexOf(")");
            String xpath = exp.substring(begin + 1, end);
            String mapping = exp.substring(0, begin);

            properties.put(xpath, mapping);
        }

        XmlToJsonConverter converter = new XmlToJsonConverter(properties);

        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {

            // optional, but recommended
            // process XML securely, avoid attacks like XML External Entities (XXE)
            dbf.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

            // parse XML file
            DocumentBuilder db = dbf.newDocumentBuilder();

            Document doc = db.parse(new File(message));

            // optional, but recommended
            // http://stackoverflow.com/questions/13786607/normalization-in-dom-parsing-with-java-how-does-it-work
            /*doc.getDocumentElement().normalize();

            System.out.println("Root Element :" + doc.getDocumentElement().getNodeName());
            System.out.println("------");

            // get <staff>
            NodeList list = doc.getElementsByTagName("staff");

            for (int temp = 0; temp < list.getLength(); temp++) {

                Node node = list.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) node;

                    // get staff's attribute
                    String id = element.getAttribute("id");

                    // get text
                    String firstname = element.getElementsByTagName("firstname").item(0).getTextContent();
                    String lastname = element.getElementsByTagName("lastname").item(0).getTextContent();
                    String nickname = element.getElementsByTagName("nickname").item(0).getTextContent();

                    NodeList salaryNodeList = element.getElementsByTagName("salary");
                    String salary = salaryNodeList.item(0).getTextContent();

                    // get salary's attribute
                    String currency = salaryNodeList.item(0).getAttributes().getNamedItem("currency").getTextContent();

                    System.out.println("Current Element :" + node.getNodeName());
                    System.out.println("Staff Id : " + id);
                    System.out.println("First Name : " + firstname);
                    System.out.println("Last Name : " + lastname);
                    System.out.println("Nick Name : " + nickname);
                    System.out.printf("Salary [Currency] : %,.2f [%s]%n%n", Float.parseFloat(salary), currency);

                }
            }*/

            return converter.convert(doc);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
