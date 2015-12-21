package org.leibnizcenter.xml;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import org.apache.xerces.dom.DocumentImpl;
import org.w3c.dom.*;
import org.w3c.dom.Entity;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maarten on 19/11/2015.
 */
public class TerseJson {
    private final Options options;

    public TerseJson() {
        this.options = new Options();
    }

    public TerseJson(Options options) {
        this.options = options;
    }

    protected Object[] getChildren(Node n) throws NotImplemented {
        switch (n.getNodeType()) {
            case Node.CDATA_SECTION_NODE:
            case Node.COMMENT_NODE:
            case Node.TEXT_NODE:
            case Node.PROCESSING_INSTRUCTION_NODE:
            case Node.ATTRIBUTE_NODE:
                //These nodes only contain text
                // PIs only have a target and a content
                // attributes only have a key and a value
                throw new IllegalStateException("Should not try to get children of node " + n.toString());
            case Node.DOCUMENT_NODE:
            case Node.ELEMENT_NODE:
            case Node.DOCUMENT_FRAGMENT_NODE:
            case Node.DOCUMENT_TYPE_NODE:
                List<Object> children = getConvertedChildNodes(n);
                return children.toArray(new Object[children.size()]);
            case Node.ENTITY_REFERENCE_NODE:
            case Node.ENTITY_NODE:
            case Node.NOTATION_NODE:
                if (shouldIgnoreNodeNotImplemented()) {
                    return new Object[]{};
                }
            default:
                throw new NotImplemented(n);
        }
    }

    protected List<Object> getConvertedChildNodes(Node n) throws NotImplemented {
        NodeList ns = n.getChildNodes();
        List<Object> children = new ArrayList<Object>(ns.getLength());
        for (int i = 0; i < ns.getLength(); i++) {
            Object node = convert(ns.item(i));
            if (node != null) {
                children.add(node);
            }
        }
        return children;
    }

    private boolean shouldIgnoreNodeNotImplemented() {
        return options.errorBehaviour.equals(ErrorBehaviour.IgnoreKnownErrors);
    }

    public String stringify(Node n) throws NotImplemented {
        return new Gson().toJson(convert(n));
    }

    /**
     * First element must be a document
     *
     * @param json JSON stream
     * @return XML document
     */
    public Document toXml(InputStream json) throws IOException, NotImplemented {
        JsonReader reader = null;
        try {
            reader = new JsonReader(new InputStreamReader(json, "utf-8"));
            return ConvertJson.parse(reader);
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException(e);
        } finally {
            if (reader != null) reader.close();
            if (json != null) json.close();
        }
    }

    public Document toXml(String json) throws IOException, NotImplemented {
        InputStream is = new ByteArrayInputStream(json.getBytes(StandardCharsets.UTF_8));
        return toXml(is);
    }

    public Object fromXml(Node n) throws NotImplemented {
        return convert(n);
    }

    public Object convert(Node n) throws NotImplemented {
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
                // Return just the string
                return getNodeValue((Text) n);
            case Node.ATTRIBUTE_NODE:
                // [key, value]
                return new String[]{n.getNodeName(), getNodeValue(n)};
            case Node.ENTITY_NODE:
                return ConvertXml.entity((Entity) n);
            case Node.ELEMENT_NODE:
                // [nodeType,tagName[,attributes,[children]]
                Object[] attrs = getAttributes(n);
                Object[] children = getChildren(n);

                return ConvertXml.element((Element) n, attrs, children);
            case Node.DOCUMENT_FRAGMENT_NODE: // XML doc without root node
                // [nodeType,[children]]
                return new Object[]{n.getNodeType(), getChildren(n)};//TODO create test
            case Node.DOCUMENT_TYPE_NODE:
                // [nodeType,name,entities,notations,publicId,systemId,internalSubset]
                DocumentType dtd = (DocumentType) n;
                String[][] entities = convertNamedNodeMap(dtd.getEntities());
                String[][] notations = convertNamedNodeMap(dtd.getNotations());
                return ConvertXml.documentType(dtd, entities, notations);
            case Node.NOTATION_NODE:
            case Node.ENTITY_REFERENCE_NODE:
                //todo
                // [nodeType,tagName[,children,[attributes]]
                if (shouldIgnoreNodeNotImplemented()) return new Object[]{};
            default:
                throw new NotImplemented(n);
        }
    }

    protected String getNodeValue(Node n) {
        String val = n.getNodeValue();

        if (val == null) return null;
        switch (options.whitespaceBehaviour) {
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

    protected String[][] getAttributes(Node n) throws NotImplemented {
        NamedNodeMap attrs = n.getAttributes();
        String[][] attributes = new String[attrs.getLength()][];
        for (int i = 0; i < attrs.getLength(); i++) {
            attributes[i] = (String[]) convert(attrs.item(i));
        }
        return attributes;
    }

    protected String[][] convertNamedNodeMap(NamedNodeMap attrs) throws NotImplemented {
        String[][] attributes = new String[attrs.getLength()][];
        for (int i = 0; i < attrs.getLength(); i++) {
            attributes[i] = (String[]) convert(attrs.item(i));
        }
        return attributes;
    }


    public enum WhitespaceBehaviour {
        Preserve, Compact, Ignore
    }

    public enum ErrorBehaviour {
        IgnoreKnownErrors, ThrowAllErrors
    }

    public static class Options {
        public WhitespaceBehaviour whitespaceBehaviour = WhitespaceBehaviour.Preserve;
        public ErrorBehaviour errorBehaviour = ErrorBehaviour.ThrowAllErrors;

        public Options() {
        }

        public Options setErrorBehaviour(ErrorBehaviour errorBehaviour) {
            this.errorBehaviour = errorBehaviour;
            return this;
        }

        public Options setWhitespaceBehaviour(WhitespaceBehaviour whitespaceBehaviour) {
            this.whitespaceBehaviour = whitespaceBehaviour;
            return this;
        }

        public Options and(WhitespaceBehaviour whitespaceBehaviour) {
            return setWhitespaceBehaviour(whitespaceBehaviour);
        }

        public Options and(ErrorBehaviour errorBehaviour) {
            return setErrorBehaviour(errorBehaviour);
        }

        public static Options with(WhitespaceBehaviour compactWhiteSpace) {
            return new Options().setWhitespaceBehaviour(compactWhiteSpace);
        }

        public static Options with(ErrorBehaviour e) {
            return new Options().setErrorBehaviour(e);
        }
    }

}
