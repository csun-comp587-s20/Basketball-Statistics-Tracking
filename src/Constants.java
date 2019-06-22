// Alex Eidt
// Basketball Statistics Tracking Program
// Constants Class
// All the constants used in the program.

import java.awt.*;
import java.util.*;
import javax.swing.JFrame;

public abstract class Constants extends JFrame {

	protected static final long serialVersionUID = 1L;
	
	/*
	 * ************************************************
	 * *		         TEXT ARRAYS                  *
	 * ************************************************
	 * These are the arrays with the statistic names and
	 * abbreviations used in basketball. 
	 * STATISTICS are all the statistics kept track of by this program
	 * STATISTIC_ABBREVIATIONS are the abbreviated statistic names used
	 * STATISTIC_ENTIRE_WORDS are all statistics spelled out entirely with no abbreviations
	 * 		-Necessary for the abbreviation explanations found in the InstructionsPanel class.
	 * STATISTIC_DISPLAY_WORDS is an extension of STATISTIC_ENTIRE_WORDS but has the calculated statistics
	 * (Percentages and total rebounds) abbreviated for display purposes.
	 * GAME_PERIODS is used for the GameClock to cycle through periods. Used for both Halves and Quarters.
	 */
    protected static final String[] STATISTICS = {"Made FG", 
    											  "Missed FG", 
    											  "Made 3pt FG", 
    											  "Missed 3pt FG", 
    											  "Made Free Throw", 
    											  "Missed Free Throw", 
    											  "Assist", 
    											  "Offensive Rebound", 
    											  "Defensive Rebound", 
    											  "Turnover", 
    											  "Steal", 
    											  "Block", 
    											  "Personal Foul", 
    											  "Technical Foul",
    											  "Flagrant I Foul",
    											  "Flagrant II Foul"};
    protected static final String[] STATISTIC_ABBREVIATIONS = {"Player", 
    														   "MIN", 
    														   "PTS", 
    														   "FGM", 
    														   "FGA", 
    														   "FG%", 
    														   "3PM", 
    														   "3PA", 
    														   "3P%", 
    														   "FTM", 
    														   "FTA", 
    														   "FT%", 
    														   "OREB", 
    														   "DREB", 
    														   "REB", 
    														   "AST", 
    														   "TOV", 
    														   "STL", 
    														   "BLK", 
    														   "PF", 
    														   "TF",
    														   "FLGI",
    														   "FLGII"};  
    protected static final String[] STATISTIC_ENTIRE_WORDS = {"Player", 
    														  "Minutes", 
    														  "Points", 
    														  "Made Field Goal", 
    														  "Missed Field Goal", 
    														  "Field Goal Percentage", 
    														  "Made 3 Pointer", 
    														  "Missed 3 Pointer", 
    														  "Three Point Percentage", 
    														  "Made Free Throw", 
    														  "Missed Free Throw", 
    														  "Free Throw Percentage", 
    														  "Offensive Rebound", 
    														  "Defensive Rebound", 
    														  "Rebound", 
    														  "Assist", 
    														  "Turnover", 
    														  "Steal", 
    														  "Block", 
    														  "Personal Foul", 
    														  "Technical Foul",
    														  "Flagrant Level I Foul",
    														  "Flagrant Level II Foul"};
	protected static final String[] STATISTIC_DISPLAY_WORDS = {"Player", 
															   "Minutes", 
															   "Points", 
															   "Made FG", 
															   "Missed FG", 
															   "FG%", 
															   "Made 3pt FG", 
															   "Missed 3pt FG", 
															   "3P%", 
															   "Made Free Throw", 
															   "Missed Free Throw", 
															   "FT%", 
															   "Offensive Rebound", 
															   "Defensive Rebound", 
															   "Rebound", 
															   "Assist", 
															   "Turnover", 
															   "Steal", 
															   "Block", 
															   "Personal Foul", 
															   "Technical Foul",
															   "Flagrant I Foul",
															   "Flagrant II Foul"};
	protected static final String[] GAME_PERIODS = {"1st", "2nd", "3rd", "4th"};
	
	/*
	 * ************************************************
	 * *			   DEFAULT CONSTANTS              *
	 * ************************************************
	 * These are the default game settings used for a game.
	 * They resemble the settings for an NBA game.
	 */
	protected static final Color DEFAULT_BACKGROUND_COLOR = Color.WHITE;
	protected static final Color DEFAULT_FONT_COLOR = Color.BLACK;
	protected static final Color DEFAULT_TEXT_BORDER_COLOR = Color.BLACK;
	protected static final String DEFAULT_FONT_TYPE = "Verdana";
	protected static final int DEFAULT_NUMBER_OF_STARTERS = 5;
	protected static final int DEFAULT_PERSONAL_FOULS_ALLOWED = 6;
	protected static final int DEFAULT_TECHNICAL_FOULS_ALLOWED = 2;
	protected static final int DEFAULT_CURRENT_PERIOD = 0;
	protected static final boolean DEFAULT_AUTO_CORRECT_NAMES = false;
	protected static final boolean DEFAULT_PERIOD_TYPE = true; // Default Period Type is Quarters
	protected static final double DEFAULT_GAME_LENGTH = 12.0;
	protected static final double DEFAULT_TIME_REMAINING = 12.0;
	protected static final int DEFAULT_TIMEOUTS = 7;
	protected static final int DEFAULT_FLAGRANT_1 = 2;
	protected static final int DEFAULT_FLAGRANT_2 = 1;
	
