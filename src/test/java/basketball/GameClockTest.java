package basketball;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
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
	// Automated test to test multiple various time inputs | Part of pull req #4
	@Test
	public void gameClockGetTenthsTimeAutomatedTest(){
		Random rand = new Random();
      	for(int i = 0; i < 1000; i++){
        	int rand_int = rand.nextInt(1000);
        	GameClock clock_test = new GameClock(rand_int);
        	assertEquals(rand_int, clock_test.getTenthsTime());
    	}
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
	// Part of pull request #4 | #5
	@Test(expected = IllegalArgumentException.class)
	public void gameClockCheckTimeTest(){
		GameClock clock_test = new GameClock(-40);
	}
	@Test
	public void gameClockAddTimeTest0(){
		GameClock clock_test = new GameClock(15);
		clock_test.addTime(15);
		assertEquals(30, clock_test.getTenthsTime());
	}
	@Test
	public void gameClockAddTimeTest1(){
		GameClock clock_test = new GameClock(30);
		clock_test.addTime(15);
		String output_string = "4.5";
		assertEquals(output_string, clock_test.getTime());
	}
	@Test
	public void gameClockAddTimeAutomatedTest(){
		Random rand = new Random();
      	for(int i = 0; i < 1000; i++){
        	int rand_int = rand.nextInt(1000);
        	GameClock clock_test = new GameClock(rand_int);
        	clock_test.addTime(10);
        	int total_time = rand_int + 10;
        	assertEquals(total_time, clock_test.getTenthsTime());
    	}
	}
	@Test
	public void gameClockIsRunningTestTrue(){
		GameClock clock_test = new GameClock(50);
		clock_test.running = true;
		assertTrue(clock_test.isRunning());
	}
	@Test
	public void gameClockIsRunningTestFalse(){
		GameClock clock_test = new GameClock(50);
		clock_test.running = false;
		assertFalse(clock_test.isRunning());	
	}
	@Test
	public void gameClockSetTimeTest(){
		GameClock clock_test = new GameClock(200);
		clock_test.setTime(20);
		String new_time = "2.0";
		assertEquals(new_time, clock_test.getTime());
	}
	@Test(expected = IllegalStateException.class)
	public void gameClockSubtractTimeTest0(){
		GameClock clock_test = new GameClock(10);
		clock_test.subtractTime(20);
	}
	@Test
	public void gameClockSubtractTimeTest1(){
		GameClock clock_test = new GameClock(500);
		clock_test.subtractTime(200);
		int new_time = 300;
		assertEquals(new_time, clock_test.getTenthsTime());
	}
	@Test
	public void gameClockSubtractTimeTest2(){
		GameClock clock_test = new GameClock(500);
		clock_test.subtractTime(200);
		String new_time = "30.0";
		assertEquals(new_time, clock_test.getTime());
	}
	// Switched private function to public for testing purposes
	@Test
	public void gameClockPrivateGetTimeTest0(){
		GameClock clock_test = new GameClock(0);
		// clock_test.getTime(599);
		String time_value = "00:59";
		assertEquals(time_value, clock_test.getTime(599));
	}
	@Test
	public void gameClockPrivateGetTimeTest1(){
		GameClock clock_test = new GameClock(0);
		String time_value = "00:025";
		assertEquals(time_value, clock_test.getTime(25));
	}
	@Test
	public void gameClockPrivateGetTimeTest2(){
		GameClock clock_test = new GameClock(0);
		String time_value = "017:05";
		assertEquals(time_value, clock_test.getTime(1025));	
	}
	@Test
	public void gameClockPrivateGetTimeTest3(){
		GameClock clock_test = new GameClock(0);
		String time_value = "150:00";
		assertEquals(time_value, clock_test.getTime(9000));	
	}
	@Test
	public void gameClockPrivateGetTimeTest4(){
		GameClock clock_test = new GameClock(0);
		String time_value = "209:1";
		assertEquals(time_value, clock_test.getTime(12541));	
	}
	@Test
	public void gameClockStartTimerTest0(){
		GameClock clock_test = new GameClock(10);
		clock_test.startTimer(); 
		assertTrue(clock_test.running);
	}
	@Test
	public void gameClockStartTimerTest1(){
		GameClock clock_test = new GameClock(15);
		clock_test.startTimer(); 
		assertEquals(1, clock_test.getPeriod());
	}
	@Test
	public void gameClockStopTimerTest(){
		GameClock clock_test = new GameClock(10);
		clock_test.startTimer();
		clock_test.stopTimer();
		assertFalse(clock_test.running);
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