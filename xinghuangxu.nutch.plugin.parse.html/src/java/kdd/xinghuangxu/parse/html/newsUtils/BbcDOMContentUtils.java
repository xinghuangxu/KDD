package kdd.xinghuangxu.parse.html.newsUtils;

import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import kdd.xinghuangxu.parse.html.util.DOMContentUtils;
import kdd.xinghuangxu.parse.html.util.NodeWalker;

public class BbcDOMContentUtils extends DOMContentUtils {

	public Node getStoryBodyNode(Node node) {
		NodeWalker walker = new NodeWalker(node);
		while (walker.hasNext()) {
			Node currentNode = walker.nextNode();
			String nodeName = currentNode.getNodeName();
			// short nodeType = currentNode.getNodeType();

			if ("div".equalsIgnoreCase(nodeName)) {
				NamedNodeMap attrs = currentNode.getAttributes();
				for (int i = 0; i < attrs.getLength(); i++) {
					Node attr = attrs.item(i);
					if ("class".equalsIgnoreCase(attr.getNodeName())) {
						String attrValue = attr.getNodeValue();
						if (attrValue.equalsIgnoreCase("story-body"))
							return currentNode;
					}
				}
			}

		}
		return null;
	}

	public String getStoryBody(StringBuffer sb, Node node) {
		Node storyNode = getStoryBodyNode(node);
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
			if("blockquote".equalsIgnoreCase(nodeName)){
				walker.skipChildren();
			}
//			if (abortOnNestedAnchors && "a".equalsIgnoreCase(nodeName)) {
//				anchorDepth++;
//				if (anchorDepth > 1) {
//					abort = true;
//					break;
//				}
//			}
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
