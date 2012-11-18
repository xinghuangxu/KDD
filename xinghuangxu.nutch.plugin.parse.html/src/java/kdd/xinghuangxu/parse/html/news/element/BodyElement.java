package kdd.xinghuangxu.parse.html.news.element;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

/**
 * 
 * @author xinghuang
 *
 */
public class BodyElement extends LeafElement {

	public BodyElement() {
		name="body";
	}
	
	
	@Override
	public void parse(ParseHelper helper) throws NewsParsingException {
		value=helper.getBody();
	}
}
