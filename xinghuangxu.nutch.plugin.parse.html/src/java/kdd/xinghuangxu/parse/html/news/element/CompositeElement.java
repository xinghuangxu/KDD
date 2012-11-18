package kdd.xinghuangxu.parse.html.news.element;

import java.util.List;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

/**
 * 
 * @author xinghuang
 *
 */
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
	public void setValue(String value) {
		// TODO Auto-generated method stub
		
	}
	

	@Override
	public void parse(ParseHelper helper) throws NewsParsingException
	{
		for(int i=0;i<elements.size();i++)
			elements.get(i).parse(helper);
	}
	
	@Override
	public String toString() {
		StringBuilder sb= new StringBuilder();
		sb.append("<"+name+">");
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
