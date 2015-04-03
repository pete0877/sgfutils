package com.gobaduchi.sgfutils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.gobaduchi.common.GameRecord;

/**
 * Parser of the SGF format tree. Takes in instance of SGFTreeNode and produces
 * GameRecord while making few assumptions and interpretations (e.g. the main
 * branch of the tree contains the sequence of moves of the actual game).
 * 
 * @see http://www.red-bean.com/sgf/user_guide/index.html
 * @author Pete Martin
 */
public final class GameRecordParser {

	/** Logger instance */
	private static final Log logger = LogFactory.getLog(GameRecordParser.class);

	public GameRecord parse(SGFTreeNode sgfTree) {

		GameRecord result = new GameRecord();

		return result;
	}
}
