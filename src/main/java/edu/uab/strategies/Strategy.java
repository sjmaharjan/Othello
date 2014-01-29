/**
 * 
 */
package edu.uab.strategies;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Stack;

import edu.uab.components.Board;
import edu.uab.components.Disc;
import edu.uab.components.Player;
import edu.uab.components.PlayerAI;
import edu.uab.game.Game;

/**
 * @author sjmaharjan
 * 
 */
public abstract class Strategy {
	protected int moveCount = 0;
	protected Player player;
	// for internal stability count
	protected int[][] stableDiscMatrix;
	protected Stack<int[]> unProcessedStableDisc;

	public static final int WINNING_VALUE = 10000;
	public static final int LOSING_VALUE = -WINNING_VALUE;

	// best row and column index
	public int rowIndex = -1;
	public int columnIndex = -1;
	private int scores[] = { 34, 434, 42, 2, 3, 4, 4, 34, 32, 34, 1, 2, 23, 12,
			34, 35, 46, 57, 58, 59, 58, 56, 23, 59, 99, 81, 62, 1, 23, 81, 97,
			46, 0, 60, 2, 59, 9, 33, 60, 61, 84, 84, 79, 60, 74, 56, 44, 33,
			23, 12, 35, 45, 67, 23, 1, 2, 3, 4, 5, 6, 7, 8 };
	int counters = 0;

	// weighted cell position on board
	protected int[][] weightedPositionMatrix = {
			{ 120, -20, 20, 5, 5, 20, -20, 120 },
			{ -20, -40, -5, -5, -5, -5, -40, -20 },
			{ 20, -5, 1, 5, 3, 3, 15, -5, 20 }, { 5, -5, 3, 3, 3, 3, -5, 5 },
			{ 5, -5, 3, 3, 3, 3, -5, 5 }, { 20, -5, 15, 3, 3, 15, -5, 20 },
			{ -20, -40, -5, -5, -5, -5, -40, -20 },
			{ 120, -20, 20, 5, 5, 20, -20, 120 } };

	public Strategy() {

	}
	public void initializeStatbleDiscMatrix(int[][] matrix) {

		for (int row = 0; row < matrix.length; row++) {
			for (int column = 0; column < matrix.length; column++) {
				matrix[row][column] = 0;
			}
		}
	}

	public abstract int[] getNextBestMove(Game game,Player player);

	

	public int getRowIndex() {
		return rowIndex;
	}

	public void setRowIndex(int rowIndex) {
		this.rowIndex = rowIndex;
	}

	public int getColumnIndex() {
		return columnIndex;
	}

	public void setColumnIndex(int columnIndex) {
		this.columnIndex = columnIndex;
	}

	/**
	 * @param player
	 * @param board
	 * @return
	 */
	public int heuristicValueOf(Player player, Board board) {
		// TODO need to implement heuristic

		// Random rm = new Random();
		// int value=scores[counters];
		// counters++;
		// TODO need to normalize the features
		// int heuristic = 36 * getInternalStablity(board, player)
		// + getCMAC(this.moveCount) * getCurrentMobility(board, player)
		// + 99 * getPotentialMobility(board, player)
		// + getDiscDifferece(board, player)
		// + getConvertableDisc(board, player)
		// + getWeightSquares(board, player);
		int heuristic = getInternalStablity(board, player)
				+ getCurrentMobility(board, player)
				+ getPotentialMobility(board, player)
				+ getDiscDifferece(board, player)
				+ getConvertableDisc(board, player)
				+ getWeightSquares(board, player);

		return heuristic;
	}

	/**
	 * @param moveCount2
	 * @return
	 */
	private int getCMAC(int moveCount2) {
		if (this.moveCount >= 1 && this.moveCount <= 25)
			return 50 + 2 * this.moveCount;
		else
			return 75 + this.moveCount;

	}

	/**
	 * @param board
	 * @return
	 */
	public int heuristicValueOf(Board board) {
		if (this.getAllPossibleMovesCount(board, player.getDisc()) == 0
				&& this.getAllPossibleMovesCount(board, this.player
						.getOpponent().getDisc()) == 0)
			return this.finalValue(board, this.player);
		return this.heuristicValueOf(this.player, board);
	}

	public int finalValue(Board board, Player player) {
		if (getDiscDifferece(board, player) > 0)
			return WINNING_VALUE;
		else if (getDiscDifferece(board, player) < 0)
			return LOSING_VALUE;
		else
			return 0;
	}

