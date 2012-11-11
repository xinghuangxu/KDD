package kdd.xinghuangxu.parse.html;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Map;

import kdd.xinghuangxu.parse.html.dataStruc.Content;
import kdd.xinghuangxu.parse.html.dataStruc.HTMLMetaTags;
import kdd.xinghuangxu.parse.html.dataStruc.Metadata;
import kdd.xinghuangxu.parse.html.dataStruc.Outlink;
import kdd.xinghuangxu.parse.html.dataStruc.ParseResult;
import kdd.xinghuangxu.parse.html.dataStruc.ParseStatus;


import org.apache.nutch.util.EncodingDetector;
import org.w3c.dom.DOMException;
import org.w3c.dom.DocumentFragment;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class HTMLParser {
	
	private DOMContentUtils utils;

	
	  public ParseResult getParse(Content content) {
		  
		  
		    HTMLMetaTags metaTags = new HTMLMetaTags();

		    URL base;
		    try {
		      base = new URL(content.getBaseUrl());
		    } catch (MalformedURLException e) {
		      return new ParseStatus(e).getEmptyParseResult(content.getUrl());
		    }

		    String text = "";
		    String title = "";
		    Outlink[] outlinks = new Outlink[0];
		    Metadata metadata = new Metadata();

		    // parse the content
		    DocumentFragment root;
		    try {
		      byte[] contentInOctets = content.getContent();
		      InputSource input = new InputSource(new ByteArrayInputStream(contentInOctets));

//		      EncodingDetector detector = new EncodingDetector(conf);
//		      detector.autoDetectClues(content, true);
//		      detector.addClue(sniffCharacterEncoding(contentInOctets), "sniffed");
//		      String encoding = detector.guessEncoding(content, defaultCharEncoding);

//		      metadata.set(Metadata.ORIGINAL_CHAR_ENCODING, encoding);
//		      metadata.set(Metadata.CHAR_ENCODING_FOR_CONVERSION, encoding);

//		      input.setEncoding(encoding);
//		      if (LOG.isTraceEnabled()) { LOG.trace("Parsing..."); }
		      root = parse(input);
//		    } catch (IOException e) {
//		      return new ParseStatus(e).getEmptyParseResult(content.getUrl(), getConf());
//		    } catch (DOMException e) {
//		      return new ParseStatus(e).getEmptyParseResult(content.getUrl(), getConf());
//		    } catch (SAXException e) {
//		      return new ParseStatus(e).getEmptyParseResult(content.getUrl(), getConf());
//		    } catch (Exception e) {
//		      LOG.error("Error: ", e);
//		      return new ParseStatus(e).getEmptyParseResult(content.getUrl(), getConf());
//		    }
		      
		      
		    // get meta directives
		    HTMLMetaProcessor.getMetaTags(metaTags, root, base);
		   
		    // check meta directives
		    if (!metaTags.getNoIndex()) {               // okay to index
		      StringBuffer sb = new StringBuffer();
		      utils.getText(sb, root);          // extract text
		      text = sb.toString();
		      sb.setLength(0);
		      if (LOG.isTraceEnabled()) { LOG.trace("Getting title..."); }
		      utils.getTitle(sb, root);         // extract title
		      title = sb.toString().trim();
		    }
		      
		    if (!metaTags.getNoFollow()) {              // okay to follow links
		      ArrayList<Outlink> l = new ArrayList<Outlink>();   // extract outlinks
		      URL baseTag = utils.getBase(root);
		      if (LOG.isTraceEnabled()) { LOG.trace("Getting links..."); }
		      utils.getOutlinks(baseTag!=null?baseTag:base, l, root);
		      outlinks = l.toArray(new Outlink[l.size()]);
		      if (LOG.isTraceEnabled()) {
		        LOG.trace("found "+outlinks.length+" outlinks in "+content.getUrl());
		      }
		    }
		    
		    ParseStatus status = new ParseStatus(ParseStatus.SUCCESS);
		    if (metaTags.getRefresh()) {
		      status.setMinorCode(ParseStatus.SUCCESS_REDIRECT);
		      status.setArgs(new String[] {metaTags.getRefreshHref().toString(),
		        Integer.toString(metaTags.getRefreshTime())});      
		    }
		    ParseData parseData = new ParseData(status, title, outlinks,
		                                        content.getMetadata(), metadata);
		    ParseResult parseResult = ParseResult.createParseResult(content.getUrl(), 
		                                                 new ParseImpl(text, parseData));
		    
		    return parseResult;
		    
		    // run filters on parse
		    /*
		     *  ParseResult filteredParse = this.htmlParseFilters.filter(content, parseResult, 
		                                                            metaTags, root);
		    if (metaTags.getNoCache()) {             // not okay to cache
		      for (Map.Entry<org.apache.hadoop.io.Text, Parse> entry : filteredParse) 
		        entry.getValue().getData().getParseMeta().set(Nutch.CACHING_FORBIDDEN_KEY, 
		                                                      cachingPolicy);
		    }
		    return filteredParse;
		     */

		  }
	

}
