package soya.framework.pdf;

import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;
import com.itextpdf.kernel.pdf.canvas.parser.listener.ITextExtractionStrategy;
import com.itextpdf.kernel.pdf.canvas.parser.listener.SimpleTextExtractionStrategy;
import com.itextpdf.kernel.pdf.tagging.IStructureNode;
import com.itextpdf.kernel.pdf.tagging.PdfStructTreeRoot;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Map;

public class IText {
    public static final String SRC = "C:/github/Restruts/build/nio.pdf";

    public static String[] TITLE_LEVEL = new String[] {
            "# ",
            "## ",
            "### ",
            "#### ",
            "##### ",
            "###### ",
            "####### ",
            "######## ",
            "######### ",
            "########## "
    };

    public static void main(String[] args) throws Exception {
        ByteBuffer buffer = ByteBuffer.allocateDirect(1024);
        ByteOrder byteOrder;

        PdfDocument pdfDoc = new PdfDocument(new PdfReader(SRC));

        PdfOutline outline = pdfDoc.getOutlines(false);
        walkThroughOutline(outline, 0);

        PdfCatalog catalog = pdfDoc.getCatalog();

        /*PdfNameTree destsTree = pdfDoc.getCatalog().getNameTree(PdfName.Dests);
        PdfOutline root = pdfDoc.getOutlines(false);
        walkOutlines(root, destsTree.getNames(), pdfDoc);*/
    }

    private static void walkThroughOutline(PdfOutline outline, int level) {
        if(outline.getDestination() != null ) {
            System.out.println(TITLE_LEVEL[level] + outline.getTitle());

            /*outline.getContent().entrySet().forEach(e -> {
                PdfName pdfName = e.getKey();
                PdfObject value = e.getValue();
                System.out.println(pdfName.getValue() + ": " + value.getClass());
            });*/

        }

        for(PdfOutline child: outline.getAllChildren()) {
            walkThroughOutline(child, level + 1);
        }
    }

    private static void walkOutlines(PdfOutline outline, Map<String, PdfObject> names, PdfDocument pdfDocument) {
        if (outline.getDestination() != null) {
            int pageNum = pdfDocument.getPageNumber((PdfDictionary) outline.getDestination().getDestinationPage(names));
            ITextExtractionStrategy strategy = new SimpleTextExtractionStrategy();

            System.out.println("======================= " + outline.getTitle() + ": page " +
                    pageNum);

            /*try {
                String text = PdfTextExtractor.getTextFromPage(pdfDocument.getPage(pageNum), strategy);
                System.out.println(text);

            } catch (Exception e) {

            }*/
        }

        for (PdfOutline child : outline.getAllChildren()) {
            walkOutlines(child, names, pdfDocument);
        }
    }

}
