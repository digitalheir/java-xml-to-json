package org.leibnizcenter;

import com.google.gson.Gson;
import org.leibnizcenter.xml.DomHelper;
import org.leibnizcenter.xml.TerseJson;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    private static final TerseJson.WhiteSpace COMPACT_WHITE_SPACE = TerseJson.WhiteSpace.Compact;

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String xml = ("<root>" +
                "<!-- thïs ïs à cómmënt. -->" +
                "  <el ampersand=\"    a &amp;    b\">" +
                "    <selfClosing/>" +
                "  </el>" +
                "</root>");

        // Parse XML to DOM
        Document doc = DomHelper.parse(xml);

        // Conver DOM to terse representation, and convert to JSON
        Object terseDoc = new TerseJson(COMPACT_WHITE_SPACE).convert(doc);
        String json = new Gson().toJson(terseDoc);

        System.out.println(json);
    }
}
