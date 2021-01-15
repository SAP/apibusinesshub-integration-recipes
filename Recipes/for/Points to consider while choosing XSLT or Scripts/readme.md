# Points to consider while choosing XSLT or Scripts

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Points-to-consider-while-choosing-XSLT-or-Scripts ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Points-to-consider-while-choosing-XSLT-or-Scripts ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Points-to-consider-while-choosing-XSLT-or-Scripts ) \|

![Meghna Shishodiya](https://github.com/author-profile.png?size=50 ) | [Meghna Shishodiya](https://github.com/author-profile ) |
----|----|

Use this recipe to choose when to use script v/s XSLT

[Download the integration flow Sample](zip-file-name.zip)\
[Download the reuseable integration flow](zip-file-name.zip)

## Recipe

**Motivation:**

Points to consider while choosing XSLT or Scripts

**Guidelines:**

There are no straight guidelines on using one over the other – it depends on the usecase.

You may consider the following points while making a decision:
1.	If you need to change the XML structure, like cloning a node or deleting a node, etc., it is easy and quick to develop it using XSLT.
2.	If you need to perform some calculations based on the XML data and set some variables, an optimal instrument would be a script.
3.	XML parsing within a script is susceptible to XXE attacks (XML External Entity attacks) - instead request a specific payload type as this will trigger type conversion implicitly. This will not be prone to any attacks as the parser that is running during that conversion is secure. For example instead of requesting the message body as an InputStream and then parsing it into a Document, you can request the message body as a Document.
4.	Datatype changes can prove to be very expensive and may bring down the performance. Try to keep the conversions to the minimum – e.g. if the message is the output of an XSLT as a DOM Document, converting it to InputStream and then parsing it into DOM will cause quite some overhead.
5.	Do not convert an XML to string and perform string operations to manipulate the XML. Make use of parsers within the script.
6.	XSLT makes use of the Saxon parser, so whatever holds good for Saxon over SAX, STAX or DOM parsers also apply here.
7.	Consider the parser capabilities and footprint before choosing a DOM parser over SAX/STAX-
Although it may be easy *DOM* parsing the XML in the script, it is important to note that DOM parsers are memory intensive as they load the entire XML in memory and create the XML tree from it. This is an important consideration especially when working with big datasets as they can cause an out-of-memory situation. However, if the dataset is not huge, DOM parsing can prove to be more performant. DOM parsing can also be a better choice when you need to parse the XML back and forth. *SAX/STAX* are very helpful when working with huge datasets as they stream the XML and do not load the entire XML in memory. Moving the XML back and forth may be expensive with these parsers.
8.	Refer a [blog](https://blogs.sap.com/2017/06/20/stream-the-xmlslurper-input-in-groovy-scripts/) on streaming the *xmlslurper* input in groovy scripts.
