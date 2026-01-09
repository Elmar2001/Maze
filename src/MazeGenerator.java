import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MazeGenerator {
	static int SIZE;
	static DFSSolve solver;

	static JScrollPane scrollPane;

	public static void main(String[] args) {
		/* Maze size input window */
		JFrame ask = new JFrame();
		ask.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		ask.pack();
		ask.setSize(200, 100);
		ask.setLayout(null);

		// Center on screen
		ask.setLocationRelativeTo(null);

		JTextField text = new JTextField();
		text.setBounds(10, 10, 50, 40);
		text.setText("400"); // default maze size (Total cells: 400 -> 20x20 grid)
		JButton enter = new JButton("generate");
		enter.setBounds(70, 10, 100, 40);
		ask.add(text);
		ask.add(enter);

		ask.setVisible(true);

		enter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					SIZE = Integer.parseInt(text.getText());
					ask.dispose();
					generate();
				} catch (NumberFormatException ex) {
					JOptionPane.showMessageDialog(ask, "Invalid number");
				}
			}
		});

	}

	public static void generate() {
		JFrame frame = new JFrame("Maze");

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setBackground(Color.DARK_GRAY);

		// Use BorderLayout
		frame.setLayout(new BorderLayout());

		// ======================================
		// You can uncomment Maze to use Recursive Backtracker
//		Maze maze = new Maze(SIZE);
		AldousBroderGen maze = new AldousBroderGen(SIZE);
		// ======================================

		scrollPane = new JScrollPane(maze);
		// Remove border from scrollpane for cleaner look
		scrollPane.setBorder(BorderFactory.createEmptyBorder());
		frame.add(scrollPane, BorderLayout.CENTER);


		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
		panel.setBackground(Color.DARK_GRAY);

		JButton b = new JButton("Solve");
		JButton reset = new JButton("Reset");

		JLabel sclabel = new JLabel("Score: " + maze.score);
		sclabel.setForeground(Color.RED);

		panel.add(b);
		panel.add(reset);
		panel.add(sclabel);

		frame.add(panel, BorderLayout.EAST);

		frame.pack();

		// Limit size if too big
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		if (frame.getWidth() > screenSize.width || frame.getHeight() > screenSize.height) {
			frame.setSize(Math.min(frame.getWidth(), screenSize.width - 50),
						  Math.min(frame.getHeight(), screenSize.height - 50));
		}

		frame.setLocationRelativeTo(null); // Center
		frame.setVisible(true);

		maze.requestFocusInWindow();

		/* Maze solver */
		b.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (solver != null) return; // Already solving or solved

				solver = new DFSSolve(maze, maze.curr);

				scrollPane.setViewportView(solver);
				frame.revalidate();
				frame.repaint();
			}
		});

		/* Maze resetter */
		reset.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (solver != null) {
					solver.reset();
					solver = null;

					scrollPane.setViewportView(maze);
					frame.revalidate();
					frame.repaint();
				}

				maze.requestFocusInWindow();
				maze.curr = maze.grid[0][0];
				maze.score = 0;
				maze.repaint();
			}
		});

		/* Score updater */
		new Timer(100, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sclabel.setText("Score: " + maze.score);
			}
		}).start();

	}
}
