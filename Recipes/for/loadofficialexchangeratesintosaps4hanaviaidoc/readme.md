# Load official exchange rates into SAP S/4HANA via IDoc
\| [Recipes by Topic](../../readme.md ) \| [Recipes by Author](../../author.md ) \| [Request Enhancement](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,enhancement&template=recipe-request.md&title=Improve%20Load%20official%20exchange%20rates%20into%20SAP%20S4HANA%20via%20IDoc ) \| [Report a bug](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,bug&template=bug_report.md&title=Issue%20with%20Load%20official%20exchange%20rates%20into%20SAP%20S4HANA%20via%20IDoc ) \| [Fix documentation](https://github.com/SAP-samples/cloud-integration-flow/issues/new?assignees=&labels=Recipe%20Fix,documentation&template=bug_report.md&title=Docu%20fix%20Load%20official%20exchange%20rates%20into%20SAP%20S4HANA%20via%20IDoc ) \|

![cahthuranag](https://github.com/cahthuranag.png?size=50 )|[cahthuranag](https://github.com/cahthuranag)|
----|----|

Customs duty in SAP GTS and foreign-currency postings in SAP S/4HANA often have to use an official rate: the central bank reference rate, or the customs valuation rate a customs authority sets (for example the EU monthly rate under Article 146 of Implementing Regulation 2015/2447, the US CBP rate under 19 CFR 159, or the CBIC notified rate in India). The [Bloomberg exchange rate recipe](../saps4hanaintegrationwithbloombergbank) picks a file up from a bank SFTP server; this recipe instead calls an HTTP API on a timer and posts the table as an `EXCHANGE_RATE01` IDoc, so the rates land in TCURR under the exchange rate type you choose.

The data source in the sample is the allratestoday.com REST API, which serves central bank and customs authority tables as JSON with the authority's own table date. The fetch step is a separate script, so another JSON source can be swapped in by replacing `FetchRates.groovy`.

[Download the integration flow](Official_Exchange_Rates_to_SAP_IDoc.zip)

## Recipe

Step|Code|Why?
----|----|----
Store the API key|Secure Parameter, default alias `AllRatesToday_API_Key`|Keeps the key out of the flow. Read in the script with `SecureStoreService.getUserCredential()`
Trigger on the source's cadence|Timer start event, externalized as `Schedule`|Central banks publish daily, customs tables weekly or monthly
Fetch the table|`FetchRates.groovy`: `GET /api/v1/customs/{country}/latest` or `/api/v1/central-bank/{code}/latest` with `Authorization: Bearer <key>`|`Customs_Country` (ISO code) takes priority over `Source` (bank code such as `ecb`)
Map to the IDoc|`ExchangeRateToIdoc.groovy`: one `E1EXCHANGE_RATE` segment per rate, `VALID_FROM` = the table's `rate_date`|Postings and duty use the date the authority set, not the run date
Respect TCURR limits|5 decimals; rates above 9999.99999 are skipped unless a ratio is set in `Factors` (e.g. `JPY:100`)|`EXCH_RATE` is DEC 9,5; the ratios must match OBBS
Pick one side of two-sided tables|`Table_Rate_Type` = `import` or `export`|India, Thailand, Korea, Iceland and Uganda customs publish separate import and export rates; load each into its own rate type
Send to SAP|IDoc receiver adapter over Cloud Connector, as in the Bloomberg recipe|Message type `EXCHANGE_RATE`, basic type `EXCHANGE_RATE01`

## Configuration
Parameter|Meaning|Default
----|----|----
`Source`|Central bank or tax authority code (`ecb`, `boe`, `rbi`, `hmrc`, `bazg`, ...)|`ecb`
`Customs_Country`|ISO country code for the customs valuation rate (`NL`, `US`, `IN`, ...)|`-` (not set)
`Rate_Type`|Exchange rate type (KURST), e.g. `M`, or a customs type such as `ZCUS`|`M`
`Home_Currency`|Write every rate as foreign to home currency (direct quotation)|`-` (table's own direction)
`Currencies`|Comma-separated list to load|`-` (all)
`Factors`|Translation ratios matching OBBS, e.g. `JPY:100,KRW:1000`|`-`
`Table_Rate_Type`|`import` or `export` for two-sided customs tables|`-` (import)
`Lookback_Days`|`0` = latest table; `1`-`366` = load every table of the last N days once (backfill)|`0`
`IDoc_Address`, `IDoc_Location_ID`, `IDoc_Credentials`|IDoc receiver: `http://<virtual host>:<port>/sap/bc/srt/idoc?sap-client=<client>`|
`Sender_Port`, `Sender_Partner`, `Receiver_Port`, `Receiver_Partner`|EDI_DC40 partner data; must match WE20|

`-` means "not set", because a Content Modifier constant cannot be empty. Use one configured copy of the flow per source, for example ECB into `M` and the Dutch customs table into `ZCUS`.

## Backend setup
* **SRTIDOC**: register the IDoc SOAP inbound service; **SICF**: activate `/sap/bc/srt/idoc`.
* **WE20**: inbound partner profile for the sender logical system with message type `EXCHANGE_RATE`. Partner data in EDI_DC40 must match, or the IDoc stops in status 56.
* **OBBS**: translation ratios for currencies quoted per 100 or 1000 units, matching `Factors`.
* **SAP GTS**: assign the customs rate type to the legal regulation and distribute TCURR from the feeder system as usual (`RFALEX00`, message type `EXCHANGE_RATE`).

## Status
The Groovy scripts are unit-tested with Groovy 4 and stand-ins for the Cloud Integration `Message` API. The flow follows the layout of the Bloomberg recipe's flow but has not yet been deployed on a Cloud Integration tenant by the author: import it into a test tenant first.

### Related Recipes
* [SAP S/4HANA Integration with Third-Party - Exchange Rate Load](../saps4hanaintegrationwithbloombergbank)
* [Accessing credentials from a script](../Accessing%20credentails%20from%20a%20script)

## References
* [IDoc adapter in SAP Integration Suite](https://help.sap.com/docs/cloud-integration/sap-cloud-integration/configure-idoc-receiver-adapter)
* [Source code and tests](https://github.com/AllRates-Today/sap-official-exchange-rates)
