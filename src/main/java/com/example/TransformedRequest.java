package com.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class TransformedRequest extends HttpServletRequestWrapper {

   private final InputStream xmlStream;

   public TransformedRequest(Transformer transformer,
      HttpServletRequest request) throws IOException,
      ParserConfigurationException, SAXException, TransformerException {
      super(request);

      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(request.getInputStream());

      DOMSource dsrc = new DOMSource(doc);
   
      StringWriter out = new StringWriter();
      StreamResult result = new StreamResult(out);
      transformer.transform(dsrc, result);

      xmlStream = new ByteArrayInputStream(out.toString().getBytes());
   }

   public ServletInputStream getInputStream() throws IOException {
      return new ServletInputStream() {
         public int read() throws IOException {
            return xmlStream.read();
         }
      };
   }
}
