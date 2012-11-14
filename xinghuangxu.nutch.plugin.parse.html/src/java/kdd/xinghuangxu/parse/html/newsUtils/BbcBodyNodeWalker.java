package kdd.xinghuangxu.parse.html.newsUtils;


import org.w3c.dom.Node;

import kdd.xinghuangxu.parse.html.util.NodeWalker;

public class BbcBodyNodeWalker extends NodeWalker {

	public BbcBodyNodeWalker(Node rootNode) {
		super(rootNode);
	}

	@Override
	public Node nextNode() {
		// if no next node return null
		if (!hasNext()) {
			return null;
		}

		// pop the next node off of the stack and push all of its children onto
		// the stack
		currentNode = nodes.pop();
		String currentNodeName = currentNode.getNodeName();
		currentChildren = currentNode.getChildNodes();
		int childLen = (currentChildren != null) ? currentChildren.getLength()
				: 0;

		// put the children node on the stack in first to last order
		if ("p".equalsIgnoreCase(currentNodeName)) {
			// NamedNodeMap attrs = currentNode.getAttributes();
			// if (attrs.getLength() == 0)
			for (int i = childLen - 1; i >= 0; i--) {
				nodes.add(currentChildren.item(i));
			}
		} else {
			for (int i = childLen - 1; i >= 0; i--) {
				short nodeType = currentChildren.item(i).getNodeType();
				if (nodeType != Node.TEXT_NODE)
					nodes.add(currentChildren.item(i));
			}
		}

		return currentNode;
	}

}
