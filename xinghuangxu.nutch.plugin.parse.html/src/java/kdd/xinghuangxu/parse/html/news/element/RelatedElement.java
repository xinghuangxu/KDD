package kdd.xinghuangxu.parse.html.news.element;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

public class RelatedElement extends CompositeElement{

	String url;
	public RelatedElement(String url) {
		this.url=url;
		name="related";
	}
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("<"+name+">");
		sb.append(url);
		sb.append("</"+name+">");
		return sb.toString();
	}
	
	@Override
	public void parse(ParseHelper helper) throws NewsParsingException {
		// TODO Auto-generated method stub
		return;
	}


}
