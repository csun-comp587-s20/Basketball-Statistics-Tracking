package basketball;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class UndoTest{
	@Test
	public void undoGetNameTest(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Undo undo = new Undo(player_one, "FGM", "4:40");
		assertEquals(player_one, undo.getPlayer());
	}
	@Test
	public void undoAstPlayer(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo = new Undo(player_one, "FGM", "10:40");
		undo.setAstPlayer(player_two);
		assertEquals(player_two, undo.getAstPlayer());
	}
	@Test
	public void undoGetStatFromPlayer(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Undo undo = new Undo(player_one, "FGM", "4:40");
		assertEquals("FGM", undo.getStatFromPlayer());
	}
	@Test
	public void undoGetTimeOf(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Undo undo = new Undo(player_one, "FGM", "10:40");
		assertEquals("10:40", undo.getTimeOf());
	}
	@Test
	public void undohasAssited(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo = new Undo(player_one, "FGM", "4:40");
		undo.setAstPlayer(player_two);
		assertTrue(undo.hasAssistPlayer());
	}
	@Test
	public void undoToStringArr(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo = new Undo(player_one, "FGM", "10:40");
		undo.setAstPlayer(player_two);
		ArrayList<String> test = new ArrayList<String>();
		test.add("Kobe");
		test.add("Bryant");
		test.add("Bryant");
		test.add("Lebron");
		test.add("James");
		test.add("James");
		test.add("FGM");
		test.add("10:40");
		assertEquals(test, undo.toStringArray());
	}
    @Test
	public void undoToString(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo = new Undo(player_one, "FGM", "10:40");
		undo.setAstPlayer(player_two);
		assertEquals("FGM - Assisted by: Lebron James 10:40", undo.toString());
	}            
	@Test
	public void undoToString1(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo = new Undo(player_one, "Substitution", "5:50");
		undo.setAstPlayer(player_two);
		assertEquals("Substitution for: James 5:50", undo.toString());
	}
	@Test
	public void undoToString2(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Undo undo = new Undo(player_one, "3PM", "3:20");
		assertEquals("3PM 3:20", undo.toString());
	}
	@Test
	public void undoToCompareTo(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo_one = new Undo(player_one, "3PM", "DONE");
		Undo undo_two = new Undo(player_two, "3PM", "DONE");
		assertEquals(1, undo_one.compareTo(undo_two));
	}
	@Test
	public void undoToCompareTo1(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo_one = new Undo(player_one, "3PM", "OT");
		Undo undo_two = new Undo(player_two, "3PM", "4:40");
		assertEquals(-1, undo_one.compareTo(undo_two));
	}
	@Test
	public void undoToCompareTo2(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo_one = new Undo(player_one, "3PM", "4:40");
		Undo undo_two = new Undo(player_two, "3PM", "OT");
		assertEquals(1, undo_one.compareTo(undo_two));
	}
	@Test
	public void undoToCompareTo3(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		Undo undo_one = new Undo(player_one, "3PM", "2 3:30");
		Undo undo_two = new Undo(player_two, "3PM", "4 4:30");
		assertEquals(2, undo_one.compareTo(undo_two));
	}
	@Test
	public void undoGetTime(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Undo undo_one = new Undo(player_one, "3PM", "4:59");
		assertEquals(299, undo_one.getTime("4:59"));
	}
	@Test
	public void undoComparePeriod(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Undo undo_one = new Undo(player_one, "3PM", "4:59");
		assertEquals(60, undo_one.comparePeriods("1", "1", "3:00", "4:00", ""));
	}
	@Test
	public void undoComparePeriod1(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Undo undo_one = new Undo(player_one, "3PM", "4:59");
		assertEquals(1, undo_one.comparePeriods("2", "3", "3:00", "4:00", ""));
	}

}