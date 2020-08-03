package model;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class GUI {

	JFrame frame;
	Node[][] map;
	Map canvas;
	JPanel panel = new JPanel();

	int cells = 30;

	int startx = 0;
	int starty = 0;
	int targetx = cells-1;
	int targety = cells-1;
	boolean solving = false;

	JLabel toolsLabel = new JLabel("<html>Choose the type of node you want to draw</html>");
	JLabel algorithmLabel = new JLabel("<html>Choose the algorithm you want to visualize</html>");
	JLabel controlsLabel = new JLabel("<html>Controls</html>", SwingConstants.CENTER);
	JLabel warning = new JLabel("<html>Wait after the path is drawn and press 'Clear' to continue</html>", SwingConstants.CENTER);
	
	String[] pinceles = { "Wall", "Eraser","Starting node", "Target node"};
	String[] algoritmos = { "Dijkstra's algorithm", "A* algorithm","Depth-First Search", "Breadth-First Search"};
	JComboBox tools = new JComboBox(pinceles);
	JComboBox algo = new JComboBox(algoritmos);

	JButton startSearch = new JButton("Start");
	JButton clearMap = new JButton("Clear");

	// Enum
	enum Cells {
		START, TARGET, EMPTY, WALL, VISITED, PATH
	}

	// Constantes
	private int WIDTH = 825;
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

	public void resetVariables() {
		 startx = 0;
		 starty = 0;
		 targetx = 29;
		 targety = 29;
		 solving=false;
	}
	
	public void createEmptyMap() {
		map = new Node[cells][cells];
		for (int i = 0; i < cells; i++) {
			for (int j = 0; j < cells; j++) {
				map[i][j] = new Node(Cells.EMPTY, i, j);
			}
		}
		resetVariables();
		map[startx][starty].setCellType(Cells.START);
		map[targetx][targety].setCellType(Cells.TARGET);

	}

	public void initialize() throws Exception {

		map[startx][starty].setCellType(Cells.START);
		map[targetx][targety].setCellType(Cells.TARGET);

		frame = new JFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		int space = 25;
		controlsLabel.setBounds(610, 10, 200, 25);
		controlsLabel.setFont(controlsLabel.getFont().deriveFont(20.0f));

		startSearch.setBounds(610, 40, 200, 25);
		space += 30;
		clearMap.setBounds(610, 25 + space, 200, 25);
		space += 50;
		algorithmLabel.setBounds(610, 25 + space, 200, 25);
		space += 35;
		algo.setBounds(610, 25 + space, 200, 25);
		space += 50;
		toolsLabel.setBounds(610, 25 + space, 200, 25);
		space += 35;
		tools.setBounds(610, 25 + space, 200, 25);
		space +=50;
		warning.setBounds(610, 30+space, 200, 30);
		warning.setVisible(false);
		warning.setForeground(Color.BLUE);
		startSearch.addActionListener(new ActionListener() {
		
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Algorithms a = new Algorithms();
				startSearch.setEnabled(false);
				tools.setEnabled(false);
				algo.setEnabled(false);
				try {
					switch (algo.getSelectedItem().toString()) {
						case "Depth-First Search": {
							solving = true;
							new Thread(() -> a.DFS(startx, starty)).start();
							break;
						}
						case "Breadth-First Search": {
							solving = true;
							new Thread(() -> a.BFS(startx, starty)).start();
							break;
						}
						case "Dijkstra's algorithm": {
							solving = true;
							new Thread(() -> a.Dijkstra(startx, starty)).start();
							break;
						}
						case "A* algorithm": {
							solving = true;
							new Thread(() -> a.AStar(startx, starty)).start();
							break;
						}
					}
					
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				warning.setVisible(true);
			}
		});

		clearMap.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (!solving) {
					createEmptyMap();
					canvas.repaint();
					startSearch.setEnabled(true);
					tools.setEnabled(true);
					algo.setEnabled(true);
					warning.setVisible(false);
				}
			}
		});

		frame.getContentPane().add(controlsLabel);
		frame.getContentPane().add(toolsLabel);
		frame.getContentPane().add(tools);
		frame.getContentPane().add(startSearch);
		frame.getContentPane().add(clearMap);
		frame.getContentPane().add(algorithmLabel);
		frame.getContentPane().add(algo);
		frame.getContentPane().add(warning);

		
		
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
		private int g;
		private int h;

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

		public int getG() {
			return g;
		}

		public void setG(int g) {
			this.g = g;
		}

		public int getH() {
			return h;
		}

		public void setH(int h) {
			this.h = h;
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
			return findShortestPath().size() - 1;
		}

		public int h() {
			return Math.abs((this.getX() - targetx)) + Math.abs((this.getY() - targety));
		}

		public List<Node> getNeighbours() {

			List<Node> neighbours = new ArrayList<Node>();
			int row = this.getX();
			int col = this.getY();

			if (row - 1 >= 0 && validateType(row - 1, col)) {
				neighbours.add(map[row - 1][col]);
			}
			if (col - 1 >= 0 && validateType(row, col - 1)) {
				neighbours.add(map[row][col - 1]);
			}
			if (row + 1 < cells && validateType(row + 1, col)) {
				neighbours.add(map[row + 1][col]);
			}
			if (col + 1 < cells && validateType(row, col + 1)) {
				neighbours.add(map[row][col + 1]);
			}
			return neighbours;
		}

		public boolean validateType(int row, int col) {
			boolean qualified = false;
			if(map[row][col].getCellType().equals(Cells.EMPTY) || map[row][col].getCellType().equals(Cells.TARGET)) {
				qualified = true;
			}
			
			return qualified;
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
				if (n.getPrev() == null)
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
			Color color = null;
			for (int x = 0; x < cells; x++) {
				for (int y = 0; y < cells; y++) {
					switch (map[x][y].getCellType()) {
					case START:
						color = Color.MAGENTA;
						break;
					case TARGET:
						color = Color.RED;
						break;
					case EMPTY:
						color = Color.WHITE;
						break;
					case WALL:
						color = Color.BLACK;
						break;
					case VISITED:
						color = Color.CYAN;
						break;
					case PATH:
						color = Color.YELLOW;
						break;
					}
					g.setColor(color);
					g.fillRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
					g.setColor(Color.BLACK);
					g.drawRect(x * CSIZE, y * CSIZE, CSIZE, CSIZE);
				}
			}
		}

		public void chooseTool(MouseEvent arg0) {

			String option = tools.getSelectedItem().toString();

			int x = arg0.getX() / CSIZE;
			int y = arg0.getY() / CSIZE;

			switch (option) {
			case "Starting node":
				paintPrincipalNode(x, y, option);
				break;
			case "Target node":
				paintPrincipalNode(x, y, option);
				break;
			case "Eraser":
				paintOrErase(x, y, Cells.WALL, Cells.EMPTY);
				break;
			case "Wall":
				paintOrErase(x, y, Cells.EMPTY, Cells.WALL);
				break;
			}
			SwingUtilities.invokeLater(() -> canvas.repaint());

		}
		
		public void paintOrErase(int x, int y, Cells checkIfNotThis, Cells setThisType) {
			if (qualified(x) && qualified(y) && map[x][y].getCellType().equals(checkIfNotThis)) {
				if (!((x == startx && y == starty) || (x == targetx && y == targety))) {
					map[x][y].setCellType(setThisType);
				}
			}
		}

		public void paintPrincipalNode(int x, int y, String node) {
			
				if (qualified(x) && qualified(y)) {
					
					if(map[x][y].getCellType().equals(Cells.EMPTY)) {
						
						if(node.equalsIgnoreCase("Starting node")) {
							map[startx][starty].setCellType(Cells.EMPTY);
							startx = x;
							starty = y;
							map[startx][starty].setCellType(Cells.START);
						}
						else {
							map[targetx][targety].setCellType(Cells.EMPTY);
							targetx = x;
							targety = y;	
							map[targetx][targety].setCellType(Cells.TARGET);
						}
					}
					
				}
			
			SwingUtilities.invokeLater(() -> canvas.repaint());
		}

		public boolean qualified(int position) {
			boolean ok = true;
			if(position>=cells) {
				ok = false;
			}
			if(position<0) {
				ok = false;
			}
			return ok;
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
			new Thread(() -> chooseTool(arg0)).start();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			new Thread(() -> chooseTool(arg0)).start();
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
		}

		public List<Node> DFS(int row, int col) {
			List<Node> neighbours = map[row][col].getNeighbours();

			for (Node n : neighbours) {
				// This if statement avoids keep visiting nodes after having found a path
				if (!map[targetx][targety].isVisited()) {

					if (n.getCellType().equals(Cells.TARGET)) {
						n.setVisited(true);
						return map[row][col].findShortestPath();
					} else if (!n.isVisited() && n.getCellType().equals(Cells.EMPTY)) {
						n.setPrev(map[row][col]);
						n.setCellType(Cells.VISITED);
						canvas.paintWithDelay(10);

						List<Node> spath = null;
						spath = DFS(n.getX(), n.getY());
						if (spath != null && !spath.isEmpty()) {
							paintPath(spath);
							solving=false;
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
				} else if (current.getCellType().equals(Cells.TARGET)) {
					found = true;
					paintPath(current.findShortestPath());
					q.clear();
					solving=false;
				}
			}
		}

		public void AStar(int row, int col) {
			Set<Node> open = new LinkedHashSet<Node>();
			List<Node> closed = new ArrayList<Node>();
			initializeDistances();

			map[row][col].setG(map[row][col].g());
			map[row][col].setH(map[row][col].h());
			open.add(map[row][col]);

			while (!map[targetx][targety].isVisited() && open.size() > 0) {

				Node min = minF(open);
				closed.add(min);
				open.remove(min);

				// All nodes in the open list are walkable so we only need to take care of not
				// treating the start node as an empty one.
				if (!min.getCellType().equals(Cells.START)) {
					min.setVisited(true);
					min.setCellType(Cells.VISITED);
					canvas.paintWithDelay(10);
				}

				List<Node> currentNeighbours = min.getNeighbours();
				// Set prev for only those who don't have a previous one
				min.setNeighboursPrev();

				for (Node n : currentNeighbours) {

					if (n.getCellType().equals(Cells.TARGET)) {
						n.setVisited(true);
						paintPath(n.findShortestPath());
						solving=false;
						break;
					} // Neighbours will always be walkable so we only check if its the target node
					else if (!closed.contains(n)) {

						if (!open.contains(n)) {
							n.setG(n.g());
							n.setH(n.h());
							open.add(n);
						} else {
							// Check if a new shorter path is found for the node
							Node auxPrev = n.getPrev();
							int auxG = n.getG();
							n.setPrev(min);
							n.setG(n.g());
							if (n.getG() >= auxG) {
								n.setPrev(auxPrev);
								n.setG(auxG);
							}
						}
					}
				}
			}
		}

		public void Dijkstra(int row, int col) {
			List<Node> l = new ArrayList<Node>();
			initializeDistances();
			map[row][col].setG(0);
			l.add(map[row][col]);
			Node current = l.get(0);

			while (!l.isEmpty() && !map[targetx][targety].isVisited()) {

				for (Node n : current.getNeighbours()) {

					if (n.getCellType().equals(Cells.EMPTY) && !l.contains(n)) {
						n.setPrev(current);
						n.setG(n.g());
						l.add(n);
						n.setCellType(Cells.VISITED);
						canvas.paintWithDelay(5);
					} else if (n.getCellType().equals(Cells.EMPTY) && l.contains(n)) {

						Node auxCurrent = n.getPrev();
						int auxG = n.getG();
						n.setPrev(current);
						n.setG(n.g());

						if (n.getG() > auxG) {
							n.setPrev(auxCurrent);
							n.setG(auxG);
						} else {
							l.remove(n);
							l.add(n);
						}
					} else if (n.getCellType().equals(Cells.TARGET)) {
						n.setVisited(true);
						n.setPrev(current);
						paintPath(n.findShortestPath());
						solving=false;
						break;
					}

				}
				l.remove(current);
				current = nextPromisingNode(l);
			}

		}

		public Node nextPromisingNode(List<Node> lista) {
			if (lista.isEmpty())
				return null;

			Node nextp = lista.get(0);

			for (int i = 0; i < lista.size(); i++) {
				if (nextp.getG() > lista.get(i).getG()) {
					nextp = lista.get(i);
				}
			}

			return nextp;
		}

		public void paintPath(List<Node> path) {
			Collections.reverse(path);
			for (Node n : path) {
				if (n.getCellType().equals(Cells.VISITED)) {
					n.setCellType(Cells.PATH);
				}
				canvas.paintWithDelay(15);
			}
		}

		public Node minF(Set<Node> open) {
			int i = 0;
			Node min = null;
			for (Node n : open) {
				if (i == 0) {
					min = n;
				}

				if (min.f() >= n.f()) {
					min = n;
				}
				i++;
			}
			return min;
		}

		public void initializeDistances() {
			for (int i = 0; i < cells; i++) {
				for (int j = 0; j < cells; j++) {
					map[i][j].setG(Integer.MAX_VALUE);
					map[i][j].setH(Integer.MAX_VALUE);
				}
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