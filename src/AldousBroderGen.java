
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;

public class AldousBroderGen extends JPanel {

	private static final long serialVersionUID = 1L;

	int score = 0;

	private Random r = new Random();
	public Cell[][] grid;
	int size;

	public Cell curr;
	public int w;
	public boolean solved = false;

	public AldousBroderGen(int n) {

		this.size = (int) Math.sqrt(n);
		this.w = 30;
		this.grid = new Cell[size][size];
		initialize();

		grid[size - 1][size - 1].walls[1] = false;

		grid[0][0].walls[0] = false;

		addKeyListener(new Al());
		setFocusable(true);

		curr = grid[r.nextInt(size - 1)][r.nextInt(size - 1)];
//		curr = grid[0][0];
	}

	public void initialize() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				grid[i][j] = new Cell(i, j, w);
	}


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

	private void carve(Graphics g) {
		curr.mark(g);
		curr.visited = true;
		Cell next;

		do {
			next = curr.getAnyNeighbor(grid);
		} while (next == null);

		if (next.visited == false)
			Cell.removeWalls(curr, next);

		curr = next;
		repaint();
	}

	@Override
	public void paintComponent(Graphics g) {
		for (int i = 0; i < grid[0].length; i++)
			for (int j = 0; j < grid[0].length; j++)
				grid[i][j].draw(g);

		if (!solved) {
			if (!isDone())
				carve(g);
			else {
				System.out.println("DONE");
				curr = grid[0][0];
				curr.mark(g);
				solved = true;
				return;
			}
		} else
			curr.mark(g);

	}

	public boolean isDone() {

		for (int i = 0; i < grid[0].length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (!grid[i][j].visited)
					return false;

		return true;
	}
}