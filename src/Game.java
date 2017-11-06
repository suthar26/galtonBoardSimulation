import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.font.TextAttribute;
import java.awt.image.BufferStrategy;
import java.text.AttributedString;
import java.util.Arrays;
import java.util.Random;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener{
	//screen dimensions and variables
	static final int WIDTH = 1024;
	static final int HEIGHT = WIDTH / 4 * 3; //4:3 aspect ratio
	private JFrame frame;

	
	//State variables to determine if in menu, or in actual simulation
	private int galtonState=0;
	private final int galtonMenu=0;
	private final int galtonPlay=1;
	
	//String to store user input
	String str;
	
	//doubly linked list to store the balls in each slot
	DLList results;
	
	//counter to determine how many balls have been dropped
	public int count;
	
	//strings to show outputs
	AttributedString as;
	AttributedString timer;
	Font courier; 
	
	//variables to store time
	long time;
	long totalTime;
	
	//boolean to initialize the playstate variables
	Boolean playInit;
	
	//play state variables
	int num;
	Random rand;
	
	//game updates per second
	static final int UPS = 60;

	//variables for the thread
	private Thread thread;
	private boolean running;

	//used for drawing items to the screen
	private Graphics2D graphics;

	//initialize game objects, load media(pics, music, etc)
	public void init() {
		//gives string a value of 0 to ensure display does not break
		str = "0";
		
		// sets count to 0
		count=0;
		
		//creates a doubly linked list with 25 elements, to represent the 25 slots
		results = new DLList();
		for(int i = 0; i < 25; i++){
			results.add(0);
		}
		
		//creates a random
		rand = new Random();
		
		//sets the play init to false, becomes true once it has initialized
		playInit = false;
		
		
		courier = new Font("TimesNewRoman", Font.BOLD, 10);
	}

	//update game objects
	public void update() {
		
		//if the state is the play state
		if(galtonState == galtonPlay){
			if(!playInit){
				//converts the inputed string to an int
				num = Integer.parseInt(str);
			}
				
			
			
			
			//runs until the count equals the number of runs
			if(count<num){
				//for(int i = 0; i < Math.ceil(num/100.0); i++){
				
				//starts the ball position as 25
				int position = 25;
				
				//loop runs 25 times, to simulate 25 levels of pegs
				for(int j = 0; j < 25; j++){
					
					//generates a random boolean, if its true it increments position, if not it decrements
					if(rand.nextBoolean())
						position++;
					else
						position--;
				}
				
				//As the values are only even from 0-48, divide by 2 to get even and odd from 0-24
				position /=2;
				
				//increments the value at the position of the array
				results.set(position, results.get(position)+1);
				
				//increments the count
				count++;
				
				//updates the time
				totalTime = System.currentTimeMillis()-time;
				//}
			}
		}


	}

	//draw things to the screen
	public void draw() {
		
		//creates the background with a white rectangle
		graphics.setColor(Color.white);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		
		//displays the menu
		if(galtonState == galtonMenu){
			//displays the prompt string and input values
			graphics.setColor(Color.red);
			Font courier = new Font("TimesNewRoman", Font.BOLD, 25);
			as = new AttributedString("Enter the amount of runs:" + str);
			as.addAttribute(TextAttribute.FONT, courier);
			graphics.drawString(as.getIterator(), 400, 55);
		}
		
		//displays the simulation
		else if(galtonState == galtonPlay){
			
			//displays the x and y axis
			graphics.setColor(Color.blue);
			graphics.drawLine(50, 550, 800, 550);
			graphics.drawLine(50, 50, 50, 550);
			
			//draws the total number of runs completed and the time elapsed
			timer = new AttributedString("Number of runs: " + count + " Time in ms: " + totalTime);
			timer.addAttribute(TextAttribute.FONT, courier);
			graphics.drawString(timer.getIterator(), 100, 125);
			
			//displays the array values
			as = new AttributedString(results.toString());
			as.addAttribute(TextAttribute.FONT, courier);
			graphics.drawString(as.getIterator(), 0, 25);
			
			//loops through the DLList to display each position as a bar
			for(int i = 0; i < 25; i++){
				graphics.fillRect(i*30, (int)(551-(results.get(i)/(count/1000.0))), 30, (int)((results.get(i)/(count/1000.0))));
			}
		}
	}

	//main method
	public static void main(String[] args) {
		//creates the game object and creates the frame
		Game game = new Game();
		game.frame.setResizable(false);
		game.frame.add(game); //game is a component because it extends Canvas
		game.frame.pack();
		game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		game.frame.setLocationRelativeTo(null);
		game.frame.setVisible(true);
		game.start();
	}

	//game object
	public Game() {
		//creates the jframe and key listener
		Dimension size = new Dimension(WIDTH, HEIGHT);
		setPreferredSize(size);
		frame = new JFrame();
		addKeyListener(this);
	}

	//starts a new thread for the game
	public synchronized void start() {
		thread = new Thread(this, "Game");
		running = true;
		thread.start();
	}

	//main game loop
	public void run() {
		init();
		long startTime = System.nanoTime();
		double ns = 1000000000.0 / UPS;
		double delta = 0;
		int frames = 0;
		int updates = 0;

		long secondTimer = System.nanoTime();
		while(running) {
			long now = System.nanoTime();
			delta += (now - startTime) / ns;
			startTime = now;
			while(delta >= 1) {
				update();
				delta--;
				updates++;
			}
			render();
			frames++;

			if(System.nanoTime() - secondTimer > 1000000000) {
				this.frame.setTitle(updates + " ups  ||  " + frames + " fps");
				secondTimer += 1000000000;
				frames = 0;
				updates = 0;
			}
		}
		System.exit(0);
	}

	public void render() {
		BufferStrategy bs = getBufferStrategy(); //method from Canvas class

		if(bs == null) {
			createBufferStrategy(3); //creates it only for the first time the loop runs (trip buff)
			return;
		}

		graphics = (Graphics2D)bs.getDrawGraphics();
		draw();
		graphics.dispose();
		bs.show();
	}

	//stops the game thread and quits
	public synchronized void stop() {
		running = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {

	}

	@Override
	public void keyTyped(KeyEvent e) {
		
		//only records values when in the menu
		if(galtonState == galtonMenu){
		
			//if the input is a number, adds it to str
			if(e.getKeyChar() > 47 && e.getKeyChar() < 58)
				str = str + e.getKeyChar();
			
			//if the input is a backspace, it removes the last character in the string
			else if(e.getKeyChar() == KeyEvent.VK_BACK_SPACE){
			
				//does not remove the initial 0 of the string
				if(str.length()>1)
					str = str.substring(0, str.length()-1);
			}
			
			//if enter is pressed, changes the state of the window
			else if(e.getKeyChar() == KeyEvent.VK_ENTER){
				galtonState = galtonPlay;
				time = System.currentTimeMillis();
			}
		}
	}
}
