# XmlReaderUtility

# Overview

`XmlReaderUtility` is a Java utility class designed to facilitate reading, editing, and saving XML files. The class provides methods to read XML from a file or clipboard, edit specific elements based on their tags and values, and save the modified XML to a specified location.

# How to Use

### 1. Reading an XML File

```java
XmlReaderUtility xmlUtility = new XmlReaderUtility();
String filePath = "path/to/your/xmlfile.xml";
Document document = xmlUtility.readXmlFromFile(filePath);
```
### 2. Reading XML from Clipboard
```java
XmlReaderUtility xmlUtility = new XmlReaderUtility();
Document document = xmlUtility.readXmlFromClipboard();
```
### 3. Editing an XML Element
```java
String parentElementTag = "food";
String identifierTag = "name";
String identifierValue = "French Toast";
String targetElementTag = "price";
String newValue = "Free to people that really like french toast!";
xmlUtility.editXmlElementByName(document, parentElementTag, identifierTag, identifierValue, targetElementTag, newValue);
```
### 4. Saving an XML Document
```java
String outputPath = "path/to/save/modified.xml";
xmlUtility.saveXml(document, outputPath);
```

# Class Methods
## Document readXmlFromFile(String filePath)
Reads an XML file from the specified file path and returns a Document object.

## Document readXmlFromInputStream(InputStream inputStream)
Reads XML content from an input stream and returns a Document object.

## Document readXmlFromClipboard()
Reads XML content from the system clipboard and returns a Document object.

## void editXmlElementByName(Document document, String parentElementTag, String identifierTag, String identifierValue, String targetElementTag, String newValue)
Edits a specific element within the XML document based on the parent element tag, identifier tag, and value, and updates the specified target element's value.

## void saveXml(Document document, String filePath)
Saves the XML document to a specified file path.

# Example Usage in a Test Class
```java
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

import static org.junit.Assert.assertNotNull;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class XmlEditorTest {

    private XmlReaderUtility xmlUtility = new XmlReaderUtility();
    private String outputDirectory = "output";

    @Before
    public void setUp() throws Exception {
        // Ensure the output directory exists
        Path outputDir = Paths.get(outputDirectory);
        if (!Files.exists(outputDir)) {
            Files.createDirectories(outputDir);
        }
    }

    private void testXmlEditing(String inputFilePath, String parentElementTag, String identifierTag, String identifierValue, String targetElementTag, String newValue, String outputFilePath) throws Exception {
        // Get the input stream from the resources folder
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(inputFilePath);
        assertNotNull("Input stream should not be null", inputStream);

        // Parse the input stream into a Document
        Document document = xmlUtility.readXmlFromInputStream(inputStream);
        assertNotNull("Document should be read from file", document);

        // Edit the document
        xmlUtility.editXmlElementByName(document, parentElementTag, identifierTag, identifierValue, targetElementTag, newValue);

        // Save the modified document to the writable directory
        xmlUtility.saveXml(document, outputFilePath);
    }

    @Test
    public void testReadFromFileAndEdit() throws Exception {
        testXmlEditing(
            "breakfast_menu.xml",
            "food",
            "name",
            "French Toast",
            "price",
            "Free to people that really like french toast!",
            outputDirectory + "/breakfast_menu_output.xml"
        );
    }

    @Test
    public void testEditCDCatalog() throws Exception {
        testXmlEditing(
            "cd_catalog.xml",
            "CD",
            "TITLE",
            "Greatest Hits",
            "PRICE",
            "The price is too damn high!",
            outputDirectory + "/cd_catalog_output.xml"
        );
    }

    @Test
    public void testEditPlantCatalog() throws Exception {
        testXmlEditing(
            "plant_catalog.xml",
            "PLANT",
            "COMMON",
            "Buttercup",
            "PRICE",
            "This should actually be a number but it's not",
            outputDirectory + "/plant_catalog_output.xml"
        );
    }
}
```
# Notes
* Place the XML files to be edited in the src/main/resources directory.
* The modified XML files will be saved in the output directory.
* This README.md provides a concise overview of how to use the XmlReaderUtility class, including example usage in a test class