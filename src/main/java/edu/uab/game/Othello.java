/**
 * 
 */
package edu.uab.game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.Scanner;

import edu.uab.components.Board;
import edu.uab.components.Disc;
import edu.uab.components.Player;
import edu.uab.components.PlayerAI;
import edu.uab.components.PlayerHuman;
import edu.uab.utility.GameHelper;

/**
 * @author sjmaharjan
 * 
 */
public class Othello extends Game {

	public Othello(Board board) {
		super(board);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.controller.Game#initializeGame()
	 */
	@Override
	public void initializeGame(String initializeCommand) {
		createPlayers(initializeCommand);
		this.currentPlayer = player1;
	}

	/**
	 * 
	 */
	public void createPlayers(String initializeCommand) {
		System.out.println("(C (Communication))");
		//creating player on the basis of command send by refree
		switch (GameHelper.translate(initializeCommand)) {
		case YOU_BLACK:
			System.out.println("(R B)");
			this.player2 = new PlayerHuman(this, Disc.WHITE);
			this.player1 = new PlayerAI(this, Disc.BLACK);
			this.player1.setStrategy(this.strategy);
			break;
		case YOU_WHITE:
			System.out.println("(R W)");
			this.player1 = new PlayerHuman(this, Disc.BLACK);
			this.player2 = new PlayerAI(this, Disc.WHITE);
			this.player2.setStrategy(this.strategy);
			break;
		default:
			System.out.println("(C (Comunication error))");
			break;
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.controller.Game#makePlay(edu.uab.components.Player)
	 */
	@Override
	public void makePlay(Player player) {
		this.currentPlayer = player;
		// need to validate the claim of opponents : win (n) :pass (W) or (B)
		// :last move
		boolean opponentMoveValid = validateOpponentMove();

		if (opponentMoveValid) {
			boolean validWin = false;
			if (this.isLastMoveWin()) {
				validWin = verifyOpponentWinClaim();
				this.setLastMoveWin(false);
			}
			if (!validWin) {
				if (currentPlayer.hasAnyMove()) {
					currentPlayer.makeMove();
				} else {
					// pass the move to another player
					// communicated as (W) or (B)
					System.out.println("(" + currentPlayer + ")");
					this.setLastMovePass(true);
					this.setLastMoveInValid(false);
				}
			}
		} else {
			this.setLastMoveInValid(false);
			this.setLastMoveWin(false);
			//this.setLastMovePass(false);
			this.moveCount--;
			System.out.println("(C (Play your move ,last move invalid))");
		}

	}

	/**
	 * @return
	 */
	private boolean validateOpponentMove() {
		boolean valid = true;
		if (this.isLastMovePass()) {
			valid = checkPassAllowed();
			this.setLastMovePass(false);
		} else if (this.isLastMoveWin()) {
			valid = verifyOpponentWinClaim();
		} else if (this.isLastMoveInValid()) {
			valid = false;
		}
		return valid;
	}

	/**
	 * @return
	 */
	private boolean verifyOpponentWinClaim() {
		Player opponent = (this.currentPlayer == player1) ? player2 : player1;
		if (!currentPlayer.hasAnyMove()
				&& (currentPlayer.getDiscCount() < opponent.getDiscCount()))
			return true;
		else
			return false;
	}

	/**
	 * @return
	 */
	private boolean checkPassAllowed() {
		Player opponent = (this.currentPlayer == player1) ? player2 : player1;
		if (opponent.hasAnyMove()) {
			return false;
		} else
			return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.controller.Game#endOfGame()
	 */
	@Override
	public boolean endOfGame() {
		Player opponent = (this.currentPlayer == this.player1) ? this.player2
				: this.player1;
		if (!opponent.hasAnyMove() && !currentPlayer.hasAnyMove()) {
			currentPlayer = null;
			return true;
		} else {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.controller.Game#printWinner()
	 */
	@Override
	public void printWinner() {
		int discCountPlayer1 = this.player1.getDiscCount();
		int discCountPlayer2 = this.player2.getDiscCount();

		if (discCountPlayer1 > discCountPlayer2)
			System.out.println("(C (" + "Player 1 " + this.player1
					+ " won the game by "
					+ (discCountPlayer1 - discCountPlayer2) + " discs))");

		else if (discCountPlayer2 > discCountPlayer1)
			System.out.println("(C (Player 2 " + this.player2
					+ " won the game by "
					+ (discCountPlayer2 - discCountPlayer1) + " discs))");

		else
			System.out.println("(C (Game draw))");
		System.out.println("("+(this.board.getDiscCount(Disc.BLACK)-this.board.getDiscCount(Disc.WHITE))+")");
	}

}
