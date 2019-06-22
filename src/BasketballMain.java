// Alex Eidt
// Basketball Statistics Tracking Program
// Basketball Main
// Starts the Basketball Statistics Tracking Program.

import java.io.*;
import java.util.*;

@SuppressWarnings("serial")
public abstract class BasketballMain extends Constants {

	public static void main(String[] args) throws FileNotFoundException {
		// Default settings of an NBA Game used for initial tracking
    	GameSettings settings = new GameSettings(); 
    	// The main menu panel used when the program launches
        GetPlayersPanel mainMenu = new GetPlayersPanel(settings);
        mainMenu.createTextField();
        mainMenu.createAllButtons();
        mainMenu.addElements();
        mainMenu.frame();
    }
    
	// Parameters:
	// 'list': List of players that are part of the game
	// 'undo': List of Undos (only used for scanned in games), otherwise this is empty for new games
	// 'fileName': The file name of the file where the game data is stored
	// 'scan': Whether a file is being scanned in or not
	// 'settings': The settings used for this game
    public static void run(ArrayList<Player> list, ArrayList<Undo> undo, String fileName, 
    					   boolean scan, GameSettings settings) {  
    	int numStarters = (int) settings.getSetting(Setting.NUMBER_OF_STARTERS);
    	if (!scan) { // If a brand new game is being started
    		if (list.size() > numStarters) { // If the number of players exceeds the number of starters
    			// Open up the Starters Selection Panel to select starters
            	StartersPanel getStarters = new StartersPanel(list, fileName, settings);
            	getStarters.createAllButtons();
            	getStarters.addElements();
            	getStarters.frame();
        	} else { // If number of players equals number of starters
        		startTracking(list, new ArrayList<Player>(0), undo, fileName, settings);
        	}
    	} else { // If a game is being scanned in from a file
    		if (list.size() == numStarters) { // If number of players equals number of starters
        		startTracking(list, new ArrayList<Player>(0), undo, fileName, settings);
    		} else { // If the number of players exceeds the number of starters
        		ArrayList<Player> bench = new ArrayList<Player>();
        		ArrayList<Player> starters = new ArrayList<Player>();       		
        		for (Player player : list) {
        			if (player.isStarter()) {
        				starters.add(player);
        			} else {
        				bench.add(player);
        			}
        		}
        		startTracking(starters, bench, undo, fileName, settings);
    		}
    	}  	
    }
    
    // Parameters:
	// 'starters': List of players that are starting the game
    // 'bench': List of players that are starting the game on the bench
	// 'undo': List of Undos (only used for scanned in games), otherwise this is empty for new games
	// 'fileName': The file name of the file where the game data is stored
    // 'settings': The settings used for this game
    public static void startTracking(ArrayList<Player> starters, ArrayList<Player> bench, 
    								 ArrayList<Undo> undo, String fileName, GameSettings settings) {
        ManagementPanel manageGame = new ManagementPanel(starters, bench, undo, fileName, settings);
        manageGame.createTableButton();
        if (bench.isEmpty()) {
            manageGame.createTeamButtons();
        } else {
            manageGame.createPlayerButtons();
        }
        manageGame.createUndoButton();
        manageGame.createDoneButton();
        manageGame.createStartStopButton();
        manageGame.createTimeoutButton();
        //manageGame.createSettingsButton();
        manageGame.addElements();
        manageGame.frame();
    }
}
