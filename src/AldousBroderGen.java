
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.Timer;

public class AldousBroderGen extends JPanel {

	private static final long serialVersionUID = 1L;

	int score = 0;

	private Random r = new Random();
	public Cell[][] grid;
	int size;

	public Cell curr;
	public int w;
	public boolean solved = false;

	private Timer timer;
	private boolean generating = true;

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

		timer = new Timer(1, new ActionListener() { // Faster timer for Aldous-Broder as it is slow
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

	public void initialize() {
		for (int i = 0; i < size; i++)
			for (int j = 0; j < size; j++)
				grid[i][j] = new Cell(i, j, w);
	}


	public class Al extends KeyAdapter {

		public void keyPressed(KeyEvent e) {
			if (generating) return;

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
				JOptionPane.showMessageDialog(AldousBroderGen.this, "YOU WON!");
				curr = grid[0][0];
				score = 0;
			}
			repaint();
		}

	}

	private void step() {
		if (generating) {
			if (!isDone()) {
				curr.visited = true;
				Cell next;

				do {
					next = curr.getAnyNeighbor(grid);
				} while (next == null);

				if (next.visited == false)
					Cell.removeWalls(curr, next);

				curr = next;
			} else {
				System.out.println("DONE");
				curr = grid[0][0];
				generating = false;
				timer.stop();
			}
			repaint();
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				grid[i][j].draw(g);

		curr.mark(g);

	}

	public boolean isDone() {

		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (!grid[i][j].visited)
					return false;

		return true;
	}
}
