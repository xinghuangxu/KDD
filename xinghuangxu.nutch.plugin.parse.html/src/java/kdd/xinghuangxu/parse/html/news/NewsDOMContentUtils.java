package kdd.xinghuangxu.parse.html.news;

import java.net.URL;

import kdd.xinghuangxu.parse.html.dataStruc.Outlink;

import org.w3c.dom.Node;

public interface NewsDOMContentUtils {

	public abstract String getDate(Node node, URL url);

	public abstract Outlink[] getRelatedStoryLinks(Node node, URL url);

	public abstract String getStoryBody(StringBuffer sb, Node node);

	public abstract String getNewsTitle(StringBuffer sb, Node node);

}