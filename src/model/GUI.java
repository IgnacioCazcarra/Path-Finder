package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.ImageObserver;
import java.text.AttributedCharacterIterator;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class GUI {
	JFrame frame;
	Node[][] map;
	Map canvas;
	JPanel panel = new JPanel();
	int cells = 30;
	int startx = -1;
	int starty = -1;
	int finishx = -1;
	int finishy = -1;
	
	//Constantes
	private int WIDTH = 607;
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
		
		int psx = Integer.parseInt(sx.getText());
		int psy = Integer.parseInt(sy.getText());
		
		int pfx = Integer.parseInt(fx.getText());
		int pfy = Integer.parseInt(fy.getText());
		
		if(psx>=cells || psy>=cells || pfx>=cells || pfy>=cells) throw new Exception("ERROR: Por lo menos uno de los valores ingresados excede la capacidad maxima");
		if(psx<0 || psy<0 || pfx<0 || pfy<0) throw new Exception("ERROR: Por lo menos uno de los valores ingresados es menor a la posicion minima (0)");
		if(psx==psy && pfx==pfy) throw new Exception("ERROR: Los puntos no pueden tener las mismas coordenadas");

		
		map[psx][psx].setCellType(1);
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
		frame.getContentPane().add(panel);
		
		canvas = new Map();
		canvas.setBounds(0,0, MSIZE+1, MSIZE+1);
		frame.getContentPane().add(canvas);
	}
	
	
	class Node{
		//1-Start cell, 2-Finish cell, 3-Empty cell, 4-Wall cell
		private int cellType;
		private int x;
		private int y;
		
		public Node(int cellType, int x, int y) {
			super();
			this.cellType = cellType;
			this.x = x;
			this.y = y;
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
		public void paintComponent(Graphics g) {	//REPAINT
			super.paintComponent(g);
			for(int x = 0; x < cells; x++) {	//PAINT EACH NODE IN THE GRID
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
					}
					g.fillRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
					g.setColor(Color.BLACK);
					g.drawRect(x*CSIZE,y*CSIZE,CSIZE,CSIZE);
				}
			}
		}
		
		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			// TODO Auto-generated method stub
			
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
}
