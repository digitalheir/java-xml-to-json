# XML to terse JSON
[![GitHub version](https://badge.fury.io/gh/digitalheir%2Fjava-xml-to-json.svg)](http://badge.fury.io/gh/digitalheir%2Fjava-xml-to-json)
[![Build Status](https://travis-ci.org/digitalheir/java-xml-to-json.svg?branch=master)](https://travis-ci.org/digitalheir/java-xml-to-json)

This is a project to convert XML documents to JSON. It does this by taking a disciplined approach
 to create a terse JSON structure. The library has support for node types such as comments and processing instructions,
 but not for DTDs, so it is possible that conversion will produce an error. Conversions are also *not* guaranteed to be
 complete. You may lose some information in the process.

## JavaDoc
JavaDoc is available at http://phil.uu.nl/~trompper/xml-to-json-0.2.0-javadoc/

## Usage
Download [the latest JAR](https://github.com/digitalheir/java-xml-to-json/releases/latest) or grab from Maven:

```xml
<dependencies>
        <dependency>
            <groupId>org.freehenquet</groupId>
            <artifactId>xml-to-json</artifactId>
            <version>0.2.0</version>
        </dependency>
</dependencies>
```

or Gradle:
```groovy
compile 'org.freehenquet:xml-to-json:0.2.0'
```

## Installation


## Motivation
There are a bunch of libraries out there that convert, most notably in [`org.json.XML`](http://www.json.org/javadoc/org/json/XML.html). 
The problem with most XML-to-JSON implementations is that we lose a lot of information about ordering / attributes / whatever.

Another motivation was to have a light-weight serialization of XML, since complete transforms 
[tend to be wordy](https://github.com/digitalheir/ruby-xml-to-json). We 
trade off  some readability for a reduction in bytes. Resulting JSON strings are typically slightly larger in bytesize 
than their XML brothers.

## Usage
```java
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
```

produces

```json
[9,[{"1":"root","3":[[8," thïs ïs à cómmënt. "]," ",{"1":"el","2":[["ampersand","    a \u0026    b"]],"3":[" ",{"1":"selfClosing"}," "]}]}]]
```

## Details

### Node types
```json
{ 
    "1": "element",
    "2": "attribute",
    "3": "text",
    "4": "cdata_section",
    "5": "entity_reference",
    "6": "entity",
    "7": "processing_instruction",
    "8": "comment",
    "9": "document",
    "10": "document_type",
    "11": "document_fragment",
    "12": "notation"
}
```

### Document
Translates to a fixed length JSON array:

|index|type|description|
|---|---|---|
|0|int|[9](#node-types)|
|1|array|Children; can be any JSON element|

### Element
Translates to a JSON object, keyed by integers:

|key|type|description|optional/required|
|---|---|---|---|
|"1"|String|tag name|required|
|"2"|Array of [key, value] arrays|attributes|optional|
|"3"|Array|children; any JSON element|optional|

### Attribute
Translates to a fixed length JSON array:

|index|type|description|
|---|---|---|
|0|String|attribute name|
|1|String|attribute value|

### Text
Text nodes are converted to JSON strings

### Comment
Translates to a fixed length JSON array:

|index|type|description|
|---|---|---|
|0|int|[8](#node-types)|
|1|String|Comment text|

### CDATA
Translates to a fixed length JSON array:

|index|type|description|
|---|---|---|
|0|int|[4](#node-types)|
|1|String|attribute value|

### Processing instruction
Translates to a fixed length JSON array:

|index|type|description|
|---|---|---|
|0|targ|[7](#node-types)|
|1|String|target|
|1|String|content|

### Entity Reference
Not yet implemented

### Entity 
Not yet implemented

### Document type
Not yet implemented

### Document fragment
Not yet implemented

### Notation
Not yet implemented
