import org.freehenquet.Const;
import org.freehenquet.xml.TerseJson;
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
        System.out.println(new TerseJson().toString());
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
