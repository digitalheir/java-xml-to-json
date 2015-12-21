package org.leibnizcenter.xml;

import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Created by maarten on 21-12-15.
 */
class ConvertXml {
    private ConvertXml() {
        throw new IllegalStateException();
    }


    public static Object[] entity(org.w3c.dom.Entity e) {
        /**
         * The public identifier associated with the entity if specified, and
         * <code>null</code> otherwise.
         */
        String pubId = e.getPublicId();

        /**
         * The system identifier associated with the entity if specified, and
         * <code>null</code> otherwise. This may be an absolute URI or not.
         */
        String sysId = e.getSystemId();

        /**
         * For unparsed entities, the name of the notation for the entity. For
         * parsed entities, this is <code>null</code>.
         */
        String notName = e.getNotationName();

        /**
         * An attribute specifying the encoding used for this entity at the time
         * of parsing, when it is an external parsed entity. This is
         * <code>null</code> if it an entity from the internal subset or if it
         * is not known.
         * @since DOM Level 3
         */
        String inpEnc = e.getInputEncoding();

        /**
         * An attribute specifying, as part of the text declaration, the encoding
         * of this entity, when it is an external parsed entity. This is
         * <code>null</code> otherwise.
         * @since DOM Level 3
         */
        String xmlEnc = e.getXmlEncoding();

        /**
         * An attribute specifying, as part of the text declaration, the version
         * number of this entity, when it is an external parsed entity. This is
         * <code>null</code> otherwise.
         * @since DOM Level 3
         */
        String xmlVersion = e.getXmlVersion();
        return new String[]{e.getNodeName(), e.getNodeValue()};
    }

    public static Object element(Element n, Object[] attrs, Object[] children) {
        if (children.length <= 0 && attrs.length <= 0) {
            return new Object[]{n.getNodeType(), n.getNodeName()};
        } else if (attrs.length <= 0) {
            return new Object[]{n.getNodeType(), n.getNodeName(), children};
        } else {
            return new Object[]{n.getNodeType(), n.getNodeName(), children, attrs};
        }
    }

    /**
     * Each Document has a doctype attribute whose value is either null or a DocumentType object.
     * The DocumentType interface in the DOM Core provides an interface to the list of entities
     * that are defined for the document, and little else because the effect of namespaces and
     * the various XML schema efforts on DTD representation are not clearly understood as of
     * this writing.
     *
     * DOM Level 3 doesn't support editing DocumentType nodes. DocumentType nodes are read-only.
     */
    public static Object documentType(DocumentType dtd, String[][] entities, String[][] notations) {
        return new Object[]{
                dtd.getNodeType(),// short
                dtd.getName(),// String
                entities,// NamedNodeMap
                notations,// NamedNodeMap
                dtd.getPublicId(),// String
                dtd.getSystemId(),// String
                dtd.getInternalSubset() //String
        };
    }
}
