package org.freehenquet;

import com.google.gson.Gson;
import org.freehenquet.xml.TerseNode;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

/**
 * Created by Maarten on 18/11/2015.
 */
public class TerseJson {
    /**
     * @param xml Well-formed XML string
     * @return JSON string
     */
    public static String from(String xml) throws ParserConfigurationException, IOException, SAXException {
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        return from(is);
    }

    /**
     * @param is {@link InputStream} to a well-formed XML string
     * @return JSON string
     */
    public static String from(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = dBuilder.parse(is);
        return from(doc);
    }

    public static final String[] nodeTypes = new String[13];

    static {
        nodeTypes[1] = "ELEMENT".toLowerCase(Locale.US);
        nodeTypes[2] = "ATTRIBUTE".toLowerCase(Locale.US);
        nodeTypes[3] = "TEXT".toLowerCase(Locale.US);
        nodeTypes[4] = "CDATA_SECTION".toLowerCase(Locale.US);
        nodeTypes[5] = "ENTITY_REFERENCE".toLowerCase(Locale.US);
        nodeTypes[6] = "ENTITY".toLowerCase(Locale.US);
        nodeTypes[7] = "PROCESSING_INSTRUCTION".toLowerCase(Locale.US);
        nodeTypes[8] = "COMMENT".toLowerCase(Locale.US);
        nodeTypes[9] = "DOCUMENT".toLowerCase(Locale.US);
        nodeTypes[10] = "DOCUMENT_TYPE".toLowerCase(Locale.US);
        nodeTypes[11] = "DOCUMENT_FRAGMENT".toLowerCase(Locale.US);
        nodeTypes[12] = "NOTATION".toLowerCase(Locale.US);
    }

    private static String from(Node n) {
        return new Gson().toJson(TerseNode.convert(n));
    }
}
