package kdd.xinghuangxu.parse.html.news.element;

import java.util.List;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

public abstract class CompositeElement implements NewsElement {

	protected String name;
	protected String key;
	protected List<NewsElement> elements;
	
	public CompositeElement(String key){
		this.key=key;
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return key;
	}

	@Override
	public abstract void parse(ParseHelper helper) throws NewsParsingException;
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("<"+name+">");
		sb.append("<id>"+key+"</id>");
		for(int i=0;i<elements.size();i++)
			sb.append(elements.get(i).toString());
		sb.append("</"+name+">");
		return sb.toString();
	}
	
	public  boolean isEmpty(){
		if(elements==null||elements.size()==0){
			return true;
		}
		return false;
	}
}
