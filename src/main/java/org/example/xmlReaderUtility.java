package org.example;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;

public class xmlReaderUtility {
    public Document readXmlFromFile(String filePath) throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new IllegalArgumentException("File not found: " + filePath);
        }
        return readXmlFromInputStream(inputStream);
    }

    public Document readXmlFromInputStream(InputStream inputStream) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(inputStream);
    }

    public Document readXmlFromClipboard() throws Exception {
        String clipboardContent = getClipboardContent();
        if (clipboardContent != null && isXmlContent(clipboardContent)) {
            return parseXmlFromString(clipboardContent);
        }
        return null;
    }

    public void editXmlElementByName(Document document, String parentElementTag, String identifierTag, String identifierValue, String targetElementTag, String newValue) {
        NodeList parentNodeList = document.getElementsByTagName(parentElementTag);
        for (int i = 0; i < parentNodeList.getLength(); i++) {
            Node parentNode = parentNodeList.item(i);
            if (isElementNode(parentNode)) {
                Element parentElement = (Element) parentNode;
                if (findAndEditChildElement(parentElement, identifierTag, identifierValue, targetElementTag, newValue)) {
                    return;
                }
            }
        }
    }

    private boolean findAndEditChildElement(Element parentElement, String identifierTag, String identifierValue, String targetElementTag, String newValue) {
        NodeList childNodes = parentElement.getChildNodes();
        boolean foundIdentifier = false;
        for (int j = 0; j < childNodes.getLength(); j++) {
            Node childNode = childNodes.item(j);
            if (isElementNode(childNode)) {
                Element childElement = (Element) childNode;
                if (identifierTag.equals(childElement.getTagName()) && identifierValue.equals(childElement.getTextContent())) {
                    foundIdentifier = true;
                }
                if (foundIdentifier && targetElementTag.equals(childElement.getTagName())) {
                    childElement.setTextContent(newValue);
                    return true;
                }
            }
        }
        return false;
    }

    private boolean isElementNode(Node node) {
        return node.getNodeType() == Node.ELEMENT_NODE;
    }

    public void saveXml(Document document, String filePath) throws Exception {
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(new File(filePath));
        transformer.transform(source, result);
    }

    private String getClipboardContent() {
        try {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            Transferable contents = clipboard.getContents(null);
            boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
            if (hasTransferableText) {
                return (String) contents.getTransferData(DataFlavor.stringFlavor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean isXmlContent(String content) {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.parse(new InputSource(new StringReader(content)));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private Document parseXmlFromString(String xmlContent) throws Exception {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        return builder.parse(new InputSource(new StringReader(xmlContent)));
    }
}
