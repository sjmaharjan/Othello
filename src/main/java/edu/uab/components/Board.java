/**
 * 
 */
package edu.uab.components;

import edu.uab.exceptions.BoardException;

/**
 * @author sjmaharjan
 * 
 */
public class Board {
	public static final int DEFAULT_BOARD_SIZE = 8;
	private int boardSize;
	private Disc[][] cells;
	private int totolDiscsFlipped = 0;

	public Board() {
		this(DEFAULT_BOARD_SIZE);
	}

	public Board(int boardSize) {
		this.boardSize = boardSize;
		cells = new Disc[boardSize][boardSize];
		for (int row = 0; row < boardSize; row++) {
			for (int column = 0; column < boardSize; column++) {
				cells[row][column] = Disc.NONE;
			}
		}
		// initialize the board with 2 white and 2 black at center
		if (this.boardSize > 2) {
			cells[boardSize / 2 - 1][boardSize / 2] = Disc.BLACK;
			cells[boardSize / 2][boardSize / 2 - 1] = Disc.BLACK;
			cells[boardSize / 2 - 1][boardSize / 2 - 1] = Disc.WHITE;
			cells[boardSize / 2][boardSize / 2] = Disc.WHITE;
		}

	}

	/*
	 * copy constructor used in cloning the board object when we explore the
	 * game tree need to clone the board object
	 */
	public Board(Board board) {
		this.boardSize = board.getBoardSize();
		this.cells = new Disc[this.boardSize][this.boardSize];
		for (int row = 0; row < boardSize; row++) {
			for (int column = 0; column < boardSize; column++) {
				cells[row][column] = board.getDiscAt(row, column);
			}
		}

	}

	public Disc getDiscAt(int row, int column) {
		return this.cells[row][column];
	}

	public int getBoardSize() {
		return boardSize;
	}

	public void setBoardSize(int boardSize) {
		this.boardSize = boardSize;
	}

	public int getTotolDiscsFlipped() {
		return totolDiscsFlipped;
	}

	public void setTotolDiscsFlipped(int totolDiscsFlipped) {
		this.totolDiscsFlipped = totolDiscsFlipped;
	}

	public int getDiscCount(Disc disc) {
		int count = 0;
		for (int row = 0; row < boardSize; row++) {
			for (int column = 0; column < boardSize; column++) {
				if (disc == this.getDiscAt(row, column))
					count++;
			}
		}
		return count;
	}

	public void setDiscAt(int row, int column, Disc disc) {
		if (isValidCell(row, column, disc)) {
			this.cells[row][column] = disc;
			setTotolDiscsFlipped(0);
			filpAllPossibleDiscs(row, column, disc);
		} else
			throw new BoardException(
					"(C (can't set the disc at cell with given row index and column index))");

	}

	/**
	 * @param column
	 * @param row
	 * @param disc
	 */
	private void filpAllPossibleDiscs(int row, int column, Disc disc) {
		for (int rowIndexChange = -1; rowIndexChange <= 1; rowIndexChange++) {
			for (int columnIndexChange = -1; columnIndexChange <= 1; columnIndexChange++) {
				if ((rowIndexChange != 0) || (columnIndexChange != 0)) {
					if (this.checkDirection(row, column, rowIndexChange,
							columnIndexChange, disc)) {
						filpDiscs(row, column, rowIndexChange,
								columnIndexChange, disc);
					}
				}
			}
		}

	}

	/**
	 * @param row
	 * @param column
	 * @param rowIndexChange
	 * @param columnIndexChange
	 * @param disc
	 */
	private void filpDiscs(int row, int column, int rowIndexChange,
			int columnIndexChange, Disc disc) {
		row += rowIndexChange;
		column += columnIndexChange;

		while (this.getDiscAt(row, column) == Disc.getAnotherDisc(disc)) {
			this.cells[row][column] = disc;
			row += rowIndexChange;
			column += columnIndexChange;
			totolDiscsFlipped++;
		}
	}

	/**
	 * @param row
	 * @param column
	 * @param disc
	 * @return
	 */
	public boolean isValidCell(int row, int column, Disc disc) {

		// checking row column validity
		if (row < boardSize && row >= 0 && column >= 0 && column < boardSize) {
			// check if there is any disc
			boolean hasDisc = (this.getDiscAt(row, column) != Disc.NONE);
			// proceed further if there are not any discs
			if (!hasDisc) {
				if (disc == Disc.NONE)
					return true;
				else {
					// checking all 8 possible direction
					for (int rowIndexChange = -1; rowIndexChange <= 1; rowIndexChange++) {
						for (int columnIndexChange = -1; columnIndexChange <= 1; columnIndexChange++) {
							if ((rowIndexChange != 0)
									|| (columnIndexChange != 0)) {
								if (this.checkDirection(row, column,
										rowIndexChange, columnIndexChange, disc)) {
									return true;
								}
							}
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * @param row
	 * @param column
	 * @param rowIndexChange
	 * @param columnIndexChange
	 * @param disc
	 * @return
	 */
	private boolean checkDirection(int row, int column, int rowIndexChange,
			int columnIndexChange, Disc disc) {
		Disc opponentDisc = Disc.getAnotherDisc(disc);
		boolean opponentDiscFound = false;
		row += rowIndexChange;
		column += columnIndexChange;
		// checking if opponent discs are enclosed
		while ((row >= 0) && (row < this.getBoardSize()) && (column >= 0)
				&& (column < this.getBoardSize())) {
			if (opponentDiscFound) {
				if (this.getDiscAt(row, column) == disc) {
					return true;
				} else if (this.getDiscAt(row, column) == Disc.NONE) {
					return false;
				}
			} else {

				if (this.getDiscAt(row, column) == opponentDisc) {
					opponentDiscFound = true;
				} else {
					return false;
				}
			}
			row += rowIndexChange;
			column += columnIndexChange;
		}
		return false;
	}

	/*
//	 * test if any valid cells left for given disc used for testing end of game
	 * for given player
	 */
	public boolean canSetAnyCell(Disc disc) {
		for (int row = 0; row < boardSize; row++) {
			for (int column = 0; column < boardSize; column++) {
				if (isValidCell(row, column, disc))
					return true;
			}
		}
		return false;
	}

	public void printBoard() {
		System.out.println("(C    a  b  c  d  e  f  g  h  )");
		for (int row = 0; row < boardSize; row++) {
			System.out.print("(C "+(row + 1) + " ");
			for (int column = 0; column < boardSize; column++) {
				System.out.print(cells[row][column]);
			}
			System.out.print(" )");
			System.out.println();
		}
	}

}
