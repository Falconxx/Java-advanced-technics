package com.academic.jnlp.app.view;

import java.awt.Color;
import java.util.List;
import java.util.Random;
import java.util.function.IntFunction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class AppFrame extends JFrame {

	private static final long serialVersionUID = 3145238300374460866L;
	
	private final int ONE_PANEL_SIZE = 40;
	private final int GAP_SIZE = 10;

	Color[] colors = new Color[] { Color.RED, Color.GREEN, Color.ORANGE, Color.CYAN, Color.BLUE, Color.MAGENTA,
			Color.BLACK, Color.PINK };
	private List<PanelWithColor> panels;

	public AppFrame() {
		getContentPane().setLayout(null);
		setTitle("Random color");

		final int totalWidth = colors.length * ONE_PANEL_SIZE + (colors.length + 3) * GAP_SIZE;

		IntFunction<? extends PanelWithColor> indexToPanelWithColor = i -> {
			int gap = (i * ONE_PANEL_SIZE) + (i + 1) * GAP_SIZE;
			return new PanelWithColor(colors[i], ONE_PANEL_SIZE, gap);
		};

		panels = IntStream.range(0, colors.length).mapToObj(indexToPanelWithColor).collect(Collectors.toList());
		panels.forEach(panel -> getContentPane().add(panel));

		setSize(totalWidth, 200);

		JPanel resultPanel = new JPanel();
		resultPanel.setBounds(GAP_SIZE, ONE_PANEL_SIZE + GAP_SIZE * 2, totalWidth - GAP_SIZE * 4, 100);
		getContentPane().add(resultPanel);

		JButton btnRandom = new JButton("Random!");
		resultPanel.add(btnRandom);
		btnRandom.addActionListener(e -> {
			List<PanelWithColor> collect = panels.stream().filter(p -> p.isMarked()).collect(Collectors.toList());
			if (!collect.isEmpty()) {
				Random random = new Random();
				Color randomColor = collect.get(random.nextInt(collect.size())).getColor();
				resultPanel.setBackground(randomColor);
			}
		});

	}

}
