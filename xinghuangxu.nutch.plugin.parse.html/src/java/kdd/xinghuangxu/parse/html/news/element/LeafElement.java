package kdd.xinghuangxu.parse.html.news.element;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

public abstract class LeafElement implements NewsElement{

	private String name;
	private String value;
	
	
	public LeafElement(ParseHelper helper) throws NewsParsingException{
		value=parse(helper).toString();
	}
	
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public abstract StringBuilder parse(ParseHelper helper) throws NewsParsingException;
	
}
