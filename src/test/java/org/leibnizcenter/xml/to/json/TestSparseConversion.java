package org.leibnizcenter.xml.to.json;

import org.junit.Assert;
import org.junit.Test;
import org.leibnizcenter.xml.NotImplemented;
import org.leibnizcenter.xml.TerseJson;
import org.leibnizcenter.xml.helpers.DomHelper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Maarten on 18/11/2015.
 */
public class TestSparseConversion {
    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    @Test
    public void testGeneral() throws IOException, SAXException, ParserConfigurationException, NotImplemented {
        InputStream is = TestSparseConversion.class.getClassLoader().getResourceAsStream("test.xml");
        TerseJson.Options opts = new TerseJson.Options().setWhitespaceBehaviour(TerseJson.WhitespaceBehaviour.Ignore);
        Object[] jsonNode = (Object[]) new TerseJson(
                opts).convert(DomHelper.parse(is));
//        System.out.println(jsonNode);
        Assert.assertEquals(jsonNode[0], (short) 9);
        Assert.assertEquals(((Object[]) ((Object[]) jsonNode[1])[0])[0], (short) 1);
        Assert.assertEquals(((Object[]) ((Object[]) jsonNode[1])[0])[1], "myRoot");
        Assert.assertEquals((((Object[]) ((Object[]) ((Object[]) jsonNode[1])[0])[2]).length), 6);
        Assert.assertEquals(((String[][]) ((Object[]) ((Object[]) jsonNode[1])[0])[3])[0][1], "root");
    }

    @Test
    public void testDoctype() throws IOException, SAXException, ParserConfigurationException, NotImplemented {
        String xml = "<?xml version=\"1.0\" standalone=\"yes\" ?>\n" +
                "<!DOCTYPE author [\n" +
                "  <!ELEMENT author (#PCDATA)>\n" +
                "  <!ENTITY js \"Jo Smith\">\n" +
                "]>\n" +
                "<author>&js;</author>";

        InputStream is = new ByteArrayInputStream(xml.getBytes());
        Object[] jsonNode = (Object[]) new TerseJson(TerseJson.Options
                .with(TerseJson.WhitespaceBehaviour.Compact)
                .and(TerseJson.ErrorBehaviour.ThrowAllErrors)
        ).convert(DomHelper.parse(is));

        System.out.println(jsonNode);
    }
}

