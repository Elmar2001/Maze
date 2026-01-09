
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Stack;

import javax.swing.JPanel;
import javax.swing.Timer;

public class DFSSolve extends JPanel {

	private static final long serialVersionUID = 1L;

	public final Stack<Cell> stack = new Stack<Cell>();

	public Cell[][] grid;
	int size;
	int w = 30; // Assuming default width

	Cell curr = null;
	Cell next = null;

	boolean solved = false;
	Cell marked;

	Timer timer;


	public DFSSolve(Maze m) {
		this.grid = m.grid;
		this.size = m.size;
		this.w = m.w;
		curr = grid[0][0];
		initTimer();
	}

	public DFSSolve(AldousBroderGen m) {
		this.grid = m.grid;
		this.size = m.size;
		this.w = m.w;
		curr = grid[0][0];
		initTimer();
	}

	public DFSSolve(Maze m, Cell current) {
		this(m);
		curr = current;
		marked = new Cell(current.x, current.y, size);
	}

	public DFSSolve(AldousBroderGen m, Cell current) {
		this(m);
		curr = current;
		marked = new Cell(current.x, current.y, size);
	}

	private void initTimer() {
		timer = new Timer(50, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				step();
			}
		});
		timer.start();
	}

	public void step() {
		if (solved) {
			timer.stop();
			return;
		}

		if (curr.x == size - 1 && curr.y == size - 1) {
			System.out.println("Solved");
			curr.path = true;
			stack.push(curr);
			solved = true;
			repaint();
			return;
		}
		curr.deadEnd = true;

		next = curr.getPathNeighbor(grid);
		if (next != null) {
			stack.push(curr);
			curr = next;
			curr.path = true;
		} else if (stack.size() > 0) {
			curr.deadEnd = true;
			curr.path = false;
			curr = stack.pop();
		}
		repaint();
	}

	boolean reset = false;

	public void reset() {
		for (int i = 0; i < grid[0].length; i++)
			for (int j = 0; j < grid[0].length; j++) {
				grid[i][j].path = false;
				grid[i][j].deadEnd = false;
			}
		System.out.println("Reset");
		if (timer != null) timer.stop();

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(size * w, size * w);
	}


	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.setColor(Color.DARK_GRAY);
		g.fillRect(0, 0, getWidth(), getHeight());

		// Draw walls
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				grid[i][j].draw(g);

		curr.mark(g);

		if (solved) {
			// Draw path from stack
			// To avoid concurrent modification if timer was running (it shouldn't be), we iterate carefully.
			// Actually stack iteration is fine.
			for (Cell c : stack) {
				if (marked != null && c.x == marked.x && c.y == marked.y) break;
				c.path = true; // Ensure it's marked as path
				c.markPath(g);
			}
		}
	}

}
