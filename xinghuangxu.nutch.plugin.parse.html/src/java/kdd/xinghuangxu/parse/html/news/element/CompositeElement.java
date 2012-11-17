package kdd.xinghuangxu.parse.html.news.element;

import java.util.List;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

public abstract class CompositeElement implements NewsElement {

	protected String name;
	protected List<NewsElement> elements;
	
	public CompositeElement(){
	
	}
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return "";
	}

	@Override
	public abstract void parse(ParseHelper helper) throws NewsParsingException;
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		for(int i=0;i<elements.size();i++)
			sb.append(elements.get(i).toString());
		return sb.toString();
	}
	
	public  boolean isEmpty(){
		if(elements==null||elements.size()==0){
			return true;
		}
		return false;
	}
}
