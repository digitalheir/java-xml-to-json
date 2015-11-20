import com.google.gson.Gson;
import org.freehenquet.Const;
import org.freehenquet.xml.DomHelper;
import org.freehenquet.xml.TerseJson;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Maarten on 19/11/2015.
 */
public class TestWhiteSpaceReduction {
    @Test
    public void test() throws IOException, SAXException, ParserConfigurationException {
        Document doc = DomHelper.parse("<!-- This     is      a       comment -->" +
                "<root attribute=\"   white     space    \">\n \t  <firstElement>" +
                "Text w" +
                "<secondElement>ith     a      lot</secondElement>" +
                "     of  </firstElement>   whitespace" +
                "</root>");

        Object[] noWhitespaceReduction = (Object[]) new TerseJson().convert(doc);
        Object[] whitespaceReduction = (Object[]) new TerseJson(TerseJson.WhiteSpace.Compact).convert(doc);
        Object[] ignoreWhitespace = (Object[]) new TerseJson(TerseJson.WhiteSpace.Ignore).convert(doc);

        System.out.println(new Gson().toJson(noWhitespaceReduction));
        System.out.println(new Gson().toJson(whitespaceReduction));
        System.out.println(new Gson().toJson(ignoreWhitespace));
    }
}
