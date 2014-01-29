/**
 * 
 */
package edu.uab.exceptions;

/**
 * @author sjmaharjan
 * 
 */
public class BoardException extends RuntimeException {
	/**
	 * @param arg0
	 */
	public BoardException(String arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 */
	public BoardException(Throwable arg0) {
		super(arg0);
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public BoardException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

}
