package com.example.rest;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.example.Report;

@Path("/weather")
public class WeatherService {

   private static final Logger LOG
      = LoggerFactory.getLogger(WeatherService.class);

   @POST
   @Path("/summary")
   @Consumes("application/xml")
   public String makeSummary(Report report) {
      LOG.info("Making summary for " + report.getAreaDescription());

      StringBuilder sb = new StringBuilder(report.getAreaDescription());
      sb.append(":\n");
      sb.append(report.getSummary());
      sb.append(". ");
      int t  = report.getFahrenheit();
      int hi = (int)calcHeatIndex(t, report.getHumidity());
      sb.append(t);
      if (hi != t) {
         sb.append(" \u00B0F; feels like ");
         sb.append(hi);
      }
      sb.append(" \u00B0F. Wind ");
      sb.append(report.getWind());
      sb.append(".\n");
      return sb.toString();
   }

   // attempted port from
   // http://www.hpc.ncep.noaa.gov/html/heatindex.shtml
   public static double calcHeatIndex(double tempF, double relH) {
      double hi = tempF;
      if (relH > 100) {
         throw new IllegalArgumentException(
            "Relative humidity cannot exceed 100%.");
      } else if (relH < 0) {
         throw new IllegalArgumentException(
            "Relative humidity cannot be less than 0%.");
      } else if (tempF <= 40.0) {
         hi = tempF;
      } else {
         double hitemp = 61.0 + ((tempF - 68.0) * 1.2) + (relH * 0.094);
         double fptemp = tempF;
         double hifinal = 0.5 * (fptemp + hitemp);

         if (hifinal > 79.0) {
            hi = -42.379 + 2.04901523 * tempF + 10.14333127 * relH
                  - 0.22475541 * tempF * relH - 6.83783
                  * (Math.pow(10, -3)) * (Math.pow(tempF, 2)) - 5.481717
                  * (Math.pow(10, -2)) * (Math.pow(relH, 2)) + 1.22874
                  * (Math.pow(10, -3)) * (Math.pow(tempF, 2)) * relH
                  + 8.5282 * (Math.pow(10, -4)) * tempF
                  * (Math.pow(relH, 2)) - 1.99 * (Math.pow(10, -6))
                  * (Math.pow(tempF, 2)) * (Math.pow(relH, 2));
            if ((relH <= 13) && (tempF >= 80.0) && (tempF <= 112.0)) {
               double adj1 = (13.0 - relH) / 4.0;
               double adj2 = Math
                     .sqrt((17.0 - Math.abs(tempF - 95.0)) / 17.0);
               double adj = adj1 * adj2;
               hi = hi - adj;
            } else if ((relH > 85.0) && (tempF >= 80.0) && (tempF <= 87.0)) {
               double adj1 = (relH - 85.0) / 10.0;
               double adj2 = (87.0 - tempF) / 5.0;
               double adj = adj1 * adj2;
               hi = hi + adj;
            }
         } else {
            hi = hifinal;
         }
      }
      return Math.round(hi);
   }
}
