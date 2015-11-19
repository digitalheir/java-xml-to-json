# XML to terse JSON
This is a project to convert XML documents to and fro JSON. It does this by taking a disciplined approach
 to create a terse JSON structure. The library had support for node types such as comments and processing instructions,
 but not for DTDs, so it is possible that the conversion is lossy.

## Motivation
There are a bunch of libraries out there that convert, most notably in [`org.json.XML`](http://www.json.org/javadoc/org/json/XML.html). 
The problem with most XML-to-JSON implementations is that we lose a lot of information about the order.

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