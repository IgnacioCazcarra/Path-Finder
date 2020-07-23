package model;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
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
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;

public class GUI {
	JFrame frame;
	Node[][] map;
	int cells = 30;
	JPanel panel = new JPanel();
	private int WIDTH = 607;
	private int HEIGHT = 630;
	Map canvas;
	Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
	int MSIZE = 600;
	int CSIZE = MSIZE/cells;

	
	public static void main(String[] args) {
		new GUI();
	}
	
	public GUI() {
		createEmptyMap();
		initialize();
	}
	
	public void createEmptyMap() {
		map = new Node[cells][cells];
		
		for(int i = 0 ; i < cells; i++) {
			for(int j = 0 ; j < cells; j++) {
				map[i][j] = new Node(3,i,j);
			}
		}
		
				
	}
	
	public void initialize() {
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

					g.setColor(Color.WHITE);
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
