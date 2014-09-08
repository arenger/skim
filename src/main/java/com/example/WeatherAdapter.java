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

   private String xslPath;

   // Create a Transformer object for every thread
   private final ThreadLocal<Transformer> localTrans =
      new ThreadLocal<Transformer>() {
         @Override protected Transformer initialValue() {
            Transformer t = null;
            try {
               TransformerFactory xFactory = TransformerFactory.newInstance();
               StreamSource ssrc = new StreamSource(
                  WeatherAdapter.class.getResourceAsStream(getXslPath()));
               t = xFactory.newTransformer(ssrc);
            } catch (TransformerConfigurationException e) {
               LOG.error("Could not create transformer", e);
            }
            return t;
         }
      };

   private String getXslPath() {
      // The xslPath member variable cannot be final, I don't think,
      // because it is initialized via call to "init", not a constructor.
      return xslPath;
   }

   @Override
   public void init(FilterConfig config) throws ServletException {
      xslPath = config.getInitParameter("xslPath");
      if (xslPath == null) {
         throw new ServletException("Expected init-param not set: xslPath");
      }
      if (WeatherAdapter.class.getResource(xslPath) == null) {
         throw new ServletException("Invalid xslPath: " + xslPath);
      }
   }

   @Override
   public void doFilter(ServletRequest req, ServletResponse res,
         FilterChain chain) throws IOException, ServletException {
      if (req instanceof HttpServletRequest) {
         try {
            chain.doFilter(new TransformedRequest(
               localTrans.get(), (HttpServletRequest)req), res);
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
