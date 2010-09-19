package kites.visual;

import javax.swing.JOptionPane;

import kites.exceptions.DecompositionException;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.NodeException;
import kites.exceptions.SyntaxErrorException;
import kites.exceptions.UnificationException;

/**
 * This class can display various types of message boxes with nice texts from exceptions
 */
public class MsgBox {
	/**
	 * Display an error message including a description derived from an exception.
	 * @param e the exception to get the message from
	 */
	public static void error(Exception e) {
		JOptionPane.showMessageDialog(null, getHeadline(e) + ":\n\n" + e.getMessage(), "KiTES - Fehler", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Display an error message 
	 * @param e the error message
	 */
	public static void error(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Fehler", JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Display an informative message including a description derived from an exception.
	 * @param e the exception to get the message from
	 */
	public static void info(Exception e) {
		JOptionPane.showMessageDialog(null, getHeadline(e) + ":\n\n" + e.getMessage(), "KiTES - Information", JOptionPane.INFORMATION_MESSAGE);
	}

	/**
	 * Display an informative message
	 * @param e the message
	 */
	public static void info(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	/**
	 * Display a warning message including a description derived from an exception.
	 * @param e the exception to get the message from
	 */
	public static void warning(Exception e) {
		JOptionPane.showMessageDialog(null, getHeadline(e) + ":\n\n" + e.getMessage(), "KiTES - Warning", JOptionPane.WARNING_MESSAGE);
	}

	/**
	 * Display a warning message
	 * @param e the message
	 */
	public static void warning(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Warning", JOptionPane.WARNING_MESSAGE);
	}
	
	/**
	 * Display a yes/no question.
	 * @param e the question
	 * @return true if yes was clicked, false otherwise
	 */
	public static boolean question(String e) {
		int retval = JOptionPane.showConfirmDialog(null, e, "KiTES", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
		
		if(retval == JOptionPane.YES_OPTION) {
			return true;
		}
		else {
			return false;
		}
	}
	
	/**
	 * Gives a nice (german) headline for a specific exception (only the built in ones)
	 * @param e the exception to get the message from
	 */
	private static String getHeadline(Exception e) {
		String headline = e.getClass().toString();
		
		if(e instanceof SyntaxErrorException)
			headline = "Syntaxfehler";
		if(e instanceof DecompositionException)
			headline = "Zerlegungsfehler";
		if(e instanceof NodeException)
			headline = "Fehler im Syntaxbaum";
		if(e instanceof NoRewritePossibleException)
			headline = "Fehler bei Termersetzung";
		if(e instanceof UnificationException)
			headline = "Unifizierbarkeitsfehler";
		
		return headline;
	}
}
