package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridLayout;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Collections;

import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

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
	int startx = -1;
	int starty = -1;
	int finalx = -1;
	int finaly = -1;
	// Botones
	String[] pinceles = { "Nodo inicial", "Nodo final", "Borrador", "Muro" };

	// Enum
	enum Cells {
		START, FINISH, EMPTY, WALL, VISITED, PATH
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

		valuePanel.add(new JLabel("X (entre 1 y " + (cells - 1) + "): "));
		JTextField sx = new JTextField("");
		valuePanel.add(sx);
		valuePanel.add(new JLabel("Y (entre 1 y " + (cells - 1) + "): "));
		JTextField sy = new JTextField("");
		valuePanel.add(sy);

		valuePanel.add(new JLabel("Ingrese la posicion del nodo final"));
		valuePanel.add(new JLabel(""));
		valuePanel.add(new JLabel("X (entre 1 y " + (cells - 1) + "): "));
		JTextField fx = new JTextField("");
		valuePanel.add(fx);
		valuePanel.add(new JLabel("Y (entre 1 y " + (cells - 1) + "): "));
		JTextField fy = new JTextField("");
		valuePanel.add(fy);

		int result = JOptionPane.showConfirmDialog(null, valuePanel, " ", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);

		if (result == JOptionPane.OK_CANCEL_OPTION)
			throw new Exception("Operacion cancelada");

		startx = Integer.parseInt(sx.getText());
		starty = Integer.parseInt(sy.getText());

		finalx = Integer.parseInt(fx.getText());
		finaly = Integer.parseInt(fy.getText());

		if (startx >= cells || starty >= cells || finalx >= cells || finaly >= cells)
			throw new Exception("ERROR: Por lo menos uno de los valores ingresados excede la capacidad maxima");
		if (startx < 0 || starty < 0 || finalx < 0 || finaly < 0)
			throw new Exception("ERROR: Por lo menos uno de los valores ingresados es menor a la posicion minima (0)");
		if (startx == finalx && starty == finaly)
			throw new Exception("ERROR: Los puntos no pueden tener las mismas coordenadas");

		map[startx][starty].setCellType(Cells.START);
		map[finalx][finaly].setCellType(Cells.FINISH);
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
		private Node prev;

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

		public Node getPrev() {
			return prev;
		}

		public void setPrev(Node prev) {
			this.prev = prev;
		}

		@Override
		public boolean equals(Object obj) {
			Node other = (Node) obj;
			if (cellType != other.cellType)
				return false;
			return true;
		}

		public int f() {
			return g() + h();
		}

		public int g() {
			return findShortestPath().size()-1;
		}

		public int h() {
			return Math.abs((this.getX() - finalx)) + Math.abs((this.getY() - finaly));
		}
		
		public List<Node> getNeighbours() {
		
			List<Node> neighbours = new ArrayList<Node>();
			int row = this.getX(); 
			int col = this.getY();
			
			if (row - 1 >= 0 && (map[row - 1][col].getCellType().equals(Cells.EMPTY)
					|| map[row - 1][col].getCellType().equals(Cells.FINISH))) {
				neighbours.add(map[row - 1][col]);
			}
			if (col - 1 >= 0 && (map[row][col - 1].getCellType().equals(Cells.EMPTY)
					|| map[row][col - 1].getCellType().equals(Cells.FINISH))) {
				neighbours.add(map[row][col - 1]);
			}
			if (row + 1 < cells && (map[row + 1][col].getCellType().equals(Cells.EMPTY)
					|| map[row + 1][col].getCellType().equals(Cells.FINISH))) {
				neighbours.add(map[row + 1][col]);
			}
			if (col + 1 < cells && (map[row][col + 1].getCellType().equals(Cells.EMPTY)
					|| map[row][col + 1].getCellType().equals(Cells.FINISH))) {
				neighbours.add(map[row][col + 1]);
			}
			return neighbours;
		}
		
		public List<Node> findShortestPath() {
			List<Node> lista = new ArrayList<Node>();
			Node current = this;
			
			lista.add(current);
			while (!current.getCellType().equals(Cells.START)) {
				current = current.prev;
				lista.add(current);
			}
			return lista;
		}

		public void setNeighboursPrev() {
			for (Node n : getNeighbours()) {
				n.setPrev(this);
			}
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
			if (!((x == startx && y == starty) || (x == finalx && y == finaly))) {
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
				e.printStackTrace();
			}
			SwingUtilities.invokeLater(() -> canvas.repaint());
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			new Thread(() -> paintWalls(arg0)).start();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			Algorithms a = new Algorithms();

			try {
				new Thread(() -> a.DFS(startx, starty)).start();
			} catch (Exception e) {
				e.printStackTrace();
			}
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

		public Algorithms() {}

		public List<Node> DFS(int row, int col) {
			List<Node> neighbours = map[row][col].getNeighbours();

			for (Node n : neighbours) {
				if (!map[finalx][finaly].isVisited()) {
					if (n.getCellType().equals(Cells.FINISH)) {
						n.setVisited(true);
						return map[row][col].findShortestPath();
					}
					if (!n.isVisited() && n.getCellType().equals(Cells.EMPTY)) {
						n.setPrev(map[row][col]);
						n.setCellType(Cells.VISITED);
						canvas.paintWithDelay(10);

						List<Node> spath = null;
						spath = DFS(n.getX(), n.getY());
						if (spath != null && !spath.isEmpty()) {
							paintPath(spath);
							break;
						}
					}
				}
			}
			return null;
		}

		public void BFS(int row, int col) {
			Queue<Node> q = new LinkedList<Node>();

			q.add(map[row][col]);
			boolean found = false;

			while (!found && !q.isEmpty()) {
				Node current = q.remove();

				List<Node> s = current.getNeighbours();
				q.addAll(s);
				
				q = removeDuplicates(q);
				
				current.setNeighboursPrev();
				
				if (!current.isVisited() && current.getCellType().equals(Cells.EMPTY)) {
					current.setCellType(Cells.VISITED);
					current.setVisited(true);
					canvas.paintWithDelay(5);
				} else if (current.getCellType().equals(Cells.FINISH)) {
					found = true;
					paintPath(current.findShortestPath());
					q.clear();
				}
			}
		}

		public void paintPath(List<Node> path) {
			Collections.reverse(path);
			for (Node n : path) {
				if (n.getCellType().equals(Cells.VISITED))
					n.setCellType(Cells.PATH);
				canvas.paintWithDelay(15);
			}
		}

		public Queue<Node> removeDuplicates(Queue<Node> q) {
			Set<Node> aux = new LinkedHashSet<Node>(q);
			q.clear();
			q.addAll(aux);
			return q;
		}

	}
}
