package basketball;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;

public class PlayerTest{
	@Test
	public void playerGetNameTest(){
		Player new_player = new Player("Kobe", "Bryant", "Bryant");
		String output_string = "Kobe";
		assertEquals(output_string, new_player.getName());
	}
	@Test
	public void playerGetLastNameTest(){
		Player new_player = new Player("Lebron", "James", "James");
		String output_string = "James";
		assertEquals(output_string, new_player.getLastName());
	}
	@Test
	public void playerGetDisplayNameTest(){
		Player new_player = new Player("Rajon", "Rondo", "Rondo");
		String output_string = "Rondo";
		assertEquals(output_string, new_player.getDisplayName());
	}
	@Test
	public void playerAlternateConstructorTest0(){
		Player new_player = new Player("Allen");
		String output_string = "Allen";
		assertEquals(output_string, new_player.getName());
	}
	@Test
	public void playerAlternateConstructorTest1(){
		Player new_player = new Player("Allen");
		String output_string = "";
		assertEquals(output_string, new_player.getLastName());
	}
	@Test
	public void playerAlternateConstructorTest2(){
		Player new_player = new Player("Allen");
		String output_string = "";
		assertEquals(output_string, new_player.getDisplayName());
	}
	@Test
	public void playerSetNameTest0(){
		Player new_player = new Player("");
		String first_name = "Melo";
		new_player.setName(first_name);
		assertEquals(first_name, new_player.getName());
	}

	// Pull Req #3
	@Test
	public void playerSetNameTest1(){
		Player new_player = new Player("Paul", "Pierce", "Pierce");
		String first_name = "Raymond";
		new_player.setName(first_name);
		assertEquals(first_name, new_player.getName());
	}
	@Test
	public void playerSetDisplayNameTest0(){
		Player new_player = new Player("");
		String display_name = "Allen";
		new_player.setDisplayName(display_name);
		assertEquals(display_name, new_player.getDisplayName());
	}
	@Test
	public void playerSetDisplayNameTest1(){
		Player new_player = new Player("Ray", "Allen", "Allen");
		String display_name = "Lopez";
		new_player.setDisplayName(display_name);
		assertEquals(display_name, new_player.getDisplayName());
	}
	@Test
	public void playerCompareToTest0(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Kobe", "Bryant", "Bryant");
		assertEquals(0, player_one.compareTo(player_two));
	}
	@Test
	public void playerCompareToTest1(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		assertEquals(-1, player_one.compareTo(player_two));
	}
	@Test
	public void playerSetOnFloorTest(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		player_one.setOnFloor();
		assertTrue(player_one.isStarter());
	}
	@Test
	public void playerSetOffFloorTest(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		player_one.setOffFloor();
		assertFalse(player_one.isStarter());
	}
	@Test
	public void playerEqualsTest0(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Lebron", "James", "James");
		assertFalse(player_one.equals(player_two));
	}
	@Test
	public void playerEqualsTest1(){
		Player player_one = new Player("Lebron", "James", "James");
		Player player_two = new Player("Lebron", "James", "James");
		assertTrue(player_one.equals(player_two));
	}
	@Test
	public void playerEqualsTest2(){
		Player player_one = new Player("Lebron", "James", "James");
		// Player player_two = new Player("Lebron", "James", "James");
		assertTrue(player_one.equals(player_one));
	}
	@Test
	public void playerToString(){
		Player player_one = new Player("Michael", "Jordan", "Jordan");
		String output_string = "Michael Jordan";
		assertEquals(output_string, player_one.toString());
	}
}