import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Life extends JFrame {
	JPanel[][] cells;
	int[][] matrix;
	int width, height;

	Life(int x, int y) {
		JPanel o = new JPanel();
		matrix = new int[x][y];
		cells = new JPanel[x][y];
		o.setLayout(new GridLayout(x, y));
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++) {
				cells[i][j] = new JPanel();
				o.add(cells[i][j]);
			}
		width = x;
		height = y;
		this.setBounds(100, 100, 400, 400);
		GridLayout bl = new GridLayout(1, 1);
		bl.setHgap(0);
		bl.setVgap(0);
		this.setLayout(bl);
		this.getContentPane().add(o);
		for (int i = 0; i < x; i++)
			for (int j = 0; j < y; j++) {
				if (matrix[i][j] == 0)
					cells[i][j].setBackground(Color.white);
				else
					cells[i][j].setBackground(Color.red);
			}
		this.setVisible(true);
	}

	public void refresh() {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (matrix[i][j] == 0)
					cells[i][j].setBackground(Color.white);
				else if (matrix[i][j] == 1)
					cells[i][j].setBackground(Color.black);
				else if (matrix[i][j] == 2)
					cells[i][j].setBackground(Color.green);
				else
					cells[i][j].setBackground(Color.red);
			}
	}

	void set(int x, int y, int z) {
		matrix[x][y] = z;
	}

	void next() {
		int intermMatrix[][] = new int[matrix.length][matrix[0].length];
		if (matrix.length == 0 || matrix[0].length == 0) {
			System.out.println("This is no game board, please behave!");
			return;
		}
		if (matrix.length == 1 && matrix[0].length == 1) {
			System.out
					.println("It's not possible to play a proper game on this game board, please type in something sensible!");
			return;
		}
		if (matrix.length == 1) {
			for (int y = 0; y < matrix[0].length; y++)
				intermMatrix[0][y] = countRowEntry(y);
			copyToMatrix(intermMatrix);
			return;
		}
		if (matrix[0].length == 1) {
			for (int x = 0; x < matrix.length; x++)
				intermMatrix[x][0] = countLineEntry(x);
			copyToMatrix(intermMatrix);
			return;
		}
		System.out.println(matrix.length);
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++) {
				intermMatrix[i][j] = countAliveCellsAround(i, j);
				System.out.println(" i = " + i + " j = " + j + " neighb = "
						+ intermMatrix[i][j]);
			}
		copyToMatrix(intermMatrix);

	}

	public void copyToMatrix(int[][] intermMatrix) {
		for (int i = 0; i < matrix.length; i++)
			for (int j = 0; j < matrix[0].length; j++) {
				if (matrix[i][j] == 1) { // alive cell
					if (intermMatrix[i][j] < 2 || intermMatrix[i][j] > 3) {
						matrix[i][j] = 0;
					} else {
						matrix[i][j] = 1;
					}
				} else { // dead cell
					if (intermMatrix[i][j] == 3) {
						matrix[i][j] = 1;
					}
				}
			}
	}

	public int countRowEntry(int y) {
		int numAlive = 0;
		if (y == 0)
			if (matrix[0][1] == 1)
				return 1;
			else
				return 0;
		if (y == matrix[0].length - 1) {
			if (matrix[0][matrix[0].length - 2] == 1) {
				return 1;
			} else
				return 0; // corners
		}
		if (matrix[0][y - 1] == 1) {
			numAlive++;
		}
		if (matrix[0][y + 1] == 1) {
			numAlive++;
		}
		System.out.println(numAlive);
		return numAlive;
	}

	public int countLineEntry(int x) {
		int numAlive = 0;
		if (x == 0)
			if (matrix[1][0] == 1)
				return 1;
			else
				return 0;
		if (x == matrix.length - 1) {
			if ((matrix[matrix.length - 2][0]) == 1) {
				return 1;
			} else
				return 0; // corners
		}
		if (matrix[x - 1][0] == 1) {
			numAlive++;
		}
		if (matrix[x + 1][0] == 1) {
			numAlive++;
		}
		return numAlive;
	}

	public int countAliveCellsAround(int x, int y) {
		if (x == 0) {
			if (y == 0) {
				return countTopLeftCorner();
			}
			if (y == matrix[0].length-1) {
				return countTopRightCorner();
			}
			return topField(y);
		}
		if (y == 0) {
			if (x == matrix.length-1) {
				return countDownLeftCorner();
			}
			return leftField(x);
		}
		if (x == matrix.length-1) {
			if (y == matrix[0].length-1) {
				return countDownRightCorner();
			}
			return downField(y);
		}
		if (y == matrix[0].length-1) {
			return rightField(x);
		}
		return middleField(x, y);
	}

	private int countTopLeftCorner() {
		int livingCells = 0;
		if (matrix[1][0] == 1)
			livingCells++;
		if (matrix[1][1] == 1)
			livingCells++;
		if (matrix[0][1] == 1)
			livingCells++;
		return livingCells;
	}

	private int countTopRightCorner() {
		int livingCells = 0;
		if (matrix[0][matrix[0].length - 2] == 1)
			livingCells++;
		if (matrix[1][matrix[0].length - 2] == 1)
			livingCells++;
		if (matrix[1][matrix[0].length - 1] == 1)
			livingCells++;
		return livingCells;
	}

	private int countDownLeftCorner() {
		int livingCells = 0;
		if (matrix[matrix.length - 2][0] == 1)
			livingCells++;
		if (matrix[matrix.length - 2][1] == 1)
			livingCells++;
		if (matrix[matrix.length - 1][1] == 1)
			livingCells++;
		return livingCells;
	}

	private int countDownRightCorner() {
		int livingCells = 0;
		if (matrix[matrix.length - 2][matrix[0].length - 1] == 1)
			livingCells++;
		if (matrix[matrix.length - 2][matrix[0].length - 2] == 1)
			livingCells++;
		if (matrix[matrix.length - 1][matrix[0].length - 2] == 1)
			livingCells++;
		return livingCells;
	}

	private int leftField(int x) {
		int livingCells = 0;
		if (matrix[x - 1][0] == 1)
			livingCells++;
		if (matrix[x + 1][0] == 1)
			livingCells++;
		if (matrix[x][1] == 1)
			livingCells++;
		if (matrix[x - 1][1] == 1)
			livingCells++;
		if (matrix[x + 1][1] == 1)
			livingCells++;
		return livingCells;
	}

	private int topField(int y) {
		int livingCells = 0;
		if (matrix[0][y - 1] == 1)
			livingCells++;
		if (matrix[0][y + 1] == 1)
			livingCells++;
		if (matrix[1][y - 1] == 1)
			livingCells++;
		if (matrix[1][y + 1] == 1)
			livingCells++;
		if (matrix[1][y] == 1)
			livingCells++;
		return livingCells;
	}

	private int downField(int y) {
		int livingCells = 0;
		if (matrix[matrix.length - 1][y - 1] == 1)
			livingCells++;
		if (matrix[matrix.length - 1][y + 1] == 1)
			livingCells++;
		if (matrix[matrix.length - 2][y] == 1)
			livingCells++;
		if (matrix[matrix.length - 2][y - 1] == 1)
			livingCells++;
		if (matrix[matrix.length - 2][y + 1] == 1)
			livingCells++;
		return livingCells;
	}

	private int rightField(int x) {
		int livingCells = 0;
		if (matrix[x - 1][matrix[0].length - 1] == 1)
			livingCells++;
		if (matrix[x + 1][matrix[0].length - 1] == 1)
			livingCells++;
		if (matrix[x - 1][matrix[0].length - 2] == 1)
			livingCells++;
		if (matrix[x + 1][matrix[0].length - 2] == 1)
			livingCells++;
		if (matrix[x][matrix[0].length - 2] == 1)
			livingCells++;
		return livingCells;
	}

	private int middleField(int x, int y) {
		int livingCells = 0;
		if (matrix[x - 1][y] == 1)
			livingCells++;
		if (matrix[x - 1][y - 1] == 1)
			livingCells++;
		if (matrix[x - 1][y + 1] == 1)
			livingCells++;
		if (matrix[x][y + 1] == 1)
			livingCells++;
		if (matrix[x][y - 1] == 1)
			livingCells++;
		if (matrix[x + 1][y + 1] == 1)
			livingCells++;
		if (matrix[x + 1][y] == 1)
			livingCells++;
		if (matrix[x + 1][y - 1] == 1)
			livingCells++;
		return livingCells;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("size of the board : ");
		int a = s.nextInt();
		int b = s.nextInt();
		Life l = new Life(a, b);
		System.out.println("start configuration (x = -1, for finish) ");
		while (true) {
			System.out.print(" X Y : ");
			int i = s.nextInt();
			if (i == -1)
				break;
			int j = s.nextInt();
			l.set(i, j, 1);
		}
		System.out.print("Number of transactions : ");
		int t = s.nextInt();
		for (int i = 1; i <= t; i++) {
			try {
				l.refresh();
				l.next();
				Thread.sleep(500);
			} catch (Exception e) {
			}
			l.refresh();
		}
	}
}