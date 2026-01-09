import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Stack;

public class Maze extends JPanel {

	private static final long serialVersionUID = 1L;
	public int score = 0;

	public int size; // maze size
	public Cell[][] grid; // maze
	public int w; // cell width
	public Cell curr; // current cell

	Stack<Cell> stack = new Stack<>();

	private Timer timer;
	private boolean generating = true;

	public Maze(int num_of_cells) {

		this.size = (int) Math.sqrt(num_of_cells);
		this.w = 30;
		this.grid = new Cell[size][size];
		initialize();

		this.curr = grid[0][0];

		grid[size - 1][size - 1].walls[1] = false; // finish

		grid[0][0].walls[0] = false; // start

		addKeyListener(new Al());
		setFocusable(true);

		// Timer for generation
		timer = new Timer(10, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				step();
			}
		});
		timer.start();
	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(size * w, size * w);
	}

	/* Cell mover */
	public class Al extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			if (generating) return; // Disable movement during generation

			if (e.getKeyCode() == KeyEvent.VK_UP && !curr.walls[0]) {
				if (curr.x != 0 || curr.y != 0) {
					curr = grid[curr.x][curr.y - 1];
					score++;

				}
			}

			else if (e.getKeyCode() == KeyEvent.VK_RIGHT && !curr.walls[1]) {
				curr = grid[curr.x + 1][curr.y];
				score++;
			}

			else if (e.getKeyCode() == KeyEvent.VK_LEFT && !curr.walls[3]) {
				curr = grid[curr.x - 1][curr.y];
				score++;

			}

			else if (e.getKeyCode() == KeyEvent.VK_DOWN && !curr.walls[2]) {
				curr = grid[curr.x][curr.y + 1];
				score++;

			}

			if (curr.x == size - 1 && curr.y == size - 1) {
				JOptionPane.showMessageDialog(Maze.this, "YOU WON!");
				// Reset or stop?
				// For now, just reset curr to start?
				curr = grid[0][0];
				score = 0;
				repaint();
			}
			repaint();
		}
	}

	public void initialize() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				grid[i][j] = new Cell(i, j, w);
	}

	public void step() {
		if (generating) {
			curr.visited = true;

			Cell next = curr.getNeighbor(grid);
			if (next != null) {
				next.visited = true;
				stack.push(curr);

				Cell.removeWalls(curr, next);
				curr = next;

			} else if (stack.size() > 0) {
				curr = stack.pop();
			} else {
				generating = false;
				timer.stop();
				curr = grid[0][0]; // Reset curr to start for playing
			}
			repaint();
		}
	}

	/* Call painter */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		// Draw background if needed, but Cell draw might cover it?
		// Cell.draw fills with light gray if visited, but walls are lines.
		// Unvisited cells are not filled? Cell.draw only fills if visited.
		// So we should fill background.
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				grid[i][j].draw(g);

		if (generating) {
			curr.mark(g);
		} else {
			// Draw player
			curr.mark(g);
		}

	}

}
