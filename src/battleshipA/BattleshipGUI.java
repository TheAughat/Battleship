package battleshipA;

import javax.swing.*;
import javax.swing.border.LineBorder;

import java.awt.*;
import java.awt.event.*;

//This class, along with the BattleshipSystem, and the Launcher, are all used statically, in tandem with each other.
//No objects are made or created, since we do not require different instances of these classes. They all work together simultaneously.
//The only class that needed multiple instances of itself in this game is the Ship class. Objects of the Ship class are instantiated in this class.
public class BattleshipGUI implements ActionListener, MouseListener {
	
	//Setting up all the required variables for the game. They must be accessible by the BattleshipSystem class, so they are not private.
	static JFrame frame;
	static JLabel battleshipIcon, scorebd, warnbd;
	static JTextField title, credits;
	static JTextArea textbox;
	
	static JButton[][] mapmain = new JButton[10][10];
	static JButton instr, quitgame, settingsBtn;
	
	JPanel panel, modespanel;
	
	static Color btndefault;
	
	static String fontName = "Ink Free", gameEnd = "", msgprnt = "";
	
	static Object fontStyle = Font.BOLD;
	static Font fontface = new Font(fontName, (int) fontStyle, 15);

	JRadioButton shootm, radarm;
	static boolean radarmode = false, runReplaySection = false;
	
	static int TIMER_DELAY = 10;
	
	private static Timer slowTimer;
	
	static String[] instrucs = {
			"In this game, the computer has spawned 5 ships of varying lengths all over the map above!"
			+ " You will need to shoot down all of the enemy's ships to win.",
			
			"You cannot see where the ships are due to the computer having access to futuristic"
			+ " camouflaging techniques, however, you can still hit them if you aim at the right spots. ",
			
			"Click on any spot on the map of the sea above to fire a shot at it. If it hits an enemy ship, you will be notified.",
			
			"You also have 4 radars you can use to detect invisible enemy ships in the vicinity.",
			
			"You can drop these radars at any spot on the map and check for ships within range."
			+ " The range of the radars are one spot away in all directions from the point you drop them at.",
			
			"Be careful using them, since your supply is limited. You will be able to see the areas you've previously dropped radars at,"
			+ " so you can avoid checking the same spots twice.",
			
			"Your ammunition is limited, and the number of shots you have is determined by your score. You have 10 shots to begin with.",
			
			"If you exhaust all 10 shots without hitting anything, the game ends since you will have run out of ammunition.",
			
			"You can get more ammo by sinking a ship and taking their ammo."
			+ " You can also get 1 shot from every spot of an enemy ship you hit, even if you don't sink it.",
			
			"Due to limited ammo, you must also make sure you don't hit spots you've already shot at before. You will be warned every time you do.\r\n"
			+ "After 3 warnings, you automatically lose the game.",
			
			"That's all. You can now continue playing. Enjoy!",
			
			""
	};
	
	//Used to store which message from the array of instruction messages the computer has just shown the player,
	//also informs the computer which message to show next
	static int instrMsgOn = 0;
	
	//instrViewing holds data on whether the user is currently viewing instructions or not. The "Skip" button for the instructions uses this
	//firstboot is used to open the game at the center of the screen during startup. If the user chooses to replay, the game opens at the previous location
	static boolean instrViewing, firstboot = true;
	
