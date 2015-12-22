package org.leibnizcenter.json.to.xml;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.xml.NotImplemented;
import org.leibnizcenter.xml.TerseJson;
import org.w3c.dom.Document;

import java.io.IOException;
import java.io.StringWriter;

/**
 * Created by maarten on 21-12-15.
 */
public class TestJsonToXml {
    @Test
    public void testSimple() throws IOException, NotImplemented {
        String rootNode = "[1,\"root\",[\"\\n \\t  \",[1,\"firstElement\",[\"Text w\",[1,\"secondElement\",[\"ith     a      lot\"]],\"     of  \"]],\"   whitespace\"],[[\"attribute\",\"   white     space    \"]]]";
        String docNode = "[9,[[8,\" This     is      a       comment \"]," + rootNode + "]]";

        TerseJson terseJson = new TerseJson(TerseJson.Options.with(TerseJson.ErrorBehaviour.ThrowAllErrors));
        Document xml = terseJson.toXml(docNode);

        Assert.assertEquals(getString(xml),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<!-- This     is      a       comment -->\n" +
                        "<root attribute=\"   white     space    \">\n" +
                        "    <firstElement>Text w<secondElement>ith     a\n" +
                        "        lot</secondElement>     of  </firstElement>   whitespace</root>\n");

        xml = terseJson.toXml(rootNode);
        Assert.assertEquals(getString(xml),
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                        "<root attribute=\"   white     space    \">\n" +
                        "    <firstElement>Text w<secondElement>ith     a\n" +
                        "        lot</secondElement>     of  </firstElement>   whitespace</root>\n");
    }

    protected String getString(Document xml) throws IOException {
        OutputFormat format = new OutputFormat(xml);
        format.setIndenting(true);
        StringWriter sw = new StringWriter();
        XMLSerializer serializer = new XMLSerializer(sw, format);
        serializer.serialize(xml);

        return sw.toString();
    }
}
