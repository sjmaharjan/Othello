/**
 * 
 */
package edu.uab.strategies;

import java.util.ArrayList;
import java.util.List;

import edu.uab.components.Board;
import edu.uab.components.Player;
import edu.uab.game.Game;

/**
 * @author sjmaharjan
 * 
 */
public class IterativeDeepening extends Strategy {

	/**
	 * @param player
	 */
	public long timeToThink;
	public static final int MAX_BOARD_VALUE1 = Integer.MAX_VALUE;
	public static final int MIN_BOARD_VALUE1 = -MAX_BOARD_VALUE1;

	public IterativeDeepening(long thinkTime) {

		this.timeToThink = thinkTime;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see edu.uab.strategies.Strategy#getNextBestMove(edu.uab.game.Game)
	 * 
	 * int iterativeDeepening(ChessBoard board, int depth, int firstguess){for(d
	 * = 1; d <= depth; d++){firstguess = searchMethod(board, firstguess, d);if
	 * timeUp() break; } return firstguess; }
	 */
	@Override
	public int[] getNextBestMove(Game game, Player player) {
		this.player = player;
		return iterativeDeepening(game.getBoard(), 15, null);
	}

	public int[] iterativeDeepening(Board board, int depth, int[] firstGuess) {
		long startTime = System.currentTimeMillis();
		for (int d = 1; d <= depth; d++) {
			firstGuess = search(board, firstGuess, d);
			long elapsedTime = (System.currentTimeMillis() - startTime)/1000;
			System.out.println("(C elapsed time: "+elapsedTime+")");
			if (timeUp(elapsedTime ))
				break;
		}
		return firstGuess;
	}

	/**
	 * @return
	 */
	private boolean timeUp(long elapsedTime) {
		if (elapsedTime >= this.timeToThink)
			return true;
		else
			return false;
	}

	/**
	 * @param board
	 * @param firstGuess
	 * @param depth
	 * @return
	 */
	public int[] search(Board board, int[] firstGuess, int depth) {
		this.rowIndex = -1;
		this.columnIndex = -1;
		int alpha = alphaBeta3(board, depth, MIN_BOARD_VALUE1,
				MAX_BOARD_VALUE1, this.player, firstGuess, 5);

		return new int[] { this.rowIndex, this.columnIndex };
	}

	public int alphaBeta3(Board board, int depth, int alpha, int beta,
			Player player, int[] killer, int space) {
		// A-B search , putting killer move first
		if (depth == 0) {
			this.rowIndex = -1;
			this.columnIndex = -1;
			int result = heuristicValueOf(player, board);
			// printSpaces(space);
			// System.out.println("return==>" + result);
			return result;
		} else {
			List<int[]> moves = putFirst(killer,
					getAllPossibleMoves(board, player.getDisc()));

			if (moves.size() == 0) {
				if (anyLegalMove(board, player.getOpponent())) {

					return -(alphaBeta3(board, depth - 1, -beta, -alpha,
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

					int value = alphaBeta3(nextBoard, depth - 1, -beta, -alpha,
							player.getOpponent(), killer2, space + 5);
					value = -value;
//					printSpaces(space);
//					System.out.println("[" + player + "," + row + "," + column
//							+ " -a " + alpha + "," + " -b " + beta + "]");

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

	/**
	 * @param killer
	 * @param allPossibleMoves
	 * @return
	 */
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

}
