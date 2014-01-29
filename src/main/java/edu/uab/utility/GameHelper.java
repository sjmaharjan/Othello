/**
 * 
 */
package edu.uab.utility;

import java.util.Arrays;

import edu.uab.game.GameCommunication;

/**
 * @author sjmaharjan
 * 
 */
public class GameHelper {
	static String mapColumnletters[] = { "a", "b", "c", "d", "e", "f", "g", "h" };

	public static String getColumnRepresentation(int index) {
		return mapColumnletters[index];
	}

	public static int getColumnIndex(String move) {
		String elements[] = move.split(" "); // "(W c r)"
		return Arrays.asList(mapColumnletters).indexOf(elements[1].toLowerCase());
	}

	public static int getRowIndex(String move) {
		String elements[] = move.split(" ");// "(B c r)"
		return Integer.parseInt(elements[2].substring(0, 1)) - 1;
	}

	public static GameCommunication translate(String input) {
		String upperCaseInput = input.toUpperCase();
		if (upperCaseInput.equalsIgnoreCase("(I W)")) {
			return GameCommunication.YOU_WHITE;
		}
		if (upperCaseInput.equalsIgnoreCase("(I B)")) {
			return GameCommunication.YOU_BLACK;
		}
		if (upperCaseInput.startsWith("(C")) {
			return GameCommunication.COMMENT;
		}
		if (upperCaseInput.equalsIgnoreCase("(W)")
				|| upperCaseInput.equalsIgnoreCase("(B)")) {
			return GameCommunication.PASS;
		}
		if (upperCaseInput.startsWith("(B")) {
			return GameCommunication.PLAYER_BLACK;
		}
		if (upperCaseInput.startsWith("(W")) {
			return GameCommunication.PLAYER_WHITE;
		}
		if (upperCaseInput.matches("\\([+-]?\\d+\\)")) {
			return GameCommunication.WIN;
		}
		if(upperCaseInput.equalsIgnoreCase("(play)")){
			return GameCommunication.PLAY;
		}
		return null;

	}

}
