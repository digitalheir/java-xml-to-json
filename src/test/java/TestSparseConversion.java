import org.leibnizcenter.xml.DomHelper;
import org.leibnizcenter.xml.TerseJson;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Maarten on 18/11/2015.
 */
public class TestSparseConversion {
    @Test
    public void test() throws IOException, SAXException, ParserConfigurationException {
        InputStream is = TestSparseConversion.class.getClassLoader().getResourceAsStream("test.xml");
        Object[] jsonNode = (Object[]) new TerseJson(TerseJson.WhiteSpace.Ignore).convert(DomHelper.parse(is));
//        System.out.println(jsonNode);
        Assert.assertEquals(jsonNode[0], (short) 9);
        Assert.assertEquals(((Object[]) ((Object[]) jsonNode[1])[0])[0], (short) 1);
        Assert.assertEquals(((Object[]) ((Object[]) jsonNode[1])[0])[1], "myRoot");
        Assert.assertEquals((((Object[]) ((Object[]) ((Object[]) jsonNode[1])[0])[2]).length), 6);
        Assert.assertEquals(((String[][])((Object[]) ((Object[]) jsonNode[1])[0])[3])[0][1], "root");
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}

