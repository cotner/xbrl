<?xml version="1.0" encoding="utf-8"?>

<xsl:transform 
  xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
  xmlns:edgar="http://www.sec.gov/Archives/edgar"
  xmlns:xbrli="http://www.xbrl.org/2003/instance"
  xmlns:link="http://www.xbrl.org/2003/linkbase"
  xmlns:xlink="http://www.w3.org/1999/xlink"
  xmlns:generic="http://xbrl.org/2008/generic"
  xmlns:entity="http://xbrlapi.org/entities" 
  xmlns:label="http://xbrl.org/2008/label"
  version="2.0">

  <xsl:output method="xml" encoding="utf-8" indent="yes"/>

  <xsl:key name="cik" match="edgar:cikNumber" use="."/>

  <xsl:template match="/">
    <link:linkbase
      xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
      xsi:schemaLocation="
      http://www.xbrl.org/2003/linkbase http://www.xbrl.org/2003/xbrl-linkbase-2003-12-31.xsd
      http://xbrl.org/2008/generic http://www.xbrl.org/2008/generic-link.xsd
      http://xbrl.org/2008/label http://www.xbrl.org/2008/generic-label.xsd
      http://xbrlapi.org/entities ../entities.xsd
      ">    
      <link:arcroleRef 
      xlink:type="simple" 
      arcroleURI="http://xbrl.org/arcrole/2008/element-label" 
      xlink:href="http://www.xbrl.org/2008/generic-label.xsd#element-label"/>

      <link:arcroleRef 
      xlink:type="simple" 
      arcroleURI="http://xbrlapi.org/arcrole/equivalent-entity" 
      xlink:href="../entities.xsd#equivalent-entity"/>
      
      <xsl:apply-templates select="//edgar:cikNumber"/>
    </link:linkbase>
  </xsl:template>

  <xsl:template match="edgar:cikNumber">
   <xsl:message>Adding CIK <xsl:value-of select="text()"/></xsl:message>
    <xsl:variable name="value" select="text()"/>
    <xsl:variable name="equivalents" select="//edgar:cikNumber[text() eq $value]"/>
    <xsl:if test=". is $equivalents[1]">
      <xsl:call-template name="addLink"/>
    </xsl:if>
  </xsl:template>
  
  <xsl:template name="addLink">
    <xsl:variable name="file" select="../edgar:xbrlFiles/edgar:xbrlFile[ends-with(@type,'.INS')]/@url"/>
    <xsl:variable name="identifier" select="document($file,/.)/xbrli:xbrl/xbrli:context[1]/xbrli:entity/xbrli:identifier"/>
    <generic:link xlink:type="extended" xlink:role="http://www.xbrl.org/2003/role/link">

      <entity:identifier xlink:type="resource" xlink:label="identifier" scheme="{$identifier/@scheme}" value="{$identifier/text()}"/>
      <label:label xlink:type="resource" xlink:label="label" xml:lang="en" xlink:role="http://www.xbrl.org/2003/role/label">
        <xsl:value-of select="../edgar:companyName"/>
      </label:label>
      <generic:arc xlink:type="arc" xlink:from="identifier" xlink:to="label" xlink:arcrole="http://xbrl.org/arcrole/2008/element-label"/>

      <entity:identifier xlink:type="resource" xlink:label="cik-identifier" scheme="http://sec.gov/cik" value="{.}"/>
      <label:label xlink:type="resource" xlink:label="cik-label" xml:lang="en" xlink:role="http://sec.gov/cik">
        <xsl:value-of select="."/>
      </label:label>
      <generic:arc xlink:type="arc" xlink:from="cik-identifier" xlink:to="cik-label" xlink:arcrole="http://xbrl.org/arcrole/2008/element-label"/>

      <generic:arc xlink:type="arc" xlink:from="cik-identifier" xlink:to="identifier" xlink:arcrole="http://xbrlapi.org/arcrole/equivalent-entity"/>

    </generic:link>
    
  </xsl:template>

</xsl:transform>

