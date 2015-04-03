package com.gobaduchi.test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import com.gobaduchi.sgfutils.SGFStringCleaner;

public class SGFStringCleanerTest {

	private static final Log logger = LogFactory
			.getLog(SGFStringCleanerTest.class);

	/**
	 * Test method for {@link com.gobaduchi.util.SGFStringCleaner#process()}.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testProcess1() throws Exception {

		try {

			String input = "blah";

			SGFStringCleaner cleaner = new SGFStringCleaner();
			String result = cleaner.process(input);

			Assert.assertNotNull("result is null", result);
			Assert.assertEquals("expected '" + input + "' but got '" + result
					+ "'", result, input);

		} catch (Exception error) {
			logger.error("Error in testProcess1()", error);
			Assert.fail(error.toString());
		}
	}

	/**
	 * Test method for {@link com.gobaduchi.util.SGFStringCleaner#process()}.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testProcess2() throws Exception {

		try {

			String input = "blah\\\nok";
			String output = "blahok";

			SGFStringCleaner cleaner = new SGFStringCleaner();
			String result = cleaner.process(input);

			Assert.assertNotNull("result is null", result);
			Assert.assertEquals("expected '" + output + "' but got '" + result
					+ "'", result, output);

		} catch (Exception error) {
			logger.error("Error in testProcess2()", error);
			Assert.fail(error.toString());
		}
	}

	/**
	 * Test method for {@link com.gobaduchi.util.SGFStringCleaner#process()}.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testProcess3() throws Exception {

		try {

			String input = "blah\\\n";
			String output = "blah";

			SGFStringCleaner cleaner = new SGFStringCleaner();
			String result = cleaner.process(input);

			Assert.assertNotNull("result is null", result);
			Assert.assertEquals("expected '" + output + "' but got '" + result
					+ "'", result, output);

		} catch (Exception error) {
			logger.error("Error in testProcess3()", error);
			Assert.fail(error.toString());
		}
	}

}
