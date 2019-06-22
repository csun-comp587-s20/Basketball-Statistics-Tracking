// Alex Eidt
// Basketball Statistics Tracking Program
// StartersPanel Class
// A window that opens once the 'Start Game' button is pressed in the GetPlayersPanel
// if the number of players entered is greater than the number of starters specified in the settings.
// Allows the user to select their starters for the game before beginning to track statistics. 

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class StartersPanel extends GUISettings {
	
	private static final long serialVersionUID = 1L;
	
	// Indices of buttons in the 'buttonArray'
	private static final int UNDO_BUTTON = 0;
	private static final int STARTGAME_BUTTON = 1;
	private static final int CLOSE_BUTTON = 2;
	
	private ArrayList<Player> startingOnCourt; // The players that will be starting the game.
	private ArrayList<Player> startingOnBench; // The players that will be on the bench to begin the game.
	private ArrayList<Player> players; // Total list of players.
	private ArrayList<JButton> playerButtons; // List of JButtons for each player.
	private ArrayList<String> displayNames; // The names of players who are selected for being a starter.
	private int starters; // The number of starters currently selected 
	private JTextArea playerList; // The names of players who are selected for being a starter.
	private JLabel header; // The header of the panel that reads 'Select X Starter(s)'
	private JPanel buttons; // The panel on the left side of the panel that lists all buttons in 'playerButtons'.
	private JButton[] buttonArray; // All the non-player buttons used in the StartersPanel.
	private JFrame frame; // The frame the StartersPanel is in.
	private String fileName; // The fileName where the game data will be stored.
	private String startersPlural; // Plural/Singular version of 'Starter(s)'.
	private JSplitPane pane; // SplitPane used to split the player buttons and the rest of the panel.
	private GameSettings SETTINGS; // The settings used to format this panel and the number of starters.
	
	final private Color background; // The background color as chosen by the user in the settings.
	final int numberStarters; // The number of starters as chosen by the user in the settings.
	
	// Parameters:
	// 'players': The total list of players
	// 'fileName': The name of the file the game data will be stored in
	// 'settings': The settings being used for the game
	public StartersPanel(ArrayList<Player> players, String fileName, GameSettings settings) {
		this.SETTINGS = settings;
		this.startersPlural = "Starter";
		int numberStarters = (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS);
		if (numberStarters != 1) {
			this.startersPlural += "s";
		}
		this.players = players;
		this.startingOnCourt = new ArrayList<Player>(numberStarters);
		this.startingOnBench = new ArrayList<Player>();
		this.playerButtons = new ArrayList<JButton>(this.players.size());
		this.displayNames = new ArrayList<String>(numberStarters);
		this.background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
		this.numberStarters = (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS);
        this.header = formatLabel("Select " + numberStarters + " " + startersPlural, 60, SETTINGS);
        this.playerList = formatTextArea(SCREENWIDTH / 2 + 90, 500, SETTINGS);
		this.buttons = new JPanel();
        this.buttons.setBackground((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR));
        String[] buttonNames = {" Undo", " Start Game", " Close"};
        int[] sizes = {FONT_SIZE / 2, FONT_SIZE / 2, FONT_SIZE / 2};
        this.buttonArray = createButtonArray(buttonNames, sizes, SETTINGS);
        this.buttonArray[UNDO_BUTTON].setEnabled(false);
        this.buttonArray[STARTGAME_BUTTON].setEnabled(false);
    	this.fileName = fileName;
    	int[] indices = {UNDO_BUTTON, STARTGAME_BUTTON, CLOSE_BUTTON};
    	String[] icons = {UNDO_BUTTON_ICON, START_BUTTON_ICON, CLOSE_BUTTON_ICON};
    	formatIcons(this.buttonArray, indices, icons);
	}
	
	public void createAllButtons() {
		createPlayerButtons();
		createUndoButton();
		createStartButton();
		createCloseButton();
	}
	
	// Post: Adds functionality to all player buttons that appear in a grid on the left side of the StartersPanel.
	public void createPlayerButtons() {
        for (Player player : this.players) {
        	JButton button = new JButton(player.getName() + " " + player.getLastName());
        	formatButton(button, 300, 100, 30, SETTINGS);
        	buttons.add(button);
        	playerButtons.add(button);
        	button.addActionListener(new ActionListener() {
        		public void actionPerformed(ActionEvent e) {
        			// If the number of starters required has not yet been reached
        			if (starters < numberStarters) {
    					starters++;
            			startingOnCourt.add(player); 
                        displayNames.add(player.getName() + " " + player.getLastName());
                        playerList.setText(displayNames.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
                        button.setEnabled(false);
                        buttonArray[UNDO_BUTTON].setEnabled(true);
                        // If the number of starters is reached, all player buttons are disabled
                        if (startingOnCourt.size() == numberStarters) {
                        	buttonArray[STARTGAME_BUTTON].setEnabled(true);
                        	for (JButton btn : playerButtons) {
                        		btn.setEnabled(false);
                        	}
                        } else { // The pressed player button is disabled
                        	buttonArray[STARTGAME_BUTTON].setEnabled(false);
                        }
        			}
        		}
        	});
        }
	}
	
	// Post: Adds the function for the 'Undo' button which removes the most recently added player
	//       to the starters list.
	public void createUndoButton() {
    	this.buttonArray[UNDO_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
				Player remove = startingOnCourt.get(startingOnCourt.size() - 1);
				String removeName = remove.getName() + " " + remove.getLastName();
				// Reactivate the button of the most recently removed player
				for (JButton btn : playerButtons) {
					if (btn.getText().equals(removeName)) {
						btn.setEnabled(true);
						break;
					}
				}
            	for (JButton btn : playerButtons) {
            		if (!displayNames.contains(btn.getText())) {
            			btn.setEnabled(true);
            		}
            	}
		        displayNames.remove(removeName);
		        playerList.setText(displayNames.toString().replaceAll("\\[", "").replaceAll("\\]", ""));
				startingOnCourt.remove(remove); 
				starters--;		  
				if (startingOnCourt.isEmpty()) {
					buttonArray[UNDO_BUTTON].setEnabled(false);
				}
				if (startingOnCourt.size() < (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS)) {
					buttonArray[STARTGAME_BUTTON].setEnabled(false);
				}
    		}
    	});
	}
	
	// Post: Adds the function for the 'Start' button. Asks the user to confirm whether they want to 
	//       start the game. 
	public void createStartButton() {
    	this.buttonArray[STARTGAME_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (starters == numberStarters) {
    				JFrame confirmFrame = new JFrame("Confirm " + startersPlural);
                    JPanel confirmPanel = new JPanel();
                    confirmPanel.setBackground(background);
                    
                    JLabel doneMessage = formatLabel("Confirm " + startersPlural, 35, SETTINGS);
                    confirmPanel.add(doneMessage);
                    confirmPanel.setBorder(new MatteBorder(10, 10, 10, 10, background));
                    // Yes button
                    JPanel yesPanel = new JPanel();
                    JButton yes = new JButton("Yes");
                    formatButton(yes, 300, 100, 30, SETTINGS);
                    yesPanel.setBackground(background);

                    yes.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                        	// Set starters to be on the court
                        	for (int i = 0; i < startingOnCourt.size(); i++) {
                        		Player starter = startingOnCourt.get(i);
                        		starter.setOnFloor();
                        		players.remove(starter);
                        	}
                        	// Rest of players go to the bench
                        	startingOnBench.addAll(players);
                            confirmFrame.dispose();
                            startTracking(startingOnCourt, startingOnBench, new ArrayList<Undo>(), fileName, SETTINGS);
                            frame.dispose();
                        }
                    });
                    yesPanel.add(yes);
                    confirmFrame.getRootPane().setDefaultButton(yes);
                    JPanel noPanel = new JPanel();
                    noPanel.setBackground(background);
                    noButton(confirmFrame, noPanel, 30, 300, 100, "No", SETTINGS, false);
                    JPanel panels = new JPanel();
                    panels.add(confirmPanel);
                    panels.add(yesPanel);
                    panels.add(noPanel);
                    panels.setBackground(background);
                    panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));
                    formatFrame(confirmFrame, panels, 500, 500);
    			}			
    		}
    	});
	}
	
	// Post: Adds functionality for the 'Close' button. Closes the StartersPanel window when pressed.
	public void createCloseButton() {
    	this.buttonArray[CLOSE_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			startingOnCourt.clear();
    			startingOnBench.clear();
    			frame.dispose();
    		}
    	});
	}
	
	// Post: Adds all components to the StartersPanel.
	public void addElements() {  		
		// The panel that stores the header of the StartersPanel
    	JPanel labels = new JPanel();
    	labels.add(this.header);
        labels.setBackground(background);
        labels.setBorder(new MatteBorder(40, 10, 20, 10, background));
        
        // List of players currently selected of starters that appear as a comma separated list
        JPanel list = new JPanel();
        list.add(this.playerList);
        list.setBackground(background);
        
        
        // The panel that stores the 'Undo' button
    	JPanel undoButton = new JPanel();
        undoButton.add(this.buttonArray[UNDO_BUTTON]);
        undoButton.setBackground(background);
        
        // The panel that stores the 'Start Game' button
        JPanel start = new JPanel();
        start.add(this.buttonArray[STARTGAME_BUTTON]);
        start.setBackground(background);
        
        // The panel that stores the 'Close' button
        JPanel close = new JPanel();
        close.add(this.buttonArray[CLOSE_BUTTON]);
        close.setBackground(background);
        
        // The panel that stores all three buttons on the StartersPanel
        JPanel undoStartClose = new JPanel(new GridLayout(3, 1));
        undoStartClose.add(undoButton);
        undoStartClose.add(start);
        undoStartClose.add(close);
        undoStartClose.setBackground(background);
        
        // All the elements of the StartersPanel
        JPanel total = new JPanel();
        formatPanel(total, new Component[]{labels, list, undoStartClose}, null, null, null, background);
        
        // Adds a scroll bar to the list of player buttons on the left/right side of the StartersPanel
        this.buttons.setLayout(new GridLayout(players.size(), 1));
        JScrollPane scrollPane = addScrollPane(this.buttons);
        
        // The panel that stores the scrollable list of players that appears on either side of the StartersPanel
        JPanel panePanel = new JPanel(new GridLayout(1, 1));
        panePanel.add(scrollPane);
        
        // If the user selects the right handed option, the scrollable player list appears on the right
        if (RIGHT_HANDED) {
            this.pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, total, panePanel);
            this.pane.setDividerLocation(SCREENWIDTH - (SCREENWIDTH * 2 / 5));
        } else { // Scrollable player list appears on left for left handed users
            this.pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panePanel, total);
            this.pane.setDividerLocation(SCREENWIDTH * 2 / 7);
        }
        this.pane.setEnabled(false);     
	}
    
	// Post: Puts the StartersPanel in a Frame.
    public void frame() {
        this.frame = new JFrame("Select " + numberStarters + " " + startersPlural);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.add(this.pane);
    }
    
    // Post: Returns a JPanel with all the components of a StartersPanel.
    public JPanel getStartersPanel() {
    	this.pane.setBorder(new MatteBorder(10, 10, 10, 10, DEFAULT_TEXT_BORDER_COLOR));
    	this.pane.setBackground(background);
    	JPanel panel = new JPanel(new GridLayout(1, 1));
    	panel.setPreferredSize(new Dimension(SCREENWIDTH - 250, 900));
    	panel.setBackground(background);
    	panel.add(this.pane);
    	return panel;
    }
}
