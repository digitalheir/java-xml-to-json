import org.freehenquet.Xml;
import org.freehenquet.XmlToJsonSparse;
import org.junit.Test;

import java.io.InputStream;

/**
 * Created by Maarten on 18/11/2015.
 */
public class TestSparseConversion {
    @Test
    public void test(){
        InputStream is = TestSparseConversion.class.getResourceAsStream("/test.xml");
        Xml.toSparseJson(is);
    }
}
