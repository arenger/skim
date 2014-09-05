<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
   <xsl:output method="xml"/>
   <xsl:template match="/">
      <report>
         <areaDescription>
            <xsl:value-of select="/dwml/data[@type='current observations']/location/area-description"/>
         </areaDescription>
         <fahrenheit>
            <xsl:value-of select="/dwml/data[@type='current observations']/parameters/temperature[@type='apparent']/value"/>
         </fahrenheit>
         <humidity>
            <xsl:value-of select="/dwml/data[@type='current observations']/parameters/humidity[@type='relative']/value"/>
         </humidity>
         <wind>
            <xsl:value-of select="concat(round(/dwml/data[@type='current observations']/parameters/wind-speed[@type='sustained']/value * 1.152), ' mph')"/>
         </wind>
         <summary>
            <xsl:value-of select="/dwml/data[@type='current observations']/parameters/weather/weather-conditions[@weather-summary]/@weather-summary"/>
         </summary>
      </report>
   </xsl:template>
</xsl:stylesheet>
