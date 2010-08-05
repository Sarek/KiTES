package kites.visual;

import javax.swing.JOptionPane;

import kites.exceptions.DecompositionException;
import kites.exceptions.NoRewritePossibleException;
import kites.exceptions.NodeException;
import kites.exceptions.SyntaxErrorException;
import kites.exceptions.UnificationException;

public class MsgBox {
	public static void error(Exception e) {
		JOptionPane.showMessageDialog(null, getHeadline(e) + ":\n\n" + e.getMessage(), "KiTES - Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void error(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void info(Exception e) {
		JOptionPane.showMessageDialog(null, getHeadline(e) + ":\n\n" + e.getMessage(), "KiTES - Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void info(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void warning(Exception e) {
		JOptionPane.showMessageDialog(null, getHeadline(e) + ":\n\n" + e.getMessage(), "KiTES - Warning", JOptionPane.WARNING_MESSAGE);
	}

	public static void warning(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Warning", JOptionPane.WARNING_MESSAGE);
	}
	
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