	public List<int[]> getAllPossibleMoves(Board board, Disc disc) {
		List<int[]> validCells = new ArrayList<int[]>();
		for (int row = 0; row < board.getBoardSize(); row++) {
			for (int column = 0; column < board.getBoardSize(); column++) {
				if (board.isValidCell(row, column, disc)) {
					int cell[] = { row, column };
					validCells.add(cell);
				}
			}
		}

		Collections.sort(validCells, new WeightComparator());
		return validCells;

	}

	/*
	 * strategies: piece difference, corner occupancy corner closeness mobility
	 * current mobility potential mobility stability edge stability internal
	 * stability frontier disc disc squares end game solver complete search to
	 * the end of game maximize final disc difference or win, loss, draw
	 */

	// calculating the disc difference give the board and player
	public int getAllPossibleMovesCount(Board board, Disc disc) {
		int result = 0;
		for (int row = 0; row < board.getBoardSize(); row++) {
			for (int column = 0; column < board.getBoardSize(); column++) {
				if (board.isValidCell(row, column, disc)) {
					result++;
				}
			}
		}

		return result;

	}

	public boolean anyLegalMove(Board board, Player player) {
		if (getAllPossibleMovesCount(board, player.getDisc()) > 0)
			return true;
		return false;
	}

	public int getDiscDifferece(Board board, Player player) {
		int me = board.getDiscCount(player.getDisc());
		int opponent = board.getDiscCount(player.getOpponent().getDisc());
		return (me - opponent);
	}

	public int getCurrentMobility(Board board, Player player) {
		return (getAllPossibleMovesCount(board, player.getDisc()) - getAllPossibleMovesCount(
				board, player.getOpponent().getDisc()));
	}

	public int getCurrentConvertableDiscCount(Board board, Player player) {
		int total = 0;
		List<int[]> possibleMoves = getAllPossibleMoves(board, player.getDisc());
		Disc disc = player.getDisc();
		Board b = board;
		for (int[] nextMove : possibleMoves) {
			int row = nextMove[0];
			int column = nextMove[1];
			Board nextBoard = new Board(b);
			nextBoard.setDiscAt(row, column, disc);
			total += nextBoard.getTotolDiscsFlipped();

		}

		return total;
	}

	public int getConvertableDisc(Board board, Player player) {
		return getCurrentConvertableDiscCount(board, player)
				- getCurrentConvertableDiscCount(board, player.getOpponent());
	}

	public int getPotentialMobility(Board board, Player player) {

		Player opponent = player.getOpponent();
		Disc opponentDisc = opponent.getDisc();
		int frontier = 0;
		int emptyToOpponent = 0;
		int opponentToEmpty = 0;
		// search the whole board
		for (int row = 0; row < board.getBoardSize(); row++) {
			for (int column = 0; column < board.getBoardSize(); column++) {
				// examine the cell
				Disc cell = board.getDiscAt(row, column);
				// if empty or opponent disc
				if (cell == opponentDisc || cell == Disc.NONE) {
					boolean found = false;
					boolean foundOpponent = false;
					// search all possible eight directions
					for (int rowIndexChange = -1; rowIndexChange <= 1; rowIndexChange++) {
						for (int columnIndexChange = -1; columnIndexChange <= 1; columnIndexChange++) {
							if ((rowIndexChange != 0)
									|| (columnIndexChange != 0)) {
								int rowIndex = row + rowIndexChange;
								int colIndex = columnIndexChange + column;
								if ((rowIndex >= 0)
										&& (rowIndex < board.getBoardSize())
										&& (colIndex >= 0)
										&& (colIndex < board.getBoardSize())) {
									Disc discAt = board.getDiscAt(rowIndex,
											colIndex);
									// calculating the frontier disc and sum of
									// number of empty squares adjacent to each
									// of opponent discs
									if (discAt == Disc.NONE
											&& cell == opponentDisc) {
										found = true;
										emptyToOpponent++;
									}
									// calculating the number of empty squares
									// adjacent to opponent disc
									if (discAt == opponentDisc
											&& cell == Disc.NONE) {
										foundOpponent = true;
									}

								}
							}

						}

					}
					if (found) {
						frontier++;
					}
					if (foundOpponent) {
						opponentToEmpty++;
					}
				}
			}
		}
		// System.out.println("frontier: " + frontier + "emptytoOpp "
		// + emptyToOpponent + " opponetToEmpty " + opponentToEmpty);

		return (frontier + emptyToOpponent + opponentToEmpty);
	}

