# Invoke Java functions from XSLT Mapping

![Kamlesh Zanje](https://github.com/kamleshzanje.png?size=50 )|[Kamlesh Zanje](https://github.com/kamleshzanje)|
----|----|

Writing reflexive extension functions in Java to be invoked from XSLT Mappings.

[Download the integration flow Sample](XSLT_3.0_Java_function_call.zip)

## Recipe

Step|Code|Why?
----|----|----
Extensibility capability - [Java function call](http://www.saxonica.com/html/documentation/extensibility/functions/)|```<xsl:template match="/"	xmlns:date="java:java.util.Date"> <xsl:value-of select="date:new()"/>	</xsl:template>``` | Writing reflexive extension functions in Java|



## References
* [SAP Help - Create XSLT Mapping](https://help.sap.com/viewer/368c481cd6954bdfa5d0435479fd4eaf/Cloud/en-US/5ce1f15f54244d4aa557e9c79d93a684.html)
* [Cloud Platform Integration – XSLT Mapping is enriched with XSLT 3.0 specification](https://blogs.sap.com/2019/04/16/cloud-platform-integration-xslt-mapping-is-enriched-with-xslt-3.0-specification/)
* [What is new in XSLT 3.0](https://www.w3.org/TR/xslt-30/#whats-new-in-xslt3)

## Sample integration flow
The integration flow depicted in the recipe is very simple which contains start timer and XSLT Mapping step through which we can leverage the invocation of java function call from XSLT script.
![iflowimage](XSLT_Mapping_Java_function_call.jpg)

### Sample Script
This is the script used in the sample integration flow which starts with namespace declaration and then invokes the java function which display the current date.
```
<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="3.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
	<xsl:template match="/"
        xmlns:date="java:java.util.Date">
    <xsl:value-of select="date:new()"/>
	</xsl:template>
</xsl:stylesheet>

```


### Sample Output
Output of the Java function invocation from the XSLT script can be experienced in the Monitor's message processing view.
![Output Image](XSLT_Mapping_Java_function_call_output.jpg)
