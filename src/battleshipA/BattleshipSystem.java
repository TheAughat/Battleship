package battleshipA;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JOptionPane;

//This class, along with the BattleshipGUI, and the Launcher, are all used in a static way, in tandem with each other.
//No objects are made or created, since we do not require different instances of these classes. They all work together simultaneously.
public class BattleshipSystem {
	
	//Setting up all the required variables for the game. They must be accessible by the BattleshipGUI class, so they are not private.
	static Random rand = new Random();
	static int score = 0;
	static int radars = 4;
	static int[] warnings = {0, 3};
	
	static boolean a = false, b = false, s = false, d = false, p = false;
	
	static ArrayList<String> unsunkShips = new ArrayList<String>();
	static ArrayList<String> sunkShips = new ArrayList<String>();
	
	static int shotsFired = 0, hits = 0, misses = 0;
	
	//Stores the areas where radars detected ships. 0s means the radars did not scan the area.
	//1 means one or more ships were detected. 2 means no ships were detected.
	static int[][] radarDetections = {
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
			{0, 0, 0, 0, 0, 0, 0, 0, 0, 0}
	};
	
	//This array stores the occurences of all ships on the board.
	//The Os are replaced by the ship's special symbol, which is specified upon creation of the ship in the BattleshipGUI class.
	static String[][] seaZone = {
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"},
			{"O", "O", "O", "O", "O", "O", "O", "O", "O", "O"}
	};
	
	
	//Spawns a ship on the board vertically or horizontally.
	//The method will keep running in a loop until it finds a valid space for the ship.
	//Spaces that are not out of bounds of the board and not already occupied by other ships are valid spaces.
	//The ship that it's meant to spawn is passed to it as an argument when the method is called
	public static void spawnShip(Ship shp) {
		//shipPosition decides whether the ship is going to be laid out vertically or horizontally
		//true - vertical, false - horizontal
		boolean shipPosition = rand.nextBoolean();
		
		//Will hold the x and the y coordinate that the ship must spawn at.
		int[] spawnAt = new int[2];
		
		while (true) {
			//Generate random number between 0 and 9 for both elements of the array "spawnAt"
			spawnAt[0] = rand.nextInt(((9 + 1) - 0) + 0);
			spawnAt[1] = rand.nextInt(((9 + 1) - 0) + 0);
			
			if (shipPosition) {
				//Spawns the ship vertically if shipPosition was true
				
				//Check whether the spawn coordinate will lead to the ship being out of bounds (10 by 10 board means that each row is from 0 to 9)
				if (9 - spawnAt[0] >= shp.GetLength()) {
					boolean occupied = false;
					for (int i=spawnAt[0]+1; i<=spawnAt[0] + shp.GetLength(); i++) {
						//Check whether the coordinates the ship is going to spawn at is already occupied by another ship
						if (seaZone[i][spawnAt[1]] != "O") {
							occupied = true;
							break;
						}
					}
					//Finally, if the coordinates are empty, the ship is spawned.
					if (!occupied) {
						for (int i=spawnAt[0]+1; i<=spawnAt[0] + shp.GetLength(); i++) {
							seaZone[i][spawnAt[1]] = shp.GetSymbol();
						}
						break;
					}
				}
			}
			else {
				//The following two blocks of code work similarly to the ones above, except these are tweaked to spawn the ship horizontally.
				//They use different for loops and conditional evaluations, so combining the code for vertical and horizontal spawning was not possible.
				if (9 - spawnAt[1] >= shp.GetLength()) {
					boolean occupied = false;
					for (int i=spawnAt[1]+1; i<=spawnAt[1] + shp.GetLength(); i++) {
						if (seaZone[spawnAt[0]][i] != "O") {
							occupied = true;
							break;
						}
					}
					if (!occupied) {
						for (int i=spawnAt[1]+1; i<=spawnAt[1] + shp.GetLength(); i++) {
							seaZone[spawnAt[0]][i] = shp.GetSymbol();
						}
						break;
					}
				}
			}
		}
	}
	
	
	//This method is called whenever the player clicks on a button in the 10 by 10 board
	//It takes in the coordinate of the button from the actionListener of the BattleshipGUI class as its parameters when it is called
	public static void shoot(int x, int y) {
		//The shotsFired variable is used to keep track of the user's stats and is displayed at the end of the game
		shotsFired++;
		
		//this boolean is used to display the count of warnings shown (eg: you've accumulated 2 of 3 warnings.)
		//If I don't have this in here, the list of sunk and unsunk ships will instantaneously overwrite the warnings message, and it wont be seen by the user
		boolean warningsShown = checkWarnings(x, y);
		
		if (!warningsShown) {
			//Don't do anything in this if block if the user receives a warning for clicking on a spot they had already shot at before
			score--;
			updatescore();
			
			if (checkShipHit(x, y)) {
				seaZone[x][y] = "H";
				BattleshipGUI.mapmain[x][y].setText("H");
				hits++;
				JOptionPane.showMessageDialog(BattleshipGUI.frame, "My ship was hit!", "Outcome", 1);
				score++;
				updatescore();
			}
			else {
				seaZone[x][y] = "M";
				BattleshipGUI.mapmain[x][y].setText("M");
				misses++;
				JOptionPane.showMessageDialog(BattleshipGUI.frame, "You missed!", "Outcome", 0);
				if (score < -9) {
					BattleshipGUI.gameEnd = "score";
				}
			}
		}
		shootResult(warningsShown);
	}
	
	
	//This method checks whether the button the player clicked on was already shot at before and returns true or false.
	//Button coordinates in the 2D array are the parameters.
	private static boolean checkWarnings(int x, int y) {
		if (seaZone[x][y] == "M" || seaZone[x][y] == "H") {
			warnings[0]++;
			updatewarnings();
			score--;
			updatescore();
			
			//If player shoots at an "M" square
			if (BattleshipGUI.mapmain[x][y].getText() == "M") {
				JOptionPane.showMessageDialog(BattleshipGUI.frame, "WARNING: You've already tried to attack that spot and missed..! You've wasted ammunition!",
						"WARNING!", 2);
				misses++;
			}
			//If they shoot at a "W" square
			else {
				JOptionPane.showMessageDialog(BattleshipGUI.frame, "WARNING: That spot had already been hit..! You've wasted ammunition!", "WARNING!", 2);
			}
			
			//Display this message regardless of whether it was a "M" or "H" square
			BattleshipGUI.msgprnt = "You have accumulated " + warnings[0] + " of " + warnings[1] + " warnings."
					+ "\nGetting more than the permitted number of warnings will directly disqualify you.";
			BattleshipGUI.displayText();
			return true;
		}
		return false;
	}
	
	
	//Updates the results of the shot fired in the game and also informs the user about how many ships are remaining.
	//Uses the warningsShown as a parameter, and does nothing if it is true.
	private static void shootResult(boolean warningsShown) {
		checkGameEnded();
		
		//We don't need to check if a ship was hit when a warning was shown since that spot had already been fired at.
		if (BattleshipGUI.gameEnd == "" && !warningsShown) {
			checkShipDestroyedCycle();
			
			//The substrings are used to remove the "[" and "]" symbols that get printed when printing an arraylist.
			String theUnsunk = unsunkShips.toString();
			theUnsunk = theUnsunk.substring(1, theUnsunk.length() - 1);
			
			if (sunkShips.isEmpty()) {
				BattleshipGUI.msgprnt = "Unsunk ships remaining: " + theUnsunk + ".";
				BattleshipGUI.displayText();
			}
			else {
				//getting rid of the "[" and "]" for sunkShips and printing it along with unsunkShips if sunkShips is not empty
				BattleshipGUI.msgprnt = "Sunk ships: " + (sunkShips.toString()).substring(1, sunkShips.toString().length() - 1) + "."
						+ "\nUnsunk ships remaining: " + theUnsunk + ".";
				BattleshipGUI.displayText();
			}
			
			if (unsunkShips.isEmpty()) {
				BattleshipGUI.gameEnd = "win";
				disableBoardBtns(true);
				JOptionPane.showMessageDialog(BattleshipGUI.frame, "You have sunk all of the computer's ships. You win!", "You win!", 1);
				hitsToMisses();
				BattleshipGUI.msgprnt = "Computer System: \"Oh, no! I... I lost?! This can't be real! This is a joke, right?\"";
				BattleshipGUI.displayText();
				BattleshipGUI.instr.setText("Next...");
				BattleshipGUI.instrMsgOn = 0;
			}
		}
	}
	
	
	//Checks whether the game has ended with the player's loss.
	//It does not check for the player's win because that must happen only when no warnings have been shown.
	private static void checkGameEnded() {
		if (warnings[0] >= warnings[1] || score < -9) {
			if (score < -9) {
				BattleshipGUI.gameEnd = "score";
				JOptionPane.showMessageDialog(BattleshipGUI.frame, "Your score has dipped too low.\nYou lose!", "You Lose!", 0);
			}
			else {
				BattleshipGUI.gameEnd = "warn";
				JOptionPane.showMessageDialog(BattleshipGUI.frame, "You have accumulated too many warnings.\nYou lose!", "You Lose!", 0);
			}
			
			//Show the player their gameplay statistics and disable the board from being clickable.
			//The player can then continue by clicking the "Next" button. The functionality for that is in the BattleshipGUI class for the "instr" button
			hitsToMisses();
			disableBoardBtns(true);
			
			BattleshipGUI.msgprnt = "Computer System: \"Muhahahaha! As foretold by the gods, I have won and you have lost!\"";
			BattleshipGUI.displayText();
			BattleshipGUI.instr.setText("Next...");
			BattleshipGUI.instrMsgOn = 0;
		}
	}
	
	
	//Checks if a ship was hit by the user and returns true or false accordingly.
	//It checks which symbol was hit (if any) and decrements the number of spots/squares that specific ship takes up on the board.
	//Parameters are the coordinates of the user's shot on a 10 x 10 2D Array.
	private static boolean checkShipHit(int m, int n) {
		if (seaZone[m][n] == "A") {
			BattleshipGUI.aircraft.SetSpots(BattleshipGUI.aircraft.GetSpots() - 1);
			return true;
		}
		else if (seaZone[m][n] == "B") {
			BattleshipGUI.battleship.SetSpots(BattleshipGUI.battleship.GetSpots() - 1);
			return true;
		}
		else if (seaZone[m][n] == "S") {
			BattleshipGUI.subm.SetSpots(BattleshipGUI.subm.GetSpots() - 1);
			return true;
		}
		else if (seaZone[m][n] == "D") {
			BattleshipGUI.destroyer.SetSpots(BattleshipGUI.destroyer.GetSpots() - 1);
			return true;
		}
		else if (seaZone[m][n] == "P") {
			BattleshipGUI.patrol.SetSpots(BattleshipGUI.patrol.GetSpots() - 1);
			return true;
		}
		
		return false;
	}
	
	
	//If a ship is destroyed, update the list of sunk and active ships and award the user the appropriate score.
	//Parameters are the ship object being checked and the type of ship it is (for example, "Battleship", or "Destroyer").
	private static boolean checkShipDestroyed(Ship shp, String type) {
		if (shp.GetSpots() == 0) {
			unsunkShips.remove(unsunkShips.indexOf(type));
			sunkShips.add(type);
			score += shp.GetLength() * 2;
			JOptionPane.showMessageDialog(BattleshipGUI.frame, "You sank my " + type + "!" + "\nYou've received " + (shp.GetLength() * 2) + " points!",
					"Ship sunk!", 1);
			updatescore();
			return true;
		}
		else {
			return false;
		}
	}
	
	
	//This method is performed every time the user fires a shot.
	//It checks whether or not any particular ship has lost all the spots it occupied on the board.
	//If it has, then this method calls the checkShipDestroyed method,
	//-and sets up a boolean to prevent it from re-calling checkShipDestroyed() for previously destroyed ships every time that the user fires a shot.
	private static void checkShipDestroyedCycle() {
		if (BattleshipGUI.aircraft.GetSpots() == 0 && !a) {
			a = (checkShipDestroyed(BattleshipGUI.aircraft, "Aircraft Carrier"))?true:false;
		}
		else if (BattleshipGUI.battleship.GetSpots() == 0 && !b) {
			b = (checkShipDestroyed(BattleshipGUI.battleship, "Battleship"))?true:false;
		}
		else if (BattleshipGUI.subm.GetSpots() == 0 && !s) {
			s = (checkShipDestroyed(BattleshipGUI.subm, "Submarine"))?true:false;
		}
		else if (BattleshipGUI.destroyer.GetSpots() == 0 && !d) {
			d = (checkShipDestroyed(BattleshipGUI.destroyer, "Destroyer"))?true:false;
		}
		else if (BattleshipGUI.patrol.GetSpots() == 0 && !p) {
			p = (checkShipDestroyed(BattleshipGUI.patrol, "Patrol Boat"))?true:false;
		}
	}
	
	
	//Show the player the area the radar is going to cover and asks them whether they really want to use one of their radars there.
	public static void confirmRadar(int x, int y) {
		if (radars == 0) {
			JOptionPane.showMessageDialog(BattleshipGUI.frame, "Sorry, you are out of radars to use.", "Radar Supply Ran Out", 2);
		}
		else {			
			colorRadar(x, y, Color.YELLOW);
			int ryesno = JOptionPane.showConfirmDialog(
					BattleshipGUI.frame, "You are about to use a radar at this location. Is this okay?", "Use Radar?", JOptionPane.YES_NO_OPTION);
			
			if (ryesno == 0) executeRadar(x, y);
			else cancelRadar(x, y);
		}
		
	}
	
	
	//Checks the area confirmed by the player for any ships.
	private static void executeRadar(int x, int y) {
		//This arraylist is used to store the number of ships detected by any single radar attempt
		ArrayList<String> shipsDetected = new ArrayList<String>();
		
		//The boolean alreadyDetected is used so that the radar does not count the same ship twice
		//For example, if 2 squares on the radar are active, but are occupied by the same ship, the radar counts only 1 ship, not 2.
		boolean alreadyDetected = false;
		
		for (int i=x-1; i<=x+1; i++) {
			for (int j=y-1; j<=y+1; j++) {
				//Check the selected area for active/unsunk ships
				try {
					if (seaZone[i][j] != "O" && seaZone[i][j] != "H" && seaZone[i][j] != "M") {
						//if the radar finds a ship symbol, this is executed 
						alreadyDetected = false;
						//If the symbol found is already in arraylist detected, don't add it in again because it has already been counted
						for (String s : shipsDetected) {
							if (s == seaZone[i][j]) {
								alreadyDetected = true;
								break;
							}
						}
						//If the symbol found is not in detected, add it in
						if (!alreadyDetected) {
							shipsDetected.add(seaZone[i][j]);
						}
					}
				//A try catch is used so that no exceptions are thrown if the radar is used at corners and edges.
				//The catch block does not need to do anything aside from exist
				} catch (Exception f) {}
			}
		}
		//Color the board according to whether any ships were detected or not
		if (shipsDetected.size() >= 1) {
			colorRadar(x, y, Color.GREEN);
		} else {
			colorRadar(x, y, Color.RED);
		}
		//Inform the user how many ships were detected
		radarResult(shipsDetected);
		//Update the number of radars remaining and show it to the user
		radars--;
		updateradars();
	}
	
	
	//Informs the user whether or not any ships were detected, and if any were, then how many (only upto three)
	public static void radarResult(ArrayList<String> shipsDetected) {
		if (shipsDetected.size() == 0) {
			BattleshipGUI.msgprnt = "Radar did not detect anything.";
			BattleshipGUI.displayText();
		}
		else if (shipsDetected.size() == 1) {
			BattleshipGUI.msgprnt = "Radar detected a ship within proximity!";
			BattleshipGUI.displayText();
		}
		else if (shipsDetected.size() == 2) {
			BattleshipGUI.msgprnt = "Radar detected two ships within proximity!";
			BattleshipGUI.displayText();
		}
		else if (shipsDetected.size() > 2) {
			BattleshipGUI.msgprnt = "Radar detected multiple ships in the area!";
			BattleshipGUI.displayText();
		}
	}
	
	
	//If the player changes their mind about wanting to use a radar at a particular spot
	private static void cancelRadar(int x, int y) {
		//Remove the yellow highlight from the board
		colorRadar();
		
		BattleshipGUI.msgprnt = "Radar cancelled...";
		BattleshipGUI.displayText();
	}
	
	
	//Colors the area of the board according to the new input from the user
	//Also saves these changes into radarDetections
	private static void colorRadar(int x, int y, Color col) {
		for (int i=x-1; i<=x+1; i++) {
			for (int j=y-1; j<=y+1; j++) {
				try {
					BattleshipGUI.mapmain[i][j].setBackground(col);
					if (col == Color.GREEN) {
						radarDetections[i][j] = 1;
					}
					else if (col == Color.RED) {
						radarDetections[i][j] = 2;
					}
				//The exception handler catches cases that are out of bounds of the board (since x+1 and y+1 at the edge of the board will go out of bounds)
				//It does not need to do anything other than just exist so that no Exceptions are thrown that halt the program.
				} catch (Exception e) {
					
				}
			}
		}
	}
	
