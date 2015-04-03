package com.gobaduchi.test;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.Assert;
import org.junit.Test;

import com.gobaduchi.sgfutils.SGFTreeNode;
import com.gobaduchi.sgfutils.SGFTreeParser;

public class SGFTreeParserTest {

	private static final Log logger = LogFactory
			.getLog(SGFTreeParserTest.class);

	/**
	 * Test method for {@link com.gobaduchi.util.SGFTreeParser#parse()}.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testParse1() throws Exception {

		try {

			SGFTreeParser parser = new SGFTreeParser();

			SGFTreeNode root = parser
					.parse("(;AA[123]BB[456(654)]CC[789\\]];AAA[12]BBB[56\\\\](;KK[-1-]LL[-2-])XXXX[bye])");
			logger.debug("Root: \n\n" + root.toString() + "\n\n");

			Assert.assertTrue("Root does not have 4 children", root.children
					.size() == 4);

		} catch (Exception error) {
			logger.error("Error in testParse1()", error);
			Assert.fail(error.toString());
		}
	}

	/**
	 * Test method for {@link com.gobaduchi.util.SGFTreeParser#parse()}.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testParse2() throws Exception {

		try {

			SGFTreeParser parser = new SGFTreeParser();

			String input = "";

			input += "(;GM[1]FF[4]CA[UTF-8]AP[CGoban:3]ST[2]\n";
			input += "RU[Japanese]SZ[19]HA[6]KM[0.50]TM[1800]OT[5x30 byo-yomi]\n";
			input += "GN[grafton cup]PW[mr. white]PB[mr. black]WR[9d]BR[30k]DT[2009-01-26,02-14]CP[worcester news 2009]GC[This was an excellent game (and lots of fun).\n";
			input += "\n";
			input += "Lets test some escape characters:\n";
			input += "[\n";
			input += "\\]\n";
			input += "\\\\\n";
			input += "(\n";
			input += ")\n";
			input += "END]EV[masters finals]RO[4]PC[north grafton]WT[team snow]BT[team coal]SO[worcester news]AN[mr. watch [9 dan\\]]US[mr. pen (not a pencil)]AB[dd][pd][dj][pj][dp][pp]C[This game is intereting...\n";
			input += "... but unfortunately fake.\n";
			input += "\n";
			input += "Lets test some escape characters:\n";
			input += "[\n";
			input += "\\]\n";
			input += "\\\\\n";
			input += "(\n";
			input += ")\n";
			input += "END]RE[W+Resign]\n";
			input += ";W[nc]\n";
			input += ";B[qf]LB[pf:A]C[Another option is to play at A.]\n";
			input += ";W[pb]\n";
			input += ";B[qc]CR[fc][cf]LB[jd:1][jj:2][jp:3]TR[qn][nq]SQ[cn][fq]\n";
			input += ";W[qb]C[Now we have two options now.]\n";
			input += "(;B[lc]\n";
			input += ";W[ne]\n";
			input += "(;B[ic]\n";
			input += ";W[kd]\n";
			input += ";B[kc])\n";
			input += "(;B[le]\n";
			input += ";W[pc]\n";
			input += ";B[qd]))\n";
			input += "(;B[rb]\n";
			input += ";W[pc]C[Lets branch out more. ]\n";
			input += "(;B[qd]\n";
			input += ";W[od]\n";
			input += ";B[oe]\n";
			input += ";W[ne])\n";
			input += "(;B[rd]\n";
			input += ";W[od]\n";
			input += ";B[pe]\n";
			input += ";AW[dl][pl][dn][pn]AB[gp][jp][mp]\n";
			input += ";W[jj]\n";
			input += ";B[jd])))\n";

			SGFTreeNode root = parser.parse(input);
			logger.debug("Root: \n\n" + root.toString() + "\n\n");

		} catch (Exception error) {
			logger.error("Error in testParse2()", error);
			Assert.fail(error.toString());
		}
	}

	/**
	 * Runs through blind parsing of about 70 .sgf files from gameset1 folder
	 * {@link com.gobaduchi.util.SGFTreeParser#parse()}.
	 * 
	 * @throws java.lang.Exception
	 */
	@Test
	public void testParse3() throws Exception {

		String fileName = "";

		try {

			for (Integer n = 1; n <= 69; n++) {

				fileName = "./test/resources/gameset1/" + n.toString() + ".sgf";

				logger.debug("SGF file: " + fileName);

				StringBuilder builder = new StringBuilder();

				FileInputStream fstream = new FileInputStream(fileName);
				DataInputStream in = new DataInputStream(fstream);
				BufferedReader br = new BufferedReader(
						new InputStreamReader(in));
				String line;
				while ((line = br.readLine()) != null) {
					builder.append(line);
					builder.append("\n");
				}
				in.close();

				logger.debug("SGF string: " + builder.toString());

				SGFTreeParser parser = new SGFTreeParser();
				SGFTreeNode root = parser.parse(builder.toString());
				logger.debug("Root: \n\n" + root.toString() + "\n\n");
			}
			
		} catch (Exception error) {

			logger.error("Error in testParse3()", error);
			Assert.fail("Error in testParse3() while parsing SGF file: "
					+ fileName + ": " + error.toString());
		}
	}

}