	//These two vars are used to set the location of the game if the player were to choose to replay
	static Point location = new Point(0, 0);
	
	
	//Instantiating the settings menu. We don't need more than one instance of this in the entire game, but instantiating it rather than using it in
	//a static way made some things easier. For example, automatically resetting all variables and settings upon choosing to replay the game.
	SettingsMenu options;
	
	
	//Creating the ship objects
	static Ship aircraft;
	static Ship battleship;
	static Ship subm;
	static Ship destroyer;
	static Ship patrol;
	
	
	static ImageIcon img;
	
	
	BattleshipGUI(){
		
		aircraft = new Ship(5, 5, "A");
		battleship = new Ship(4, 4, "B");
		subm = new Ship(3, 3, "S");
		destroyer = new Ship(3, 3, "D");
		patrol = new Ship(2, 2, "P");
		
		frame = new JFrame("Battleship, Variant A");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(538, 890);
		frame.setLayout(null);
		
		//Sets the Battleship image as an icon for the frame/window.
		img = new ImageIcon(getClass().getResource("battleship.png"));
		
		//Sets the battleship image up in the actual game. It appears next to the title.
		//Two different images were used because the one for the frame was too small to use here
		ImageIcon img1 = new ImageIcon(getClass().getResource("battleship1.png"));
		
		
		//These show the title of the game in the GUI and my name below it
		//I had trouble getting JLabels to work here for some reason, so I used textboxes instead.
		//The textboxes worked perfectly, which is why they are left as is, so that I don't run the risk of messing something up
		title = new JTextField();
		title.setBounds(164, 10, 272, 50);
		title.setFont(new Font("Ink Free", Font.BOLD, 40));
		title.setEditable(false);
		title.setText("BATTLESHIP");
		title.setForeground(new Color(50, 118, 153));
		
		credits = new JTextField();
		credits.setBounds(222, 60, 180, 30);
		credits.setFont(new Font("Ink Free", Font.BOLD, 17));
		credits.setEditable(false);
		credits.setText("by Daniel Rodrigues");
		credits.setForeground(new Color(50, 118, 153));
		
		//removes the border around the textboxes.
		title.setBorder(null);
		credits.setBorder(null);
		
		//This creates the main textbox
		textbox = new JTextArea();
		textbox.setBounds(20, 693, 480, 80);
		textbox.setFont(fontface);
		textbox.setEditable(false);
		textbox.setBackground(new Color(196, 222, 237));
		textbox.setLineWrap(true);
		textbox.setWrapStyleWord(true);
		textbox.setBorder(new LineBorder(Color.BLACK, 2));
		
		instr = new JButton("Instructions");
		instr.addActionListener(this);
		instr.setFont(fontface);
		instr.setFocusable(false);
		instr.setBounds(30, 786, 220, 50);
		
		quitgame = new JButton("Quit Game");
		quitgame.addActionListener(this);
		quitgame.setFont(fontface);
		quitgame.setFocusable(false);
		quitgame.setBounds(270, 786, 220, 50);
		
		settingsBtn = new JButton("Settings");
		settingsBtn.addActionListener(this);
		settingsBtn.setFont(fontface);
		settingsBtn.setFocusable(false);
		settingsBtn.setBounds(20, 660, 480, 22);
		
		//The extra spaces are added before the words so that I can position them in the exact spot of the screen I want
		scorebd = new JLabel("        Score: " + BattleshipSystem.score);
		warnbd = new JLabel("    Warnings: " + BattleshipSystem.warnings[0] + "/" + BattleshipSystem.warnings[1]);
		
		shootm = new JRadioButton("Shooting Mode");
		radarm = new JRadioButton("Radar Mode");
		ButtonGroup modes = new ButtonGroup();
		modes.add(shootm); modes.add(radarm);
		shootm.addActionListener(this); radarm.addActionListener(this);
		shootm.setFocusable(false); radarm.setFocusable(false);
		shootm.setSelected(true);
		
		modespanel = new JPanel();
		modespanel.setBounds(85, 93, 500, 54);
		modespanel.setLayout(new GridLayout(2, 2));
		modespanel.add(shootm); modespanel.add(radarm);
		modespanel.add(scorebd); modespanel.add(warnbd);
		
	
		//Creating the main board of buttons
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				mapmain[i][j] = new JButton("");
				mapmain[i][j].addActionListener(this);
				mapmain[i][j].setFont(fontface);
				mapmain[i][j].setFocusable(false);
				mapmain[i][j].setForeground(new Color(31, 107, 150));
				mapmain[i][j].addMouseListener(this);
			}
		}
		
		panel = new JPanel();
		panel.setBounds(10, 150, 500, 500);
		panel.setLayout(new GridLayout(10, 10, 4, 4));
		panel.setBackground(new Color(148, 205, 237));
		
		for (JButton[] l1 : mapmain) {
			for (JButton b1 : l1) {
				panel.add(b1);
			}
		}
		
		battleshipIcon = new JLabel("", img1, JLabel.CENTER);
		battleshipIcon.setBounds(40, 7, 80, 80);
		
		
		options = new SettingsMenu();
		options.fontsWarningShown = false;
		//If the player has added the godeye cheat in the launcher, then the settings menu created has the option to toggle godeye on or off.
		if (Launcher.godeye) options.setEyeConVisible(true);
		
		
		frame.setIconImage(img.getImage());
		
		frame.add(panel);
		frame.add(title);
		frame.add(credits);
		frame.add(textbox);
		frame.add(instr);
		frame.add(quitgame);
		frame.add(modespanel);
		frame.add(battleshipIcon);
		frame.add(settingsBtn);
		
		frame.setResizable(false);
		
		//Stores the default coloring of the JButtons, so that recoloring them after using the radar can work
		btndefault = (mapmain[1][1].getBackground());

		//If the game starts for the first time, use a specific location, else start at the same point the previous game ended
		if (firstboot) {
			frame.setLocationRelativeTo(null);
			firstboot = false;
		}
		else {
			//the 'location' variable is modified in BattleshipSystem.replay()
			frame.setLocation(location);
		}

		
		frame.setVisible(true);
		
		BattleshipSystem.unsunkShips.add("Aircraft Carrier");
		BattleshipSystem.spawnShip(aircraft);
		BattleshipSystem.unsunkShips.add("Battleship");
		BattleshipSystem.spawnShip(battleship);
		BattleshipSystem.unsunkShips.add("Submarine");
		BattleshipSystem.spawnShip(subm);
		BattleshipSystem.unsunkShips.add("Destroyer");
		BattleshipSystem.spawnShip(destroyer);
		BattleshipSystem.unsunkShips.add("Patrol Boat");
		BattleshipSystem.spawnShip(patrol);


		//If they enter the cheat "ammo" at the launcher
		if (Launcher.pointHack) {
			BattleshipSystem.score += 50;
			BattleshipSystem.updatescore();
		}
		
		//If they enter the cheat "detective" at the launcher
		if (Launcher.radarHack) {
			BattleshipSystem.radars += 10;
		}
	}
	
	
	
	//Uncomment the main method if you want to start the game from this class. Otherwise, it starts from the Launcher.
