package com.example;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "report")
public class Report {
   private String areaDescription;
   private int    fahrenheit;
   private int    humidity;
   private String summary;
   private String wind;
      
   public Report() {}

   @XmlElement
   public String getAreaDescription() {
      return areaDescription;
   }

   public void setAreaDescription(String areaDescription) {
      this.areaDescription = areaDescription;
   }

   @XmlElement
   public int getFahrenheit() {
      return fahrenheit;
   }

   public void setFahrenheit(int fahrenheit) {
      this.fahrenheit = fahrenheit;
   }

   @XmlElement
   public int getHumidity() {
      return humidity;
   }

   public void setHumidity(int humidity) {
      this.humidity = humidity;
   }

   @XmlElement
   public String getSummary() {
      return summary;
   }

   public void setSummary(String summary) {
      this.summary = summary;
   }

   @XmlElement
   public String getWind() {
      return wind;
   }

   public void setWind(String wind) {
      this.wind = wind;
   }
}
