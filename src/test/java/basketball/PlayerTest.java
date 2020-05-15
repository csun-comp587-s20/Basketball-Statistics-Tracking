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
	@Test
	public void playerEqualsNullTest(){
		Player player_one = new Player("Karl");
		assertFalse(player_one.equals(null));
	}


	//Joseph Castro Tests
	@Test
	public void playerEqualsBool(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		Player player_two = new Player("Kobe", "Bryant", "Bryant");
		assertTrue(player_one.isEqual(player_two));
	}
	@Test
	public void playerEqualsBool1(){
		Player player_one = new Player("Lebron", "James", "James");
		Player player_two = new Player("Lebron", "James", "James");
		assertTrue(player_one.isEqual(player_two));
	}
	@Test
	public void playerFieldGoalPercent(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		assertEquals("50.0", player_one.getPercentage(2, 4));
	}
	@Test
	public void playerFieldGoalPercentZero(){
		Player player_one = new Player("Michael", "Jordan", "Jordan");
		assertEquals("0.0", player_one.getPercentage(0, 24));
	}
	@Test
	public void playerFieldGoalPercentPerfect(){
		Player player_one = new Player("Lebron", "James", "James");
		assertEquals("100", player_one.getPercentage(8, 8));
	}
	@Test(expected = IllegalArgumentException.class)
	public void playerFieldGoalPercentError(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		player_one.getPercentage(-3, -5);
	}
	@Test
	public void playerStatMap(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		assertEquals("FGM", player_one.mapStat("Made FG"));
	}
	@Test
	public void playerStatMap1(){
		Player player_one = new Player("Michael", "Jordan", "Jordan");
		assertEquals("Player", player_one.mapStat("Player"));
	}
	@Test
	public void playerFouledOut(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		player_one.setStat("PF", "6");
		assertTrue(player_one.hasFouledOut(6,0,0,0));
	}
	@Test
	public void playerTechEject(){
		Player player_one = new Player("Lebron", "James", "James");
		player_one.setStat("TF", "2");
		assertTrue(player_one.hasFouledOut(0,2,0,0));
	}
	@Test
	public void playerFlagrant1(){
		Player player_one = new Player("Lebron", "James", "James");
		player_one.setStat("PF", "4");
		player_one.setStat("TF", "1");
		player_one.setStat("FLGI", "2");
		assertTrue(player_one.hasFouledOut(6,2,2,2));
	}
	@Test
	public void playerFlagrant2(){
		Player player_one = new Player("Michael", "Jordan", "Jordan");
		player_one.setStat("PF", "4");
		player_one.setStat("TF", "1");
		player_one.setStat("FLGI", "1");
		player_one.setStat("FLGII", "2");
		assertTrue(player_one.hasFouledOut(6,2,2,2));
	}
	@Test
	public void playerStatLine(){
		Player player_one = new Player("Michael", "Jordan", "Jordan");
		player_one.add(true, "FGM");
		player_one.add(true, "FGM");
		player_one.setStat("FG%", "100");
		assertEquals("Michael Jordan: 4 Points | 0 Rebounds | FG%: 100 | 3P% 0.0 | FT%: 0.0", player_one.getStatLine());
	}
	@Test
	public void playerGetStat(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		player_one.add(true, "FGM");
		player_one.add(true, "FGM");
		player_one.setStat("FG%", "100");
		assertEquals("100", player_one.getStat("FG%"));
	}
	@Test
	public void playerGetStat1(){
		Player player_one = new Player("Lebron", "James", "James");
		player_one.add(true, "3PM");
		player_one.add(true, "3PM");
		player_one.add(true, "3PA");
		player_one.add(true, "3PA");
		player_one.setStat("3P%", "50");
		assertEquals("50.0", player_one.getStat("3P%"));
		assertEquals("2", player_one.getStat("Missed 3pt FG"));
	}
	@Test
	public void playerGetStat2(){
		Player player_one = new Player("Michael", "Jordan", "Jordan");
		assertEquals("Michael_Jordan", player_one.getStat("Player"));
	}
	@Test
	public void playerGetStat3(){
		Player player_one = new Player("Michael", "Jordan", "Jordan");
		player_one.add(true, "FGM");
		player_one.add(true, "FGA");
		assertEquals("1", player_one.getStat("Missed FG"));
	}
	@Test
	public void playerGetStat4(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		player_one.setStat("MIN", "40");
		assertEquals("40", player_one.getStat("MIN"));
	}
	@Test
	public void playerGetStat5(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		player_one.add(true, "FTM");
		player_one.add(true, "FTM");
		player_one.setStat("FTM", "2");
		assertEquals("2", player_one.getStat("FTM"));
	}
	@Test
	public void playerGetStat6(){
		Player player_one = new Player("Kobe", "Bryant", "Bryant");
		player_one.setStat("Missed Free Throw", "2");
		assertEquals("2", player_one.getStat("Missed Free Throw"));
	}

}