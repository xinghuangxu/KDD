package kdd.xinghuangxu.parse.html.news.element;

import com.sun.xml.internal.bind.v2.model.core.LeafInfo;

import kdd.xinghuangxu.parse.html.news.ParseHelper;
import kdd.xinghuangxu.parse.html.news.exception.NewsParsingException;

public class IdElement extends LeafElement {

	public IdElement() {
		name="id";
	}

	@Override
	public void parse(ParseHelper helper) throws NewsParsingException {
		value=helper.getURL().toString();
	}

}
