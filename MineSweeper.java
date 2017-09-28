/*----------------------------------------------------------------------------*
	Program:  MineSweeper
	Author:   sanshumor
 *----------------------------------------------------------------------------*/

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.border.Border;
import javax.swing.Icon;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

/*
	Public Class: MineSweeper
		Interfaces: none
		Parent Class: none
	Role: public class of MineSweeper.java that calls a new MainWindow object
*/
public class MineSweeper
{

	public static void main(String[] args)
	{
		String lab = "Lab 7.1 -- ";
		MainWindow mainWindow = new MainWindow(lab);
		mainWindow.setVisible(true);
	}
}

/*
	Class: MainWindow
		Interfaces: ActionListener
		Parent Class: JFrame
	Role: Custom JFrame class that acts as a GUI instanciation and MainWindow Component's Behavior
*/
class MainWindow extends JFrame implements ActionListener{
	int GRID_WIDTH = 10;
	int GRID_HEIGHT = 10;
	final int FRAME_WIDTH = 500;
	final int FRAME_HEIGHT = 500;
	final int OFFSET = 5;
	final int FRAME_X = 10;
	final int FRAME_Y = 10;
	final int BOMB_COUNT = (((GRID_HEIGHT*GRID_WIDTH)/100)*9);

	Square[][] grid = new Square[GRID_WIDTH][GRID_HEIGHT];

	//random object
	Random random = new Random();

	//aesthetic objects
	Border raisedBevel = BorderFactory.createRaisedBevelBorder();
	private InitalIcon initalIcon = new InitalIcon();

	GridBagConstraints gbc = new GridBagConstraints();
	GridBagConstraints p1constraints = new GridBagConstraints();
	GridBagConstraints p2constraints = new GridBagConstraints();

	JMenuBar mainMenu;
	JMenu gameMenu, optionsMenu, helpMenu, difficultyMenu;
	JMenuItem easyMode, mediumMode, hardMode;

	JLabel score;
	JLabel smilely;
	JLabel seconds_elapsed;

	public MainWindow(String lab){
		super(lab + "MineSweeper");
		initUX();
	}

