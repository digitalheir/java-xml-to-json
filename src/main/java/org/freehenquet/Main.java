package org.freehenquet;

import com.google.gson.Gson;
import org.freehenquet.xml.DomHelper;
import org.freehenquet.xml.TerseJson;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

public class Main {
    private static final boolean COMPACT_WHITE_SPACE = true;

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
