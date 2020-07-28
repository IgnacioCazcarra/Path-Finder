package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;


public class GUI {
	JFrame frame;
	Node[][] map;
	Map canvas;
	JPanel panel = new JPanel();
	int cells = 30;
	int psx = -1;
	int psy = -1;
	int pfx = -1;
	int pfy = -1;
	// Botones
	String[] pinceles = { "Nodo inicial", "Nodo final", "Borrador", "Muro" };
	//Enum
	enum Cells{
		START,FINISH,EMPTY,WALL,VISITED,PATH
	}
	// Constantes
	private int WIDTH = 700;
	private int HEIGHT = 630;
	int MSIZE = 600;
	int CSIZE = MSIZE / cells;

	public static void main(String[] args) {
		new GUI();
	}

	public GUI() {
		try {
			createEmptyMap();
			initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void createEmptyMap() {
		map = new Node[cells][cells];
		for (int i = 0; i < cells; i++) {
			for (int j = 0; j < cells; j++) {
				map[i][j] = new Node(Cells.EMPTY, i, j);
			}
		}

	}

	public void enterPositions() throws Exception {
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new GridLayout(0, 2, 8, 8));
		valuePanel.add(new JLabel("Ingrese la posicion del nodo inicial"));
		valuePanel.add(new JLabel(""));

		valuePanel.add(new JLabel("X (entre 0 y " + cells + "): "));
		JTextField sx = new JTextField("");
		valuePanel.add(sx);
		valuePanel.add(new JLabel("Y (entre 0 y " + cells + "): "));
		JTextField sy = new JTextField("");
		valuePanel.add(sy);

		valuePanel.add(new JLabel("Ingrese la posicion del nodo final"));
		valuePanel.add(new JLabel(""));
		valuePanel.add(new JLabel("X (entre 0 y " + cells + "): "));
		JTextField fx = new JTextField("");
		valuePanel.add(fx);
		valuePanel.add(new JLabel("Y (entre 0 y " + cells + "): "));
		JTextField fy = new JTextField("");
		valuePanel.add(fy);

		int result = JOptionPane.showConfirmDialog(null, valuePanel, " ", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if(result==JOptionPane.OK_CANCEL_OPTION) throw new Exception("Operacion cancelada");
		
		psx = Integer.parseInt(sx.getText());
		psy = Integer.parseInt(sy.getText());

		pfx = Integer.parseInt(fx.getText());
		pfy = Integer.parseInt(fy.getText());

		if (psx >= cells || psy >= cells || pfx >= cells || pfy >= cells)
			throw new Exception("ERROR: Por lo menos uno de los valores ingresados excede la capacidad maxima");
		if (psx < 0 || psy < 0 || pfx < 0 || pfy < 0)
			throw new Exception("ERROR: Por lo menos uno de los valores ingresados es menor a la posicion minima (0)");
		if (psx == pfx && psy == pfy)
			throw new Exception("ERROR: Los puntos no pueden tener las mismas coordenadas");

		map[psx][psy].setCellType(Cells.START);
		map[pfx][pfy].setCellType(Cells.FINISH);
	}

	public void initialize() throws Exception {

		enterPositions();
		frame = new JFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.getContentPane().add(panel);
		makeGrid();
	}

	public void makeGrid() {
		canvas = new Map();
		canvas.setBounds(0, 0, MSIZE + 1, MSIZE + 1);
		frame.getContentPane().add(canvas);
	}

	class Node {
		
		private Cells cellType;
		private int x;
		private int y;
		private boolean visited = false;

		public Node(Cells cellType, int x, int y) {
			super();
			this.cellType = cellType;
			this.x = x;
			this.y = y;
		}

		public boolean isVisited() {
			return visited;
		}

		public void setVisited(boolean visited) {
			this.visited = visited;
		}

		public Cells getCellType() {
			return cellType;
		}

		public void setCellType(Cells cellType) {
			this.cellType = cellType;
		}

		public int getX() {
			return x;
		}

		public void setX(int x) {
			this.x = x;
		}

		public int getY() {
			return y;
		}

		public void setY(int y) {
			this.y = y;
		}

		@Override
		public boolean equals(Object obj) {
			Node other = (Node) obj;
			if (cellType != other.cellType)
				return false;
			return true;
		}
	}

	class Map extends JPanel implements MouseListener, MouseMotionListener {

		public Map() {
			addMouseListener(this);
			addMouseMotionListener(this);
		}

		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			for (int x = 0; x < cells; x++) {
				for (int y = 0; y < cells; y++) {
					switch (map[x][y].getCellType()) {
					case START:
						g.setColor(Color.MAGENTA);
						break;
					case FINISH:
						g.setColor(Color.RED);
						break;
					case EMPTY:
						g.setColor(Color.WHITE);
						break;
					case WALL:
						g.setColor(Color.BLACK);
						break;
					case VISITED:
						g.setColor(Color.CYAN);
						break;
					case PATH:
						g.setColor(Color.YELLOW);
						break;
					}
					g.fillRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
					g.setColor(Color.BLACK);
					g.drawRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
				}
			}
		}

		public void paintWalls(MouseEvent arg0) {
			int x = arg0.getX() / CSIZE;
			int y = arg0.getY() / CSIZE;
			if (!((x == psx && y == psy) || (x == pfx && y == pfy))) {
				if (map[x][y].getCellType().equals(Cells.EMPTY)) {
					map[x][y].setCellType(Cells.WALL);
				}
			}
			SwingUtilities.invokeLater(() -> canvas.repaint());
		}

		public void paintWithDelay(int ms) {
			try {
				Thread.sleep(ms);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			SwingUtilities.invokeLater(() -> canvas.repaint());
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			new Thread(() -> paintWalls(arg0)).start();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			Algorithms a = new Algorithms();
			new Thread(() -> a.BFS(psx, psy)).start();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mousePressed(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

	}

	class Algorithms {

		public Algorithms() {
		};

		public boolean DFS(int row, int col) {

			Set<Node> neighbours = getNeighbours(row, col);

			for (Node n : neighbours) {
				if (n.getCellType().equals(Cells.FINISH)) {
					map[row][col].setCellType(Cells.PATH);
					canvas.paintWithDelay(10);
					return true;
				}
				if (!n.isVisited() && n.getCellType().equals(Cells.EMPTY)) {
					n.setCellType(Cells.VISITED);
					canvas.paintWithDelay(10);
					if (DFS(n.getX(), n.getY())) {
						if (map[row][col].getCellType().equals(Cells.VISITED)) {
							map[row][col].setCellType(Cells.PATH);
						}
						canvas.paintWithDelay(10);
						return true;
					}
				}
			}
			return false;
		}

		public void BFS(int row, int col) {
			Queue<Node> q = new LinkedList<Node>();
			
			q.add(map[row][col]);
			boolean found = false;

			while (!found) {
				Node current = q.remove();

				q.addAll(getNeighbours(current.getX(), current.getY()));
				System.out.println(q.size());
				Set<Node> aux = new LinkedHashSet<Node>(q);
				q.clear();
				q.addAll(aux);
				
				if (!current.isVisited() && current.getCellType().equals(Cells.EMPTY)) {
					current.setCellType(Cells.VISITED);
					current.setVisited(true);
					canvas.paintWithDelay(5);
				} else if (current.getCellType().equals(Cells.FINISH)) {
					found = true;
					q.clear();
				}
			}
		}
		
		public Set<Node> getNeighbours(int row, int col) {
			Set<Node> neighbours = new HashSet<Node>();

			if (row - 1 >= 0 && (map[row - 1][col].getCellType().equals(Cells.EMPTY) || map[row - 1][col].getCellType().equals(Cells.FINISH))) {
				neighbours.add(map[row - 1][col]);	
			}
			if (col - 1 >= 0 && (map[row][col - 1].getCellType().equals(Cells.EMPTY) || map[row][col - 1].getCellType().equals(Cells.FINISH))) {
				neighbours.add(map[row][col - 1]);
			}
			if (row + 1 < cells && (map[row + 1][col].getCellType().equals(Cells.EMPTY) || map[row + 1][col].getCellType().equals(Cells.FINISH))) {
				neighbours.add(map[row + 1][col]);
			}
			if (col + 1 < cells && (map[row][col + 1].getCellType().equals(Cells.EMPTY) || map[row][col + 1].getCellType().equals(Cells.FINISH))) {
				neighbours.add(map[row][col + 1]);
			}
			return neighbours;
		}

	}
}
