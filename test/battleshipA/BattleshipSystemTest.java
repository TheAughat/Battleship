package battleshipA;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BattleshipSystemTest {

	@BeforeEach
	void setUp() throws Exception {
	}
	
	private void setUpShipList() {
		BattleshipSystem.unsunkShips.add("Ship 1");
		BattleshipSystem.unsunkShips.add("Ship 2");
		BattleshipSystem.unsunkShips.add("Third Ship");
	}
	
	@Test
	public void canCreateListOfShips() {
		setUpShipList();
		assertEquals("[Ship 1, Ship 2, Third Ship]", BattleshipSystem.unsunkShips.toString());
	}
	
	//We don't need to call setUpShipList() in this method as long as canCreateListOfShips() runs before this
	@Test
	public void canUpdateListOfShips() {
		BattleshipSystem.unsunkShips.remove("Ship 2");
		assertEquals("[Ship 1, Third Ship]", BattleshipSystem.unsunkShips.toString());
		BattleshipSystem.unsunkShips.clear();
		assertEquals("[]", BattleshipSystem.unsunkShips.toString());
	}
}
