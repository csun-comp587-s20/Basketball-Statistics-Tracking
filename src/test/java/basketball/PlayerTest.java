package basketball;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
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
	public void playerSetNameTest(){
		Player new_player = new Player("");
		String first_name = "Melo";
		new_player.setName(first_name);
		assertEquals(first_name, new_player.getName());
	}
}