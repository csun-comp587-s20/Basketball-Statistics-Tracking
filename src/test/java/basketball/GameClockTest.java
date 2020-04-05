package basketball;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.junit.Test;
import java.util.*;

public class GameClockTest{
	@Test
	public void gameClockGetPeriodTest0(){
		GameClock clock_test = new GameClock(20);
		assertEquals(1, clock_test.getPeriod());
	}
	@Test
	public void gameClockGetPeriodTest1(){

	}
	@Test
	public void gameClockGetTenthsTimeTest0(){
		GameClock clock_test = new GameClock(100);
		assertEquals(100, clock_test.getTenthsTime());
	}
	@Test
	public void gameClockGetTimeTest0(){
		GameClock clock_test = new GameClock(300);
		String output_string = "30.0";
		assertEquals(output_string, clock_test.getTime());
	}
	@Test
	public void gameClockGetTimeTest1(){
		GameClock clock_test = new GameClock(650);
		String output_string = "1:05";
		assertEquals(output_string, clock_test.getTime());
	}
	@Test
	public void gameClockGetTimeTest2(){
		GameClock clock_test = new GameClock(1450);
		String output_string = "2:25";
		assertEquals(output_string, clock_test.getTime());
	}
	// Figure out how to test private, boolean, and void functions

	// @Test
	// public void addTimeTest0(){
	// 	GameClock clock_test = new GameClock(0);
	// 	int test_time = 1000;
	// 	assertEquals(test_time, clock_test.getTenthsTime(clock_test.addTime(1000)));
	// }
	// @Test
	// public void gameClockIsRunningFalseTest(){
	// 	GameClock clock_test = new GameClock();
	// 	assertFalse(clock_test.isRunning());
	// }
}