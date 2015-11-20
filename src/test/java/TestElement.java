import org.leibnizcenter.xml.DomHelper;
import org.leibnizcenter.xml.TerseJson;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Maarten on 19/11/2015.
 */
public class TestElement {
    @Test
    public void test() throws IOException, SAXException, ParserConfigurationException {
        Document doc = DomHelper.parse(
                "<el>" +
                "  <el/>" +
                "  <el attrKey=\"attrValue\"/>" +
                "  <el attrKey=\"attrValue\"><child/></el>" +
                "  <el><child/></el>" +
                "</el>");

        Object[] terseDoc = (Object[]) new TerseJson(TerseJson.WhiteSpace.Ignore).convert(doc);
        Assert.assertEquals(terseDoc.length, 2);
        Assert.assertEquals(terseDoc[0], (short) 9);

        Object[] children = (Object[]) terseDoc[1];
        Assert.assertEquals(children.length, 1);

        children = ((Object[]) ((Object[])children[0])[2]);

        Assert.assertEquals(((Object[]) children[0])[0], (short)1);
        Assert.assertEquals(((Object[]) children[0]).length, 2);
        Assert.assertEquals(((Object[]) children[1]).length, 4);
        Assert.assertEquals(((Object[]) children[2]).length, 4);
        Assert.assertEquals(((Object[]) children[3]).length, 3);
    }
}