	public void initUX(){
		/*  ======== FRAME attributes ========  */
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setDefaultLookAndFeelDecorated(true);
		setLayout(new GridBagLayout());

		/* ========= FRAME layout ============ */
		gbc.fill = GridBagConstraints.BOTH;
		gbc.ipadx = 0;
		gbc.ipady = 0;
		gbc.weightx = 1; //# between 0 and 1.0
		gbc.weighty = 1;
		gbc.anchor = GridBagConstraints.CENTER;

		/*  ========= FRAME.ContentPane ==========  */
		// ====------- MENUBAR ---------=====
		mainMenu = new JMenuBar();
		this.setJMenuBar(mainMenu);

		gameMenu = new JMenu("Game");
		mainMenu.add(gameMenu);

		optionsMenu = new JMenu("Options");
		mainMenu.add(optionsMenu);

			difficultyMenu = new JMenu("Level of Difficulty");
			optionsMenu.add(difficultyMenu);
				
				//in menu difficultyMenu, easyMode is pressed
				Action makeEasy = new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						this.GRID_WIDTH = 5;
						this.GRID_HEIGHT = 5;
					}
				};
				easyMode = new JMenuItem("Easy");
				difficultyMenu.add(easyMode);

				//in menu difficultyMenu, mediumMode is pressed
				Action makeMedium = new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						this.GRID_WIDTH = 10;
						this.GRID_HEIGHT = 10;
					}
				};
				mediumMode = new JMenuItem("Medium");
				difficultyMenu.add(mediumMode);

				//in menu difficultyMenu, hardMode is pressed
				Action makeHard = new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						this.GRID_WIDTH = 12;
						this.GRID_HEIGHT = 12;
					}
				};
				hardMode = new JMenuItem("Hard");
				difficultyMenu.add(hardMode);

		helpMenu = new JMenu("Help");
		mainMenu.add(helpMenu);


		// ====------- PANEL 1 ---------=====
		JPanel panel1 = new JPanel(new GridBagLayout());
		panel1.setSize(FRAME_WIDTH, FRAME_HEIGHT-(FRAME_HEIGHT-OFFSET));
		gbc.gridx = 0;
		gbc.gridy = 0;
		/* ====-------- PANEL 1 Layout ----------==== */
		p1constraints.ipadx = 0;
		p1constraints.ipady = 0;
		//0: squishes all the components together, 1: pushes all components apart
		p1constraints.weightx = 1; //# between 0 and 1.0
		p1constraints.weighty = 1;
		p1constraints.anchor = GridBagConstraints.FIRST_LINE_START;

		/* ====-------- PANEL 1 Components ----------==== */
		p1constraints.gridx = 0;
		p1constraints.gridy = 0;
		panel1.add(new JLabel("Score:"),p1constraints);		// This can be anonymous (since never updated)
		p1constraints.gridx = 1;
		p1constraints.gridy = 0;
		panel1.add(new JLabel("Smiley:"),p1constraints);    // This can be anonymous (since never updated)
		p1constraints.gridx = 2;
		p1constraints.gridy = 0;
		panel1.add(new JLabel("Seconds Elapsed:"),p1constraints);    // This can be anonymous (since never updated)

		p1constraints.anchor = GridBagConstraints.CENTER;

		p1constraints.gridx = 0;
		p1constraints.gridy = 1;
		score = new JLabel("Score");
		panel1.add(score, p1constraints);
		p1constraints.gridx = 1;
		p1constraints.gridy = 1;
		smilely = new JLabel("Smiley");
		panel1.add(smilely,p1constraints);
		p1constraints.gridx = 2;
		p1constraints.gridy = 1;
		seconds_elapsed = new JLabel("Seconds Elapsed");
		panel1.add(seconds_elapsed,p1constraints);

		this.add(panel1, gbc);

		// ====-------- PANEL 2 ---------====
		JPanel panel2 = new JPanel(new GridBagLayout());
		panel2.setSize(FRAME_WIDTH, FRAME_HEIGHT-OFFSET);
		/* ====-------- PANEL 2 Layout ----------==== */
		p2constraints.ipadx = 0;
		p2constraints.ipady = 0;
		//squishes all the components together
		p2constraints.weightx = 0; //# between 0 and 1.0
		p2constraints.weighty = 0;
		p2constraints.anchor = GridBagConstraints.CENTER;

		/* ====-------- PANEL 2 Components ----------==== */
		//Initalize "grid[GRID_WIDTH][GRID_HEIGHT]" (of Squares)
		for(int r=0; r < grid.length; r++){
			for(int c=0; c < grid[GRID_HEIGHT-1].length; c++){
				grid[c][r]= new Square(c,r);
				grid[c][r].setBorder(raisedBevel);
				grid[c][r].setIcon(initalIcon);
				p2constraints.gridx = c;
				p2constraints.gridy = r;
				panel2.add(grid[c][r], p2constraints);
			}
		}

		gbc.ipadx = 0;
		gbc.ipady = 0;
		//insets: top, left, bottom, right
		gbc.insets = new Insets(30,60,30,60);
		gbc.gridx = 0;
		gbc.gridy = 1;
		this.add(panel2, gbc);

		//make Bombs
		makeBombs(grid.length, grid[GRID_HEIGHT-1].length);
		//setValues()
	}

	public void makeBombs(int cMax, int rMax){
		int[][] rArray = new int[GRID_WIDTH][GRID_HEIGHT];

		for(int num=0; num < BOMB_COUNT; num++){
			int rc = random.nextInt(cMax);
			int rr = random.nextInt(rMax);

			rArray[rc][rr] = rArray[rc][rr]+1;

			while(rArray[rc][rr] != 1){
				rc = random.nextInt(cMax);
				rr = random.nextInt(rMax);
				rArray[rc][rr] = rArray[rc][rr]++;
			}

			setBombs(grid[rc][rr]);
		}
	}

	public void setBombs(Square sqr){
		sqr.setBomb(true);
	}

	public void actionPerformed( ActionEvent e ){
		System.out.println("actionPerformed in MainWindow class");
		//use this action listener to override label (smilely face) behavior when pressed

	}

}

