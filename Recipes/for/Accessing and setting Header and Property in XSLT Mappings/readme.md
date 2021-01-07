# Accessing and setting Header and Property in XSLT Mappings

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Accessing-and-setting-Header-and-Property-in-XSLT-Mappings ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Accessing-and-setting-Header-and-Property-in-XSLT-Mappings ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Accessing-and-setting-Header-and-Property-in-XSLT-Mappings ) \|

![Meghna Shishodiya](https://github.com/author-profile.png?size=50 ) | [Meghna Shishodiya](https://github.com/author-profile ) |
----|----|

This recipe will guide you on how to access and set Header and Property in XSLT Mappings

[Download the integration flow Sample](Accessing.zip)


## Recipe

__Motivation:__

You need to access and set Header and Property in an XSLT Script

__Solution:__

All parameters defined in the XLST mapping are automatically bound to Camel headers. In case a property or header with that name already exists, its value gets auto assigned to the parameter.


First the namespace where the extension functions (setheader, setProperty) is registered needs to be defined.

```
<xsl:stylesheet version="2.0"
xmlns:xsl=http://www.w3.org/1999/XSL/Transform
xmlns:hci=http://sap.com/it/>
```

Furthermore, the exchange must be included in the XSLT as a parameter.

```
<xsl:param name="exchange"/>
```

__Setting a header or property:__

When this is done, the extension functions can be called from within the XSLT sheet. The provided functions have three parameters (the first parameter must be the exchange, the other two are header name and header value or exchange property name and value (all strings)).

Here is an example:
~~~
<xsl:template match="/">
   <xsl:value-of select="hci:setHeader($exchange, 'myname', 'myvalue')"/>
   <xsl:value-of select="hci:setProperty($exchange, 'myname', 'myvalue')"/>
</xsl:template>
~~~

__Getting the value of a header or property:__

In order to get the value of a header/property that already exists, simply define a parameter with the same name.

Example: assume that a header/property with the name a1 already exists and has a value 123, simple define a parameter with the same name:

```
<xsl:param name= “a1”>
```

Now 123 is automatically assigned as a value to a1.


## Sample integration flow

![iflowimage](Flow.png)
