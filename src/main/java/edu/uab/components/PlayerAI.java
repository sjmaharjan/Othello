/**
 * 
 */
package edu.uab.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.uab.exceptions.BoardException;
import edu.uab.game.Game;
import edu.uab.strategies.AlphaBetaKillerHeuristic;
import edu.uab.strategies.AlphaBetaMinMaxStrategy;
import edu.uab.strategies.RandomNextMove;
import edu.uab.utility.GameHelper;

/**
 * @author sjmaharjan
 * 
 */
public class PlayerAI extends Player {

	/**
	 * @param othello
	 * @param black
	 */
	public PlayerAI(Game othello, Disc disc) {
		super(othello, disc);
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.components.Player#makeMove()
	 */
	@Override
	public void makeMove() {
		System.out.println("(C Thinking...)");
		int[] cell = strategy.getNextBestMove(this.game,this);
		
		if (cell[0] != -1 && cell[1] != -1) {
			try {
				this.game.getBoard().setDiscAt(cell[0], cell[1], disc);
				this.game.setLastMoveInValid(false);
				
				System.out.println("(" + this + " "
						+ GameHelper.getColumnRepresentation(cell[1]) + " "
						+ (cell[0] + 1) + ")");
				// win condition to be checked here
				if (haveIWon())
					System.out.println("("
							+ this.getBoard().getDiscCount(Disc.BLACK) + ")");
			} catch (BoardException be) {
				this.game.setLastMoveInValid(true);
				System.out.println("(C (" + this + " invalid move [" +GameHelper.getColumnRepresentation(cell[1]) + ","
						+ (cell[0] + 1)+"]");
			}

			this.game.getBoard().printBoard();
		}else{
			
			//no moves so pass the game...
			this.game.setLastMovePass(true);
		}
	}
}
