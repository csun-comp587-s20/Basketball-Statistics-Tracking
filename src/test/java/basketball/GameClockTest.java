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

	// @Test(expected = IllegalArgumentException.class)
	// public void testCheckTime(){

	// }

	// @Test
	// public void testAddTime(){
	// 	GameClock clock = new GameClock();
	// 	int originalTime = clock.time;
	// 	clock.addTime(5);
	// 	assertEquals(clock.time, originalTime + 5);
	// }

	// Figure out how to test private, boolean, and void functions
	// To test private functions -> make them public (sol 1)
	// Another solution is to use reflection -> at run-time the program can say give me this private method on game clock and call it 
	// Advantage to reflection -> dont have to modify original code

	// To test void functions -> 
	// Might need to mock Timer, implement (interface) something that satisfies same interface that timer/timertask has
	// 
	// Need to make an interface for time
	// public interface TimerFactory{
	// 	public TimerInterface makeTimer();
	// }
	// public class RealTimerFactory implements TimerFactory{
	// 	public TimerInterface makeTimer(){
	// 		return new Timer();
	// 	}
	// // }
	// public class TestingTimerFactory implements TimerFactory{
	// 	public TimerInterface makeTimer(){
	// 		return new TestingTimer();
	// 	}
	// }
	// public GameClock(int duration, TimerFactory factory){

	// }
	// GameClock testingCLock = new GameClock(5, new TestingTimerFactory());
	// GameClock realClock = new GameClock(5, new RealTimerFactory());
	// can set the timer to a specific start/stop time

	// everytime you need a timer you ask for one from factory, which returns real timers
	// factory would return a timer from the factory everytime we need to run the test

	// https://www.oracle.com/technical-resources/articles/java/javareflection.html
	// https://en.wikipedia.org/wiki/Factory_method_pattern


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