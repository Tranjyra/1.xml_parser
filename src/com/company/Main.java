package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.*;

public class Main {

    private static final String INPUT_XML = "src/com/company/xml_input.xml";
//    private static final String FORMAT_XSLT = "src/com/company/xslt_format.xslt";

    public static void main(String[] args) {

        try (InputStream inputStream = new FileInputStream(INPUT_XML)) {

            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.parse(inputStream);

            NodeList listOfStudents = document.getElementsByTagName("student");
            //System.out.println(listOfStudents.getLength()); // 7

            for (int i = 0; i < listOfStudents.getLength(); i++) {

                NodeList studentsSubjects = listOfStudents.item(i).getChildNodes();
                double averageMark = 0;

                for (int j = 0; j < studentsSubjects.getLength(); j++) {

                    if (studentsSubjects.item(j).getNodeName().equals("subject")) {
                        averageMark += Double.parseDouble(studentsSubjects.item(j).getAttributes().getNamedItem("mark").getNodeValue());
                    }

                    if (studentsSubjects.item(j).getNodeName().equals("average")) {
                        averageMark /= 3;
                        studentsSubjects.item(j).setTextContent(String.valueOf(averageMark));
                    }

                }

            }

            try (FileOutputStream outputStream = new FileOutputStream("src/com/company/xml_output.xml")) {
                writeXml(document, outputStream);
            }

        } catch (ParserConfigurationException | SAXException | IOException | TransformerException e) {
            e.printStackTrace();
        }

    }

    private static void writeXml(Document document, OutputStream outputStream) throws TransformerException, UnsupportedEncodingException {

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();

        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        DOMSource source = new DOMSource(document);
        StreamResult result = new StreamResult(outputStream);

        transformer.transform(source, result);

    }

}
