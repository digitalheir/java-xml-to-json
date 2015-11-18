# XML to JSON
This is a project to convert XML documents to and fro JSON. It does this by taking a discplined approach
 to create a sparse JSON structure.

## Motivation
There are a bunch of libraries out there, most notably in [`org.json.XML`](http://www.json.org/javadoc/org/json/XML.html). 
The problem with most XML-to-JSON implementations is that the conversion is incomplete, and can be wordy.

## Usage
```java
public class Main {
    public static void main(String[] args){
        JsonElement json = Xml.toJson(xmlStr);
        System.out.println(json);
    }
}
```