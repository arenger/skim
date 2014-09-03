package com.example.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Report;

@Path("/weather")
public class WeatherService {

   private static final Logger LOG
      = LoggerFactory.getLogger(WeatherService.class);

   @GET
   @Path("/summary")
   @Consumes("application/xml")
   public String getSummary(Report report) {
      LOG.info("Making summary for " + report.getAreaDescription());
      return report.getAreaDescription();
   }
}
