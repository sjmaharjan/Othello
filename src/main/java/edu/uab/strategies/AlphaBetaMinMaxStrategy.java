/**
 * 
 */
package edu.uab.strategies;

import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import edu.uab.components.Board;
import edu.uab.components.Disc;
import edu.uab.components.Player;
import edu.uab.components.PlayerAI;
import edu.uab.components.PlayerHuman;
import edu.uab.exceptions.BoardException;
import edu.uab.game.Game;
import edu.uab.game.Othello;

/**
 * @author sjmaharjan
 * 
 */
public class AlphaBetaMinMaxStrategy extends Strategy {
	/**
	 * @param player
	 */

	public static final int MAX_BOARD_VALUE = Integer.MAX_VALUE;
	public static final int MIN_BOARD_VALUE = -MAX_BOARD_VALUE;

	public AlphaBetaMinMaxStrategy() {
		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * edu.uab.strategies.Strategy#getNextBestMove(edu.uab.components.Board,
	 * edu.uab.components.Disc)
	 * 
	 * function alphabeta(node, depth, α, β, Player) if depth = 0 or node is a
	 * terminal node return the heuristic value of node if Player = MaxPlayer
	 * for each child of node α := max(α, alphabeta(child, depth-1, α,
	 * β,not(Player) )) if β ≤ α break (* Beta cut-off *) return α else for each
	 * child of node β := min(β, alphabeta(child, depth-1, α, β, not(Player) ))
	 * if β ≤ α break (* Alpha cut-off *) return β (* Initial call *)
	 * alphabeta(origin, depth, -infinity, +infinity, MaxPlayer)
	 */

	public int alphaBeta(Board board, int depth, int alpha, int beta,
			Player player, boolean isMaximizing, int space) {
		this.rowIndex = 0;
		this.columnIndex = 0;
		Disc disc = player.getDisc();
		boolean playerPass = false;
		boolean isLeaf = false;

		// handling the pass moves:
		List<int[]> possibleMoves = new ArrayList<int[]>();
		if (depth != 0) {
			possibleMoves = getAllPossibleMoves(board, disc);

			if (possibleMoves.size() == 0) {
				playerPass = true;
				possibleMoves = getAllPossibleMoves(board, player.getOpponent()
						.getDisc());
			}
		}

		if (possibleMoves.size() == 0) {

			isLeaf = true;
		}

		if (depth == 0 || isLeaf) {
			// printSpaces(space);
			this.rowIndex = -1;
			this.columnIndex = -1;
			int value = heuristicValueOf(board);
			// System.out.println("leaf-->" + value);
			return value;
		}

		// max player:
		int bestRow = -1;
		int bestColumn = -1;
		int bestValue = (isMaximizing ? MIN_BOARD_VALUE : MAX_BOARD_VALUE);
		if (isMaximizing) {
			// maxplayer passed the move
			if (playerPass) {
				Board nextBoard = new Board(board);
				Player opponent = player.getOpponent();
				alpha = Math.max(
						alpha,
						alphaBeta(nextBoard, depth - 1, alpha, beta, opponent,
								false, space + 5));
			} else {
				// for all childrens
				for (int[] move : possibleMoves) {
					int row = move[0];
					int column = move[1];
					// store best row and column

					Board nextBoard = new Board(board);
					nextBoard.setDiscAt(row, column, disc);
					Player opponent = player.getOpponent();

					alpha = Math.max(
							alpha,
							alphaBeta(nextBoard, depth - 1, alpha, beta,
									opponent, false, space + 5));
					// printSpaces(space);
					// System.out.println("[" + player + "," + row + "," +
					// column
					// + " -a " + alpha + "," + " -b " + beta + "]");
					if (alpha > bestValue) {
						bestValue = alpha;
						bestRow = row;
						bestColumn = column;
					}
					if (beta <= alpha)
						break;
				}
			}
			this.rowIndex = bestRow;
			this.columnIndex = bestColumn;
			return alpha;
		} else {// min player:
			// min player passed the
			if (playerPass) {
				Board nextBoard = new Board(board);
				Player opponent = player.getOpponent();
				beta = Math.min(
						beta,
						alphaBeta(nextBoard, depth - 1, alpha, beta, opponent,
								true, space + 5));
			} else {
				// for all childrens
				for (int[] move : possibleMoves) {
					int row = move[0];
					int column = move[1];

					Board nextBoard = new Board(board);
					nextBoard.setDiscAt(row, column, disc);
					Player opponent = player.getOpponent();
					beta = Math.min(
							beta,
							alphaBeta(nextBoard, depth - 1, alpha, beta,
									opponent, true, space + 5));
					// printSpaces(space);
					// System.out.println("[" + player + "," + row + "," +
					// column
					// + " -a " + alpha + "," + " -b " + beta + "]");

					if (beta < bestValue) {
						bestValue = beta;
						// store best row and column
						bestRow = row;
						bestColumn = column;
					}
					if (beta <= alpha)

						break;
				}
			}
			this.rowIndex = bestRow;
			this.columnIndex = bestColumn;
			return beta;

		}

	}

	public static void main(String args[]) {
		// Test code
		Board b = new Board(4);

		Game g = new Othello(b);
		Player player = new PlayerAI(g, Disc.BLACK);
		AlphaBetaMinMaxStrategy strategy = new AlphaBetaMinMaxStrategy();
		g.setPlayer1(player);
		g.setPlayer2(new PlayerHuman(g, Disc.WHITE));
		System.out.println(strategy.alphaBeta(b, 4, MIN_BOARD_VALUE,
				MAX_BOARD_VALUE, player, true, 5));
		System.out.println("[" + strategy.getRowIndex() + ","
				+ strategy.getColumnIndex() + "]");
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.strategies.Strategy#getNextBestMove(edu.uab.game.Game,
	 * edu.uab.components.Player)
	 */
	@Override
	public int[] getNextBestMove(Game game,Player player) {
		this.player= player;
		this.moveCount = game.getMoveCount();
		int value = alphaBeta(game.getBoard(), 4, MIN_BOARD_VALUE,
				MAX_BOARD_VALUE, this.player, true, 5);
		return new int[] { this.rowIndex, this.columnIndex };
	}

}
