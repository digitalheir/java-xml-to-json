package org.leibnizcenter.xml;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import org.apache.xerces.dom.*;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.IOException;

/**
 * Created by maarten on 21-12-15.
 */
public class ConvertJson {

    /**
     * First element must be a document
     *
     * @param reader JSON stream
     * @return XML document
     */
    public static Document parse(JsonReader reader) throws IOException, NotImplemented {
        reader.beginArray();
        int nodeType = reader.nextInt();
        assert nodeType == Node.DOCUMENT_NODE;

        CoreDocumentImpl doc = new DocumentImpl();

        reader.beginArray(); // Children nodes

        while (!reader.peek().equals(JsonToken.END_ARRAY)) {
            Node child = parseNextNode(
                    doc, reader
            );
            doc.appendChild(child);
        }
        reader.endArray();

        return doc;
    }

    public static Node parseNextNode(CoreDocumentImpl doc, JsonReader reader) throws IOException, NotImplemented {
        if (reader.peek().equals(JsonToken.STRING)) {
            return doc.createTextNode(reader.nextString());
        } else {
            reader.beginArray();

            int nodeType = reader.nextInt();
            Node node;
            switch (nodeType) {
                case Node.CDATA_SECTION_NODE:
                    // [nodeType, nodeVal]
                    String data = reader.nextString();
                    node = new CDATASectionImpl(doc, data);
                    break;
                case Node.COMMENT_NODE:
                    // [nodeType, nodeVal]
                    String comment = reader.nextString();
                    node = new CommentImpl(doc, comment);
                    break;
                case Node.PROCESSING_INSTRUCTION_NODE:
                    // [nodeType, nodeTarget,nodeVal]
                    String target = reader.nextString();
                    String val = reader.nextString();
                    node = new ProcessingInstructionImpl(doc, target, val);
                    break;
                case Node.TEXT_NODE:
                    // Return just the string
                    node = new TextImpl(doc, reader.nextString());
                    break;
                case Node.DOCUMENT_NODE:
                    // [nodeType, nodeChildren]
                case Node.ATTRIBUTE_NODE:
                    // [key, value]
                case Node.DOCUMENT_FRAGMENT_NODE: // XML doc without root node
                    throw new IllegalStateException();
                case Node.ELEMENT_NODE:
                    // [nodeType,tagName[,children,[attributes]]
                    String tagName = reader.nextString();
                    Element e = new ElementImpl(doc, tagName);
                    node = e;
                    if (!reader.peek().equals(JsonToken.END_ARRAY)) {
                        setChildren(doc, reader, e);
                        if (!reader.peek().equals(JsonToken.END_ARRAY)) {
                            setAttributes(reader, e);
                        }
                    }
                    break;
                case Node.DOCUMENT_TYPE_NODE:
                case Node.NOTATION_NODE:
                case Node.ENTITY_NODE:
                case Node.ENTITY_REFERENCE_NODE:
                    //todo
                    // [nodeType,tagName[,attributes,[children]]
                default:
                    throw new NotImplemented();
            }
            reader.endArray();
            return node;
        }
    }

    protected static void setChildren(CoreDocumentImpl doc, JsonReader reader, Element e) throws IOException, NotImplemented {
        //Children
        reader.beginArray();
        while (!reader.peek().equals(JsonToken.END_ARRAY)) {
            e.appendChild(parseNextNode(doc, reader));
        }
        reader.endArray();
    }

    protected static void setAttributes(JsonReader reader, Element e) throws IOException {
        reader.beginArray();
        while (!reader.peek().equals(JsonToken.END_ARRAY)) {
            reader.beginArray();
            String key = reader.nextString();
            String value = reader.nextString();
            e.setAttribute(key, value);
            reader.endArray();
        }
        reader.endArray();
    }


}
