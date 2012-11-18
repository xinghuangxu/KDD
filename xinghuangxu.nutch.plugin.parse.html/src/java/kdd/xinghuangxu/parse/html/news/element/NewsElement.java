package kdd.xinghuangxu.parse.html.news.element;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

/**
 * 
 * @author xinghuang
 *
 */
public interface NewsElement {
	
	public String getName();
	public String getValue();
	public void setValue(String value);
	public void parse(ParseHelper helper)throws NewsParsingException;
	
}
