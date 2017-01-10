package com.java.kanke.utils.kmeans;

import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileOutputStream;

public class XmlParse {
	private static String filename="src/datas.xml";
	public static Document getDocument() throws Exception{
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		/*factory.setIgnoringElementContentWhitespace(true);*/
		DocumentBuilder document=factory.newDocumentBuilder();
		Document dom=document.parse(filename);
		return dom;
	}
	public static void write2Xml(Document document) throws Exception{
		TransformerFactory factory=TransformerFactory.newInstance();
		Transformer transformer=factory.newTransformer();
		transformer.transform(new DOMSource(document),new StreamResult(new FileOutputStream(filename)));
	}
}
