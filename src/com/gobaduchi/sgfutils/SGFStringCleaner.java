package com.gobaduchi.sgfutils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Prepares SGF content string for parsing (currently just removes \\ line break escapes)
 * 
 * @author Pete Martin
 */
public final class SGFStringCleaner {

	private static final Log logger = LogFactory.getLog(SGFStringCleaner.class);

	public String process(String input) throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("Process input: '" + input + "'");

		if (input == null)
			throw new Exception("Input is null");

		if (logger.isDebugEnabled())
			logger.debug("Processing: '" + input + "'");

		StringBuilder builder = new StringBuilder();

		// Plow through all characters in the array
		int n = 0;
		while (n < input.length()) {

			char c = input.charAt(n);
			boolean lastChar = (n == (input.length() - 1));

			// Skip over soft-line breaks (character \ followed by new-line
			// break)
			char next = (lastChar ? ' ' : input.charAt(n + 1));

			if (c == '\\' && next == '\n') {
				if (logger.isDebugEnabled())
					logger
							.debug("Found backslash and new-line next to eachother");
				n += 2;
				continue;
			}

			builder.append(c);
			n++;
		}

		String result = builder.toString();
		if (logger.isDebugEnabled())
			logger.debug("Result: '" + result + "'");

		return result;
	}
}