	public void stableDiscCount(Board board, Player player) {
		// put corner discs in the stack if any as they are stable and mark it
		// on the matrix

		Disc myDisc = player.getDisc();
		unProcessedStableDisc = new Stack<int[]>();
		// corner discs
		for (int row = 0; row < board.getBoardSize(); row += board
				.getBoardSize() - 1) {
			for (int column = 0; column < board.getBoardSize(); column += board
					.getBoardSize() - 1) {

				if (board.getDiscAt(row, column) == myDisc) {
					int[] data = { row, column };
					unProcessedStableDisc.add(data);
					// mark stable in matrix
					// System.out.println("Column " + row + ", " + column);
					this.stableDiscMatrix[row][column] = 1;

				}
			}
		}
		while (!unProcessedStableDisc.isEmpty()) {
			int[] data = unProcessedStableDisc.pop();
			// check its adjacent disc if stable
			for (int[] cell : getAdjacentCells(board, data[0], data[1])) {
				// already marked stable
				if (stableDiscMatrix[cell[0]][cell[1]] == 1)
					continue;
				boolean stable = false;
				if (board.getDiscAt(cell[0], cell[1]) == myDisc) {
					stable = isStableHorizontal(cell, stableDiscMatrix);
					if (stable)
						stable = isStableVertical(cell, stableDiscMatrix);
					if (stable)
						stable = isStableLeftDiagonal(cell, stableDiscMatrix);
					if (stable)
						stable = isStableRightDiagonal(cell, stableDiscMatrix);
					if (stable) {
						stableDiscMatrix[cell[0]][cell[1]] = 1;
						unProcessedStableDisc.add(cell);
					}
				}
			}

		}

	}

	/**
	 * @param cell
	 * @param stableDiscMatrix
	 * @return
	 */
	private boolean isStableLeftDiagonal(int[] cell, int[][] stableDiscMatrix) {
		int rowIndex = cell[0];
		int columnIndex = cell[1];
		if (rowIndex - 1 < 0 || columnIndex - 1 < 0)
			return true;
		if (stableDiscMatrix[rowIndex - 1][columnIndex - 1] == 1)
			return true;
		if (rowIndex + 1 > stableDiscMatrix.length - 1
				|| columnIndex + 1 > stableDiscMatrix.length - 1)
			return true;
		if (stableDiscMatrix[rowIndex + 1][columnIndex + 1] == 1)
			return true;

		return false;
	}

	/**
	 * @param cell
	 * @param stableDiscMatrix
	 * @return
	 */
	private boolean isStableRightDiagonal(int[] cell, int[][] stableDiscMatrix) {
		int rowIndex = cell[0];
		int columnIndex = cell[1];
		if (rowIndex - 1 < 0 || columnIndex + 1 > stableDiscMatrix.length - 1)
			return true;
		if (stableDiscMatrix[rowIndex - 1][columnIndex + 1] == 1)
			return true;
		if (rowIndex + 1 > stableDiscMatrix.length - 1 || columnIndex - 1 < 0)
			return true;
		if (stableDiscMatrix[rowIndex + 1][columnIndex - 1] == 1)
			return true;

		return false;
	}

	/**
	 * @param cell
	 * @param stableDiscMatrix
	 * @return
	 */
	private boolean isStableVertical(int[] cell, int[][] stableDiscMatrix) {
		int rowIndex = cell[0];
		int columnIndex = cell[1];
		if (rowIndex - 1 < 0)
			return true;
		if (stableDiscMatrix[rowIndex - 1][columnIndex] == 1)
			return true;
		if (rowIndex + 1 > stableDiscMatrix.length - 1)
			return true;
		if (stableDiscMatrix[rowIndex + 1][columnIndex] == 1)
			return true;

		return false;
	}

	/**
	 * @param cell
	 * @param stableDiscMatrix
	 * @return
	 */
	private boolean isStableHorizontal(int[] cell, int[][] stableDiscMatrix) {
		int rowIndex = cell[0];
		int columnIndex = cell[1];
		if (columnIndex - 1 < 0)
			return true;
		if (stableDiscMatrix[rowIndex][columnIndex - 1] == 1)
			return true;
		if (columnIndex + 1 > stableDiscMatrix.length - 1)
			return true;
		if (stableDiscMatrix[rowIndex][columnIndex + 1] == 1)
			return true;

		return false;
	}