//	public static void main(String[] args) {
//		new BattleshipGUI();
//		msgprnt = "Computer System: \"Welcome, mortal! I shall be your opponent!\nClick the \"Instructions\" button below if you don't know what to do.\"";
//		displayText();
//	}
	
	

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == quitgame) {
			//The if block executes only when the player is viewing instructions. It works as the 'skip' button when viewing instructions.
			if (instrViewing) {
				instrViewing = false;
				instrMsgOn = 0;
				quitgame.setText("Quit Game");
				textbox.setText("");
				instr.setText("Instructions");
				BattleshipSystem.disableBoardBtns(false);
			}
			else {
				System.exit(0);
			}
		}
		

		if (e.getSource() == settingsBtn) {
			
			//reset textspeed slider bar everytime the user opens the options menu, since if they have made changes previously and NOT applied those changes
			//you want to reset the value of the bar to represent what the text speed actually is 
			options.setTextSpeed((50 - TIMER_DELAY) * 2);
			
			//sets the location of the options menu directly in the center (vertically and horizontally) of the main game GUI
			options.frame.setLocation(BattleshipGUI.frame.getLocation().x += 99, BattleshipGUI.frame.getLocation().y += 285);

			frame.setVisible(false);
			options.frame.setVisible(true);
		}
		
		
		//Functionality for the Instructions button
		if (e.getSource() == instr) {
			//This is what executes during gametime when player wants instructions
			if (gameEnd.isEmpty()) {
				BattleshipSystem.disableBoardBtns(true);
				quitgame.setText("Skip");
				instrViewing = true;
				
				instructions();
			}
			//This is what executes once the player either loses or wins
			//The instructions button is used to advance the text in either of the conditions
			else {
				endingSequence();
			}
		}
		
		
		//Mode Selection
		//1. Radar mode
		if (e.getSource() == radarm) {
			radarmode = true;
			scorebd.setText("  Radars left: " + BattleshipSystem.radars);
			BattleshipSystem.colorRadar();
		}
		//2. Shooting mode
		else if (e.getSource() == shootm){
			radarmode = false;
			scorebd.setText("        Score: " + BattleshipSystem.score);
			for (int i=0; i<10; i++) {
				for (int j=0; j<10; j++) {
					mapmain[i][j].setBackground(btndefault);
				}
			}
		}
		
		
		//Main Board Shooting and Radar Action Events
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				if (e.getSource() == mapmain[i][j]) {
					if (!radarmode) {
						BattleshipSystem.shoot(i, j);
					}
					else {
						BattleshipSystem.confirmRadar(i, j);
					}
				}
			}
		}
		
		
	}
	
	
	//Displays a single message to the user from the array of instruction messages.
	//This method is called every time the user clicks the instructions button and the game has not ended.
	//If the game has ended, the endingSequence() method is called instead.
	public static void instructions() {
		msgprnt = instrucs[instrMsgOn];
		displayText();
		
		if (instrMsgOn == 11) {
			instrMsgOn = 0;
			instrViewing = false;
			instr.setText("Instructions");
			quitgame.setText("Quit Game");
			BattleshipSystem.disableBoardBtns(false);
		}
		else if (instrMsgOn == 10) {
			instr.setText("Instructions (finish...)");
			instrMsgOn++;
		}
		else {
			instrMsgOn++;
			instr.setText("Instructions (next...)");
		}
	}
	
	
	//When the game has ended and the instructions button becomes the "Next" button, this method gets called.
	public static void endingSequence() {
		if (runReplaySection) {
		//runReplaySection checks whether the player has read all messages from the computer upon winning or losing.
		//If they have, only then are they asked to replay.
			int replaychoice = JOptionPane.showConfirmDialog(frame, "Do you want to play again?", "Replay?", JOptionPane.YES_NO_OPTION);
			BattleshipSystem.replay((replaychoice == 0)?true:false);
		}
		
		if (gameEnd == "win") {
			if (instrMsgOn == 2) {
				BattleshipGUI.msgprnt = "Computer System: \"Let's go another round! This time, I will win!\"";
				BattleshipGUI.displayText();
				instrMsgOn++;
				runReplaySection = true;
			}
			else if (instrMsgOn == 1) {
				BattleshipGUI.msgprnt = "Computer System: \"Don't get cocky! I may have lost THIS BATTLE, but the victor of the WAR is yet to be declared!\"";
				BattleshipGUI.displayText();
				instrMsgOn++;
			}
			else if (instrMsgOn == 0) {
				BattleshipGUI.msgprnt = "Computer System: \"You have sunk all of my ships! How can this be?\n...You little..!\"";
				BattleshipGUI.displayText();
				instrMsgOn++;
			}
		}
		else if (gameEnd == "warn" || gameEnd == "score"){
			if (instrMsgOn == 1) {
				msgprnt = "Computer System: \"Do you still have any fight left in you, mortal? Do you wish me to trounce you once more?\"";
				displayText();
				runReplaySection = true;
				instrMsgOn++;
			}
			else if (instrMsgOn == 0) {
				msgprnt = "Computer System: \"Really, did anyone expect any different? A fleshy-bio going up against an advanced inorganic like me is unthinkable!\"";
				displayText();
				instrMsgOn++;
			}
		}
	}
	
	
	//Prints the text from the "msgprnt" variable into the textbox in the GUI character-by-character.
	//It uses the TimerListener class to do this.
	//It was put in a separate class because the actionListener of this class is already too long
	public static void displayText() {
		if (slowTimer == null || !slowTimer.isRunning()) {
			//Disable the instructions button since clicking it while text is already printing causes visual glitches (no exceptions, just glitches)
			instr.setEnabled(false);
			
			textbox.setText("");
			
			//Creates a new instance of the declared timer object and starts it so that it can print the characters from "msgprnt" to the textbox
			slowTimer = new Timer(TIMER_DELAY, new TimerListener());
			slowTimer.start();
		}
	}
	
	
	//Restart some variables. Called when replaying the game. Not manually resetting these specific variables causes glitches
	public static void restartGuiVars() {
		instrMsgOn = 0; gameEnd = ""; runReplaySection = false;
		fontName = "Ink Free";
		fontStyle = "Bold";
		fontface = new Font("Ink Free", Font.BOLD, 15);
	}



	
	//The following three methods MUST be included even though they're empty because the MouseListener does not work without them
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
	}

	@Override
	public void mouseReleased(MouseEvent e) {
	}




	//The following two methods are only used if God-Eye has been activated. 
	//These methods allow the user to see which squares on the board host ships.
	@Override
	public void mouseEntered(MouseEvent e) {
		if (!radarmode && Launcher.godeye) {
			for (int i=0; i<10; i++) {
				for (int j=0; j<10; j++) {
					//Check each button on the board to find out which button the mouse is on and retrieve the coordinates
					if (e.getSource() == mapmain[i][j]) {
						for (int x=i-1; x<i+2; x++) {
							for (int y=j-1; y<j+2; y++) {
								//Color the area surrounding the mouse white and show any symbols that aren't the default symbol
								try {
									mapmain[x][y].setBackground(Color.white);
									if (BattleshipSystem.seaZone[x][y] != "O") {
										mapmain[x][y].setText(BattleshipSystem.seaZone[x][y]);
									}
								//Exception handler to catch out of bounds errors
								} catch (Exception f) {
									
								}
							}
						}
					}
				}
			}
		}
	}

	//Follows similar logic to the method above, just this time recolors the buttons not near the mouse to their original color
	//and also gets rid of any symbols visible to the user that are outside of the mouse's range
	@Override
	public void mouseExited(MouseEvent e) {
		if (!radarmode && Launcher.godeye) {
			for (int i=0; i<10; i++) {
				for (int j=0; j<10; j++) {
					if (e.getSource() == mapmain[i][j]) {
						for (int x=i-1; x<i+2; x++) {
							for (int y=j-1; y<j+2; y++) {
								try {
									mapmain[x][y].setBackground(btndefault);
									if (BattleshipSystem.seaZone[x][y] == "H" || BattleshipSystem.seaZone[x][y] == "M") {
										mapmain[x][y].setText(BattleshipSystem.seaZone[x][y]);
									}
									else {
										mapmain[x][y].setText("");
									}
								} catch (Exception f) {
									
								}
							}
						}
					}
				}
			}
		}
	}
	
	
}

