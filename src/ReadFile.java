// Alex Eidt
// Basketball Statistics Tracking Program
// ReadFile Class
// Reads in an input .BBALL file and creates all necessary components that allow the user to resume tracking.

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class ReadFile extends Constants {

	private static final long serialVersionUID = 1L;
	
	private Scanner scanFile;				// Scanner for file
	private Scanner getLines;				// Scanner to get number of lines in the file
	private ArrayList<String[]> undoFile;   // List of undo's listed at bottom of game file
	private ArrayList<Player> players;		// List of players from game file 
	private GameSettings colors;			// Settings used for coloring/formatting of program
	
	// Post: Constructs a ReadFile given the 'fileName' of the input file and the 'settings'
	//       used to format the program.
	public ReadFile(String fileName, GameSettings settings) {
		try {
			this.scanFile = new Scanner(new File(fileName));
			this.getLines = new Scanner(new File(fileName));
			System.out.println(fileName + " was scanned");
		}
		catch (Exception e) {
			System.out.println("Error, file not found");
			System.out.println(e.getMessage());
		}
		if (!this.scanFile.hasNextLine()) {
			System.out.println(fileName + " is empty.");
			throw new IllegalArgumentException();
		}
		this.undoFile = new ArrayList<String[]>();
		this.players = new ArrayList<Player>();
		this.colors = settings;
	}

	// Indices of game data in 'boxscoreData'.
	private static final int NUM_STARTERS = 0;
	private static final int PERSONAL_FOULS = 1;
	private static final int TECHNICAL_FOULS = 2;
	private static final int PERIOD_TYPE = 3;
	private static final int GAME_LENGTH = 4;
	private static final int TIME_REMAINING = 5;
	private static final int CURRENT_PERIOD = 6;
	private static final int TIMEOUTS = 7;
	private static final String PERIOD_TYPE_ID = "false";
	
	// Post: Returns the GameSettings used in the game that is being scanned in from the file.
	public GameSettings getGameSettingsData() {
		String[] boxscoreData = this.scanFile.nextLine().split("_");
		GameSettings data = new GameSettings();
		data.setSetting(Integer.valueOf(boxscoreData[NUM_STARTERS]), Setting.NUMBER_OF_STARTERS);
		data.setSetting(Integer.valueOf(boxscoreData[PERSONAL_FOULS]), Setting.PERSONAL_FOULS);
		data.setSetting(Integer.valueOf(boxscoreData[TECHNICAL_FOULS]), Setting.TECHNICAL_FOULS);
		data.setSetting(!boxscoreData[PERIOD_TYPE].equals(PERIOD_TYPE_ID), Setting.PERIOD_TYPE);
		data.setSetting(Double.valueOf(boxscoreData[GAME_LENGTH]), Setting.GAME_LENGTH);
		data.setSetting((Double.valueOf(boxscoreData[TIME_REMAINING])), Setting.TIME_REMAINING);
		data.setSetting(Integer.valueOf(boxscoreData[CURRENT_PERIOD]), Setting.CURRENT_PERIOD);
		data.setSetting(Integer.valueOf(boxscoreData[TIMEOUTS]), Setting.TIMEOUTS);
		data.setSetting(this.colors.getSetting(Setting.BACKGROUND_COLOR), Setting.BACKGROUND_COLOR);
		data.setSetting(this.colors.getSetting(Setting.FONT_COLOR), Setting.FONT_COLOR);
		data.setSetting(this.colors.getSetting(Setting.TEXT_BORDER_COLOR), Setting.TEXT_BORDER_COLOR);
		data.setSetting(this.colors.getSetting(Setting.FONT_TYPE), Setting.FONT_TYPE);
		return data;
	}
	
	// Post: Returns a list of the players in the input file with all their statistical data.
	public ArrayList<Player> getPlayers() {
		int numPlayers = 0;
    	Pattern pattern = Pattern.compile("Player\\s.+_.+");
    	// Count the number of players in the input file
		while (this.getLines.hasNextLine()) {
			String line = this.getLines.nextLine();
            Matcher match = pattern.matcher(line);
			if (match.find()) {
				numPlayers++;
			}
		}
		this.getLines.close();	
		// Add all statistics for each player as they appear in the input file
		for (int i = 0; i < numPlayers; i++) {
			Player player = new Player("", "", "");
			for (int j = 0; j < STATISTIC_ABBREVIATIONS.length + 1; j++) {
				if (this.scanFile.hasNextLine()) {
					String line = this.scanFile.nextLine();
					Scanner lineScan = new Scanner(line);
					String columnName = lineScan.next();
					String stat = lineScan.next();
					player.setStat(columnName, stat);	
					lineScan.close();
				}
			}
			this.players.add(player);
		}		
		// Scan in all undo's into the 'undoFile' list
		while (this.scanFile.hasNextLine()) {
			String[] undoLine = this.scanFile.nextLine().split(", ");
			if (!undoLine[undoLine.length - 1].equals("DONE")) {
				this.undoFile.add(undoLine);
			}
		}		
		this.scanFile.close();	
		return this.players;
	}
	
	// Indices of data in each String[] in 'undoFile'.
	private static final int PLAYER_NAME = 0;
	private static final int PLAYER_LAST_NAME = 1;
	private static final int PLAYER_DISPLAY_NAME = 2;
	private static final int ASSISTPLAYER_NAME = 3;
	private static final int ASSISTPLAYER_LAST_NAME = 4;
	private static final int ASSISTPLAYER_DISPLAY_NAME = 5;
	private static final int STATISTIC = 6;
	private static final int TIME = 7;
	
	// Post: Returns a list of every undo stored in the input file.
	public ArrayList<Undo> getUndoArray() {
		ArrayList<Undo> undoArray = new ArrayList<Undo>();
		for (String[] line : this.undoFile) {
			Undo undo = null;
			for (Player player : this.players) {
				// Match player with undo
				boolean match = player.getName().equals(line[PLAYER_NAME]) 
						        && player.getLastName().equals(line[PLAYER_LAST_NAME]) 
						        && player.getDisplayName().equals(line[PLAYER_DISPLAY_NAME]);
				if (match) {
					undo = new Undo(player, line[STATISTIC], line[TIME]);
					for (Player astPlayer : this.players) {
						// Match AssistPlayer with undo
						boolean matchAst = astPlayer.getName().equals(line[ASSISTPLAYER_NAME]) 
								           && astPlayer.getLastName().equals(line[ASSISTPLAYER_LAST_NAME]) 
								           && astPlayer.getDisplayName().equals(line[ASSISTPLAYER_DISPLAY_NAME]);
						if (matchAst) {
							undo.setAstPlayer(astPlayer);
							break;
						}
					}
					break;
				}
			}
			undoArray.add(undo);
		}
		return undoArray;
	}
}
