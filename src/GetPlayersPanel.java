// Alex Eidt
// Basketball Statistics Tracking Progam
// GetPlayersPanel Class
// The window that opens when the program is started. Allows the user to create their team by entering
// player names or reloading old games.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.regex.*;
import javax.swing.*;
import javax.swing.border.*;

/*
* The GetPlayersPanel" class opens a window for the user to either create a new team or to load an old game
* that they want to continue.
* The user can either "Submit" new players by entering their first name into a text field and press "Done"
* when they enough players (Determined by the class constant "ROSTER_SIZE"), or they can "Load an Old Game".
*/

public class GetPlayersPanel extends GUISettings {
	
    private static final long serialVersionUID = 1L;
    
	// Indices of buttons in the 'buttonArray'
	private static final int SUBMIT_BUTTON = 0;
	private static final int UNDO_BUTTON = 1;
	private static final int START_BUTTON = 2;
	private static final int OLDGAMES_BUTTON = 3;
	private static final int INSTRUCTIONS_BUTTON = 4;
	private static final int CLOSE_BUTTON = 5;
	private static final int SETTINGS_BUTTON = 6;
	
	// Whether a file is being scanned in or not
	private static final boolean IS_SCAN = true;
	
    private ArrayList<Player> players; // The list of players on the team inputted by the user
    private String fileName; // The name of the file the game's data will be stored in
    private JButton[] buttonArray; // All the buttons on the GetPlayersPanel
    private JTextField name; // The text field used to enter player names
    private JLabel title; // The large header seen at the top of the panel
    private JFrame frame; // The frame that opens when the program is started
    private JFrame confirmFrame; // The frame that opens when the 'Done' button is pressed
    private JFrame oldGames; // The frame that opens when the 'Load Old Games' button is pressed
    private JFrame settingsFrame; // The frame that opens when the 'Settings' button is pressed
    private JPanel pane; // Frames all elements in GetPlayersPanel into a single JPanel used for display in the InstructionsPanel
    private GameSettings SETTINGS; // The settings used to configure the current game
    private ArrayList<JPanel> panels; // All the panels in the GetPlayersPanel, used to quickly change background color in Settings
    private ArrayList<String> displayNames; // The player names as they appear in the 'labelList'.
    private InstructionPanel instructionsPanel; // The instructions window opened when the 'Instructions' button is pressed
    private JPanel playerNames; // The dynamic list of player names updated whenever a player is entered or removed
    
    private final Color background; // The background color as determined by the user in the settings.

    /*
     * The fields below are all used for the radio buttons in the Settings window (see createSettingsButton())
     * The value of each field is associated with a corresponding value for the name listed.
     * For example:
     * 		For PERSONAL_FOULS_ALLOWED, the field may store the value 1, but this corresponds to 5 personal fouls allowed
     */
    private int NUMBER_STARTERS; 
    private int PERSONAL_FOULS_ALLOWED;
    private int TECHNICAL_FOULS_ALLOWED;
    private int PERIODTYPE;
    private int PERIODLENGTH;
    private int AUTOCORRECT_NAMES;
    private int TIMEOUTS;
    
