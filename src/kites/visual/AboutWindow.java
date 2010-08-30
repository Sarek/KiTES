package kites.visual;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class AboutWindow extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6022651378659252251L;

	public AboutWindow() {
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		
		setSize(500, 400);
	    setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		setTitle("KiTES v0.1");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		JLabel name = new JLabel("KiTES");
		name.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		name.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		name.setVerticalAlignment(JLabel.CENTER);
		name.setHorizontalAlignment(JLabel.CENTER);
		name.setFont(name.getFont().deriveFont((float) 80.0));
		
		JLabel version = new JLabel("v0.9 - 2010-08-30");
		version.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		version.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		version.setVerticalAlignment(JLabel.CENTER);
		version.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel subtitle = new JLabel("Ein Interpreter für Termersetzungssysteme");
		subtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		subtitle.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		subtitle.setVerticalAlignment(JLabel.CENTER);
		subtitle.setHorizontalAlignment(JLabel.CENTER);
		subtitle.setFont(subtitle.getFont().deriveFont((float) 16.0));
		
		JLabel description = new JLabel("Entstanden im Rahmen einer gleichnamigen Bachelorarbeit");
		JLabel description2 = new JLabel("an der Christian-Albrechts-Universität zu Kiel,");
		JLabel description3 = new JLabel("betreut von Prof. Dr. Thomas Wilke");
		description.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		description2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		description3.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		description.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		description2.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		description3.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		description.setVerticalAlignment(JLabel.CENTER);
		description2.setVerticalAlignment(JLabel.CENTER);
		description3.setVerticalAlignment(JLabel.CENTER);
		description.setHorizontalAlignment(JLabel.CENTER);
		description2.setHorizontalAlignment(JLabel.CENTER);
		description3.setHorizontalAlignment(JLabel.CENTER);
		
		JLabel copyright = new JLabel("Copyright (C) 2010 Sebastian Schäfer.");
		JLabel copyright2 = new JLabel("Diese Software ist unter der GNU General Public License lizenziert.");
		copyright.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		copyright2.setAlignmentX(JLabel.CENTER_ALIGNMENT);
		copyright.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		copyright2.setAlignmentY(JLabel.CENTER_ALIGNMENT);
		copyright.setVerticalAlignment(JLabel.CENTER);
		copyright2.setVerticalAlignment(JLabel.CENTER);
		copyright.setHorizontalAlignment(JLabel.CENTER);
		copyright2.setHorizontalAlignment(JLabel.CENTER);
		
		panel.add(Box.createVerticalStrut(40));				
		panel.add(name);
		panel.add(Box.createVerticalStrut(15));
		panel.add(version);
		panel.add(Box.createVerticalStrut(25));
		panel.add(subtitle);
		panel.add(Box.createVerticalStrut(15));
		panel.add(description);
		panel.add(description2);
		panel.add(description3);
		panel.add(Box.createVerticalStrut(15));
		panel.add(copyright);
		panel.add(copyright2);
		this.add(panel);
	}
}
