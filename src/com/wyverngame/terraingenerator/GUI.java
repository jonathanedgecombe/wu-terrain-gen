package com.wyverngame.terraingenerator;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public final class GUI {
	private final JProgressBar progress;
	private final JLabel label;
	private final JTextField size, seed;
	private final JButton button;

	public GUI() {
		this.progress = new JProgressBar();
		this.label = new JLabel(" ");
		this.size = new JTextField("2048", 5);
		this.seed = new JTextField(Long.toString(new Random().nextLong()), 16);

		JFrame frame = new JFrame("Jonneh's Map Generator");
		frame.setLayout(new BorderLayout());

		JPanel panel = new JPanel(new BorderLayout());
		JPanel panel2 = new JPanel(new BorderLayout());

		JPanel wrapper = new JPanel();
		wrapper.setLayout(new BorderLayout());
		wrapper.setBorder(BorderFactory.createEmptyBorder(1, 1, 0, 0));
		wrapper.add(progress, BorderLayout.CENTER);
		panel.add(wrapper, BorderLayout.CENTER);

		this.button = new JButton("Generate");
		this.button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Generate")) {
					button.setEnabled(false);
					generate();
				}
			}
		});

		panel.add(button, BorderLayout.EAST);
		panel.add(label, BorderLayout.SOUTH);

		JPanel top = new JPanel();
		top.setLayout(new BoxLayout(top, BoxLayout.LINE_AXIS));

		top.add(new JLabel(" Map Size: "));
		top.add(size);
		top.add(new JLabel("  Seed: "));
		top.add(seed);

		JButton randomize = new JButton("Randomize");
		top.add(randomize);
		randomize.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (e.getActionCommand().equals("Randomize")) {
					seed.setText(Long.toString(new Random().nextLong()));
				}
			}
		});

		panel.add(top, BorderLayout.NORTH);

		panel2.add(panel, BorderLayout.WEST);
		frame.add(panel2, BorderLayout.NORTH);
		frame.pack();
		panel.setMaximumSize(panel.getSize());

		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}

	public void generate() {
		label.setForeground(Color.BLACK);

		int size = 2048;
		try {
			size = Integer.parseInt(this.size.getText().trim());
		} catch (NumberFormatException ex) {
			label.setForeground(Color.RED);
			label.setText(" '" + this.size.getText().trim() + "' is not a valid number.");
			return;
		}

		final String name = this.seed.getText().trim().replaceAll("[^\\w-]+", "_").toLowerCase();

		long seed;
		try {
			seed = Long.parseLong(this.seed.getText().trim());
		} catch (NumberFormatException ex) {
			seed = this.seed.getText().trim().hashCode();
		}

		final int si = size;
		final long se = seed;

		if (size < 0 || (size & (size - 1)) != 0) {
			label.setText(" Size must be a power of 2 between 1024 and 32768.");
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Terragen.generate(si, se, label, progress, button, name);
				} catch (IOException e) {
					label.setForeground(Color.RED);
					label.setText(" " + e.toString());
				}
			}
		}).start();
	}

	public static void main(String[] args) {
		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
				| UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}

		new GUI();
	}
}
