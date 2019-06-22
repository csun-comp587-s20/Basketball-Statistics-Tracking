// Alex Eidt
// Basketball Statistics Tracking Program
// GameSettings Class
// GameSettings keeps track of all the settings used for the actual game/game play and the
// settings used to format the program (background color, etc.).

import java.awt.*;
import java.awt.event.*;

public class GameSettings extends BasketballMain implements ActionListener {
	
	private Color background_color; // Background color used throughout the program
	private int number_of_starters; // Number of starters for the game
	private int personal_fouls; // Number of personal fouls allowed
	private int technical_fouls; // Number of technical fouls allowed
	private int current_period; // The current period of the game (1st quarter/half, 2nd quarter/half ...)
	private boolean period_type; // Period Type ---> Quarters: true / Halves: false
	private boolean auto_correct_names; // Whether names should be auto capitalized
	private double game_length; // The length of each period
	private double time_remaining; // The time remaining in the current period
	private int timeouts; // The number of timeouts for the team
	
    private static final long serialVersionUID = 1L;
    
    // Post: Set the settings of the game to resemble those of a typical NBA
    //       basketball game with four 12-minute quarters, 6 personal and 2 technical
    //       fouls allowed per player.
    //       The background colors are set to white and all borders and text colors are
    //       set to black.
    public GameSettings() {
    	this.background_color = DEFAULT_BACKGROUND_COLOR;
    	this.number_of_starters = DEFAULT_NUMBER_OF_STARTERS;
    	this.personal_fouls = DEFAULT_PERSONAL_FOULS_ALLOWED;
    	this.technical_fouls = DEFAULT_TECHNICAL_FOULS_ALLOWED;
    	this.auto_correct_names = DEFAULT_AUTO_CORRECT_NAMES;
    	this.period_type = DEFAULT_PERIOD_TYPE;
    	this.game_length = DEFAULT_GAME_LENGTH;
    	this.time_remaining = DEFAULT_TIME_REMAINING;
    	this.current_period = DEFAULT_CURRENT_PERIOD;
    	this.timeouts = DEFAULT_TIMEOUTS;
    }
    
    // Post: Returns an object representing the setting linked with a certain integer
    //       defined by the "Settings" constants in the Constants superclass.
    //       If the parameter 'choose' is not linked with a setting, the method returns null.
    protected Object getSetting(Setting choose) {
    	switch(choose) {
    	case BACKGROUND_COLOR:
    		return this.background_color;
    	case NUMBER_OF_STARTERS:
    		return this.number_of_starters;
    	case PERSONAL_FOULS:
    		return this.personal_fouls;
    	case TECHNICAL_FOULS:
    		return this.technical_fouls;
    	case CURRENT_PERIOD:
    		return this.current_period;
    	case PERIOD_TYPE:
    		return this.period_type;
    	case AUTO_CORRECT_NAMES:
    		return this.auto_correct_names;
    	case GAME_LENGTH:
    		return this.game_length;
    	case TIME_REMAINING:
    		return this.time_remaining;
    	case TIMEOUTS:
    		return this.timeouts;
    	default:
    		return null;	
    	}
    }
    
    // Post: Sets the setting linked with the parameter 'choose' with the Object 'data'.
    //       THe Object is cast to the correct type depending on which setting is being set.
    protected void setSetting(Object data, Setting choose) {
    	switch (choose) {
    	case BACKGROUND_COLOR:
    		this.background_color = (Color) data;
    		break;
    	case NUMBER_OF_STARTERS:
    		this.number_of_starters = (int) data;
    		break;
    	case PERSONAL_FOULS:
    		this.personal_fouls = (int) data;
    		break;
    	case TECHNICAL_FOULS:
    		this.technical_fouls = (int) data;
    		break;
    	case CURRENT_PERIOD:
    		this.current_period = (int) data;
    		break;
    	case PERIOD_TYPE:
    		this.period_type = (boolean) data;
    		break;
    	case AUTO_CORRECT_NAMES:
    		this.auto_correct_names = (boolean) data;
    		break;
    	case GAME_LENGTH:
    		this.game_length = (double) data;
    		break;
    	case TIME_REMAINING:
    		this.time_remaining = (double) data;
    		break;
    	case TIMEOUTS:
    		this.timeouts = (int) data;
    		break;
    	default:
    		break;
    	}
    }
    
    private static final String DIVIDER = "_";
    
    // Post: Returns a String representation of the game settings used when writing
    //       to the game file.
    public String toString() {
    	return this.number_of_starters + DIVIDER + this.personal_fouls + DIVIDER + 
    		   this.technical_fouls + DIVIDER + this.period_type + DIVIDER + 
    		   this.game_length + DIVIDER + this.time_remaining +  DIVIDER + this.current_period +
    		   DIVIDER + this.timeouts;
    }
    
    public void actionPerformed(ActionEvent arg0) {       
    }
}