    // Parameters:
    // 'settings': The settings used for the game. Can be changed in the settings window.
    public GetPlayersPanel(GameSettings settings) {
    	this.SETTINGS = settings;
    	this.background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
        this.players = new ArrayList<Player>();
        this.panels = new ArrayList<JPanel>();
        this.displayNames = new ArrayList<String>();
        this.title = formatLabel("Basketball Statistics Tracking", FONT_SIZE, SETTINGS);
        this.name = new JTextField(FONT_SIZE / 3);
        // Create all buttons used in the GetPlayersPanel
        String[] buttonNames = {" Add Player", " Undo", " Start Game", " Old Games", " Instructions", " Close", " Settings"};
        int[] sizes = {FONT_SIZE / 2, FONT_SIZE / 2, FONT_SIZE * 2 / 3, FONT_SIZE / 2, 
        			     FONT_SIZE / 2, FONT_SIZE / 2, FONT_SIZE / 2, FONT_SIZE / 2};
        this.buttonArray = createButtonArray(buttonNames, sizes, SETTINGS);
        this.buttonArray[UNDO_BUTTON].setEnabled(false);
        this.buttonArray[START_BUTTON].setEnabled(false);
        this.confirmFrame = new JFrame("Confirm Team");
        this.oldGames = new JFrame("Load an Old Game");
        this.settingsFrame = new JFrame("Settings");
        this.playerNames = new JPanel();
        // Add icons to all buttons
        int[] indices = {INSTRUCTIONS_BUTTON, SETTINGS_BUTTON, OLDGAMES_BUTTON, 
        		         CLOSE_BUTTON, UNDO_BUTTON, SUBMIT_BUTTON, START_BUTTON};
    	String[] icons = {INSTRUCTIONS_BUTTON_ICON, SETTINGS_BUTTON_ICON, OLDGAMES_BUTTON_ICON, 
    			          CLOSE_BUTTON_ICON, UNDO_BUTTON_ICON, SUBMIT_BUTTON_ICON, START_BUTTON_ICON};
    	formatIcons(this.buttonArray, indices, icons);
    	updatePlayerList();
    }
    
