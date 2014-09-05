package com.example;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.nio.charset.Charset;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;

public class XslReqWrapper extends HttpServletRequestWrapper {
   private static final Logger LOG
      = LoggerFactory.getLogger(XslReqWrapper.class);

   private static final String SIGNATURE =
      "http://graphical.weather.gov/xml/DWMLgen/schema/DWML.xsd";

   private final InputStream xmlStream;

   public XslReqWrapper(HttpServletRequest request) throws IOException {
      super(request);

      String xml = IOUtils.toString(request.getInputStream(),
            Charset.defaultCharset());
      if ((xml.length() > 500) && xml.substring(0,500).contains(SIGNATURE)) {
         try {
            LOG.info("Detected DWML xml format.  Attempting transform.");
            xml = transform(xml);
            LOG.debug("New xml = {}", xml);
         } catch (Exception e) {
            LOG.error("Problem during transform", e);
         }
      }
      xmlStream = new ByteArrayInputStream(xml.getBytes());
   }

   private String transform(String xml) throws Exception {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      Document doc = builder.parse(new ByteArrayInputStream(xml.getBytes()));

      TransformerFactory xFactory = TransformerFactory.newInstance();
      StreamSource ssrc = new StreamSource(
         this.getClass().getResourceAsStream("/skim.xsl"));
      Transformer transformer = xFactory.newTransformer(ssrc);
      DOMSource dsrc = new DOMSource(doc);
   
      StringWriter out = new StringWriter();
      StreamResult result = new StreamResult(out);
      transformer.transform(dsrc, result);
      return out.toString();
   }

   public ServletInputStream getInputStream() throws IOException {
      return new ServletInputStream() {
         public int read() throws IOException {
            return xmlStream.read();
         }
      };
   }
}
