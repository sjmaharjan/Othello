/**
 * 
 */
package edu.uab.components;

import java.awt.Color;

/**
 * @author sjmaharjan
 * 
 */
public class Disc {
	//three differnt types of disc 
	public static final Disc BLACK = new Disc(Color.BLACK);
	public static final Disc WHITE = new Disc(Color.WHITE);
	public static final Disc NONE = new Disc();

	private Color discColor;

	public Disc() {
		discColor = null;
	}

	public Disc(Color discColor) {
		this.discColor = discColor;

	}

	public Color getDiscColor() {
		return discColor;
	}

	public void setDiscColor(Color discColor) {
		this.discColor = discColor;
	}

	public static Disc getAnotherDisc(Disc disc) {
		if (disc == Disc.WHITE)
			return Disc.BLACK;
		if (disc == Disc.BLACK)
			return Disc.WHITE;
		return Disc.NONE;
	}

	@Override
	public String toString() {
		if (discColor == Color.BLACK)
			return " B ";
		if (discColor == Color.WHITE)
			return " W ";
		return " _ ";
	}
}
