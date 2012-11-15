KDD
This project is a BBC news webpage crawler and parser, used to collect corpus like:

<corpus>
<document>
  <id>http://www.reuters.com/article/2012/10/15/us-usa-congress-cliff-idUSBRE89E17U20121015</id>
  <date>Mon Oct 15, 2012 7:10pm</date>
  <title>CEOs urge compromise on U.S. fiscal cliff, debt</title>
  <body>(Reuters) - Corporate chief executives ramped up their calls on Monday
        for Congress to reach a compromise deal that keeps the looming "fiscal
        cliff" from crushing the U.S. economy and starts to shrink U.S. debt</body>
  <related>http://www.reuters.com/article/2012/10/15/us-britain-starbucks-tax-idUSBRE89E0EX20121015</related>
  <related>http://www.reuters.com/article/2012/10/15/markets-global-idUSL3E8L927L20121015</related>
  <relatedhttp://www.reuters.com/article/2012/10/13/us-defense-cuts-idUSBRE89C0HG20121013></related>
  <related>http://www.reuters.com/article/2012/10/13/us-blackrock-fink-idUSBRE89C0AG20121013</related>
  <related>http://www.reuters.com/article/2012/10/13/imf-idUSL1E8LCLJX20121013</related>
</document>
</corpus>



1. Using nekohtml or tagsoup to parse HTML into a DOM
2. Filter unneccesary DOM nodes
3. maintian a crawled webpages urls