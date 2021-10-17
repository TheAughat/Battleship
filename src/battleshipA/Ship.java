package battleshipA;

public class Ship {
	
	private int currentSpots;
	
	private int origLen;
	
	//symbol type: this identifies which ship was hit ("S" for submarine, "D" for destroyer, etc.)
	//These symbols are printed onto the core 2D array of the game, seaZone (which is in BattleshipSystem)
	private String type;
	
	//Takes in the number of spots occupied, original length, and the ship type 
	Ship(int cs, int ol, String tp){
		currentSpots = cs;
		origLen = ol;
		type = tp;
	}
	
	public int GetSpots() {
		return currentSpots;
	}
	
	public void SetSpots(int n) {
		currentSpots = n;
	}
	
	public int GetLength() {
		return origLen;
	}
	
	public String GetSymbol() {
		return type;
	}

}