	public List<int[]> getAdjacentCells(Board board, int row, int column) {
		List<int[]> adjacentCells = new ArrayList<int[]>();
		for (int rowIndexChange = -1; rowIndexChange <= 1; rowIndexChange++) {
			for (int columnIndexChange = -1; columnIndexChange <= 1; columnIndexChange++) {
				if ((rowIndexChange != 0) || (columnIndexChange != 0)) {
					int rowIndex = row + rowIndexChange;
					int colIndex = column + columnIndexChange;
					if ((rowIndex >= 0) && (rowIndex < board.getBoardSize())
							&& (colIndex >= 0)
							&& (colIndex < board.getBoardSize())) {
						adjacentCells.add(new int[] { rowIndex, colIndex });
					}

				}
			}

		}
		return adjacentCells;
	}

	public int getStableDiscCount(Board board, Player player) {
		// board.printBoard();
		int stableDics = 0;
		Disc disc = player.getDisc();
		// need to loop through all board position and then check all the eight
		// possible directions
		this.stableDiscMatrix = new int[board.getBoardSize()][board
				.getBoardSize()];
		initializeStatbleDiscMatrix(this.stableDiscMatrix);
		stableDiscCount(board, player);
		for (int row = 0; row < stableDiscMatrix.length; row++) {
			for (int column = 0; column < stableDiscMatrix.length; column++) {
				if (stableDiscMatrix[row][column] == 1)
					stableDics++;
			}
		}
		return stableDics;
	}

	public int getInternalStablity(Board board, Player player) {
		return this.getStableDiscCount(board, player)
				- this.getStableDiscCount(board, player.getOpponent());
	}

	public int getWeightSquares(Board board, Player player) {
		int score = 0;
		Disc disc = player.getDisc();
		for (int row = 0; row < board.getBoardSize(); row++) {
			for (int column = 0; column < board.getBoardSize(); column++) {
				if (board.getDiscAt(row, column) == disc)
					score += weightedPositionMatrix[row][column];
				else
					score -= weightedPositionMatrix[row][column];
			}
		}
		return score;
	}

	/**
	 * @param row
	 * @param column
	 * @return
	 */

	public int getModifiedWeightSquares(Board board, Player player) {
		int weight = getWeightSquares(board, player);

		// modifying the corner adjacent square weight on basis of its occupancy
		for (int row = 0; row < board.getBoardSize(); row += board
				.getBoardSize() - 1) {
			for (int column = 0; column < board.getBoardSize(); column += board
					.getBoardSize() - 1) {
				if (board.getDiscAt(row, column) != Disc.NONE) {
					for (int[] cell : getAdjacentCells(board, row, column)) {
						if (board.getDiscAt(cell[0], cell[1]) != Disc.NONE) {
							weight += (weightedPositionMatrix[cell[0]][cell[1]] - 5)
									* (board.getDiscAt(cell[0], cell[1]) == player
											.getDisc() ? +1 : -1);
						}

					}
				}
			}
		}
		return weight;
	}

	public boolean isCornerDisc(Board board, int row, int column) {
		// corner position
		if ((row == 0 || row == (board.getBoardSize() - 1))
				&& (column == 0 || column == (board.getBoardSize() - 1))) {
			return true;
		} else {
			return false;
		}
	}

	public boolean isEdgeDisc(Board board, int row, int column) {
		// System.out.println(row +" "+ column);
		if (!isCornerDisc(board, row, column)) {
			if (row == 0 || row == (board.getBoardSize() - 1) || column == 0
					|| column == (board.getBoardSize() - 1)) {
				return true;
			}
		}
		return false;

	}

	public void printSpaces(int space) {
		for (int i = 0; i < space; i++) {
			System.out.print(" ");
		}
	}

	public class WeightComparator implements Comparator<int[]> {

		/*
		 * (non-Javadoc)
		 * 
		 * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
		 */
		public int compare(int[] first, int[] second) {

			int row1 = first[0];
			int column1 = first[1];
			int row2 = second[0];
			int column2 = second[1];
			if (weightedPositionMatrix[row1][column1] < weightedPositionMatrix[row2][column2])
				return 1;
			if (weightedPositionMatrix[row1][column1] > weightedPositionMatrix[row2][column2])
				return -1;
			return 0;
		}

	}
}
