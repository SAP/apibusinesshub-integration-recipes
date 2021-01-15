# Fixing Unexpected character in prolog error

\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Fixing-Unexpected-character-in-prolog-error ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Fixing-Unexpected-character-in-prolog-error ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Fixing-Unexpected-character-in-prolog-error ) \|

![Meghna Shishodiya](https://github.com/author-profile.png?size=50 ) | [Meghna Shishodiya](https://github.com/author-profile ) |
----|----|

This recipe provides a hint on how to resolve the prolog error

## Recipe

**Problem:**

Prologue not proper: *"Fault: Could not generate the XML stream caused by:
com.ctc.wstx.exc.WstxUnexpectedCharException: Unexpected character in prolog; expected '<'
 at [row,col {unknown-source}]: [1,1].", caused by "WstxUnexpectedCharException:Unexpected character in prolog; expected '<'*

**Solution:**

The above error occurs when a flow returns a plain text response or a malformed XML back to the sender who is expecting an XML. In case you added a message manually via the content modifier, make sure to add the full XML not just the text.

In case you are already sending an XML, check if it is well-formed. You may use standard tools to help point out the mistake.

Also check if the receiver is returning a non-XML or a malformed response.
