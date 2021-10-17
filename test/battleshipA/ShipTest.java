package battleshipA;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ShipTest {

	private Ship ship1;
	private Ship ship2;
	
	@BeforeEach
	void setUp() throws Exception {
		ship1 = new Ship(5, 5, "A");
		ship2 = new Ship(3, 3, "S");
	}
	
	@Test
	public void canCreateNewShips() {
		assertNotNull(ship1);
		assertNotNull(ship2);
	}
	
	@Test
	public void canGetShipSpots() {
		assertEquals(5, ship1.GetSpots());
		assertEquals(3, ship2.GetSpots());
	}
	
	@Test
	public void canChangeShipSpots() {
		ship1.SetSpots(2);
		ship2.SetSpots(4);
		assertEquals(2, ship1.GetSpots());
		assertEquals(4, ship2.GetSpots());
	}
	
	@Test
	public void canGetShipLength() {
		assertEquals(5, ship1.GetLength());
		assertEquals(3, ship2.GetLength());
	}
	
	@Test
	public void canGetShipSymbols() {
		assertEquals("A", ship1.GetSymbol());
		assertEquals("S", ship2.GetSymbol());
	}

}
