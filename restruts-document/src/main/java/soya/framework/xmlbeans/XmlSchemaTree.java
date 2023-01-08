package soya.framework.xmlbeans;

import org.apache.xmlbeans.*;
import soya.framework.bean.TreeBase;
import soya.framework.bean.TreeNode;
import soya.framework.io.ResourceService;

import java.io.File;
import java.io.IOException;
import java.net.URI;

public class XmlSchemaTree extends TreeBase<XsNode> {

    public XmlSchemaTree(File file) throws XmlException, IOException {
        super();
        // Parse the XML Schema object first to get XML object
        XmlObject parsedSchema = XmlObject.Factory.parse(file,
                new XmlOptions().setLoadLineNumbers().setLoadMessageDigest());
        // We may have more than schemas to validate with
        XmlObject[] schemas = new XmlObject[]{parsedSchema};
        // Compile the XML Schema to create a schema type system
        SchemaTypeSystem sts = XmlBeans.compileXsd(schemas, null, new XmlOptions()
                .setErrorListener(null).setCompileDownloadUrls()
                .setCompileNoPvrRule());

        load(sts);

    }

    public XmlSchemaTree(SchemaTypeSystem sts) {
        load(sts);
    }

    private void load(SchemaTypeSystem sts) {
        SchemaParticle particle = sts.documentTypes()[0].getContentModel();
        SchemaLocalElement element = (SchemaLocalElement) particle;
        XsNode xsNode = new XsNode(element);

        setRoot(new DefaultTreeNode<XsNode>(element.getName().getLocalPart(), xsNode));

        processParticle(sts.documentTypes()[0].getContentModel(), true, null);
    }

    private void processParticle(SchemaParticle sp, boolean mixed, TreeNode<XsNode> parent) {

        switch (sp.getParticleType()) {
            case (SchemaParticle.ELEMENT):
                processElement(sp, mixed, parent);
                break;

            case (SchemaParticle.SEQUENCE):
                processSequence(sp, mixed, parent);
                break;

            case (SchemaParticle.CHOICE):
                processChoice(sp, mixed, parent);
                break;

            case (SchemaParticle.ALL):
                processAll(sp, mixed, parent);
                break;

            case (SchemaParticle.WILDCARD):
                processWildCard(sp, mixed, parent);
                break;

            default:
                // throw new Exception("No Match on Schema Particle Type: " + String.valueOf(sp.getParticleType()));
        }
    }

    private void processElement(SchemaParticle sp, boolean mixed, TreeNode<XsNode> parent) {

        SchemaLocalElement element = (SchemaLocalElement) sp;
        TreeNode<XsNode> treeNode = parent == null ? root() : add(parent, element.getName().getLocalPart(), new XsNode(element));

        SchemaProperty[] attrProps = sp.getType().getAttributeProperties();
        if (attrProps != null) {
            for (SchemaProperty property : attrProps) {
                add(treeNode, "@" + property.getName().getLocalPart(), new XsNode(property));
            }
        }

        if (element.getType().isSimpleType()) {
            // end
            if (!element.getType().isBuiltinType()) {
                // System.out.println("===== simple " + element.getName() + ": " + element.getType());
            }
        } else if (element.getType().getContentModel() != null) {
            // next
            processParticle(element.getType().getContentModel(), mixed, treeNode);

        } else {
            if (element.getType().getBaseType() != null) {
                //System.out.println("================== ??? " + element.getName() + ": " + element.getType().getBaseType().isSimpleType());

            }
        }
    }

    private void processSequence(SchemaParticle sp, boolean mixed, TreeNode<XsNode> parent) {
        SchemaParticle[] spc = sp.getParticleChildren();
        for (int i = 0; i < spc.length; i++) {
            processParticle(spc[i], mixed, parent);
        }
    }

    private void processChoice(SchemaParticle sp, boolean mixed, TreeNode<XsNode> parent) {
        //System.out.println(sp.getName());

    }

    private void processAll(SchemaParticle sp, boolean mixed, TreeNode<XsNode> parent) {
        //System.out.println(sp.getName());
    }

    private void processWildCard(SchemaParticle sp, boolean mixed, TreeNode<XsNode> parent) {
        //System.out.println(sp.getName());
    }


    public static void main(String[] args) throws Exception {
        File xsd = new File("C:\\Albertsons\\workspace\\CMM\\BOD\\GetGroceryOrder.xsd");

        //System.out.println(TreeUtils.print(new XmlSchemaTree(xsd)));

        System.out.println(ResourceService.getAsString(URI.create("file:///C:/Albertsons/workspace/CMM/BOD/GetGroceryOrder.xsd")));

    }

}
