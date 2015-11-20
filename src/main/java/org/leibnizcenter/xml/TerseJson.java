package org.leibnizcenter.xml;

import org.w3c.dom.Attr;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maarten on 19/11/2015.
 */
public class TerseJson {
    public WhiteSpace whitespace = WhiteSpace.Preserve;

    public TerseJson() {

    }

    public TerseJson(WhiteSpace whitespace) {
        this.whitespace = whitespace;
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
                List<Object> children = new ArrayList<Object>(ns.getLength());
                for (int i = 0; i < ns.getLength(); i++) {
                    Object node = convert(ns.item(i));
                    if (node != null) {
                        children.add(node);
                    }
                }
                return children.toArray(new Object[children.size()]);
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
                // [nodeType,tagName[,attributes,[children]]
                Object[] attrs = getAttributes(n);
                Object[] children = getChildren(n);

                if (children.length <= 0 && attrs.length <= 0) {
                    return new Object[]{n.getNodeType(), n.getNodeName()};
                } else if (attrs.length <= 0) {
                    return new Object[]{n.getNodeType(), n.getNodeName(), children};
                } else {
                    return new Object[]{n.getNodeType(), n.getNodeName(), children, attrs};
                }
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

        switch (whitespace) {
            case Preserve:
                break;

            case Ignore:
                if (n.getNodeType() == Node.TEXT_NODE && val.matches("^\\s*$")) {
                    return null;
                }
            case Compact:
                if (n.getNodeType() == Node.TEXT_NODE) {
                    val = val.replaceAll("\\s\\s+", " ");
                }
                //if (n.getNodeType() == Node.ATTRIBUTE_NODE) {
                val = val.replaceAll("(^\\s+)|(\\s+$)", ""); // Trim attribute values
                //}
            default:
                break;
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

    public enum WhiteSpace {
        Preserve, Compact, Ignore
    }
}
