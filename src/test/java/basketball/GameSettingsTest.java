package basketball;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.*;
import org.junit.Test;
import java.util.*;
import java.awt.*;

public class GameSettingsTest{
	@Test
	public void GameSettingsToString(){
		GameSettings set = new GameSettings();
		assertEquals("5_6_2_2_1_true_12.0_12.0_5.0_0_7", set.toString());
	}

	@Test
	public void GameSettingsTestLoop(){
		GameSettings set = new GameSettings();
		Random rand = new Random();
		int i = rand.nextInt(7);
		for(Constants.Setting test : Constants.Setting.values()){
			if(test == Constants.Setting.BACKGROUND_COLOR){
				set.setSetting(Color.black, test);
				assertEquals(Color.black, set.getSetting(test));
			}
			else if(test == Constants.Setting.PERIOD_TYPE){
				set.setSetting(false, test);
				assertEquals(false, set.getSetting(test));
			}
			else if(test == Constants.Setting.GAME_LENGTH ||
					test == Constants.Setting.OVERTIME_LENGTH ||
					test == Constants.Setting.TIME_REMAINING){
				set.setSetting((double)i, test);
				assertEquals((double) i, set.getSetting(test));
			}
			else if(test == Constants.Setting.FONT_COLOR || 
					test == Constants.Setting.TEXT_BORDER_COLOR ||
				 	test == Constants.Setting.FONT_TYPE){
				continue;
			}
			else{
				set.setSetting(i, test);
				assertEquals(i, set.getSetting(test));
			}
		}
	}
	


}