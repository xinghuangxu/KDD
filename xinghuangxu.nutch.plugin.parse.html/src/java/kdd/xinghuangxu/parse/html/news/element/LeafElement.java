package kdd.xinghuangxu.parse.html.news.element;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

public abstract class LeafElement implements NewsElement{

	protected String name;
	protected String value;
	
	
//	public LeafElement(ParseHelper helper) throws NewsParsingException{
//		value=parse(helper).toString();
//	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return value;
	}

	@Override
	public abstract void parse(ParseHelper helper) throws NewsParsingException;
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("<"+name+">");
		sb.append(value);
		sb.append("</"+name+">");
		return sb.toString();
	}
}
