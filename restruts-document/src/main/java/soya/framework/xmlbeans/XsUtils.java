package soya.framework.xmlbeans;

import org.apache.xmlbeans.SchemaType;

public class XsUtils {

    public static String cardinality(XsNode xsNode) {
        return xsNode.getMaxOccurs() == null ? xsNode.getMinOccurs() + "-n" : xsNode.getMinOccurs() + "-" + xsNode.getMaxOccurs();
    }

    public static String type(XsNode node) {
        if (XsNode.XsNodeType.Folder.equals(node.getNodeType())) {
            return "complex";

        } else if (XsNode.XsNodeType.Attribute.equals(node.getNodeType())) {
            return getXsType(node.getSchemaType());

        } else {
            return getXsType(node.getSchemaType());

        }
    }

    private static String getXsType(SchemaType schemaType) {
        SchemaType base = schemaType;
        while (base != null && !base.isSimpleType()) {
            base = base.getBaseType();
        }

        if (base == null || XmlBeansUtils.getXMLBuildInType(base) == null) {
            return "string";

        } else {
            XmlBeansUtils.XMLBuildInType buildInType = XmlBeansUtils.getXMLBuildInType(base);
            String type = buildInType.getName();
            if (type.startsWith("xs:")) {
                type = type.substring(3);
            }

            return type;
        }
    }
}
