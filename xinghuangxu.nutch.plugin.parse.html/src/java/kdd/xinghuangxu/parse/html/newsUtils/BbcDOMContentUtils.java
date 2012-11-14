package kdd.xinghuangxu.parse.html.newsUtils;

import java.net.URL;
import java.util.ArrayList;
import java.util.Properties;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import kdd.xinghuangxu.parse.html.HTMLMetaProcessor;
import kdd.xinghuangxu.parse.html.dataStruc.HTMLMetaTags;
import kdd.xinghuangxu.parse.html.dataStruc.Outlink;
import kdd.xinghuangxu.parse.html.util.DOMContentUtils;
import kdd.xinghuangxu.parse.html.util.NodeWalker;

public class BbcDOMContentUtils extends DOMContentUtils {
	
	
	public String getDate(Node node, URL url){
		HTMLMetaTags metaTags = new HTMLMetaTags();
		URL base= getBase(node);
		HTMLMetaProcessor.getMetaTags(metaTags, node,base != null ? base : url);
		Properties p = metaTags.getGeneralTags();
		String date=p.getProperty("originalpublicationdate");
		return date;
	}

	public Node getNode(Node node, String nodeName, String attrName, String attrValue){
		NodeWalker walker = new NodeWalker(node);
		while (walker.hasNext()) {
			Node currentNode = walker.nextNode();
			String currentNodeName = currentNode.getNodeName();
			// short nodeType = currentNode.getNodeType();

			if (currentNodeName.equalsIgnoreCase(nodeName)) {
				NamedNodeMap attrs = currentNode.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					if (attrName.equalsIgnoreCase(attr.getNodeName())) {
						String currentAttrValue = attr.getNodeValue();
						if (currentAttrValue.equalsIgnoreCase(attrValue))
							return currentNode;
					}
				}
			}

		}
		return null;
	}
	
//	public Node getRelatedLinksNode(Node node){
//		NodeWalker walker = new NodeWalker(node);
//		while (walker.hasNext()) {
//			Node currentNode = walker.nextNode();
//			String nodeName = currentNode.getNodeName();
//			// short nodeType = currentNode.getNodeType();
//
//			if ("div".equalsIgnoreCase(nodeName)) {
//				NamedNodeMap attrs = currentNode.getAttributes();
//				for (int i = 0; i < attrs.getLength(); i++) {
//					Node attr = attrs.item(i);
//					if ("class".equalsIgnoreCase(attr.getNodeName())) {
//						String attrValue = attr.getNodeValue();
//						if (attrValue.equalsIgnoreCase("story-related"))
//							return currentNode;
//					}
//				}
//			}
//
//		}
//		return null;
//	}
	
	
//	public Node getStoryBodyNode(Node node) {
//		NodeWalker walker = new NodeWalker(node);
//		while (walker.hasNext()) {
//			Node currentNode = walker.nextNode();
//			String nodeName = currentNode.getNodeName();
//			// short nodeType = currentNode.getNodeType();
//
//			if ("div".equalsIgnoreCase(nodeName)) {
//				NamedNodeMap attrs = currentNode.getAttributes();
//				for (int i = 0; i < attrs.getLength(); i++) {
//					Node attr = attrs.item(i);
//					if ("class".equalsIgnoreCase(attr.getNodeName())) {
//						String attrValue = attr.getNodeValue();
//						if (attrValue.equalsIgnoreCase("story-body"))
//							return currentNode;
//					}
//				}
//			}
//
//		}
//		return null;
//	}
	

	public Outlink[] getRelatedStoryLinks(Node node,URL url){
		ArrayList<Outlink> links = new ArrayList<Outlink>(); // extract outlinks
		URL baseTag = getBase(node);
		Node relatedStoryNode=getNode(node, "div", "class", "story-related");
		getOutlinks(baseTag != null ? baseTag : url, links, relatedStoryNode);
		return links.toArray(new Outlink[links.size()]);
	}
	
	
	public String getStoryBody(StringBuffer sb, Node node) {
		//Node storyNode = getStoryBodyNode(node);
		Node storyNode = getNode(node, "div", "class", "story-body");
		getBodyText(sb, storyNode);
		return sb.toString();
	}

	public String getNewsTitle(StringBuffer sb, Node node) {
		super.getTitle(sb, node);
		return sb.toString();
	}

	// returns true if abortOnNestedAnchors is true and we find nested
	// anchors
	private boolean getBodyText(StringBuffer sb, Node node) {
		boolean abort = false;
		NodeWalker walker = new BbcBodyNodeWalker(node);

		while (walker.hasNext()) {

			Node currentNode = walker.nextNode();
			String nodeName = currentNode.getNodeName();
			short nodeType = currentNode.getNodeType();

			if ("script".equalsIgnoreCase(nodeName)) {
				walker.skipChildren();
			}
			if ("style".equalsIgnoreCase(nodeName)) {
				walker.skipChildren();
			}
			if ("noscript".equalsIgnoreCase(nodeName)) {
				walker.skipChildren();
			}
			if ("blockquote".equalsIgnoreCase(nodeName)) {
				walker.skipChildren();
			}
			if (nodeType == Node.COMMENT_NODE) {
				walker.skipChildren();
			}
			if (nodeType == Node.TEXT_NODE) {
				String text = currentNode.getNodeValue();
				text = text.replaceAll("\\s+", " ");
				text = text.trim();
				if (text.length() > 0) {
					if (sb.length() > 0)
						sb.append(' ');
					sb.append(text);
				}
			}
		}

		return abort;
	}

}