	//Recolors the board according to the data already in radarDetections
	//This method is called when the user switches to radar mode in the game, or to recolor the yellow squares if the user cancels a radar attempt
	public static void colorRadar() {
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				if (BattleshipSystem.radarDetections[i][j] == 1) {
					BattleshipGUI.mapmain[i][j].setBackground(Color.GREEN);
				}
				else if (BattleshipSystem.radarDetections[i][j] == 2) {
					BattleshipGUI.mapmain[i][j].setBackground(Color.RED);
				}
				else if (BattleshipSystem.radarDetections[i][j] == 0) {
					//This changes all buttons that were colored yellow back to their original light blue color.
					BattleshipGUI.mapmain[i][j].setBackground(BattleshipGUI.btndefault);
				}
			}
		}
	}
	
	
	//Changes the score on the GUI according to the score variable
	public static void updatescore() {
		BattleshipGUI.scorebd.setText("        Score: " + BattleshipSystem.score);
	}
	
	
	//Changes the warning points on the GUI according to the warnings array
	private static void updatewarnings() {
		BattleshipGUI.warnbd.setText("    Warnings: " + BattleshipSystem.warnings[0] + "/" + BattleshipSystem.warnings[1]);
	}
	
	
	//Changes the radars remaining on the GUI according to the radars variable
	private static void updateradars() {
		BattleshipGUI.scorebd.setText("  Radars left: " + BattleshipSystem.radars);
	}
	
	
	//Disables all the 10 by 10 board buttons to stop the user from being able to make any input
	public static void disableBoardBtns(boolean yes) {
		if (yes) {
			for (int i=0; i<10; i++) {
				for (int j=0; j<10; j++) {
					BattleshipGUI.mapmain[i][j].setEnabled(false);
				}
			}
		}
		else {
			for (int i=0; i<10; i++) {
				for (int j=0; j<10; j++) {
					BattleshipGUI.mapmain[i][j].setEnabled(true);
				}
			}
		}

	}
	
	
	
	//Calculate the greatest common factor. This is used for the ratio of hits to misses function that displays user stats at the end of the game
	//Parameters are the hits and then the misses (in that order). The greatest common factor is returned. 
	private static int GCF(int x, int y) {
		int greater = (x > y)?x:y;
		
		while (greater != 0) {
			if (x % greater == 0 && y % greater == 0) {
				return greater;
			}
			greater--;
		}
		
		return 1;
	}
	
	//Display user stats
	//Called at the end of the game, but can theoretically be called at any point during the game as well
	private static void hitsToMisses() {
		int gcf = GCF(hits, misses), hits1 = hits / gcf, misses1 = misses / gcf;
		
		JOptionPane.showMessageDialog(BattleshipGUI.frame, "Number of shots fired: " + shotsFired + "\nHit to Miss Ratio: " + 
		hits1 + ":" + misses1 + "\nScore: " + score + "\nRadars used: " + (4 - radars), "Your Statistics", 1);
	}
	

	//Resets the game if the player chooses to replay. If they choose not to, it allows them to stay until they quit of their own accord.
	//If they change their mind about not wanting to replay, this method makes sure they have a replay button too
	public static void replay(boolean choice) {
		if (choice) {
			restartSysVars();
			BattleshipGUI.restartGuiVars();
			BattleshipGUI.frame.setVisible(false);
			
			//get the current location and set it to the location var, making the new game start at the same spot on the screen as the old game
			BattleshipGUI.location = BattleshipGUI.frame.getLocation();
			new BattleshipGUI();
			BattleshipGUI.msgprnt = "Computer System: \"I see you chose to replay, you have guts! Let's go!\"";
			BattleshipGUI.displayText();
		}
		else {
			BattleshipGUI.msgprnt = "Thank you for playing!\n\nHave a nice day!";
			BattleshipGUI.displayText();
			BattleshipGUI.instr.setText("Replay");
		}
	}
	
	
	//reset all the necessary variables of this class
	private static void restartSysVars() {
		score = 0; radars = 4; warnings[0] = 0; warnings[1] = 3;
		shotsFired = 0; hits = 0; misses = 0;
		
		a = false; b = false; s = false; d = false; p = false;
		
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				radarDetections[i][j] = 0;
				}
			}
		for (int i=0; i<10; i++) {
			for (int j=0; j<10; j++) {
				seaZone[i][j] = "O";
				}
			}
		
		sunkShips.clear(); unsunkShips.clear();
	}
	
	

}


