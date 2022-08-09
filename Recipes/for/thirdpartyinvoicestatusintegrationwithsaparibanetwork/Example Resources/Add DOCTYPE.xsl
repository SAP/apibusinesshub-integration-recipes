<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/">
	    <xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE cXML SYSTEM "http://xml.cxml.org/schemas/cXML/1.2.014/cXML.dtd"&gt;</xsl:text>
		<xsl:copy-of select="*"/>
	</xsl:template>
</xsl:stylesheet>