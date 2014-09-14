import javax.swing.*;
import java.awt.*;
import java.util.*;

public class LifeColoured extends JFrame {
	JPanel[][] cells;
	int[][] matrixColoured;
	int [][] matrixFutur;
	int width, height;

	LifeColoured(int x, int y) {
		JPanel o = new JPanel();
		matrixColoured = new int[x][y];
		matrixFutur = new int [x][y];
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
				if (matrixColoured[i][j] == 0)
					cells[i][j].setBackground(Color.white);
				else
					cells[i][j].setBackground(Color.red);
			}
		this.setVisible(true);
	}

	public void refresh() {
		for (int i = 0; i < width; i++)
			for (int j = 0; j < height; j++) {
				if (matrixColoured[i][j] == 0)
					cells[i][j].setBackground(Color.white);
				else if (matrixColoured[i][j] == 1)
					cells[i][j].setBackground(Color.black);
				else if (matrixColoured[i][j] == 2)
					cells[i][j].setBackground(Color.green);
				else
					cells[i][j].setBackground(Color.red);
			}
	}

	void set(int x, int y, int z) {
		matrixFutur[x][y] = z;
	}

	void copyToColoured(){
		for(int i = 0; i < matrixColoured.length; i++){
			for(int j = 0; j < matrixColoured[0].length; j++){
				matrixColoured[i][j] = matrixFutur[i][j];
			}
		}
	}
	
	void next() {
		copyToColoured();
		int intermMatrix[][] = new int[matrixColoured.length][matrixColoured[0].length];
		if (matrixColoured.length == 0 || matrixColoured[0].length == 0) {
			System.out.println("This is no game board, please behave!");
			return;
		}
		if (matrixColoured.length == 1 && matrixColoured[0].length == 1) {
			System.out
					.println("It's not possible to play a proper game on this game board, please type in something sensible!");
			return;
		}
		if (matrixColoured.length == 1) {
			for (int y = 0; y < matrixColoured[0].length; y++)
				intermMatrix[0][y] = countRowEntry(y);
			copyToMatrix(intermMatrix);
			return;
		}
		if (matrixColoured[0].length == 1) {
			for (int x = 0; x < matrixColoured.length; x++)
				intermMatrix[x][0] = countLineEntry(x);
			copyToMatrix(intermMatrix);
			return;
		}
		System.out.println(matrixColoured.length);
		for (int i = 0; i < matrixColoured.length; i++)
			for (int j = 0; j < matrixColoured[0].length; j++) {
				intermMatrix[i][j] = countAliveCellsAround(i, j);
				System.out.println(" i = " + i + " j = " + j + " neighb = "
						+ intermMatrix[i][j]);
			}
		copyToMatrix(intermMatrix);
		colourMatrix();
	}

	void colourMatrix(){
		for(int i = 0; i< matrixColoured.length; i++)
			for(int j = 0; j< matrixColoured[0].length; j++){
				if(matrixColoured[i][j] != matrixFutur[i][j]){
					if(matrixColoured[i][j] == 1){
						matrixColoured[i][j] = 3;
					} else {
						matrixColoured[i][j] = 2;
					}
				}
			}
	}
	
	public void copyToMatrix(int[][] intermMatrix) {
		for (int i = 0; i < matrixFutur.length; i++)
			for (int j = 0; j < matrixFutur[0].length; j++) {
				if (matrixColoured[i][j] == 1 ) { // alive cell
					if (intermMatrix[i][j] < 2 || intermMatrix[i][j] > 3) {
						matrixFutur[i][j] = 0;
					} else {
						matrixFutur[i][j] = 1;
					}
				} else { // dead cell
					if (intermMatrix[i][j] == 3) {
						matrixFutur[i][j] = 1;
					}
				}
			}
	}

	public int countRowEntry(int y) {
		int numAlive = 0;
		if (y == 0)
			if (matrixColoured[0][1] == 1)
				return 1;
			else
				return 0;
		if (y == matrixColoured[0].length - 1) {
			if (matrixColoured[0][matrixColoured[0].length - 2] == 1) {
				return 1;
			} else
				return 0; // corners
		}
		if (matrixColoured[0][y - 1] == 1) {
			numAlive++;
		}
		if (matrixColoured[0][y + 1] == 1) {
			numAlive++;
		}
		System.out.println(numAlive);
		return numAlive;
	}

	public int countLineEntry(int x) {
		int numAlive = 0;
		if (x == 0)
			if (matrixColoured[1][0] == 1)
				return 1;
			else
				return 0;
		if (x == matrixColoured.length - 1) {
			if ((matrixColoured[matrixColoured.length - 2][0]) == 1) {
				return 1;
			} else
				return 0; // corners
		}
		if (matrixColoured[x - 1][0] == 1) {
			numAlive++;
		}
		if (matrixColoured[x + 1][0] == 1) {
			numAlive++;
		}
		return numAlive;
	}

	public int countAliveCellsAround(int x, int y) {
		if (x == 0) {
			if (y == 0) {
				return countTopLeftCorner();
			}
			if (y == matrixColoured[0].length-1) {
				return countTopRightCorner();
			}
			return topField(y);
		}
		if (y == 0) {
			if (x == matrixColoured.length-1) {
				return countDownLeftCorner();
			}
			return leftField(x);
		}
		if (x == matrixColoured.length-1) {
			if (y == matrixColoured[0].length-1) {
				return countDownRightCorner();
			}
			return downField(y);
		}
		if (y == matrixColoured[0].length-1) {
			return rightField(x);
		}
		return middleField(x, y);
	}

	private int countTopLeftCorner() {
		int livingCells = 0;
		if (matrixColoured[1][0] == 1)
			livingCells++;
		if (matrixColoured[1][1] == 1)
			livingCells++;
		if (matrixColoured[0][1] == 1)
			livingCells++;
		return livingCells;
	}

	private int countTopRightCorner() {
		int livingCells = 0;
		if (matrixColoured[0][matrixColoured[0].length - 2] == 1)
			livingCells++;
		if (matrixColoured[1][matrixColoured[0].length - 2] == 1)
			livingCells++;
		if (matrixColoured[1][matrixColoured[0].length - 1] == 1)
			livingCells++;
		return livingCells;
	}

	private int countDownLeftCorner() {
		int livingCells = 0;
		if (matrixColoured[matrixColoured.length - 2][0] == 1)
			livingCells++;
		if (matrixColoured[matrixColoured.length - 2][1] == 1)
			livingCells++;
		if (matrixColoured[matrixColoured.length - 1][1] == 1)
			livingCells++;
		return livingCells;
	}

	private int countDownRightCorner() {
		int livingCells = 0;
		if (matrixColoured[matrixColoured.length - 2][matrixColoured[0].length - 1] == 1)
			livingCells++;
		if (matrixColoured[matrixColoured.length - 2][matrixColoured[0].length - 2] == 1)
			livingCells++;
		if (matrixColoured[matrixColoured.length - 1][matrixColoured[0].length - 2] == 1)
			livingCells++;
		return livingCells;
	}

	private int leftField(int x) {
		int livingCells = 0;
		if (matrixColoured[x - 1][0] == 1)
			livingCells++;
		if (matrixColoured[x + 1][0] == 1)
			livingCells++;
		if (matrixColoured[x][1] == 1)
			livingCells++;
		if (matrixColoured[x - 1][1] == 1)
			livingCells++;
		if (matrixColoured[x + 1][1] == 1)
			livingCells++;
		return livingCells;
	}

	private int topField(int y) {
		int livingCells = 0;
		if (matrixColoured[0][y - 1] == 1)
			livingCells++;
		if (matrixColoured[0][y + 1] == 1)
			livingCells++;
		if (matrixColoured[1][y - 1] == 1)
			livingCells++;
		if (matrixColoured[1][y + 1] == 1)
			livingCells++;
		if (matrixColoured[1][y] == 1)
			livingCells++;
		return livingCells;
	}

	private int downField(int y) {
		int livingCells = 0;
		if (matrixColoured[matrixColoured.length - 1][y - 1] == 1)
			livingCells++;
		if (matrixColoured[matrixColoured.length - 1][y + 1] == 1)
			livingCells++;
		if (matrixColoured[matrixColoured.length - 2][y] == 1)
			livingCells++;
		if (matrixColoured[matrixColoured.length - 2][y - 1] == 1)
			livingCells++;
		if (matrixColoured[matrixColoured.length - 2][y + 1] == 1)
			livingCells++;
		return livingCells;
	}

	private int rightField(int x) {
		int livingCells = 0;
		if (matrixColoured[x - 1][matrixColoured[0].length - 1] == 1)
			livingCells++;
		if (matrixColoured[x + 1][matrixColoured[0].length - 1] == 1)
			livingCells++;
		if (matrixColoured[x - 1][matrixColoured[0].length - 2] == 1)
			livingCells++;
		if (matrixColoured[x + 1][matrixColoured[0].length - 2] == 1)
			livingCells++;
		if (matrixColoured[x][matrixColoured[0].length - 2] == 1)
			livingCells++;
		return livingCells;
	}

	private int middleField(int x, int y) {
		int livingCells = 0;
		if (matrixColoured[x - 1][y] == 1)
			livingCells++;
		if (matrixColoured[x - 1][y - 1] == 1)
			livingCells++;
		if (matrixColoured[x - 1][y + 1] == 1)
			livingCells++;
		if (matrixColoured[x][y + 1] == 1)
			livingCells++;
		if (matrixColoured[x][y - 1] == 1)
			livingCells++;
		if (matrixColoured[x + 1][y + 1] == 1)
			livingCells++;
		if (matrixColoured[x + 1][y] == 1)
			livingCells++;
		if (matrixColoured[x + 1][y - 1] == 1)
			livingCells++;
		return livingCells;
	}

	public static void main(String[] args) {
		Scanner s = new Scanner(System.in);
		System.out.print("size of the board : ");
		int a = s.nextInt();
		int b = s.nextInt();
		LifeColoured l = new LifeColoured(a, b);
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