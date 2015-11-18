package org.freehenquet;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.Enumeration;
import java.util.Hashtable;

/**
 * Created by Maarten on 18/11/2015.
 */
public class XmlToJsonSparse extends DefaultHandler {
    private JsonArray doc;

    @Override
    public void setDocumentLocator(Locator locator) {

    }

    public void startDocument() throws SAXException {
        doc = new JsonArray();
    }

    public void endDocument() throws SAXException {
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {

    }
}