	/*
	 * ************************************************
	 * *		         MISC DATA                    *
	 * ************************************************
	 * Constants used for formatting purposes.
	 * YEAR, MONTH, and DAYNUM are used to write the date on new game files.
	 * COLOR_NAMES and COLORS have the Color name represented as a String (in COLOR_NAMES) that corresponds
	 * to the same Color of the same index in COLORS.
	 * ROUNDED_BORDERS is used mainly for button borders, default is no rounded borders.
	 * FILETYPE is the type of file the game data is written to.
	 */
    protected static final boolean RIGHT_HANDED = false;
	protected static final int SCREENWIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    protected static final int SCREENHEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    protected static final int BUTTON_HEIGHT = SCREENHEIGHT / 10;
    protected static final int FONT_SIZE = SCREENHEIGHT / 18;
    protected static final int QUARTERS = 4;
    protected static final int HALVES = 2;
    protected static final int END_OF_GAME = 10;
    protected static final int TIMER_SETTING = 100;
    protected static final Color FOULED_OUT_BUTTON_COLOR = Color.RED;
    protected static final Color BENCH_BUTTON_COLOR = Color.LIGHT_GRAY;
    protected static final int YEAR = Calendar.getInstance().get(Calendar.YEAR);
    protected static final int MONTH = Calendar.getInstance().get(Calendar.MONTH) + 1;
    protected static final int DAYNUM = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
    protected static final String[] COLOR_NAMES = {"Red", "Orange", "Yellow", "Green", "Cyan", "Blue", "Magenta", "Pink", "Light Gray", "Gray", "Dark Gray"};
    protected static final Color[] COLORS = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.PINK, Color.LIGHT_GRAY, Color.GRAY, Color.DARK_GRAY};
    protected static final boolean ROUNDED_BORDERS = false;
    protected static final String FILETYPE = "bball";
    
	/*
	 * ************************************************
	 * *		         ICON NAMES                   *
	 * ************************************************
	 * File names of all icons used for buttons
	 */
    protected static final String ICON_SYMBOL = "/";
    protected static final String SUBMIT_BUTTON_ICON = ICON_SYMBOL + "submit.png";
    protected static final String UNDO_BUTTON_ICON = ICON_SYMBOL + "undo.png";
    protected static final String START_BUTTON_ICON = ICON_SYMBOL + "start.png";
    protected static final String OLDGAMES_BUTTON_ICON = ICON_SYMBOL + "oldGames.png";
    protected static final String INSTRUCTIONS_BUTTON_ICON = ICON_SYMBOL + "instructions.png";
    protected static final String CLOSE_BUTTON_ICON = ICON_SYMBOL + "close.png";
    protected static final String SETTINGS_BUTTON_ICON = ICON_SYMBOL + "settings.png";
    protected static final String PLAYER_ICON = ICON_SYMBOL + "player.png";
    protected static final String SCORE_BUTTON_ICON = ICON_SYMBOL + "score.png";
    protected static final String PLAY_BUTTON_ICON = ICON_SYMBOL + "play.png";
    protected static final String PAUSE_BUTTON_ICON = ICON_SYMBOL + "pause.png";
    protected static final String DONE_BUTTON_ICON = ICON_SYMBOL + "done.png";
    protected static final String BOXSCORE_BUTTON_ICON = ICON_SYMBOL + "boxscore.png";
    protected static final String MAINMENU_BUTTON_ICON = ICON_SYMBOL + "mainMenu.png";
    protected static final String ABBREVIATIONS_BUTTON_ICON = ICON_SYMBOL + "abbreviations.png";
    protected static final String ROSTER_MANAGEMENT_ICON = ICON_SYMBOL + "rosterManagement.png";
    protected static final String TIMEOUT_BUTTON_ICON = ICON_SYMBOL + "timeout.png";
    protected static final String TEAMFOULS_ICON = ICON_SYMBOL + "fouls.png";
    protected static final int ICON_SIZE = 80;
    
	/*
	 * ************************************************
	 * *		         Settings                     *
	 * ************************************************
	 * Enumerated values used to identify certain settings
	 */
	enum Setting {
		 BACKGROUND_COLOR,
		 FONT_COLOR,
		 TEXT_BORDER_COLOR,
		 FONT_TYPE,
		 NUMBER_OF_STARTERS,
		 PERSONAL_FOULS,
		 TECHNICAL_FOULS,
		 CURRENT_PERIOD,
		 PERIOD_TYPE,
		 AUTO_CORRECT_NAMES,
		 GAME_LENGTH,
		 TIME_REMAINING,
		 TIMEOUTS;
	}
}
