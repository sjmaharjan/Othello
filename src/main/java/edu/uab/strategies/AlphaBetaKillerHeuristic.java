/**
 * 
 */
package edu.uab.strategies;

import java.util.ArrayList;
import java.util.List;

import edu.uab.components.Board;
import edu.uab.components.Disc;
import edu.uab.components.Player;
import edu.uab.components.PlayerAI;
import edu.uab.components.PlayerHuman;
import edu.uab.game.Game;
import edu.uab.game.Othello;

/**
 * @author sjmaharjan
 * 
 */
public class AlphaBetaKillerHeuristic extends Strategy {

	public static final int MAX_BOARD_VALUE1 = Integer.MAX_VALUE;
	public static final int MIN_BOARD_VALUE1 = -MAX_BOARD_VALUE1;

	/**
	 * @param player
	 */
	public AlphaBetaKillerHeuristic() {
		
	}

	// source: Principle of Artificial intelligence
	public int alphaBeta2(Board board, int depth, int alpha, int beta,
			Player player, int[] killer, int space) {
		// A-B search , putting killer move first
		if (depth == 0) {
			this.rowIndex = -1;
			this.columnIndex = -1;
			int result = heuristicValueOf(player, board);
			printSpaces(space);
			System.out.println("return==>" + result);
			return result;
		} else {
			List<int[]> moves = putFirst(killer,
					getAllPossibleMoves(board, player.getDisc()));
			if (moves.size() == 0) {
				if (anyLegalMove(board, player.getOpponent())) {

					return -(alphaBeta2(board, depth - 1, -beta, -alpha,
							player.getOpponent(), null, space + 5));

				} else {
					this.rowIndex = -1;
					this.columnIndex = -1;
					return finalValue(board, player);
				}
			} else {
				int[] bestMove = moves.get(0);
				int[] killer2 = null;
				int killer2Val = MAX_BOARD_VALUE1 - 1;

				for (int[] move : moves) {
					Board nextBoard = new Board(board);
					int row = move[0];
					int column = move[1];

					// make move
					nextBoard.setDiscAt(row, column, player.getDisc());

					int value = alphaBeta2(nextBoard, depth - 1, -beta, -alpha,
							player.getOpponent(), killer2, space + 5);
					value = -value;
					printSpaces(space);
					System.out.println("[" + player + "," + row + "," + column
							+ " -a " + alpha + "," + " -b " + beta + "]");

					if (value > alpha) {
						alpha = value;
						bestMove = move;
					}
					// storing the best move in killer

					if (this.rowIndex != -1 && this.columnIndex != -1
							&& value < killer2Val) {
						// printSpaces(space);
						// System.out.println("killer: " + this.rowIndex + ","
						// + this.columnIndex);
						killer2 = new int[2];
						killer2[0] = this.rowIndex;
						killer2[1] = this.columnIndex;
						killer2Val = value;

					}

					if (alpha >= beta) {
						break;
					}

				}
				// printSpaces(space);
				// System.out.println("best move: " + bestMove[0] + ","
				// + bestMove[1]);
				this.rowIndex = bestMove[0];
				this.columnIndex = bestMove[1];
				return alpha;

			}

		}

	}

	public List<int[]> putFirst(int[] killer, List<int[]> moves) {
		if (killer == null) {
			return moves;
		}
		int count = 0;
		boolean found = false;
		for (int[] move : moves) {
			if (move[0] == killer[0] && move[1] == killer[1]) {
				found = true;
				break;
			}
			count++;
		}
		if (found) {
			moves.remove(count);
			moves.add(0, killer);
		}

		return moves;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.strategies.Strategy#getNextBestMove(edu.uab.game.Game)
	 */
	@Override
	public int[] getNextBestMove(Game game,Player player) {
		this.player= player;
		this.moveCount = game.getMoveCount();
		int value = alphaBeta2(game.getBoard(), 4, MIN_BOARD_VALUE1,
				MAX_BOARD_VALUE1, this.player, null, 5);
		return new int[] { this.rowIndex, this.columnIndex };
	}

	public static void main(String args[]) {
		// Test code
		Board b = new Board(4);

		Game g = new Othello(b);
		Player player = new PlayerAI(g, Disc.BLACK);
		AlphaBetaKillerHeuristic strategy = new AlphaBetaKillerHeuristic();
		g.setPlayer1(player);
		g.setPlayer2(new PlayerHuman(g, Disc.WHITE));
		System.out.println(strategy.alphaBeta2(b, 4, MIN_BOARD_VALUE1,
				MAX_BOARD_VALUE1, player, null, 5));
		System.out.println("[" + strategy.getRowIndex() + ","
				+ strategy.getColumnIndex() + "]");

	}
}
