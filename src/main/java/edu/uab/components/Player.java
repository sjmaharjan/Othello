/**
 * 
 */
package edu.uab.components;

import edu.uab.game.Game;
import edu.uab.strategies.Strategy;

/**
 * @author sjmaharjan
 * 
 */
public abstract class Player {

	protected Game game;
	protected Disc disc;
	protected Strategy strategy;

	public Player(Game game, Disc disc) {
		this.game = game;
		this.disc = disc;
	}

	public abstract void makeMove();

	public boolean hasAnyMove() {
		return this.game.getBoard().canSetAnyCell(disc);
	}

	public Board getBoard() {
		return game.getBoard();
	}

	public void setStrategy(Strategy strategy) {
		this.strategy = strategy;
	}

	public Disc getDisc() {
		return disc;
	}

	public int getDiscCount() {
		return this.getBoard().getDiscCount(disc);
	}

	public int getOpponentDiscCount() {
		Disc opponentDisc = (disc == Disc.BLACK) ? Disc.WHITE : Disc.BLACK;
		return this.getBoard().getDiscCount(opponentDisc);
	}

	public boolean haveIWon() {
		boolean win = false;
		if (this.game.endOfGame()) {
			if (getDiscCount() > getOpponentDiscCount())
				win = true;
		}
		return win;
	}

	@Override
	public String toString() {
		return disc.toString().trim();
	}

	/**
	 * @return
	 */
	public Player getOpponent() {
		return this.game.getOpponent(this);
	}
}
