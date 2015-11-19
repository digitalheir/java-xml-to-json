# XML to terse JSON
This is a project to convert XML documents to and fro JSON. It does this by taking a disciplined approach
 to create a terse JSON structure. The library has support for node types such as comments and processing instructions,
 but not for DTDs, so it is possible that conversion will produce an error.

## Motivation
There are a bunch of libraries out there that convert, most notably in [`org.json.XML`](http://www.json.org/javadoc/org/json/XML.html). 
The problem with most XML-to-JSON implementations is that we lose a lot of information about ordering / attributes / whatever.

Another motivation was to have a light-weight serialization of XML, since complete transforms 
[tend to be wordy](https://github.com/digitalheir/ruby-xml-to-json). We 
trade off  some readability for a reduction in bytes. Resulting JSON strings are typically slightly larger in bytesize 
than their XML brothers.

## Usage
```java
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        String json = TerseJson.from("<root>" +
                "<!-- thïs ïs à cómmënt. -->" +
                "  <el ampersand=\"&amp;\">" +
                "    <selfClosing/>" +
                "  </el>" +
                "</root>");
        System.out.println(json);
    }
```

produces

```json
[9,[[1,"root",[],[[8," thïs ïs à cómmënt. "],[3,"  "],[1,"el",[[2,"ampersand","\u0026"]],[[3,"    "],[1,"selfClosing",[],[]],[3,"  "]]]]]]]
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
|1|Children; can be any JSON element|

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
