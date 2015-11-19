package org.freehenquet;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maarten on 19/11/2015.
 */
public class TerseNode extends ArrayList<Object> {

    public TerseNode(Node n) {
        add(n.getNodeType());

        String nameOrString = getNodeNameOrString(n);
        if (nameOrString != null) add(nameOrString);

        String value = getValue(n);
        if (value != null) add(value);

        ArrayList<TerseNode> attr = getAttributes(n);
        if (attr != null) add(attr);

        List<TerseNode> children = getChildren(n);
        if (children != null) add(children);
    }

    private ArrayList<TerseNode> getAttributes(Node n) {
        switch (n.getNodeType()) {
            case Node.ELEMENT_NODE:
                //Only elements have attributes
                NamedNodeMap attrs = n.getAttributes();
                ArrayList<TerseNode> attrss = new ArrayList<>(attrs.getLength());
                for (int i = 0; i < attrs.getLength(); i++) {
                    attrss.add(new TerseNode(attrs.item(i)));
                }
                return attrss;
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.TEXT_NODE:
            case Node.DOCUMENT_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
            case Node.ATTRIBUTE_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.ENTITY_NODE:
            case Node.NOTATION_NODE:
            case Node.DOCUMENT_TYPE_NODE:
                //Only elements have attributes
                return null;
            default:
                throw new Error("not implemented");
        }
    }

    private String getValue(Node n) {
        switch (n.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.TEXT_NODE:
                //These nodes only contain text; already handled
            case Node.DOCUMENT_NODE:
            case Node.ELEMENT_NODE:
                //These don't have a textual value
                return null;
            case Node.PROCESSING_INSTRUCTION_NODE:
            case Node.ATTRIBUTE_NODE:
                String val = n.getNodeValue();
                return val.length() > 0 ? val : ""; // Return target/key, may be empty
            case Node.DOCUMENT_FRAGMENT_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.ENTITY_NODE:
            case Node.NOTATION_NODE:
            case Node.DOCUMENT_TYPE_NODE:
            default:
                throw new Error("not implemented");
        }
    }

    private List<TerseNode> getChildren(Node n) {
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

                return null;
            //TODO only for nodes types that can actually have children
            case Node.DOCUMENT_NODE:
            case Node.ELEMENT_NODE:
                List<TerseNode> children = new ArrayList<>(ns.getLength());
                for (int i = 0; i < ns.getLength(); i++) {
                    children.add(new TerseNode(ns.item(i)));
                }
                return children;
            case Node.DOCUMENT_FRAGMENT_NODE:
            case Node.ENTITY_REFERENCE_NODE:
            case Node.ENTITY_NODE:
            case Node.NOTATION_NODE:
            case Node.DOCUMENT_TYPE_NODE:
            default:
                throw new Error("not implemented");
        }
    }

    public static String getNodeNameOrString(Node n) {
        switch (n.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.TEXT_NODE:
                //These nodes only contain text
                return n.getNodeValue();
            case Node.DOCUMENT_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE:
                return null;
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
                return n.getNodeName();
            default:
                throw new Error("not implemented");
        }
    }
}
