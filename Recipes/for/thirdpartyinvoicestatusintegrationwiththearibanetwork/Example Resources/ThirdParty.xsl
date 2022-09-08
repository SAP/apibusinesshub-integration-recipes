<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:ocr="https://sap.com:22/">
  <xsl:output method="xml" indent="yes"/>
  <xsl:template match="/">
    <ocr:InvoiceReceiverAriba>
        <ocr:DOC>
            <xsl:copy-of select="cXML" />
        </ocr:DOC>
    </ocr:InvoiceReceiverAriba>
  </xsl:template>
</xsl:stylesheet>