    // Post: Formats the text field used to enter player names.
    public void createTextField() {
        this.name.setToolTipText("Enter Player name here:");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                name.requestFocusInWindow();
            }
        });       
        this.name.setHorizontalAlignment(JTextField.CENTER);
        this.name.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, 40));       
        this.name.addFocusListener(new FocusListener() {
        	// When text field is clicked
        	public void focusGained(FocusEvent e) {
        		name.setText(null);
        		name.setForeground(Color.BLACK);
				buttonArray[SUBMIT_BUTTON].setEnabled(true);
        	}
        	// Default setting for text field
        	public void focusLost(FocusEvent e) {
				if (name.getText().length() == 0) {
					name.setText("Enter a Player Name");
					name.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, 40));
					name.setForeground(new Color(100, 100, 100));
					buttonArray[SUBMIT_BUTTON].setEnabled(false);
				}
			}
        });
    }
    
    // Post: Adds actionListeners to each button in the GetPlayersPanel frame
    public void createAllButtons() {
    	createSubmitButton();
    	createUndoButton();
    	createStartButton();
    	createLoadGameButton();
    	createInstructionButton();
        createSettingsButton();
    	createCloseButton();
    }

    // Post: Adds a player with a first name given by the user to the ArrayList of Players.
    //       Players with the same name cannot be included. The minimum number of players
    //       required is the number of starters set by the user or by default (5).
    public void createSubmitButton() {
        this.buttonArray[SUBMIT_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	// Search for a valid player name (First [Space] Last) using regular expressions
            	Pattern pattern = Pattern.compile(".+(\\s){1}.+");
                Matcher match = pattern.matcher(name.getText().trim());
                if (match.find()) {
                	String[] playerNameData = name.getText().trim().split(" ");
                	String firstName = playerNameData[0];
                	String lastName = playerNameData[1];
                	// Auto capitalize names if user sets that setting
                	if ((boolean) SETTINGS.getSetting(Setting.AUTO_CORRECT_NAMES)) {
                		firstName = firstName.substring(0, 1).toUpperCase() + firstName.substring(1).toLowerCase();
                		lastName = lastName.substring(0, 1).toUpperCase() + lastName.substring(1).toLowerCase();
                	}
                	// Create the new Player object
                	String display = firstName + " " + lastName.substring(0, 1) + ".";
                	Player newPlayer = new Player(firstName, lastName, display);
                    if (!players.contains(newPlayer)) {
                        players.add(newPlayer);
                        name.setText(null);
                        int size = players.size() - 1;
                        // Check to see if the players display name is already used.
                        // If yes, then the last initial is replaced by the full last name.
                        Player check = players.get(size);
                        if (displayNames.contains(check.getDisplayName())) {
                        	Player player = check;
                        	String newName = player.getName() + ". " + player.getLastName();
                        	check.setDisplayName(newName);
                        }
                        displayNames.add(check.getDisplayName()); 
                        updatePlayerList();
                        buttonArray[UNDO_BUTTON].setEnabled(true);
                    } else {
                    	confirmPanel(newPlayer.getName() + " " + newPlayer.getLastName() + " is already on the Team", null, 30, SETTINGS);
                    }
                } else {
                	confirmPanel("Please Enter a Last Name", null, 40, SETTINGS);
                }            
                if (players.size() >= (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS)) {
                	buttonArray[START_BUTTON].setEnabled(true);
                }
            }
        });
    }
    
    // Post: Removes the most recently added player from the 'labelList' and the 'players' list.
    public void createUndoButton() {
    	this.buttonArray[UNDO_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			int index = players.size() - 1;
		        displayNames.remove(players.get(index).getDisplayName());
				players.remove(players.get(index));
				buttonArray[UNDO_BUTTON].setEnabled(!players.isEmpty());
				if (players.size() < (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS)) {
					buttonArray[START_BUTTON].setEnabled(false);
				}
				updatePlayerList();
    		}
    	});
    }
    
    // Post: Allows the user to start tracking statistics for their players once
    //       enough players are on the team. If not enough players are in the players ArrayList,
    //       the user will be prompted to add more players.
    public void createStartButton() {
    	this.buttonArray[START_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) { 
            	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
                JPanel confirmPanel = new JPanel();
                confirmPanel.setBackground(background);

                JLabel doneMessage = formatLabel("Confirm Team?", 35, SETTINGS);
                confirmPanel.add(doneMessage);
                confirmPanel.setBorder(new MatteBorder(10, 10, 10, 10, background));
                
                JPanel yesPanel = new JPanel();
                JButton yes = new JButton("Yes");
                formatButton(yes, 300, 100, 30, SETTINGS);
                yesPanel.setBackground(background);
                yes.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        int countCopy = countFiles();
                        fileName = DAYNUM + "." + MONTH + "." + YEAR + "_Boxscore";
                        int actualCopy = 0;
                        File actual = new File(".");
                        for (File f : actual.listFiles()) {
                        	if (f.getName().contains("_#" + countCopy)) {
                        		countCopy++;
                        	}
                        	if (f.getName().contains(fileName)) {
                        		actualCopy++;
                        	}
                        }
                        countCopy = Math.min(countCopy, actualCopy);
                        if (countCopy != 0) {
                            fileName += "_#" + countCopy;
                        }
                        fileName += "." + FILETYPE;
                        ArrayList<Player> list = new ArrayList<Player>();
                        list.addAll(players);
                        Collections.sort(list);
                        run(list, new ArrayList<Undo>(), fileName, !IS_SCAN, SETTINGS);
                        confirmFrame.dispose();
                    }
                });
                yesPanel.add(yes);
                JPanel noPanel = new JPanel();
                noPanel.setBackground(background);
                noButton(confirmFrame, noPanel, 30, 300, 100, "No", SETTINGS, false);
                JPanel panels = new JPanel();
        		formatPanel(panels, new Component[]{confirmPanel, yesPanel, noPanel}, null, null, null, background);
                confirmFrame.getRootPane().setDefaultButton(yes);
                formatFrame(confirmFrame, panels, 500, 500);
            }
        });
    }
    
    // Post: Opens the Instructions window when pressed.
    public void createInstructionButton() {
    	this.buttonArray[INSTRUCTIONS_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			instructionsPanel = new InstructionPanel(SETTINGS);
    			instructionsPanel.addButtonFunctions();
    			instructionsPanel.addElements();			
    		}
    	});
    }
    
    // Post: The "Load Old Game" button allows the user to load any previous game they have.
    //       This feature was implemented in case the program were to close during a game. This way,
    //       the user could easily resume their tracking from where they left off.
    public void createLoadGameButton() {
    	this.buttonArray[OLDGAMES_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	File actual = new File(".");
            	int countCopy = countFiles();
                // If there are no old games available, the user is notified.
                if (countCopy == 0) {
                    confirmPanel("There are no Old Games Available", null, 30, SETTINGS);
                } else { // countCopy > 0
                    JPanel oldGamePanel = new JPanel(new GridLayout(countCopy, 1));
                    oldGamePanel.setBackground((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR));
                    JScrollPane scrollPane = addScrollPane(oldGamePanel);
                    JPanel panePanel = new JPanel(new GridLayout(1, 1));
                    panePanel.add(scrollPane);
                                   
                    formatFrame(oldGames, panePanel, 1000, Math.min(countCopy * 300, 800));
                    /*
                    * If there is at least one old game available to load, every available game is
                    * displayed for the user to choose from. When the user clicks on the game they want,
                    * the program will automatically start up with all the players and their statistics
                    * from the last time the user left off.
                    */
                    for (File f : actual.listFiles()){
                        if (f.getName().contains("_Boxscore")) {
                            // Cuts out the FILETYPE at the end of the file name.
                            JButton oldGameButton = new JButton(f.getName().substring(0, f.getName().length() - (FILETYPE.length() + 1)));
                            formatButton(oldGameButton, 250, 200, 25, SETTINGS);
                            oldGamePanel.add(oldGameButton);
                            oldGameButton.addActionListener(new ActionListener() {
                                // When the user clicks the game they want to load, the file is read.
                                public void actionPerformed(ActionEvent e) {
                                    ReadFile file = new ReadFile(f.getName(), SETTINGS);
                                    GameSettings box = file.getGameSettingsData();
                                    run(file.getPlayers(), file.getUndoArray(), f.getName(), IS_SCAN, box);
                                    oldGames.dispose();
                                }
                            });
                        }
                    }
                }
            }
        });
    }
    
    // Post: Adds function to the 'Close' button. Closes the program entirely.
    public void createCloseButton() {
    	this.buttonArray[CLOSE_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			frame.dispose();
    			confirmFrame.dispose();
    			oldGames.dispose();
    			settingsFrame.dispose();
    			try {
        			instructionsPanel.dispose();
    			} catch(NullPointerException nullP) {
    				System.out.println("Instructions Panel not Open");
    			}
    			System.exit(1);
    		}
    	});
    }
    
    // Enumerated values used for the settings.
    enum GameConfig {
         SELECT_NUMBER_STARTERS,
         SELECT_PERSONAL_FOULS,
         SELECT_TECHNICAL_FOULS,
    	 SELECT_AUTOCORRECT_NAMES,
    	 SELECT_PERIOD_TYPE,
    	 SELECT_PERIOD_LENGTH,
    	 SELECT_TIMEOUTS;
    }
    
    // Post: Adds function to the 'Settings' buttons. Allows the user to configure the game settings however 
    //       they like. Includes actual game settings (Quarters/Halves, Fouls allowed, etc.) and background color.
    public void createSettingsButton() {  	
    	this.buttonArray[SETTINGS_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    	    	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
    		    JPanel colorPanel = new JPanel(new GridLayout(COLOR_NAMES.length, 1));
    		    JScrollPane scrollPane = addScrollPane(colorPanel);
    		    for (int i = 0; i < COLOR_NAMES.length; i++) {
    		    	JButton btn = new JButton(COLOR_NAMES[i]);
    		    	formatButton(btn, 150, 110, 25, SETTINGS);
    		    	btn.setBackground(COLORS[i]);
    		    	btn.addActionListener(new ActionListener() {
    		    		public void actionPerformed(ActionEvent e) {
    		    			Color bck = btn.getBackground();
	    					SETTINGS.setSetting(bck, Setting.BACKGROUND_COLOR);
    		    			for (JPanel panel : panels) {
    		    				panel.setBackground(bck);
    		    				addBorderPanel(panel);
    		    			}
    		    			for (JButton btn : buttonArray) {
    		    				btn.setBackground(bck);
    		    			}
    		    			updatePlayerList();
    		    		}
    		    	});
    		    	colorPanel.add(btn);
    		    }
    		    JPanel scrollPanel = new JPanel(new GridLayout(1, 1));
    		    scrollPanel.add(scrollPane);
    		    
    		    JPanel firstRow = new JPanel(new GridLayout(1, 4));		    
    		    JPanel secondRow = new JPanel(new GridLayout(1, 4));  		  
    		    //JPanel thirdRow = new JPanel(new GridLayout(1, 1));  		    
    		    JPanel total = new JPanel(new GridLayout(2, 1));
    		    total.add(firstRow);
    		    total.add(secondRow);
    		    //total.add(thirdRow);
    		    int fontSize = 19;
    		    createRadioButtons(null, null, new ButtonGroup(), new String[] {"1 v 1", "2 v 2", "3 v 3", "4 v 4", "5 v 5"}, 
    		    		           firstRow, "Players", GameConfig.SELECT_NUMBER_STARTERS, fontSize);
    		    createRadioButtons(null, null, new ButtonGroup(), new String[] {"4 Fouls", "5 Fouls", "6 Fouls", "7 Fouls"}, 
    		    		           firstRow, "Fouls", GameConfig.SELECT_PERSONAL_FOULS, fontSize);   	
    		    createRadioButtons(null, null, new ButtonGroup(), new String[] {"1 Technical", "2 Technicals", "3 Technicals", "4 Technicals"}, 
    		    		           firstRow, "Technical Fouls", GameConfig.SELECT_TECHNICAL_FOULS, fontSize);
    		    createRadioButtons(null, null, new ButtonGroup(), new String[] {"Yes", "No"}, 
    		    		           firstRow, "Auto-Capitalize Names", GameConfig.SELECT_AUTOCORRECT_NAMES, fontSize);   	
    		    ButtonGroup hO = new ButtonGroup();
    		    ButtonGroup qO = new ButtonGroup();
    		    createRadioButtons(qO, hO, new ButtonGroup(), new String[] {"Quarters", "Halves"}, 
    		    		           secondRow, "Game Type", GameConfig.SELECT_PERIOD_TYPE, fontSize);   	
    		    createRadioButtons(null, null, qO, new String[] {"6 mins", "10 mins", "12 mins", "15 mins"}, 
    		    		           secondRow, "Quarter Length", GameConfig.SELECT_PERIOD_LENGTH, fontSize);
    		    enableButtons(qO, false);
    		    createRadioButtons(null, null, hO, new String[] {"10 mins", "20 mins", "25 mins", "30 mins"}, 
    		    		           secondRow, "Half Length", GameConfig.SELECT_PERIOD_LENGTH, fontSize);
    		    enableButtons(hO, false);	    
    		    createRadioButtons(null, null, new ButtonGroup(), new String[] {"2", "3", "4", "5", "7"},
    		    		           secondRow, "Timeouts", GameConfig.SELECT_TIMEOUTS, fontSize);
    		 		    
    		    JPanel settingsButtons = new JPanel(new GridLayout(1, 3));
    		    JButton close = new JButton("Apply and Close");
    		    formatButton(close, 260, 50, 20, SETTINGS);
    		    close.setBackground(DEFAULT_BACKGROUND_COLOR);
    		    close.addActionListener(new ActionListener() {
    		    	public void actionPerformed(ActionEvent e) {
    		    		settingsFrame.dispose();
    		    		updatePlayerList();
    		    	}
    		    });
    		    
    		    JButton defaultBtn = new JButton("Set Default");
    		    formatButton(defaultBtn, 200, 50, 20, SETTINGS);
    		    defaultBtn.setBackground(DEFAULT_BACKGROUND_COLOR);
    		    defaultBtn.addActionListener(new ActionListener() {
    		    	public void actionPerformed(ActionEvent e) {
    		    		Font font = new Font(DEFAULT_FONT_TYPE, Font.BOLD, 40);
    		    		title.setForeground(DEFAULT_FONT_COLOR);
    		    		title.setFont(font);
	    				name.setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, 50));
    		    		SETTINGS = new GameSettings();
    		    		for (JPanel p : panels) {
    		    			p.setBackground(DEFAULT_BACKGROUND_COLOR);
    		    			addBorderPanel(p);
    		    		}
    		    		for (JButton btn : buttonArray) {
    		    			btn.setBackground(DEFAULT_BACKGROUND_COLOR);
    		    			btn.setForeground(DEFAULT_FONT_COLOR);
    		    		}
    		    		updatePlayerList();
    		    	}
    		    });
    		    
    		    settingsButtons.add(close);
    		    settingsButtons.add(defaultBtn);
    		    noButton(settingsFrame, settingsButtons, 20, 300, 50, "Cancel", SETTINGS, true);
    		    
    		    JScrollPane totalScrollPane = addScrollPane(total);
    		    totalScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    		    JPanel totalScrollPanel = new JPanel(new GridLayout(1, 1));
    		    totalScrollPanel.add(totalScrollPane);  
    		    
    		    JSplitPane pane3 = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scrollPanel, settingsButtons);
    		    pane3.setDividerLocation(SCREENHEIGHT / 2 + 230);
    		    pane3.setEnabled(false);   
    		    
    		    JSplitPane pane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, total, pane3);
    		    pane1.setDividerLocation(SCREENWIDTH / 2);
    		    pane1.setEnabled(false);
    		    
    		    JPanel panels = new JPanel();
    		    panels.add(pane1);
    		    panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));
    		    formatFrame(settingsFrame, panels, SCREENWIDTH - 200, SCREENHEIGHT - 100);
    		}
    	});
    }
    
    // Post: Adds all the Components created to the GetPlayersPanel and formats the GetPlayersPanel.
    public void addElements() {
    	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);

    	// The panel that stores the header of the GetPlayerPanel ('BasketBall Statistics Tracking').
        JPanel header = new JPanel();
        header.add(this.title);
        header.setBackground(background);
        header.setBorder(new MatteBorder(0, 0, 40, 0, background));
        panels.add(header);
        
        // The panel that stores the text field where the player names are entered.
        JPanel textBox = new JPanel();
        textBox.add(this.name);
        textBox.setBackground(background);
        panels.add(textBox);
        
        // The panel that stores the 'Add Player' and 'Undo' buttons.
        JPanel buttonArray = new JPanel();
        formatPanel(buttonArray, new Component[]{this.buttonArray[SUBMIT_BUTTON], this.buttonArray[UNDO_BUTTON]}, 
        		    null, buttonArray.getLayout(), null, background);
        panels.add(buttonArray);
        
        // The panel that stores the 'Start Game' button.
        JPanel startGame = new JPanel();
        startGame.add(this.buttonArray[START_BUTTON]);
        startGame.setBackground(background);
        panels.add(startGame);
        
        // The panel that stores the 'Old Games', 'Instructions', 'Settings', and 'Close' buttons.
        JPanel instructSettingPanel = new JPanel(new GridLayout(4, 1));
        instructSettingPanel.add(this.buttonArray[OLDGAMES_BUTTON]);
        instructSettingPanel.add(this.buttonArray[INSTRUCTIONS_BUTTON]);
        instructSettingPanel.add(this.buttonArray[SETTINGS_BUTTON]);
        instructSettingPanel.add(this.buttonArray[CLOSE_BUTTON]);
        instructSettingPanel.setBackground(background);
        panels.add(instructSettingPanel);
        
        // The panel that frames the 'instructSettingsPanel'.
        JPanel iSPPanel = new JPanel();
        iSPPanel.add(instructSettingPanel);
        iSPPanel.setBackground(background);
        panels.add(iSPPanel);
        
        // The panel that frames the lower portion of the GetPlayersPanel that includes the four buttons
        // and the list of players that is updated as players are entered.
        JPanel frame = new JPanel(new FlowLayout());
        frame.add(iSPPanel);
        frame.add(this.playerNames);
        frame.setBackground(background);
        panels.add(frame);

        // All the elements in the GetPlayersPanel.
        JPanel total = new JPanel();
        formatPanel(total, new Component[]{header, textBox, buttonArray, startGame}, 
        		    new int[]{20, 60, 60, 60}, null, background, background);
        total.add(frame, BorderLayout.SOUTH);
        panels.add(total);
        
        this.pane = new JPanel(new FlowLayout());
        this.pane.add(total);
        this.pane.setBackground(background);
        this.pane.setBorder(new MatteBorder(50, 0, 0, 0, background));
        panels.add(this.pane);
    }
    
    // Post: Updates the list of players at the bottom of the GetPlayersPanel every time a player name
    //       is added or removed.
    private void updatePlayerList() {
    	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
    	JPanel panel = new JPanel();
    	panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    	for (int i = this.displayNames.size() - 1; i >= 0; i--) {
    		JLabel label = formatLabel(" - " + this.displayNames.get(i), 30, SETTINGS);
    		label.setBackground(DEFAULT_BACKGROUND_COLOR);
    		label.setBorder(new MatteBorder(15, 10, 15, 10, background));
    		label.setHorizontalTextPosition(JLabel.CENTER);
            panel.add(label);
    	}
    	panel.setBackground(background);
    	panels.add(panel);
    	JScrollPane pane = addScrollPane(panel);
        pane.setPreferredSize(new Dimension(SCREENWIDTH / 6, SCREENHEIGHT / 4));
        pane.setBackground(background);
        pane.setBorder(null);
    	JPanel framePane = new JPanel(new GridLayout(1, 1));
    	framePane.add(pane);
    	framePane.setBackground(background);
    	panels.add(framePane);
    	playerNames.setLayout(new BoxLayout(playerNames, BoxLayout.Y_AXIS));
    	playerNames.removeAll();
    	JLabel players = formatLabel("Players: " + this.players.size(), 35, SETTINGS);
    	players.setBorder(new MatteBorder(0, 0, 30, 0, background));
    	setIcon(players, PLAYER_ICON);
    	playerNames.setBorder(new MatteBorder(0, 60, 30, 0, background));
    	playerNames.add(players);
    	playerNames.add(pane);
    	playerNames.setBackground(background);
    	playerNames.repaint();
    	playerNames.revalidate();
    	panels.add(playerNames);
    }
    
    // Post: Re-Colors the border of the 'panel' given
    private void addBorderPanel(JPanel panel) {
		if (panel.getBorder() != null) {
			String text = panel.getBorder().getBorderInsets(panel).toString().substring(15).replaceAll("[a-z]+=", "");
			text = text.replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(",", " ");
			String[] bordNums = text.split(" ");
			int[] border = new int[bordNums.length];
			for (int i = 0; i < bordNums.length; i++) {
				border[i] = Integer.valueOf(bordNums[i]);
			}
			panel.setBorder(new MatteBorder(border[0], border[1], border[2], border[3], 
					        (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR)));
		}
    }
    
    // Post: Creates a Group of Radio Buttons
    // Parameters:
    // 'first', 'second': Used for Quarters/Halves. Dis/enables those ButtonGroups.
    // 'total': The ButtonGroup used to Group the given Radio Buttons.
    // 'names': The text used for each Radio Button.
    // 'panel': The panel the Radio Buttons will be added to.
    // 'borderText': The text that appears in the border of the Radio Buttons.
    // 'setting': The settings for the game.
    // 'fontSize': The font size of the 'borderText'.
    private void createRadioButtons(ButtonGroup first, ButtonGroup second, ButtonGroup total, String[] names, 
    		                        JPanel panel, String borderText, GameConfig setting, int fontSize) {
    	JPanel buttonPanel = new JPanel(new GridLayout(names.length, 1));
	    for (int i = 0; i < names.length; i++) {
	    	JRadioButton select = new JRadioButton(names[i]);
	    	select.setFont(new Font(DEFAULT_FONT_TYPE, Font.PLAIN, fontSize)); 		    	
	    	buttonPanel.add(select);
	    	total.add(select);
	    	select.addActionListener(new ActionListener() {
	    		public void actionPerformed(ActionEvent e) {
	    			for (int i = 0; i < names.length; i++) {
	    				if (select.getText().equals(names[i])) {
	    					switch (setting) {
	    					case SELECT_NUMBER_STARTERS:
	    						NUMBER_STARTERS = i + 1;
	        					SETTINGS.setSetting(NUMBER_STARTERS, Setting.NUMBER_OF_STARTERS);
	    						break;
	    					case SELECT_PERSONAL_FOULS:
	    						PERSONAL_FOULS_ALLOWED = i + 1;
	        					SETTINGS.setSetting(PERSONAL_FOULS_ALLOWED, Setting.PERSONAL_FOULS);
	    						break;
	    					case SELECT_TECHNICAL_FOULS:
	    						TECHNICAL_FOULS_ALLOWED = i + 1;
	        					SETTINGS.setSetting(TECHNICAL_FOULS_ALLOWED, Setting.TECHNICAL_FOULS);
	    						break;
	    					case SELECT_AUTOCORRECT_NAMES:
	    						AUTOCORRECT_NAMES = i + 1;
	        					SETTINGS.setSetting(AUTOCORRECT_NAMES % 2 != 0, Setting.PERIOD_TYPE);
	    						break;
	    					case SELECT_PERIOD_TYPE:
		    					PERIODTYPE = i + 1;
		        		    	enableButtons(first, PERIODTYPE % 2 != 0);
		        		    	enableButtons(second, PERIODTYPE % 2 == 0);
	        					SETTINGS.setSetting(PERIODTYPE % 2 != 0, Setting.PERIOD_TYPE);
		    					break;
	    					case SELECT_PERIOD_LENGTH:
		    					PERIODLENGTH = Integer.valueOf(names[i].replaceAll(" mins", ""));
	        					SETTINGS.setSetting((double) PERIODLENGTH, Setting.GAME_LENGTH);
	        		    		SETTINGS.setSetting((double) PERIODLENGTH, Setting.TIME_REMAINING);
		    					break;
	    					case SELECT_TIMEOUTS:
	    						TIMEOUTS = Integer.valueOf(names[i]);
	    						SETTINGS.setSetting(TIMEOUTS, Setting.TIMEOUTS);
	    						break;
	    					}
	    					break;
	    				}
	    			}
	    		}
	    	});
	    }
	    buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
	    setRadioButtonBorder(buttonPanel, panel, borderText, fontSize);
    }
    
    // Post: If 'enable' is true, the buttons in the ButtonGroup 'buttons' are all enabled.
    //       If 'enable' is false, the buttons in the ButtonGroup 'buttons' are all disabled.
    private void enableButtons(ButtonGroup buttons, boolean enable) {
    	Enumeration<AbstractButton> buttonGroup = buttons.getElements();
    	while (buttonGroup.hasMoreElements()) {
    		buttonGroup.nextElement().setEnabled(enable);
    	}
    }
    
    // Post: Returns the number of .BBALL files in the directory.
    private int countFiles() {
    	File file = new File(".");
        int countCopy = 0;
        for (File f : file.listFiles()) {
            Pattern pattern = Pattern.compile("(\\d+(\\.)?){3}_.+");
            Matcher match = pattern.matcher(f.getName());
            if (match.find()) {
                countCopy++;
            }
        }
        return countCopy;
    }
    
    // Post: Adds a border to a ButtonGroup of Radio Buttons.
    // Parameters:
    // 'buttonPanel': The panel that contains the ButtonGroup. 
    // 'panel': The panel the ButtonGroup will be added to with the new border.
    // 'borderText': The text that appears in the title of the border.
    // 'fontSize': The font size of the text in the title of the border.
    private void setRadioButtonBorder(JPanel buttonPanel, JPanel panel, String borderText, int fontSize) {
	    Border line = BorderFactory.createMatteBorder(2, 2, 2, 2, DEFAULT_TEXT_BORDER_COLOR);
	    Border bord = BorderFactory.createTitledBorder(line, borderText);
	    ((TitledBorder) bord).setTitleJustification(TitledBorder.LEFT);
	    buttonPanel.setBorder(bord);
	    ((TitledBorder) buttonPanel.getBorder()).setTitleFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, fontSize));	    
	    if (panel != null) {
		    panel.add(buttonPanel);
	    }
    }
    
    // Post: Puts all the Components of the GetPlayersPanel into a window that opens when the user starts the program.
    public void frame() {
        this.frame = new JFrame("Basketball Statistics Tracking");
        this.frame.add(this.pane);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.getRootPane().setDefaultButton(this.buttonArray[SUBMIT_BUTTON]);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.pack();
        this.frame.setVisible(true);
    }
    
    // Post: Returns a JPanel with all the components of a GetPlayersPanel. Used in the InstructionsPanel.
    public JPanel getPlayersPanel() {
    	JPanel panel = this.pane;
    	panel.setPreferredSize(new Dimension(SCREENWIDTH - 250, getDimension(this.pane, Dim.HEIGHT)));
		panel.setBorder(new MatteBorder(10, 10, 10, 10, DEFAULT_TEXT_BORDER_COLOR));
    	return panel;
    }
}
