package org.leibnizcenter.xml.helpers;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * Created by Maarten on 19/11/2015.
 */
public class DomHelper {
    /**
     * @param xml Well-formed XML string
     * @return JSON string
     */
    public static Document parse(String xml) throws ParserConfigurationException, IOException, SAXException {
        InputStream is = new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8));
        return parse(is);
    }

    /**
     * @param is {@link InputStream} to a well-formed XML string
     * @return JSON string
     */
    public static Document parse(InputStream is) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        return dBuilder.parse(is);
    }
}
