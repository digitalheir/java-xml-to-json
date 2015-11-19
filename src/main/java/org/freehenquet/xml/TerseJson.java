package org.freehenquet.xml;

import org.json.JSONObject;
import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Maarten on 19/11/2015.
 */
public class TerseJson {
    public boolean compactWhitespace = false;

    public TerseJson() {

    }

    public TerseJson(boolean compactWhitespace) {
        this.compactWhitespace = compactWhitespace;
    }

    public Object[] getChildren(Node n) {
        NodeList ns = n.getChildNodes();
        switch (n.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.TEXT_NODE:
                //These nodes only contain text
            case Node.PROCESSING_INSTRUCTION_NODE:
                // PIs only have a target and a content
            case Node.ATTRIBUTE_NODE:
                // attributes only have a key and a value
                throw new IllegalStateException();
            case Node.DOCUMENT_NODE:
            case Node.ELEMENT_NODE:
                Object[] children = new Object[(ns.getLength())];
                for (int i = 0; i < ns.getLength(); i++) {
                    children[i] = convert(ns.item(i));
                }
                return children;
            case Node.DOCUMENT_FRAGMENT_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.ENTITY_NODE:
            case Node.NOTATION_NODE:
            case Node.DOCUMENT_TYPE_NODE:
            default:
                throw new Error("not implemented: " + n.toString());
        }
    }

    public Object convert(Node n) {
        switch (n.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
                // [nodeType, nodeVal]
                return new Object[]{n.getNodeType(), getNodeValue(n)};
            case Node.PROCESSING_INSTRUCTION_NODE:
                // [nodeType, nodeTarget,nodeVal]
                return new Object[]{n.getNodeType(), n.getNodeName(), getNodeValue(n)};
            case Node.DOCUMENT_NODE:
                // [nodeType, nodeChildren]
                return new Object[]{n.getNodeType(), getChildren(n)};
            case Node.TEXT_NODE:
                //Return just the string
                return getNodeValue(n);
            case Node.ATTRIBUTE_NODE:
                // [key, value]
                Attr attribute = (Attr) n;
                return new String[]{attribute.getNodeName(), getNodeValue(n)};
            case Node.ELEMENT_NODE:
                // {"1":tagName,"2":[attributes],"3":children}
                Map<String, Object> obj = new HashMap<String, Object>();
                // Add tag name
                obj.put("1", n.getNodeName());

                //Only elements have attributes
                String[][] attributes = getAttributes(n);
                if (attributes != null && attributes.length > 0) obj.put("2", attributes);

                //Add children if there are any
                Object[] children = getChildren(n);
                if (children != null && children.length > 0) obj.put("3", children);
                return obj;
            case Node.DOCUMENT_FRAGMENT_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.ENTITY_NODE:
            case Node.NOTATION_NODE:
            case Node.DOCUMENT_TYPE_NODE:
            default:
                throw new Error("not implemented");
        }
    }

    private String getNodeValue(Node n) {
        String val = n.getNodeValue();
        if (n.getNodeType() == Node.TEXT_NODE && compactWhitespace && val.matches("\\s\\s+")) {
            val = val.replaceAll("\\s\\s+", " ");
        }
        if (n.getNodeType() == Node.ATTRIBUTE_NODE && compactWhitespace && val.matches("(^\\s)|(\\s$)")) {
            val = val.trim(); // Trim attribute values
        }
        return val;
    }

    public String[][] getAttributes(Node n) {
        NamedNodeMap attrs = n.getAttributes();
        String[][] attributes = new String[attrs.getLength()][];
        for (int i = 0; i < attrs.getLength(); i++) {
            attributes[i] = (String[]) convert(attrs.item(i));
        }
        return attributes;
    }
}
