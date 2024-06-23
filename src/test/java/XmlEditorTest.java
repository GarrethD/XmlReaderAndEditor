
import org.example.xmlReaderUtility;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.w3c.dom.Document;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.testng.AssertJUnit.assertNotNull;


public class XmlEditorTest {

    private xmlReaderUtility xmlUtility = new xmlReaderUtility();
    private String outputDirectory = "output";

    @BeforeTest
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
                "breakfastMeny.xml",
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
