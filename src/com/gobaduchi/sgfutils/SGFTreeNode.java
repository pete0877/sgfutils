package com.gobaduchi.sgfutils;

import java.util.ArrayList;

/**
 * SGF tree node (could be a parent to other nodes or a list of properties).
 * Neither this class nor SGFTreeNodeProperty are aware of the meaning of the
 * SGF tree elements (who the players are, sequence of moves, etc). This class
 * only encapsulates syntactical parse tree of the SGF format.
 * 
 * @author Pete Martin
 */
public final class SGFTreeNode {

	public final static int TYPE_PARENT = 0;
	public final static int TYPE_PROPERTIES = 1;

	public int type;

	public ArrayList<SGFTreeNodeProperty> properties;

	public ArrayList<SGFTreeNode> children;

	SGFTreeNode() {
		children = new ArrayList<SGFTreeNode>();
		properties = new ArrayList<SGFTreeNodeProperty>();
		type = TYPE_PARENT;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		toStructString("", builder);
		return builder.toString();
	}

	protected void toStructString(String indent, StringBuilder result) {
		result.append(indent);
		if (type == TYPE_PARENT) {
			result.append("PARENT NODE:\n");
		} else {
			result.append("PROPERTY NODE:\n");
		}

		for (SGFTreeNodeProperty property : properties) {
			result.append(indent);
			result.append("    '" + property.name + "' = '" + property.value
					+ "'\n");
		}

		for (SGFTreeNode child : children) {
			child.toStructString(indent + "      ", result);
		}
	}
}
