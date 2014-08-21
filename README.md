webscraper
==========

This is application for test task. Used JDK 1.8 and jsoup html-parser. Application  accepts as command line parameters:
- resources URL or path to plain text file containing a list of URLs
- data command(s)
- word (or list of words with “,” delimiter)
- output verbosity flag,  if on then the output should contains information about time spend on data scraping and data processing (-v)
 
Supports the following data processing commands:
- count number of provided word(s) occurrence on webpage(s). (-w)
- count number of characters of each web page (-c)
- extract sentences which contain given words (-e)

Command line parameters example:
java –jar scraper.jar http://www.bbc.com/ news,News,NEW,new,New,world –v –w –c –e
