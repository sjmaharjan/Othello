/**
 * 
 */
package edu.uab.components;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import edu.uab.exceptions.BoardException;
import edu.uab.game.Game;
import edu.uab.utility.GameHelper;

/**
 * @author sjmaharjan
 * 
 */
public class PlayerHuman extends Player {

	/**
	 * @param othello
	 * @param white
	 */
	public PlayerHuman(Game othello, Disc disc) {
		super(othello, disc);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.components.Player#makeMove()
	 */
	@Override
	public void makeMove() {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		boolean passOrWin = false;
		try {
			String move = br.readLine();
			switch (GameHelper.translate(move)) {
			case PASS:
				this.game.setLastMovePass(true);
				passOrWin = true;
				break;
			case WIN:
				this.game.setLastMoveWin(true);
				passOrWin = true;
				break;
			default:
				break;
			}
			if (!passOrWin) {

				int column = GameHelper.getColumnIndex(move);
				int row = GameHelper.getRowIndex(move);
				try {
					this.game.getBoard().setDiscAt(row, column, disc);
					this.game.setLastMoveInValid(false);
				} catch (BoardException be) {
					this.game.setLastMoveInValid(true);
					System.out.println("(C (" + this + " invalid move))");
				}
				this.game.getBoard().printBoard();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

	}

}
