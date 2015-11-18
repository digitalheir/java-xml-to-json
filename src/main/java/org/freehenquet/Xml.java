package org.freehenquet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;
import org.w3c.dom.*;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.xml.parsers.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Maarten on 18/11/2015.
 */
public class Xml {

    public static JsonArray toSparseJson(String xml) throws ParserConfigurationException, IOException, SAXException {
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));


        return toSparseJson(is);
    }

    /**
     * [nodeType, attrs, children]
     *
     * @param doc
     * @return
     */
    public static JsonArray toSparseJson(Document doc) {
        NodeList ns = doc.getChildNodes();
        JsonArray array = new JsonArray();
        for (int i = 0; i < ns.getLength(); i++) {
            Node n = ns.item(i);
            array.add(toSparseJson(n));
        }
        return array;
    }

    public static final List<String> nodeTypes = new ArrayList<String>(15);

    static {
        nodeTypes.set(1, "ELEMENT".toLowerCase(Locale.US));
        nodeTypes.set(2, "ATTRIBUTE".toLowerCase(Locale.US));
        nodeTypes.set(3, "TEXT".toLowerCase(Locale.US));
        nodeTypes.set(4, "CDATA_SECTION".toLowerCase(Locale.US));
        nodeTypes.set(5, "ENTITY_REFERENCE".toLowerCase(Locale.US));
        nodeTypes.set(6, "ENTITY".toLowerCase(Locale.US));
        nodeTypes.set(7, "PROCESSING_INSTRUCTION".toLowerCase(Locale.US));
        nodeTypes.set(8, "COMMENT".toLowerCase(Locale.US));
        nodeTypes.set(9, "DOCUMENT".toLowerCase(Locale.US));
        nodeTypes.set(10, "DOCUMENT_TYPE".toLowerCase(Locale.US));
        nodeTypes.set(11, "DOCUMENT_FRAGMENT".toLowerCase(Locale.US));
        nodeTypes.set(12, "NOTATION".toLowerCase(Locale.US));
    }

    private static JsonElement toSparseJson(Node n) {
        //Add node type
        JsonArray arr = new JsonArray();
        short type = n.getNodeType();
        arr.add(new JsonPrimitive(type));

        //Add node name or value if no meaningful name
        arr.add(getSecondVal(n));

        arr.add(new JsonPrimitive(n.getNodeName()));

        //Add type specific data
        arr.add(getTypeSpecificData(n));

        // Add children
        NodeList ns = n.getChildNodes();
        JsonArray children = new JsonArray();
        for (int i = 0; i < ns.getLength(); i++) {
            Node child = ns.item(i);
            children.add(toSparseJson(child));
        }
        arr.add(children);

        return arr;
    }

    private static JsonElement getSecondVal(Node n) {
        switch (n.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.DOCUMENT_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE:
            case Node.TEXT_NODE:
                //These nodes only contain text
                return new JsonPrimitive(n.getNodeValue());

            case Node.ELEMENT_NODE:
                // tagname
            case Node.PROCESSING_INSTRUCTION_NODE:
                //same as pi.target
            case Node.ENTITY_REFERENCE_NODE:
                // referenced entity name
            case Node.ATTRIBUTE_NODE:
                //attr key
            case Node.ENTITY_NODE:
                //entity name
            case Node.NOTATION_NODE:
                //notation name
            case Node.DOCUMENT_TYPE_NODE:
                //same as doctype.name

                return new JsonPrimitive(n.getNodeName());
            default:
                throw new NotImplementedException();
        }
    }

    private static JsonElement getTypeSpecificData(Node n) {
        // TODO ...
        switch (n.getNodeType()) {
            case Node.ELEMENT_NODE:
                JsonArray arr = new JsonArray();
                arr.add(getAttributes(n));
                return arr;
            case Node.ATTRIBUTE_NODE:
                return new JsonPrimitive(((Attr) n).getValue()); // Name is already added
            case Node.ENTITY_REFERENCE_NODE:
            case Node.ENTITY_NODE:
            case Node.DOCUMENT_NODE:
            case Node.DOCUMENT_TYPE_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE:
            case Node.NOTATION_NODE:
            default:
                throw new NotImplementedException();
        }
    }

    private static JsonArray getAttributes(Node n) {
        NamedNodeMap ns = n.getAttributes();
        JsonArray arr = new JsonArray();
        if (ns != null) {
            for (int i = 0; i < ns.getLength(); i++) {
                el = ns.item(i);
                arr.add();
            }
            return arr;
        }

//    public static String toJson(String xml) throws ParserConfigurationException, SAXException {
//        SAXParserFactory spf = SAXParserFactory.newInstance();
//        spf.setNamespaceAware(true);
//        SAXParser saxParser = spf.newSAXParser();
//        XMLReader xmlReader = saxParser.getXMLReader();
//        xmlReader.setContentHandler(new XmlToJsonSparse());
//        xmlReader.parse(convertToFileURL(filename));
//    }

    }

    public static JsonArray toSparseJson(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        return toSparseJson(doc);
    }
}
