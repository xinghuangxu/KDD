package kdd.xinghuangxu.parse.html.news.element;

import java.util.ArrayList;
import java.util.List;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

public class CompositeElement implements NewsElement {

	String name;
	String key;
	List<NewsElement> elements=new ArrayList<NewsElement>();
	
	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getValue() {
		return key;
	}

	@Override
	public StringBuilder parse(ParseHelper helper) throws NewsParsingException {
		// TODO Auto-generated method stub
		return null;
	}

}
