/*
 * This file is part of KiTES.
 * 
 * Copyright 2010 Sebastian Schäfer <sarek@uliweb.de>
 *
 *   KiTES is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   KiTES is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with KiTES.  If not, see <http://www.gnu.org/licenses/>.
 */

package kites.visual;

import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * This class displays a small window with some text in it,
 * telling who developed KiTES and which version this is.
 * @author sarek
 *
 */
public class AboutWindow extends JFrame {
	/**
	 * A serial to stop Eclipse from jammering all over the place...
	 */
	private static final long serialVersionUID = 6022651378659252251L;

	/**
	 * Create and display the window
	 */
	public AboutWindow() {
	    Toolkit tk = Toolkit.getDefaultToolkit();
	    Dimension screenSize = tk.getScreenSize();
	    
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		panel.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		
		setSize(500, 400);
	    setLocation((screenSize.width - this.getWidth()) / 2, (screenSize.height - this.getHeight()) / 2);
		setTitle("KiTES v1.0");
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
