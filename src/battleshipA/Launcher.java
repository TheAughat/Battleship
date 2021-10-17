package battleshipA;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class Launcher extends JFrame implements MouseListener, ActionListener {
	
	private JFrame frame;
	private JPanel textflow;
	private JLabel startBtn, cheatsBtn, quitBtn, bg, shipImage, title, credit;
	private Timer timerX, timerY, titleDrop, creditsPush;
	
	static boolean godeye = false, pointHack = false, radarHack = false;
	
	private static ImageIcon eyemoji;
	
	//Setting up the GUI
	private Launcher(){
		
		ImageIcon iconimg = new ImageIcon(getClass().getResource("battleship1.png"));
		eyemoji = new ImageIcon(getClass().getResource("eyemoji.png"));
		
		frame = new JFrame();
		frame.setTitle("Battleship A");
		frame.setSize(800, 480);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLayout(null);
		frame.setResizable(false);
		frame.setIconImage(iconimg.getImage());
		frame.setLocationRelativeTo(null);
		
		ImageIcon bgimg = new ImageIcon(getClass().getResource("sea.jpg"));
		bg = new JLabel(bgimg);
		bg.setBounds(0, 0, 800, 480);
		
		ImageIcon shipimgtest = new ImageIcon(getClass().getResource("shipPic.png"));
		shipImage = new JLabel(shipimgtest);
		shipImage.setBounds(0, 190, 237, 184);
		
		textflow = new JPanel();
		textflow.setBounds(50, 124, 230, 200);
		textflow.setOpaque(false);
		textflow.setLayout(new FlowLayout(0, 20, 25));
		
		startBtn = new JLabel("1. Start Game");
		startBtn.setFont(new Font("Georgia", Font.BOLD, 26));
		startBtn.setForeground(Color.WHITE);
		startBtn.addMouseListener(this);
		
		cheatsBtn = new JLabel("2. Enter Cheats");
		cheatsBtn.setFont(new Font("Georgia", Font.BOLD, 26));
		cheatsBtn.setForeground(Color.WHITE);
		cheatsBtn.addMouseListener(this);
		
		quitBtn = new JLabel("3. Quit Game");
		quitBtn.setFont(new Font("Georgia", Font.BOLD, 26));
		quitBtn.setForeground(Color.WHITE);
		quitBtn.addMouseListener(this);
		
		textflow.add(startBtn);
		textflow.add(cheatsBtn);
		textflow.add(quitBtn);
		timerX = new Timer(20, this);
		timerY = new Timer(120, this);
		titleDrop = new Timer(10, this);
		creditsPush = new Timer(2, this);
		
		title = new JLabel(("Battleship A").toUpperCase());
		title.setFont(new Font("Georgia", Font.BOLD, 50));
		title.setBounds(310, -35, 400, 35);
		title.setForeground(Color.BLUE);
		
		credit = new JLabel("Student Number: 2017796");
		credit.setFont(new Font("Georgia", Font.BOLD, 25));
		credit.setBounds(800, 130, 340, 30);
		credit.setForeground(Color.blue);
		
		frame.add(title);
		frame.add(credit);
		frame.add(textflow);
		frame.add(shipImage);
		frame.add(bg);

		
		frame.setVisible(true);
		timerX.start();
		timerY.start();
		titleDrop.start();
	}
	
	
	public static void main(String[] args) {
		new Launcher();
	}
	
	
	//Declares and initializes all variables required for the animation of the ship
	private int shipX = 0;
	private int shipY = 210;
	private boolean upwardBob = false;
	private int dropcoords = -35;
	private int pushcoords = 800;
	
	

	@Override
	public void mouseClicked(MouseEvent e) {
		//Starts the game
		if (e.getSource() == startBtn) {
			BattleshipGUI.location.x = frame.getLocation().x;
			BattleshipGUI.location.y = frame.getLocation().y -= 100;
			frame.setVisible(false);
			new BattleshipGUI();
			BattleshipGUI.msgprnt = "Computer System: \"Welcome, mortal! I shall be your opponent!"
					+ "\nClick the \"Instructions\" button below if you don't know what to do.\"";
			BattleshipGUI.displayText();
		}
		//Functionality for the quit button
		else if (e.getSource() == cheatsBtn) {
			cheater();
		}
		//End the game
		else if (e.getSource() == quitBtn) {
			quitBtn.setForeground(Color.WHITE);
			System.exit(0);
		}
	}

	
	//The following four methods are used to color the buttons when the player hovers their mouse over or clicks them
	@Override
	public void mousePressed(MouseEvent e) {
		if (e.getSource() == startBtn) {
			startBtn.setForeground(Color.BLUE);
		}
		else if (e.getSource() == cheatsBtn) {
			cheatsBtn.setForeground(Color.BLUE);
		}
		else if (e.getSource() == quitBtn) {
			quitBtn.setForeground(Color.BLUE);
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (e.getSource() == startBtn) {
			startBtn.setForeground(new Color(0, 168, 239));
		}
		else if (e.getSource() == cheatsBtn) {
			cheatsBtn.setForeground(new Color(0, 168, 239));
		}
		else if (e.getSource() == quitBtn) {
			quitBtn.setForeground(new Color(0, 168, 239));
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		if (e.getSource() == startBtn) {
			startBtn.setForeground(new Color(0, 168, 239));
		}
		else if (e.getSource() == cheatsBtn) {
			cheatsBtn.setForeground(new Color(0, 168, 239));
		}
		else if (e.getSource() == quitBtn) {
			quitBtn.setForeground(new Color(0, 168, 239));
		}
	}

	@Override
	public void mouseExited(MouseEvent e) {
		if (e.getSource() == startBtn) {
			startBtn.setForeground(Color.WHITE);
		}
		else if (e.getSource() == cheatsBtn) {
			cheatsBtn.setForeground(Color.WHITE);
		}
		else if (e.getSource() == quitBtn) {
			quitBtn.setForeground(Color.WHITE);
		}
	}
	
	
	
	
	
	
	//This method asks the user to input cheats. It does nothing if the keyword passed in is incorrect.
	//However, if it is a recognized keyword, the cheat is activated.
	private void cheater() {
		String cheat = JOptionPane.showInputDialog(frame, "Add Cheats");
		try {
			if (cheat.equals("godeye")) {
				JOptionPane.showMessageDialog(frame, "The all-seeing Eye of God is now yours to use! All manner of stealth fails before "
						+ "this eye,\ngranting it the power to see through all enemy tactics!", "Cheat Successful!", 1, eyemoji);
				godeye = true;
			}
			else if (cheat.equals("ammo")) {
				JOptionPane.showMessageDialog(frame, "You have found vast riches of ammunition! The god of war smiles upon you!\nUse your newfound might to thoroughly smite your enemies!");
				pointHack = true;
			}
			else if (cheat.equals("detective")) {
				JOptionPane.showMessageDialog(frame, "You have come upon a vast horde of extra radars, \ndropped by the battleships of yore that sunk in these seas!\r\n"
						+ "Use them wisely!");
				radarHack = true;
			}
		} catch (Exception e) {
			
		}
	}

	
	//Performs all the animations
	@Override
	public void actionPerformed(ActionEvent e) {
		
		//Values for the up-down movement of the ship (in pixels)
		int SHIP_MAX_BOB_UP = 190;
		int SHIP_MAX_BOB_DOWN = 210;
		
		//Move the ship from left to right
		if (e.getSource() == timerX) {
			
			//Evaluates whether the ship has moved off the viewport and moves it back to the left side
			if (shipX > frame.getWidth()) {
				shipX = shipImage.getWidth() * -1;
			}
			
			shipImage.setLocation(shipX++, shipY);
		}
		
		//Bounce the ship up and down to give the appearance of bobbing in the waves
		if (e.getSource() == timerY) {
			
			//Move upward
			if (upwardBob) {
				shipImage.setLocation(shipX, shipY++);
			}
			
			//Move downward
			else if (!upwardBob) {
				shipImage.setLocation(shipX, shipY--);
			}
			
			//Check whether to move upward or not
			if (shipY <= SHIP_MAX_BOB_UP) {
				upwardBob = true;
			}
			else if (shipY >= SHIP_MAX_BOB_DOWN) {
				upwardBob = false;
			}
		}
		
		//Drops down the title of the game from the top of the screen
		if (e.getSource() == titleDrop) {
			if (dropcoords >= 90) {
				titleDrop.stop();
				creditsPush.start();
			}
			title.setLocation(310, dropcoords++);
		}
		
		//Pushes in my name and student number from the right of the screen
		if (e.getSource() == creditsPush) {
			if (pushcoords <= 345) {
				creditsPush.stop();
			}
			credit.setLocation(pushcoords--, 130);
		}
	}
}
