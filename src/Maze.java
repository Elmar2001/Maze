import javax.swing.*;

import java.awt.*;
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

	}

	/* Cell mover */
	public class Al extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
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
				System.out.println("YOU WON!");
				System.exit(1);
			}
			repaint();
		}
	}

	public void initialize() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				grid[i][j] = new Cell(i, j, w);
	}

	public void backtrack(Graphics g) {

		curr.visited = true;
		curr.mark(g);

		Cell next = curr.getNeighbor(grid);
		if (next != null) {
			next.visited = true;
			stack.push(curr);

			Cell.removeWalls(curr, next);
			curr = next;

			repaint();

		} else if (stack.size() > 0) {
			curr = stack.pop();
			repaint();
		}
	}

	/* Call painter */
	public void paintComponent(Graphics g) {
		for (int i = 0; i < grid[0].length; i++)
			for (int j = 0; j < grid[0].length; j++)
				grid[i][j].draw(g);

		backtrack(g);

	}

}
