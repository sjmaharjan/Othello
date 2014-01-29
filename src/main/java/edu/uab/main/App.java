package edu.uab.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.uab.components.Board;
import edu.uab.components.Disc;
import edu.uab.game.Game;
import edu.uab.game.GameCommunication;
import edu.uab.game.Othello;
import edu.uab.strategies.IterativeDeepening;
import edu.uab.strategies.Strategy;
import edu.uab.utility.GameHelper;

/**
 * Game Starter!
 * 
 */
public class App {
	public static void main(String[] args) {
		try {
			long timeToThink=Long.parseLong(args[0]);
			Strategy gameStrategy=new IterativeDeepening( timeToThink);
			// read the command from system.out
			BufferedReader in = new BufferedReader(new InputStreamReader(
					System.in));
			String initializeCommand = "";
			while (true) {
				initializeCommand = in.readLine();
				if (initializeCommand.subSequence(0, 2).equals("(I"))
					break;
			}
			
			Board b = new Board(8);
			b.printBoard();
			Game game = new Othello(b);
			game.setStrategy(gameStrategy);
			game.playGame(initializeCommand);
		} catch (IOException e) {
			System.out.println("Couldn't read ...");
		}

	}
}
