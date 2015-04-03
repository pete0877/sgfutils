package com.gobaduchi.sgfutils;

import java.util.ArrayList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * SGF string parser. Creates tree structure composed of SGFTreeNode instances
 * (returns root to that tree).
 * 
 * @author Pete Martin
 */
public final class SGFTreeParser {

	private static final Log logger = LogFactory.getLog(SGFTreeParser.class);

	public SGFTreeNode parse(String sgf) throws Exception {

		if (sgf == null)
			throw new Exception("Input to parse() cannot be null.");

		if (logger.isDebugEnabled())
			logger.debug("Parsing input: '" + sgf + "'");

		SGFTreeNode root = null;

		// Keep moving until we encounter the first ( sign which indicates
		// beginning of the root node:
		int pos = 0;
		while (pos < sgf.length()) {
			char c = sgf.charAt(pos);
			if (c == '(') {
				root = new SGFTreeNode();
				root.type = SGFTreeNode.TYPE_PARENT;

				parseInternal(root, sgf, pos);
				break;
			}
			pos++;
		}

		if (root == null)
			throw new Exception(
					"Did not find root-starting character (open round parenthesis) in SGF string: '"
							+ sgf + "'");

		return root;
	}

	protected int parseInternal(SGFTreeNode node, String sgf, int startPos)
			throws Exception {
		// This method is called when we are just at the position of opening
		// node. The character at the current position should be '('.

		String currentPropertySet = "";

		// Indicates if we have found the matching closing ')' character
		boolean foundNodeClose = false;
		int pos = startPos + 1;

		boolean insideValue = false;
		boolean previousWasEscape = false;

		while (!foundNodeClose && pos < sgf.length()) {

			char c = sgf.charAt(pos);

			switch (c) {

			case '\\':

				if (previousWasEscape) {

					if (logger.isDebugEnabled())
						logger.debug("Found escaped backslash at position "
								+ pos);

					previousWasEscape = false;

				} else {

					if (logger.isDebugEnabled())
						logger.debug("Found escape at position " + pos);

					previousWasEscape = true;
				}

				currentPropertySet += c;
				break;

			case '\n':

				if (logger.isDebugEnabled())
					logger.debug("Found new line char at position " + pos);

				// Include new-line chars only when we are inside [values]
				if (insideValue)
					currentPropertySet += c;

				previousWasEscape = false;
				break;

			case '[':

				if (logger.isDebugEnabled())
					logger.debug("Found value start [ at position " + pos);

				insideValue = true;
				currentPropertySet += c;

				previousWasEscape = false;
				break;

			case ']':

				if (previousWasEscape) {

					if (logger.isDebugEnabled())
						logger.debug("Found escaped ] at position " + pos);

				} else {

					if (logger.isDebugEnabled())
						logger.debug("Found value end ] at position " + pos);

					insideValue = false;
				}

				currentPropertySet += c;

				previousWasEscape = false;
				break;

			case '(':
				if (insideValue) {

					if (logger.isDebugEnabled())
						logger.debug("Found ( inside value at position " + pos);

					currentPropertySet += c;

				} else {

					if (logger.isDebugEnabled())
						logger.debug("Found new-node starting ( at position "
								+ pos);

					// Last property set has ended. Record it in the node and
					// start clean one:
					if (currentPropertySet.length() > 0) {

						// New child node detected:
						SGFTreeNode childNode = new SGFTreeNode();
						childNode.type = SGFTreeNode.TYPE_PROPERTIES;

						childNode.properties = parsePropertySet(currentPropertySet);
						node.children.add(childNode);

						currentPropertySet = "";
					}

					// New child node detected:
					SGFTreeNode childNode = new SGFTreeNode();
					childNode.type = SGFTreeNode.TYPE_PARENT;

					pos = parseInternal(childNode, sgf, pos);
					node.children.add(childNode);
				}

				previousWasEscape = false;
				break;

			case ')':

				if (insideValue) {

					if (logger.isDebugEnabled())
						logger.debug("Found ) inside value at position " + pos);

					currentPropertySet += c;

				} else {

					if (logger.isDebugEnabled())
						logger.debug("Found ) ending this node at position "
								+ pos);

					// Last property set has ended. Record it in the node and
					// start clean one:
					if (currentPropertySet.length() > 0) {

						// New child node detected:
						SGFTreeNode childNode = new SGFTreeNode();
						childNode.type = SGFTreeNode.TYPE_PROPERTIES;

						childNode.properties = parsePropertySet(currentPropertySet);
						node.children.add(childNode);

						currentPropertySet = "";
					}

					foundNodeClose = true;
				}

				previousWasEscape = false;
				break;

			case ';':

				if (insideValue) {

					if (logger.isDebugEnabled())
						logger.debug("Found ; inside value at position " + pos);

					currentPropertySet += c;
				} else {

					if (logger.isDebugEnabled())
						logger
								.debug("Found ; outside value at position "
										+ pos);

					// Last property set has ended. Record it in the node and
					// start
					// clean one:
					if (currentPropertySet.length() > 0) {

						// New child node detected:
						SGFTreeNode childNode = new SGFTreeNode();
						childNode.type = SGFTreeNode.TYPE_PROPERTIES;

						childNode.properties = parsePropertySet(currentPropertySet);
						node.children.add(childNode);

						currentPropertySet = "";
					}
				}

				previousWasEscape = false;
				break;

			default:
				currentPropertySet += c;

				previousWasEscape = false;
				break;
			}

			pos++;
		}

		if (!foundNodeClose)
			throw new Exception(
					"Did not find root-ending character (close round parenthesis) in SGF string: '"
							+ sgf + "'");

		return pos - 1;
	}

	protected ArrayList<SGFTreeNodeProperty> parsePropertySet(
			String propertySetString) throws Exception {

		ArrayList<SGFTreeNodeProperty> result = new ArrayList<SGFTreeNodeProperty>();

		if (logger.isDebugEnabled())
			logger.debug("Parsing property set string: '" + propertySetString
					+ "'");

		if (propertySetString == null || propertySetString.trim().length() == 0)
			return result;

		// Input could be: FF[4]GM[1]SZ[19]AP[SGFC:1.13b]

		int pos = 0;
		boolean insideValue = false;
		boolean previousWasEscape = false;
		String lastName = "";

		String name = "";
		String value = "";

		while (pos < propertySetString.length()) {
			char c = propertySetString.charAt(pos);

			switch (c) {
			case '\\':
				if (previousWasEscape) {

					previousWasEscape = false;

					if (insideValue)
						value += c;

				} else {
					// Wait till next char:
					previousWasEscape = true;

				}
				break;
			case '[':

				if (insideValue)
					value += c;

				previousWasEscape = false;
				insideValue = true;

				break;
			case ']':

				if (previousWasEscape) {

					if (insideValue)
						value += c;

					previousWasEscape = false;

					break;

				} else {
					// We have collected the value and var name so create the
					// property:
					SGFTreeNodeProperty property = new SGFTreeNodeProperty();

					name = name.trim();
					value = value.trim();

					if (name.length() == 0)
						name = lastName;

					property.name = name;
					property.value = value;

					result.add(property);

					lastName = name;

					name = "";
					value = "";

					insideValue = false;
				}

				break;

			default:

				previousWasEscape = false;

				if (insideValue)
					value += c;
				else
					name += c;
				break;
			}

			pos++;
		}

		return result;
	}
}
