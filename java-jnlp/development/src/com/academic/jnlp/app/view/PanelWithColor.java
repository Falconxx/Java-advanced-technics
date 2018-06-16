package com.academic.jnlp.app.view;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JCheckBox;
import javax.swing.JPanel;

public class PanelWithColor extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8698791979254749828L;

	private Color color;

	private JCheckBox checkBox;

	public PanelWithColor(Color color, int size, int positionX) {
		this.color = color;

		this.setBackground(color);

		this.setSize(size, size);
		this.setLocation(positionX, 10);

		checkBox = new JCheckBox();
		this.add(checkBox, BorderLayout.CENTER);
		checkBox.setOpaque(false);
	}

	public boolean isMarked() {
		return checkBox.isSelected();
	}

	public Color getColor() {
		return color;
	}

}
