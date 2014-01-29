/**
 * 
 */
package edu.uab.game;

import edu.uab.components.Board;
import edu.uab.components.Player;
import edu.uab.strategies.Strategy;

/**
 * @author sjmaharjan
 * 
 */
public abstract class Game {
	protected Player player1;
	protected Player player2;
	protected Player currentPlayer;
	protected Board board;
	protected Strategy strategy;
	protected boolean lastMoveInValid = false;
	protected boolean lastMovePass = false;
	protected boolean lastMoveWin = false;
	protected int moveCount = 0;

	public Game(Board board) {
		this.board = board;
	}

	public Board getBoard() {
		return board;
	}

	public void setBoard(Board board) {
		this.board = board;
	}

	public boolean isLastMoveInValid() {
		return lastMoveInValid;
	}

	public void setLastMoveInValid(boolean lastMoveInValid) {
		this.lastMoveInValid = lastMoveInValid;
	}

	public boolean isLastMovePass() {
		return lastMovePass;
	}

	public void setLastMovePass(boolean lastMovePass) {
		this.lastMovePass = lastMovePass;
	}

	public boolean isLastMoveWin() {
		return lastMoveWin;
	}

	public void setLastMoveWin(boolean lastMoveWin) {
		this.lastMoveWin = lastMoveWin;
	}

	public abstract void initializeGame(String initializeCommand);

	public abstract void makePlay(Player player);

	public abstract boolean endOfGame();

	public abstract void printWinner();

	/*
	 * A template method :
	 */
	public final void playGame(String initializeCommand) {

		initializeGame(initializeCommand);

		while (!endOfGame()) {
			System.out.println("(C player " + this.currentPlayer + ")");
			makePlay(currentPlayer);
			moveCount++;
			// next player turn
			if (currentPlayer == player1)
				currentPlayer = player2;
			else
				currentPlayer = player1;
		}
		printWinner();
	}

	public int getMoveCount() {
		return moveCount;
	}

	public void setMoveCount(int moveCount) {
		this.moveCount = moveCount;
	}

	/**
	 * @param player
	 * @return
	 */
	public Player getOpponent(Player player) {
		return (player == this.player1) ? this.player2 : this.player1;
	}

	public Player getPlayer1() {
		return player1;
	}

	public void setPlayer1(Player player1) {
		this.player1 = player1;
	}

	public Player getPlayer2() {
		return player2;
	}

	public void setPlayer2(Player player2) {
		this.player2 = player2;
	}
	
	public void setStrategy(Strategy strategy) {
		this.strategy=strategy;
		
	}
}
