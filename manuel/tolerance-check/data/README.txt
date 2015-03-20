
SIX Hackathon 2015, Schiffbau, Zurich

http://www.six-group.com/careers/en/site/hackathon.html

Data plausibility checks under time-pressure


Contents of this README

1. Files on this USB Stick
2. Explanation





1. Files on this USB Stick
========================

currency_codes.csv			Refernce data
market_codes.csv			Market codes
mdf_stream.csv				01.01.2014 - 13.03.2015, 130.850 prices
news_stream.csv				31.12.2014 - 30.01.2015, 200.000 items
README.txt				this file
statistic_types.csv			Reference data
Used_instruments.csv			Reference data
value_styles.csv			Reference data
value_types.csv				Reference data
Teaser-Data-plausibility-checks.pdf	Powerpoint Slides Tease
Workshop-Hackathon-Data plausibility.pdfPowerpoint Slides Workshop


2. Explanation
==============


currency_codes.csv
------------------

ID				integer
Symbol				string, length = 3
Description			string


market_codes.csv
----------------

marketCode			integer
Exchange_or_Contributor_Name	string


mdf_stream.csv
--------------

GSN				Global Sequence Number			unique integer
marketCode			Bourse code				integer
currencyCode			Currency code				integer, see currency_codes.csv
valorNumber			Valor number				integer
date				Date					YYYY-MM-DD
valueType			VAT					integer, see ID in value_types.csv
statisticType			STT					integer, see ID in statistic_types.csv
valueStyle			VAS					integer, see ID in value_styles.csv
value				Price					float


news_stream.csv
---------------

date				Date 					YYYY-MM-DD
time				Time					range 0-235900
headline			News Headline
sources				Additional instrument information provided by news agency, e.g. ISIN, RIC


statistic_types.csv, value_types.csv, value_styles.csv
------------------------------------------------------

ID				integer
Symbol				string
Description			string


used-instruments.csv
--------------------

valorNumber			integer
valorName			string
Currency			integer, see currency_codes.csv
SecType				string

