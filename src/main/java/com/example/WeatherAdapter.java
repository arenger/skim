package com.example;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WeatherAdapter implements Filter {

   private static final Logger LOG
      = LoggerFactory.getLogger(WeatherAdapter.class);

   @Override
   public void init(FilterConfig arg0) throws ServletException {
      LOG.info("WeatherAdapter initialized");
   }

   @Override
   public void doFilter(ServletRequest req, ServletResponse res,
         FilterChain chain) throws IOException, ServletException {
      LOG.info("WeatherAdapter invoked");
      chain.doFilter(req, res);
   }

   @Override
   public void destroy() {}
}
