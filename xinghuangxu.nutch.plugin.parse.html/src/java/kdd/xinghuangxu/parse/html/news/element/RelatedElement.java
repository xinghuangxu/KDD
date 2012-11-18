package kdd.xinghuangxu.parse.html.news.element;

import java.util.ArrayList;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

/**
 * 
 * @author xinghuang
 *
 */
public class RelatedElement extends LeafElement{

	//String url;
	public RelatedElement(String url) {
		value=url;
		name="related";
	}
	

	
	
//	public void SetRelated(Document doc){
//		elements=new ArrayList<NewsElement>();
//		elements.add(doc);
//	}
	
	@Override
	public String toString() {
		if(value=="")return "";
		StringBuilder sb= new StringBuilder();
		sb.append("<"+name+">");
		sb.append(value);
		sb.append("</"+name+">");
		return sb.toString();
	}
	
	@Override
	public void parse(ParseHelper helper) throws NewsParsingException {
		// TODO Auto-generated method stub
		return;
	}


}
