import com.google.gson.Gson;
import org.freehenquet.xml.DomHelper;
import org.freehenquet.xml.TerseJson;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by Maarten on 19/11/2015.
 */
public class ComputeSizeReduction {
    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException {
        String url = "https://repository.officiele-overheidspublicaties.nl/bwb/BWBR0032203/2014-01-25_0/xml/BWBR0032203_2014-01-25_0.xml";
        computeReduction(url);


        computeReduction("http://data.rechtspraak.nl/uitspraken/content?id=ECLI:NL:CBB:2010:BN1294");

//        System.out.println(json);
    }

    private static void computeReduction(String url) throws ParserConfigurationException, IOException, SAXException {
        String xml = new Scanner(new URL(url).openStream(), "UTF-8").useDelimiter("\\A").next();
        System.out.println("XML: " + xml.getBytes().length/1024 + " kilobytes");

        String json = new Gson().toJson(new TerseJson().convert(DomHelper.parse(xml)));
        System.out.println("JSON w/o whitespace reduction : " + json.getBytes().length/1024 + " kilobytes");
        System.out.println("Thats a reduction of " + (100 - (json.getBytes().length*100) / xml.getBytes().length) + "%");

        json = new Gson().toJson(new TerseJson(true).convert(DomHelper.parse(xml)));
        System.out.println("JSON w/ whitespace reduction : " + json.getBytes().length/1024 + " kilobytes");
        System.out.println("Thats a reduction of " + (100 - (json.getBytes().length*100) / xml.getBytes().length) + "%");
    }
}
