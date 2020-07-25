package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class GUI {
	JFrame frame;
	Node[][] map;
	Map canvas;
	JPanel panel = new JPanel();
	int cells = 30;
	int psx = -1;
	int psy = -1;
	int pfx= -1;
	int pfy= -1;
	//Botones
	String[] pinceles = {"Nodo inicial","Nodo final","Borrador","Muro"};
	
	//Constantes
	private int WIDTH = 700;
	private int HEIGHT = 630;
	int MSIZE = 600;
	int CSIZE = MSIZE/cells;
	
	
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
	
	public void createEmptyMap(){	
		map = new Node[cells][cells];
		for(int i = 0 ; i < cells; i++) {
			for(int j = 0 ; j < cells; j++) {
				map[i][j] = new Node(3,i,j);
			}
		}
		
	}
	
	public void enterPositions() throws Exception {
		JPanel valuePanel = new JPanel();
		valuePanel.setLayout(new GridLayout(0, 2, 8, 8));
		valuePanel.add(new JLabel("Ingrese la posicion del nodo inicial"));
		valuePanel.add(new JLabel(""));

		valuePanel.add(new JLabel("X (entre 0 y "+cells+"): "));
		JTextField sx = new JTextField("");
		valuePanel.add(sx);
		valuePanel.add(new JLabel("Y (entre 0 y "+cells+"): "));
		JTextField sy = new JTextField("");
		valuePanel.add(sy);
		
		valuePanel.add(new JLabel("Ingrese la posicion del nodo final"));
		valuePanel.add(new JLabel(""));
		valuePanel.add(new JLabel("X (entre 0 y "+cells+"): "));
		JTextField fx = new JTextField("");
		valuePanel.add(fx);
		valuePanel.add(new JLabel("Y (entre 0 y "+cells+"): "));
		JTextField fy = new JTextField("");
		valuePanel.add(fy);
		
		int result = JOptionPane.showConfirmDialog(null, valuePanel, " ", JOptionPane.OK_CANCEL_OPTION,
				JOptionPane.PLAIN_MESSAGE);
		
		psx = Integer.parseInt(sx.getText());
		psy = Integer.parseInt(sy.getText());
		
		pfx = Integer.parseInt(fx.getText());
		pfy = Integer.parseInt(fy.getText());
		
		if(psx>=cells || psy>=cells || pfx>=cells || pfy>=cells) throw new Exception("ERROR: Por lo menos uno de los valores ingresados excede la capacidad maxima");
		if(psx<0 || psy<0 || pfx<0 || pfy<0) throw new Exception("ERROR: Por lo menos uno de los valores ingresados es menor a la posicion minima (0)");
		if(psx==pfx && psy==pfy) throw new Exception("ERROR: Los puntos no pueden tener las mismas coordenadas");

		
		map[psx][psy].setCellType(1);
		map[pfx][pfy].setCellType(2);
	}
	
	public void initialize() throws Exception {

		enterPositions();
		frame = new JFrame();
		frame.setVisible(true);
		frame.setResizable(false);
		frame.setSize(WIDTH, HEIGHT);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton c = new JButton("Holaaaa");
		c.setBounds(WIDTH-80, 0, 10, 10);
		panel.add(c);
		
		
		frame.getContentPane().add(panel);
		makeGrid();
	}
	
	public void makeGrid() {
		canvas = new Map();
		canvas.setBounds(0,0, MSIZE+1, MSIZE+1);
		frame.getContentPane().add(canvas);
	}
	
	class Node{
		//1-Start cell, 2-Finish cell, 3-Empty cell, 4-Wall cell, 5-Visited cell, 6-Path Cell
		private int cellType;
		private int x;
		private int y;
		private boolean visited=false;
		
		public Node(int cellType, int x, int y) {
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

		public int getCellType() {
			return cellType;
		}
		public void setCellType(int cellType) {
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
		
		
	}
	
	class Map extends JPanel implements MouseListener, MouseMotionListener{

		public Map() {
			addMouseListener(this);
			addMouseMotionListener(this);
		}
		public void paintComponent(Graphics g) {	
			super.paintComponent(g);
			for(int x = 0; x < cells; x++) {
				for(int y = 0; y < cells; y++) {
					switch (map[x][y].getCellType()) {
					case 1:
						g.setColor(Color.MAGENTA);break;
					case 2:
						g.setColor(Color.RED);break;
					case 3:
						g.setColor(Color.WHITE);break;
					case 4:
						g.setColor(Color.BLACK);break;
					case 5:
						g.setColor(Color.CYAN);break;
					case 6:
						g.setColor(Color.YELLOW);break;
					}
					g.fillRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
					g.setColor(Color.BLACK);
					g.drawRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
				}
			}
		}
		
		public void paintWalls(MouseEvent arg0) {
			int x = arg0.getX()/CSIZE;
			int y = arg0.getY()/CSIZE;
			if(!((x==psx && y==psy) || (x==pfx && y==pfy))) {
				if(map[x][y].getCellType()==3) {
					map[x][y].setCellType(4);	
				}
//				else if(map[x][y].getCellType()==4) {
//					map[x][y].setCellType(3);	
//				}
			}
			SwingUtilities.invokeLater(()->canvas.repaint());
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			new Thread(()->paintWalls(arg0)).start();
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
//			int x = arg0.getX()/CSIZE;
//			int y = arg0.getY()/CSIZE;
//			if(!((x==psx && y==psy) || (x==pfx && y==pfy))) {
//				if(map[x][y].getCellType()==3) {
//					map[x][y].setCellType(4);
//				}
//				else if(map[x][y].getCellType()==4) {
//					map[x][y].setCellType(3);
//				}
//			}
			Algorithms a = new Algorithms();
		    new Thread(()->a.BFS(psx,psy)).start();
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
		
	class Algorithms{

		public Algorithms() {};
				
		public boolean DFS(int row, int col) {
			
			ArrayList<GUI.Node> neighbours = getNeighbours(row, col);
			
			for(GUI.Node n : neighbours) {
				if(n.getCellType()==2) {
					map[row][col].setCellType(6);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SwingUtilities.invokeLater(()->canvas.repaint());
					return true;
				}
				if(!n.isVisited() && n.getCellType()==3) {
					n.setCellType(5);
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					SwingUtilities.invokeLater(()->canvas.repaint());
					if(DFS(n.getX(),n.getY())) {
						
						if(map[row][col].getCellType()==5) map[row][col].setCellType(6);
						try {
							Thread.sleep(10);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						SwingUtilities.invokeLater(()->canvas.repaint());
						return true;
					};
				}
			}
			return false;
		}
		
		public void BFS(int row, int col) {
//			ArrayList<GUI.Node> neighbours = getNeighbours(row, col);
//			
//			for(GUI.Node n : neighbours) {
//				for(GUI.Node n2 : getNeighbours(n.getX(), n.getY())) {
//					if()
//					SwingUtilities.invokeLater(()->canvas.repaint());
//					BFS(n2.getX(),n2.getY());
//				}
//			}
//		
		}
		
		public ArrayList<GUI.Node> getNeighbours(int row, int col){
			ArrayList<GUI.Node> neighbours = new ArrayList<GUI.Node>();

			if(row-1>=0) {
				neighbours.add(map[row-1][col]);
			}
			if(col-1>=0) {
				neighbours.add(map[row][col-1]);
			}
			if(row+1<cells) {
				neighbours.add(map[row+1][col]);
			}
			if(col+1<cells) {
				neighbours.add(map[row][col+1]);
			}
			
			return neighbours;
		}
		
	}
}
