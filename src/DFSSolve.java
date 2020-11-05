
import java.awt.Graphics;
import java.util.Stack;

import javax.swing.JPanel;

public class DFSSolve extends JPanel {

	private static final long serialVersionUID = 1L;

	public final Stack<Cell> stack = new Stack<Cell>();

	public Cell[][] grid;
	int size;

	Cell curr = null;
	Cell next = null;

	boolean solved = false;
	Cell marked;


	public DFSSolve(Maze m) {
		this.grid = m.grid;
		this.size = m.size;
		curr = grid[0][0];
	}

	public DFSSolve(AldousBroderGen m) {
		this.grid = m.grid;
		this.size = m.size;
		curr = grid[0][0];
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

	public void solve() {

		if (curr.x == size - 1 && curr.y == size - 1) {
			System.out.println("Solved");
			curr.path = true;
			stack.push(curr);
			solved = true;
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

	}


	@Override
	public void paintComponent(Graphics g) {
		curr.mark(g);
		solve();
		if (solved) {
			while (!stack.isEmpty()) {
				try {
					Cell c = stack.pop();
					if (c.x == marked.x && c.y == marked.y)
						return;

					c.path = true;
					c.markPath(g);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

}