package org.leibnizcenter.xml.to.json;

import com.google.gson.Gson;
import org.junit.Test;
import org.leibnizcenter.xml.NotImplemented;
import org.leibnizcenter.xml.TerseJson;
import org.leibnizcenter.xml.helpers.DomHelper;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * Created by Maarten on 19/11/2015.
 */
public class TestWhiteSpaceReduction {
    @Test
    public void test() throws IOException, SAXException, ParserConfigurationException,NotImplemented {
        Document doc = DomHelper.parse("<!-- This     is      a       comment -->" +
                "<root attribute=\"   white     space    \">\n \t  <firstElement>" +
                "Text w" +
                "<secondElement>ith     a      lot</secondElement>" +
                "     of  </firstElement>   whitespace" +
                "</root>");

        Object[] noWhitespaceReduction = (Object[]) new TerseJson().convert(doc);
        TerseJson.Options opts = new TerseJson.Options().setWhitespaceBehaviour(
                TerseJson.WhitespaceBehaviour.Compact);
        Object[] whitespaceReduction = (Object[]) new TerseJson(opts).convert(doc);
        TerseJson.Options optsi = new TerseJson.Options().and(
                TerseJson.WhitespaceBehaviour.Ignore);
        Object[] ignoreWhitespace = (Object[]) new TerseJson(optsi).convert(doc);

        System.out.println(new Gson().toJson(noWhitespaceReduction));
        System.out.println(new Gson().toJson(whitespaceReduction));
        System.out.println(new Gson().toJson(ignoreWhitespace));
    }
}
