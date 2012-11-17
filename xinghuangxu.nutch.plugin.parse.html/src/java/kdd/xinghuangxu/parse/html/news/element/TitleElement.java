package kdd.xinghuangxu.parse.html.news.element;

import org.w3c.dom.DocumentFragment;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

public class TitleElement extends LeafElement {

	public TitleElement() {
		name="title";
	}
	
	
	@Override
	public void parse(ParseHelper helper) throws NewsParsingException {
		value=helper.getTitle();
	}

}
