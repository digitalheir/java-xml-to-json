package org.leibnizcenter;

import com.google.gson.Gson;
import org.leibnizcenter.xml.DomHelper;
import org.leibnizcenter.xml.NotImplemented;
import org.leibnizcenter.xml.TerseJson;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    private static final TerseJson.WhitespaceBehaviour COMPACT_WHITE_SPACE = TerseJson.WhitespaceBehaviour.Compact;

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, NotImplemented {
        String xml = ("<root>" +
                "<!-- thïs ïs à cómmënt. -->" +
                "  <el ampersand=\"    a &amp;    b\">" +
                "    <selfClosing/>" +
                "  </el>" +
                "</root>");

        // Parse XML to DOM
        Document doc = DomHelper.parse(xml);

        // Convert DOM to terse representation, and convert to JSON
        TerseJson.Options opts = TerseJson.Options.with(COMPACT_WHITE_SPACE);

        Object terseDoc = new TerseJson(opts).convert(doc);
        String json = new Gson().toJson(terseDoc);

        System.out.println(json);
    }
}