/*
	Class: Square
		Interfaces: ActionListener
		Parent Class: JButton
	Role: Custom JButton Class with custom behavior
*/
class Square extends JButton implements ActionListener {
	private int row, column;
	private int width, height;

	private boolean pressed;
	private boolean bomb = false;

	private Border loweredBevel = BorderFactory.createLoweredBevelBorder();

	public IndicatorIcon indicator = new IndicatorIcon();
	public ImageIcon bombImg = new ImageIcon("images/bomb.jpg");

	public Square(int x, int y){
		row = x;
		column = y;
		this.pressed = false;
		//give each square an action listener override located in Square Class
		this.addActionListener(this);
	}

	public void actionPerformed( ActionEvent e ){
		if(this.pressed==false){
			this.pressed = true;

			//aesthetic
			this.setBorder(loweredBevel);
			this.setOpaque(true);

			//value of bomb displayed, game logic
			if (bomb == true){
				//set equal to bomb image
				this.setIcon(bombImg);
			}
			else if(bomb == false){
				//anything that isn't close to a bomb must be pressed
				indicator.setIcon("1");
				indicator.setColor(10, 12, 217);
				this.setIcon(indicator);
			}
		}
	}

	public int getSqrWidth(){
		int w = this.getWidth();
		return w;
	}

	public int getSqrHeight(){
		int h = this.getHeight();
		return h;
	}

	public void setBomb(boolean v){
		this.bomb = v;
	}

}

/*
	Class: IndicatorIcon
		Interfaces: Icon
		Parent Class: None
	Role: Paints a custom Icon that displays when loaded
*/
class InitalIcon implements Icon{

    private int width = 25;
    private int height = 25;

    private BasicStroke stroke = new BasicStroke(4);

    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2d = (Graphics2D) g.create();

        g2d.setColor(new Color(192, 192, 192));
        g2d.fillRect(x +1 ,y + 1,width -2 ,height -2);

        g2d.dispose();
    }
	//overrides needed for functionality
	public int getIconWidth() {
        return width;
    }

    public int getIconHeight() {
        return height;
    }
}

/*
	Class: IndicatorIcon
		Interfaces: Icon
		Parent Class: None
	Role: Paints a custom Icon to display data about bombs to user
*/
class IndicatorIcon implements Icon{

	private int width = 25, height = 25;
	final private int PEN_WIDTH = 4;

	private String icon = "null";

	private BasicStroke stroke = new BasicStroke(PEN_WIDTH);
	private Font custom_font = new Font("serif", Font.BOLD, 12);
	private Color custom_color = new Color(0, 0, 0);

	public void paintIcon(Component c, Graphics g, int x, int y){
		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(new Color(192, 192, 192));
		g2d.fillRect(x +1 ,y + 1,width -2 ,height -2);

		g2d.setStroke(stroke);
		g2d.setFont(custom_font);
		g2d.setColor(custom_color);

		FontMetrics metrics = g2d.getFontMetrics(custom_font);

		String icon = this.getIcon();

		g2d.drawString(icon, getXPos(c, metrics), getYPos(c, metrics));
	}

	public int getXPos(Component c, FontMetrics metrics){
		int x = (c.getWidth() - metrics.stringWidth(icon)) / 2;
		return x;
	}

	public int getYPos(Component c, FontMetrics metrics){
		int y = ((c.getHeight() - metrics.getHeight()) / 2) + metrics.getAscent();
		return y;
	}

	//overrides needed for functionality
	public int getIconWidth() {
        return width;
    }

	public int getIconHeight() {
        return height;
    }

	public void setIcon(String str) {
		this.icon = str;
	}

	public void setColor(int r, int g, int b) {
		Color myColor = new Color(r, g, b);
		this.custom_color = myColor;
	}

	public String getIcon(){
		return icon;
	}

}
