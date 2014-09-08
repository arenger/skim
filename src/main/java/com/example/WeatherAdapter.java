package com.example;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherAdapter implements Filter {

   private static final Logger LOG
      = LoggerFactory.getLogger(WeatherAdapter.class);

   private static Transformer transformer;

   @Override
   public void init(FilterConfig config) throws ServletException {
      try {
         TransformerFactory xFactory = TransformerFactory.newInstance();
         StreamSource ssrc = new StreamSource(
            this.getClass().getResourceAsStream(
            config.getInitParameter("xslPath")));
         transformer = xFactory.newTransformer(ssrc);
      } catch (TransformerConfigurationException e) {
         throw new ServletException(e);
      }
   }

   @Override
   public void doFilter(ServletRequest req, ServletResponse res,
         FilterChain chain) throws IOException, ServletException {
      if (req instanceof HttpServletRequest) {
         try {
            chain.doFilter(new TransformedRequest(
               transformer, (HttpServletRequest)req), res);
         } catch (IOException|ServletException e) {
            throw e;
         } catch (Exception e) {
            throw new ServletException(e);
         }
      } else {
         LOG.info("Not a HttpServletRequest");
         chain.doFilter(req, res);
      }
   }

   @Override
   public void destroy() {}
}
