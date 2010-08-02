package kites.visual;

import javax.swing.JOptionPane;

public class MsgBox {
	public static void error(Exception e) {
		JOptionPane.showMessageDialog(null, e.toString(), "KiTES - Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void error(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Error", JOptionPane.ERROR_MESSAGE);
	}

	public static void info(Exception e) {
		JOptionPane.showMessageDialog(null, e.toString(), "KiTES - Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void info(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Information", JOptionPane.INFORMATION_MESSAGE);
	}
	
	public static void warning(Exception e) {
		JOptionPane.showMessageDialog(null, e.toString(), "KiTES - Warning", JOptionPane.WARNING_MESSAGE);
	}

	public static void warning(String e) {
		JOptionPane.showMessageDialog(null, e, "KiTES - Warning", JOptionPane.WARNING_MESSAGE);
	}
}
