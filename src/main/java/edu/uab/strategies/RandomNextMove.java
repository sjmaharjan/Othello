/**
 * 
 */
package edu.uab.strategies;

import java.util.List;
import java.util.Random;

import edu.uab.components.Board;
import edu.uab.components.Disc;
import edu.uab.components.Player;
import edu.uab.game.Game;

/**
 * @author sjmaharjan
 * 
 */
public class RandomNextMove extends Strategy {

	

	/**
	 * @param player
	 */
	public RandomNextMove() {
		
	}

	/* (non-Javadoc)
	 * @see edu.uab.strategies.Strategy#getNextBestMove(edu.uab.game.Game, edu.uab.components.Player)
	 */
	@Override
	public int[] getNextBestMove(Game game, Player player) {
		this.player=player;
		List<int[]> moves = getAllPossibleMoves(game.getBoard(), this.player.getDisc());
		Random random = new Random();
		return (moves.get(random.nextInt(moves.size())));
	}

}
