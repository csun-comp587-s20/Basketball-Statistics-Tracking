// Alex Eidt
// Basketball Statistics Tracking Program
// ManagementPanel Class
// The ManagementPanel is where the statistic tracking actually happens.
// If the number of players entered equals the number of starters for the game, then the ManagementPanel
// will show a button for each player entered.
// If the number of players entered is greater than the number of starters for the game, then the
// ManagementPanel will split into a list of player buttons showing players that are 'Currently on Court'
// and another list showing players that are on the 'bench'.

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.Timer;
import javax.swing.*;
import javax.swing.border.*;

public class ManagementPanel extends GUISettings {
    
    private static final long serialVersionUID = 1L;
    
	// Indices of Buttons in the 'buttonArray'
	private static final int BOXSCORE_BUTTON = 0;
	private static final int UNDO_BUTTON = 1;
	private static final int DONE_BUTTON = 2;
	private static final int START_BUTTON = 3;
	private static final int SETTINGS_BUTTON = 4;
	private static final int TIMEOUT_BUTTON = 4;
	
    private String fileName; // Name of the file the game data is written to
    private JButton[] buttonArray; // All the Buttons on the ManagementPanel
    /*
     * 'menuPanel' is the panel on the ManagementPanel frame that has all the player playerButtons.
     *  If the number of starters entered by the user equals the number of players entered by
     *  the user, then 'menuPanel' includes all player playerButtons in a FlowLayout
     */
    private JPanel playersOnBenchPanel; 
    /*
     * 'onFloorPanel' only is used when the number of starters entered by the user equals the number
     * of players entered by the user. It stores a label of each player currently on the court.
     */
    private JPanel onFloorPanel;
    /*
     * 'headerPanel' is the panel that includes the label 'Roster Management' and the 'Score', 'Box Score',
     *  'Undo', 'Done', 'Start/Stop', 'Settings' and the timer.
     */
    private JPanel headerPanel;
    private ArrayList<Player> players, bench, total; // Player lists
    private ArrayList<JButton> playerButtons; // List of player buttons.
    private ArrayList<Undo> undo; // List of 'undo' representing every statistic tracked in chronological order.
    private JFrame frame; // The frame used for the ManagementPanel.
    private JFrame timerSettings; // The frame used for the timer settings.
    private JLabel score; // The label showing the total points scored by the team.
    private JLabel teamFouls; // The label showing the total team fouls.
    private boolean splitPane; // If more players are entered than starters, then the lower panel is split.
    private boolean startStop; // Alternates the function of the 'Start/Stop' button.
    private GameSettings SETTINGS; // Settings used for the game.
    private Component managementPanel; // Used to frame the ManagementPanel in a JFrame.
    private GameClock clock; // The timer used for the game.
    private int period; // The current period of the game.
    private int timeouts; // The number of timeouts for the team.
    private int teamFoulsInPeriod; // The number of team fouls for the current period.
    
	private final int personalFouls; // The number of personal fouls allowed selected by the user in the settings.
	private final int technicalFouls; // The number of technical fouls allowed selected by the user in the settings.
	private final Color background; // The background color selected by the user in the settings.
    
    // Parameters:
    // 'players': List of players currently on the court (Starting if new game is being started).
    // 'bench': List of players currently on the bench.
    // 'undo': Statistical history of the game (empty if new game is being started).
    // 'fileName': The name of the file the game data will be stored in.
    // 'settings': The settings for the current game.
    public ManagementPanel(ArrayList<Player> players, ArrayList<Player> bench, 
    		               ArrayList<Undo> undo, String fileName, GameSettings settings) {
    	// Initialize fields with parameters
        this.SETTINGS = settings;
        this.players = players;
        this.bench = bench;
        this.undo = undo;
        this.total = new ArrayList<Player>(players.size() + bench.size());
        this.total.addAll(players);
        this.total.addAll(bench);
        this.fileName = fileName;
        
    	this.personalFouls = (int) SETTINGS.getSetting(Setting.PERSONAL_FOULS);
    	this.technicalFouls = (int) SETTINGS.getSetting(Setting.TECHNICAL_FOULS);
    	this.background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
        this.timeouts = (int) SETTINGS.getSetting(Setting.TIMEOUTS);
        this.period = (int) SETTINGS.getSetting(Setting.CURRENT_PERIOD);
        this.playerButtons = new ArrayList<JButton>(this.total.size());
        this.clock = new GameClock((int)((double) this.SETTINGS.getSetting(Setting.TIME_REMAINING) * 60 * 10));
        this.score = formatLabel("Score: " + getTotal("PTS") + "    ", FONT_SIZE * 4 / 7, SETTINGS);
        for (Undo u : this.undo) {
        	if (Integer.valueOf(u.getTimeOf().trim().charAt(0) + "") == this.period + 1) {
        		if (u.getStatFromPlayer().equals("Personal Foul")) {
            		this.teamFoulsInPeriod++;
        		}
        	}
        }
        this.teamFouls = formatLabel("  Team Fouls: " + this.teamFoulsInPeriod, FONT_SIZE * 4 / 7, SETTINGS);
        this.playersOnBenchPanel = new JPanel();
        this.headerPanel = new JPanel();  
        this.onFloorPanel = new JPanel(new GridLayout(this.players.size() + 1, 1));
        this.frame = new JFrame();
        if (this.total.size() != (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS)) {
            this.playersOnBenchPanel.setLayout(new GridLayout(bench.size(), 1));
            this.splitPane = false;
            this.onFloorPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
        } else {
            this.playersOnBenchPanel.setLayout(new FlowLayout(1, 60, 40));
            this.splitPane = true;
        }
        this.playersOnBenchPanel.setBackground(background);
        // Create all buttons in the ManagementPanel
        String[] buttonNames = {" Box Score", " Undo", " Done", "   Start", " Timeouts: " + this.timeouts};//"Settings"};
        int[] sizes = {FONT_SIZE * 4 / 7, FONT_SIZE * 4 / 7, FONT_SIZE * 4 / 7, FONT_SIZE * 4 / 7, FONT_SIZE * 4 / 7}; //{300, 100, 40}};
        this.buttonArray = createButtonArray(buttonNames, sizes, SETTINGS);
        this.buttonArray[UNDO_BUTTON].setEnabled(!this.undo.isEmpty());
        this.buttonArray[START_BUTTON].setSize(100, 100);
        // Add Icons to all buttons
        int[] indices = {DONE_BUTTON, BOXSCORE_BUTTON, UNDO_BUTTON, START_BUTTON, TIMEOUT_BUTTON};
        String[] icons = {DONE_BUTTON_ICON, BOXSCORE_BUTTON_ICON, UNDO_BUTTON_ICON, PLAY_BUTTON_ICON, TIMEOUT_BUTTON_ICON};
        formatIcons(this.buttonArray, indices, icons);
    }
    
    // Post: Adds function to the 'Box Score' button. Opens a new window with a table showing the
    //       statistics for each player.
    public void createTableButton() {
        this.buttonArray[BOXSCORE_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Table table = new Table(total, getData(), SCREENWIDTH - 20, SCREENHEIGHT - 250, 
                		                FONT_SIZE / 3, FONT_SIZE / 3, SETTINGS);
                table.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                table.setVisible(true);
                table.setTitle("Box Score");
                table.setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
    }
    
    // Post: Adds function to the 'Undo' button. Allows the user to select which period of the game
    //       they want to eliminate a statistic from and shows a history of all statistics entered
    //       during that period with the most recently entered statistic shown at the top.
    public void createUndoButton() {
        this.buttonArray[UNDO_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame undoFrame = new JFrame("Undo Statistic");
                int numPeriods;
                String periodType;
                if ((boolean) SETTINGS.getSetting(Setting.PERIOD_TYPE)) {
                	numPeriods = 4;
                	periodType = "Quarter";
                } else {
                	numPeriods = 2;
                	periodType = "Half";
                }
                boolean[] hasPeriod = new boolean[numPeriods];
                for (int i = 0; i < numPeriods; i++) {
                	for (Undo u : undo) {
                		if (u.getTimeOf().contains(GAME_PERIODS[i])) {
                			hasPeriod[i] = true;
                			break;
                		}
                	}
                }
                JPanel periods = new JPanel(new GridLayout(1, numPeriods));
                JPanel undoPanel = new JPanel();
                for (int i = 0; i < numPeriods; i++) {
                	final int index = i;
                	JButton selectPeriod = new JButton(GAME_PERIODS[i] + " " + periodType);
                	selectPeriod.setEnabled(hasPeriod[i]);
                	formatButton(selectPeriod, BUTTON_HEIGHT, BUTTON_HEIGHT, FONT_SIZE / 3, SETTINGS);
                	periods.add(selectPeriod);
                	selectPeriod.addActionListener(new ActionListener() {
                		public void actionPerformed(ActionEvent e) {
                			undoPanel.removeAll();
                			int grid = 0;
                			for (int i = undo.size() - 1; i >= 0; i--) {
                            	Undo u = undo.get(i);
                            	final Player play = u.getPlayer();
                            	String undoString = u.toString();
                            	if (u.getTimeOf().contains(GAME_PERIODS[index])) {
                            		grid++;
                            		JButton undoButton = new JButton(play.toString() + " " + undoString);
                                    int undoButtonWidth = getDimension(formatLabel(undoButton.getText(), FONT_SIZE / 3, SETTINGS), Dim.WIDTH);
                                    formatButton(undoButton, undoButtonWidth, BUTTON_HEIGHT, FONT_SIZE / 3, SETTINGS);
                                    undoPanel.add(undoButton);
                                    undoButton.addActionListener(new ActionListener() {
                                    	public void actionPerformed(ActionEvent e) {
                                    		if (!u.getStatFromPlayer().equals("Substitution")) {
                                                Player astPlayer = u.getAstPlayer();
                                                if (u.hasAssistPlayer()) {
                                                	int index = total.indexOf(astPlayer);
                                                	Player player = total.get(index);
                                                	player.add(false, "Assist");
                                                }
                                                String stat = u.getStatFromPlayer();
                                                play.add(false, stat);
                                                switch (stat) {
                                                case "Personal Foul":
                                                case "PF":
                                                	teamFoulsInPeriod--;
                                                	teamFouls.setText("  Team Fouls: " + teamFoulsInPeriod);
                                                case "Technical Foul":
                                                case "TF":
                                                case "Flagrant I Foul":
                                                case "FLGI":
                                                case "Flagrant II Foul":
                                                case "FLGII":
                                                	updateOnFloorPanel();
                                                }
                                            }
                                            undo.remove(u);
                                            undoFrame.dispose();
                                            score.setText("Score: " + getTotal("PTS") + "    ");
                                            buttonArray[UNDO_BUTTON].setEnabled(!undo.isEmpty());
                                    	}
                                    });
                            	}
                            }
                			undoPanel.setLayout(new GridLayout(grid, 1));
                			undoPanel.repaint();
                			undoPanel.revalidate();
                		}
                	});
                };
                undoPanel.setBackground(background);
                periods.setBackground(background);
                JPanel panel = new JPanel();
                JLabel label = formatLabel("Undo:", FONT_SIZE * 3 / 4, SETTINGS);
                JScrollPane scrollPane = addScrollPane(undoPanel);
                scrollPane.setBorder(null);
                scrollPane.setBackground(background);
                panel.add(label);
                panel.setBackground(background);
                panel.setBorder(new MatteBorder(10, 10, 20, 10, background));
                JPanel scroll = new JPanel(new GridLayout(1, 1));
                scroll.add(scrollPane);
                scroll.setBackground(background);
                JPanel total = new JPanel();
                total.add(panel);
                total.add(periods);
                total.add(scroll);
                total.setBackground(background);
                total.setLayout(new BoxLayout(total, BoxLayout.Y_AXIS));
                formatFrame(undoFrame, total, SCREENWIDTH / 2, SCREENHEIGHT - 100);
            }
        });
    }
    
    // Post: Creates all the player buttons in the case that the number of players entered equals the
    //       number of starters entered by the user.
    public void createTeamButtons() {
        JButton playerButton;
        for (Player player: total) {
            final Player play = player;
            playerButton = new JButton(player.toString());
            final JButton button = playerButton;
            boolean isOut = !player.hasFouledOut(personalFouls, technicalFouls);
            playerButton.setEnabled(isOut);
            if (isOut) {
            	playerButton.setBackground(FOULED_OUT_BUTTON_COLOR);
            }
            int width = getDimension(formatLabel(playerButton.getText(), FONT_SIZE * 2 / 3, SETTINGS), Dim.WIDTH);
            formatButton(playerButton, width * 2, BUTTON_HEIGHT * 3 / 2, FONT_SIZE * 2 / 3, SETTINGS);
            playersOnBenchPanel.add(playerButton);
            playerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFrame statsMenu = new JFrame(play.getDisplayName() + ": " + play.getStat("PTS", false)
                    + " Points | " + play.getStat("REB", false) + " Rebounds | FG%: "
                    + play.getStat("FG%", false) + " | 3P% " + play.getStat("3P%", false)
                    + " | FT%: " + play.getStat("FT%", false));
                    JPanel panel = addStatButton(play, statsMenu, button);
                    formatFrame(statsMenu, panel, SCREENWIDTH - 250, SCREENHEIGHT - 200);
                }
            });
            playerButtons.add(playerButton);
        }
    }
    
    // Post: Creates all the player buttons in the case that the number of players entered is greater
    //       than the number of starters entered by the user.
    public void createPlayerButtons() {
        JButton playerButton;
        for (Player play : total) {
            playerButton = new JButton(play.toString());
            int width = getDimension(formatLabel(playerButton.getText(), FONT_SIZE * 2 / 3, SETTINGS), Dim.WIDTH);
            final JButton button = playerButton;
            formatButton(playerButton, width, BUTTON_HEIGHT, FONT_SIZE * 2 / 3, SETTINGS);
            playerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (players.contains(play)) {
                        JFrame statsMenu = new JFrame(play.getDisplayName() + ": " + play.getStat("PTS", false)
                                                      + " Points | " + play.getStat("REB", false) + " Rebounds | FG%: "
                                                      + play.getStat("FG%", false) + " | 3P% " + play.getStat("3P%", false)
                                                      + " | FT%: " + play.getStat("FT%", false));
                        JPanel panel = addStatButton(play, statsMenu, button);
                        if (panel != null) {
                            formatFrame(statsMenu, panel, SCREENWIDTH - 400, SCREENHEIGHT - 200);
                        }
                    } else {
                        if (play.hasFouledOut(personalFouls, technicalFouls)) {
                            confirmPanel(play.toString() + " has fouled Out!", null, 40, SETTINGS);
                        } else {
                            subNewPlayer(play, players, bench, false);
                        }
                    }
                }
            });
            playersOnBenchPanel.add(playerButton);
            playerButtons.add(playerButton);
        }
        updateOnFloorPanel();
    }
   
    // Post: Adds function to the 'Done' button. Closes the ManagementPanel.
    public void createDoneButton() {
        this.buttonArray[DONE_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Player player = new Player("DONE", "DONE", "DONE");
                undo.add(new Undo(player, "DONE", "DONE"));
                updateFile(total);
                try {
                    timerSettings.dispose();
                } catch (Exception e1) {
                    System.out.println("Timer Settings Window Not Open");
                }
                if (clock.isRunning()) {
                	clock.stopTimer();
                }
                frame.dispose();
            }
        });
    }
    
    // Post: Adds function to the 'Start/Stop' buttons used to manage the time.
    public void createStartStopButton() {
        this.buttonArray[START_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                startStop = !startStop;
                if (startStop) {
                    clock.startTimer();
                    setIcon(buttonArray, START_BUTTON, PAUSE_BUTTON_ICON);
                    buttonArray[START_BUTTON].setText("   Stop");
                } else {
                    clock.stopTimer();
                    setIcon(buttonArray, START_BUTTON, PLAY_BUTTON_ICON);
                    buttonArray[START_BUTTON].setText("   Start");
                }
            }
        });
    }
    
    // Unimplemented button.
    public void createSettingsButton() {
        this.buttonArray[SETTINGS_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                timerSettings = new JFrame("Timer Settings");
                JPanel timerPanel = new JPanel();
                formatFrame(timerSettings, timerPanel, 700, 700);
            }
        });
    }
    
    // Post: Adds function to the 'Timeouts' button. Decreases the number of timeouts by 1 every time 
    //       it is pressed and stops the clock if it is running.
    public void createTimeoutButton() {
    	this.buttonArray[TIMEOUT_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			if (timeouts > 0) {
        			timeouts--;
        			if (clock.isRunning()) {
        				clock.stopTimer();
        				startStop = !startStop;
        				setIcon(buttonArray, START_BUTTON, PLAY_BUTTON_ICON);
                        buttonArray[START_BUTTON].setText("   Start");
        			}
        			SETTINGS.setSetting(timeouts, Setting.TIMEOUTS);
        			updateFile(total);
    			}
    			buttonArray[TIMEOUT_BUTTON].setText("Timeouts: " + timeouts);
    		}
    	});
    }
    
    // Post: Adds all the Components created to the ManagementPanel and formats the ManagementPanel.
    public void addElements() {
    	// The panel that stores the header with 'Roster Management' that appears at the top of the
    	// ManagementPanel.
        JPanel title = new JPanel(new GridBagLayout());
        JLabel header = formatLabel("   Roster Management", FONT_SIZE * 5 / 6, SETTINGS);
    	setIcon(header, ROSTER_MANAGEMENT_ICON);
        title.add(header);
        title.setBackground(background);
        
        // Timer
        JPanel timePanel = new JPanel();
        Timer timer = new Timer();
        TimerTask t = new TimerTask() {
            public void run() {
            	int currentPeriod = (int) SETTINGS.getSetting(Setting.CURRENT_PERIOD);
            	// If the clock is at 0.0, then it is time for the next period of the game.
                if (clock.getTime().equals("0.0")) {
                    buttonArray[START_BUTTON].setText("   Start");
                    clock.setTime((int)((double) SETTINGS.getSetting(Setting.GAME_LENGTH) * 10 * 60));
                    if (clock.isRunning()) {
                        clock.stopTimer();
                    }
                    period++;
                    teamFoulsInPeriod = 0;
                    SETTINGS.setSetting(period, Setting.CURRENT_PERIOD);
                    boolean quarters_half = (boolean) SETTINGS.getSetting(Setting.PERIOD_TYPE);
                    // If the last period of the game is over, then the current period is set to END_OF_GAME to signal the
                    // end of the game.
                    if ((!quarters_half && currentPeriod > HALVES - 1) || (quarters_half && currentPeriod > QUARTERS - 1)) {
                        SETTINGS.setSetting(END_OF_GAME, Setting.CURRENT_PERIOD);
                    }
                }
                if (clock.isRunning()) {
                	// Increment the time the player is on the court
                    for (Player p : players) {
                        p.add(true, "MIN");
                    }
                    SETTINGS.setSetting(clock.getTenthsTime() / 600.0, Setting.TIME_REMAINING);
                    // Update the time the players are on the court every minute
                    if (clock.getTime().endsWith(":00")) {
                        updateFile(total);
                    }
                }
                String result = "";
                if (currentPeriod != END_OF_GAME) {
                    result = GAME_PERIODS[currentPeriod] + "   " + clock.getTime();
                } else { // If the game has ended
                    result = "FINAL";
                    buttonArray[START_BUTTON].setEnabled(false);
                    // Disable all buttons
                    for (JButton btn : playerButtons) {
                        btn.setEnabled(false);
                    }
                    if (clock.isRunning()) {
                    	clock.stopTimer();
                    }
                    // Stop the timer
                    timer.cancel();
                    timer.purge();
                }
                // Label showing the current period and time of game
                JLabel label = formatLabel(result, FONT_SIZE * 6 / 7, SETTINGS); 
                timePanel.removeAll();
                timePanel.add(label);
                timePanel.repaint();
                timePanel.revalidate();
            }
        };
        timer.schedule(t, 0, TIMER_SETTING);
        
        timePanel.setBackground(background);
        FlowLayout layout = new FlowLayout(1, 20, 10);
        // The first row of the ManagementPanel that has the 'Score' label, the 'Box Score', 'Undo', and 'Done' buttons
        JPanel buttonHeader = new JPanel(layout);
        setIcon(this.score, SCORE_BUTTON_ICON);
        formatPanel(buttonHeader, new Component[] {this.score, 
        		                                   this.buttonArray[BOXSCORE_BUTTON], 
        		                                   this.buttonArray[UNDO_BUTTON], 
        		                                   this.buttonArray[DONE_BUTTON]}, 
        		    null, buttonHeader.getLayout(), null, background);
        // The second row of the ManagementPanel that has the current time and period of the game (timePanel), the 'Start'
        // and 'Timeout' buttons and the 'Team Fouls' label.
        JPanel timerHeader = new JPanel(layout);
        setIcon(this.teamFouls, TEAMFOULS_ICON);
        formatPanel(timerHeader, new Component[] {timePanel, 
        		                                  this.buttonArray[START_BUTTON], 
        		                                  this.buttonArray[TIMEOUT_BUTTON], 
        		                                  this.teamFouls}, 
        		    null, timerHeader.getLayout(), null, background);
        
        // The panel that stores all of the components above in a 3 x 1 grid with the header coming first,
        // then the first row of buttons, followed by the second row of buttons.
        this.headerPanel.setLayout(new GridLayout(3, 1));
        formatPanel(this.headerPanel, new Component[] {title, buttonHeader, timerHeader},
        		    new int[] {0, 0, 40, 0}, headerPanel.getLayout(), background, background);
        
        // The panel that stores the 'Bench' label that appears above all the players currently on the bench
        JPanel bench = new JPanel();
        formatPanel(bench, new Component[] {formatLabel("Bench: ", FONT_SIZE * 2 / 3, SETTINGS)}, 
        		    new int[] {10, 10, 10, 10}, bench.getLayout(), DEFAULT_TEXT_BORDER_COLOR, DEFAULT_BACKGROUND_COLOR);
        // The panel that stores all the players on the bench. A scroll bar is added if the number of players on the bench
        // is large
        JPanel menu = new JPanel();
        JScrollPane scrollPane = addScrollPane(this.playersOnBenchPanel);
        formatPanel(menu, new Component[] {bench, scrollPane}, null, null, null, background);
        // The panel that stores all the players currently on the court
        JPanel onCourtPanel = new JPanel();
        formatPanel(onCourtPanel, new Component[] {this.onFloorPanel}, null, null, null, background);
        
        // Switches which side the bench panel and the on court panel are based on if the user selected
        // left/right handed
        if (!this.splitPane) {
            JSplitPane pane;
            if (RIGHT_HANDED) {
            	pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, menu, onCourtPanel);
            } else {
            	pane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, onCourtPanel, menu);
            }
            pane.setDividerLocation(JScrollPane.CENTER_ALIGNMENT);
            this.managementPanel = pane;
        } else {
            this.managementPanel = this.playersOnBenchPanel;
        }
    }
    
    // Post: Opens a window asking the user to substitute the given Player 'play' for a new player.
    // Parameters:
    // 'play': The player being substituted.
    // 'players': The list of Players from which the substitute is chosen.
    // 'bench': The list of Players from which the player is pulled out of.
    // 'freeze': Whether a player is being substituted because they have fouled out of the game (If so freeze: true)
    private void subNewPlayer(Player play, ArrayList<Player> players, ArrayList<Player> bench, boolean freeze) {
    	JFrame confirmFrame = new JFrame("Substitution - " + play.getDisplayName() + " for: ");
        JPanel confirmPanel = new JPanel();
        confirmPanel.setBackground(background);
        int numberButtons = 0;
        JPanel subPanel = new JPanel();
        for (Player player : players) {
        	if (!player.hasFouledOut(personalFouls, technicalFouls)) {
        		numberButtons++;
        		JButton sub = new JButton(player.getDisplayName());
                formatButton(sub, 200, 80, 30, SETTINGS);
                subPanel.setBackground(background);
                subPanel.add(sub);
                sub.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        bench.remove(play);
                        bench.add(player);
                        players.remove(player);
                        players.add(play);
                        if (freeze) {
                            play.setOffFloor();
                            player.setOnFloor();
                        } else {
                            player.setOffFloor();
                            play.setOnFloor();
                        }              
                        onFloorPanel.revalidate();
                        playersOnBenchPanel.revalidate();
                        confirmFrame.dispose();
                        updateOnFloorPanel();
                    }
                });	
        	}
        }
        if (numberButtons == 0) {
        	updateOnFloorPanel();
        } else {
            confirmPanel.setLayout(new BoxLayout(confirmPanel, BoxLayout.Y_AXIS));
            if (!freeze) {
                noButton(confirmFrame, subPanel, FONT_SIZE * 2 / 3, BUTTON_HEIGHT, BUTTON_HEIGHT, "Cancel", SETTINGS, false);
                subPanel.setLayout(new GridLayout(players.size() + 1, 1));
            } else {
                subPanel.setLayout(new GridLayout(numberButtons, 1));
            }
            JPanel panels = new JPanel();
            panels.add(confirmPanel);
            panels.add(subPanel);
            panels.setBackground(background);
            panels.setLayout(new BoxLayout(panels, BoxLayout.Y_AXIS));
            formatFrame(confirmFrame, panels, SCREENWIDTH / 2, SCREENHEIGHT - 200);
            playersOnBenchPanel.revalidate();
        }
    }
    
    // Post: Returns a two-dimensional String Array with all the player data. Used for the table from the 'Box Score'
    //       button.
    private String[][] getData() {
        String[][] data = new String[total.size() + 1][STATISTIC_ABBREVIATIONS.length];
        int i = 0;
        for (Player player : total){
            for (int column = 0; column < STATISTIC_ABBREVIATIONS.length; column++) {
                data[i][column] = player.getStat(STATISTIC_ABBREVIATIONS[column], false);
            }
            i++;
        }
        return data;
    }
    
    // Post: Updates the 'onFloorPanel' to include all players currently on the court. Used to update after substitutions
    //       are made.
    private void updateOnFloorPanel() {
        Set<String> fouledOut = new HashSet<String>(this.total.size());
        for (Player player : this.total) {
        	if (player.hasFouledOut(personalFouls, technicalFouls)) {
        		fouledOut.add(player.toString());
        	}
        }
    	if (!this.splitPane) {
    		this.onFloorPanel.removeAll();
        	this.playersOnBenchPanel.removeAll();
            JLabel starters = formatLabel("Currently On Court:", FONT_SIZE * 2 / 3, this.SETTINGS);
            starters.setBorder(new MatteBorder(10, 10, 10, 10, DEFAULT_TEXT_BORDER_COLOR));
            this.onFloorPanel.add(starters);
            Set<String> names = new HashSet<String>(this.players.size() * 2);
            for (Player player : this.players) {
            	names.add(player.toString());
            }
        	for (JButton playerButton : this.playerButtons) {
        		String text = playerButton.getText();
        		if (names.contains(text)) {
        			this.onFloorPanel.add(playerButton);
        			playerButton.setBackground(DEFAULT_BACKGROUND_COLOR);
        		} else {
        			this.playersOnBenchPanel.add(playerButton);
        			playerButton.setBackground(BENCH_BUTTON_COLOR);
        			playerButton.setEnabled(true);
        		}
        		if (fouledOut.contains(text)) {
        			playerButton.setBackground(FOULED_OUT_BUTTON_COLOR);
        			playerButton.setEnabled(false);
        		}
        	}
        	this.onFloorPanel.repaint();
        	this.onFloorPanel.revalidate();
        	this.playersOnBenchPanel.repaint();
        	this.playersOnBenchPanel.revalidate();	
    	} else {
    		for (JButton playerButton : this.playerButtons) {
    			if (fouledOut.contains(playerButton.getText())) {
    				playerButton.setBackground(FOULED_OUT_BUTTON_COLOR);
    				playerButton.setEnabled(false);
    			} else {
    				playerButton.setBackground(DEFAULT_BACKGROUND_COLOR);
    				playerButton.setEnabled(true);
    			}
    		}
    	}
    }
    
    // Post: Opens a window with an array of buttons each labeled with a certain basketball statistic. Clicking on a button
    //       will increment that players statistic and close the window that appeared. In the case of made shots, the user will
    //       be asked if the shot was assisted. In the case of missed shots, the user will be asked if someone on their team
    //       secured the offensive rebound for the shot.
    // Parameters:
    // 'play': The player to which the statistic will be added.
    // 'frame': The frame the window will appear in.
    // 'button': The button that corresponds to the player.
    private JPanel addStatButton(Player play, JFrame frame, JButton button) {
        JPanel statsMenuPanel = new JPanel(new GridLayout(STATISTICS.length / 2, 2));
        JButton statbutton;
        for (String stat : STATISTICS) {
            statbutton = new JButton(stat + "  " + play.getStat(stat, false));
            formatButton(statbutton, BUTTON_HEIGHT, BUTTON_HEIGHT * 7 / 10, FONT_SIZE / 3, SETTINGS);
            statsMenuPanel.add(statbutton);
            Undo recent = new Undo(play, stat, GAME_PERIODS[(int) SETTINGS.getSetting(Setting.CURRENT_PERIOD)] + " " + clock.getTime());
            if (frame != null) {
                statbutton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        play.add(true, stat);
                    	if (play.hasFouledOut(personalFouls, technicalFouls)) {
                    		if (stat.equals("Personal Foul")) {
                        		teamFoulsInPeriod++;
                    		}
                    		if (!splitPane) {
                                subNewPlayer(play, bench, players, true);
                    		}
                    		button.setEnabled(false);
                    		button.setBackground(FOULED_OUT_BUTTON_COLOR);
                    	} else {
                    		switch (stat) {
                        	case "Made FG":
                        	case "Made 3pt FG":
                        		if ((int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS) != 1 && frame != null) {
                        			openNewStatWindow(recent, play, "Assist", "Assisted FG?", "Who got the Assist?");
                    			}
                    			break;
                            case "Personal Foul":
                            	teamFoulsInPeriod++;
                            case "Technical Foul":
                            case "Flagrant I Foul":
                            case "Flagrant II Foul":
                            	if (clock.isRunning()) {
                            		clock.stopTimer();
                                    startStop = !startStop;
                                    buttonArray[START_BUTTON].setText("   Start");
                                    setIcon(buttonArray, START_BUTTON, PLAY_BUTTON_ICON);
                            	}
                            	break;
                            case "Missed FG":
                            case "Missed 3pt FG":
                            case "Missed Free Throw":
                            	openNewStatWindow(recent, play, "OREB", "Offensive Rebounded?", "Who got the Offensive Rebound?");
                            	break;
                        	}
                    	}
                        buttonArray[UNDO_BUTTON].setEnabled(true);
                        undo.add(recent);
                        updateFile(players);
                        if (frame != null) {
                            frame.dispose();
                        }
                        score.setText("Score: " + getTotal("PTS") + "    ");
                    	teamFouls.setText("  Team Fouls: " + teamFoulsInPeriod);
                    }
                });
            }
        }
        return statsMenuPanel;
    }
    
    // Post: In the case that the statistic chosen in the method addStatButton is a shot or a missed shot,
    //       a new window opens prompting the user:
    //       -For a shot:
    //       Was the shot assisted? (Yes/No)
    //       -For a missed shot:
    //       Did someone on the users team secure the offensive rebound? (Yes/No)
    // Parameters:
    // 'recent': The Undo object that will be used to chronologically archive this players statistical contribution
    //           to the game.
    // 'play': The player that got the statistic.
    // 'stat': The statistic in question.
    // 'frameText': The text the appears in the Yes/No frame title bar.
    private void openNewStatWindow(Undo recent, Player play, String stat, String frameText, String frameHeader) {
    	JFrame assistFrame = new JFrame(frameText);
        JPanel assistPanel = new JPanel(new GridLayout(2, 1));
        formatFrame(assistFrame, assistPanel, SCREENWIDTH / 3, SCREENHEIGHT / 2);
        JButton yes = new JButton("Yes");
        formatButton(yes, BUTTON_HEIGHT, BUTTON_HEIGHT, FONT_SIZE * 2 / 3, SETTINGS);
        yes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame(frameHeader);
                JPanel panel = new JPanel(new GridLayout(players.size() - getFouledOut(), 1));
                formatFrame(frame, panel, SCREENWIDTH / 3, SCREENHEIGHT / 2 + (SCREENHEIGHT / 6));
                JButton assistPlayerButton;
                for (Player assistPlayer : players) {
                    boolean nameMatch = assistPlayer.getName().equals(play.getName()) && assistPlayer.getLastName().equals(play.getLastName());
                    if (!nameMatch && !assistPlayer.hasFouledOut(personalFouls, technicalFouls)) {
                        assistPlayerButton = new JButton(assistPlayer.toString());
                        formatButton(assistPlayerButton, BUTTON_HEIGHT, BUTTON_HEIGHT * 3 / 2, FONT_SIZE / 2, SETTINGS);
                        panel.add(assistPlayerButton);
                        assistPlayerButton.addActionListener(new ActionListener() {
                            public void actionPerformed(ActionEvent e) {
                                assistPlayer.add(true, stat);
                                frame.dispose();
                                score.setText("Score: " + getTotal("PTS") + "    ");
                                switch (stat) {
                                case "Offensive Rebound":
                                case "OREB":
                                	undo.add(new Undo(assistPlayer, "Offensive Rebound", recent.getTimeOf()));
                                	break;
                                default:
                                	recent.setAstPlayer(assistPlayer);
                                	break;
                                }
                                updateFile(players);
                            }
                        });
                    }
                }
                assistFrame.dispose();
            }
        });
        assistPanel.add(yes);
        noButton(assistFrame, assistPanel, FONT_SIZE * 2 / 3, BUTTON_HEIGHT, BUTTON_HEIGHT, "No", SETTINGS, false);
    }
    
    // Post: Updates the file with 'fileName' to have all the current statistics of each player and the time remaining
    //       and current period of the game.
    private void updateFile(ArrayList<Player> players) {
        CreateFile gameFile = new CreateFile(this.fileName);
        gameFile.openFile();
        gameFile.addGameConfig(SETTINGS);
        for (Player player : players){
            for (int column = 0; column < STATISTIC_ABBREVIATIONS.length; column++) {
            	gameFile.addInformation(STATISTIC_ABBREVIATIONS[column], player.getStat(STATISTIC_ABBREVIATIONS[column], false));
            }
        	gameFile.addInformation("OnCourt", player.isStarter() + "");
        }
        gameFile.addUndo(this.undo);
        gameFile.closeFile();
    }
    
    // Post: Puts all the Components of the ManagementPanel into a window that opens when the user starts the game.
    public void frame() {
        this.frame.add(this.headerPanel, BorderLayout.NORTH);
        this.frame.getRootPane().setDefaultButton(this.buttonArray[START_BUTTON]);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.add(this.managementPanel);
    }
    
    // Post: Returns a JPanel with all the components of a ManagementPanel. Used in the InstructionsPanel.
    public JPanel getManagementPanel() {
        JPanel panel = new JPanel();
        panel.add(this.headerPanel, BorderLayout.NORTH);
        if (this.managementPanel instanceof JSplitPane) {
            ((JSplitPane) this.managementPanel).setDividerLocation(SCREENWIDTH / 3);
        }
        JPanel managementP = new JPanel(new GridLayout(1, 1));
        managementP.add(this.managementPanel);
        managementP.setPreferredSize(new Dimension(SCREENWIDTH - 250, 700));
        panel.setBorder(new MatteBorder(10, 10, 10, 10, DEFAULT_TEXT_BORDER_COLOR));
        panel.setPreferredSize(new Dimension(SCREENWIDTH - 250, 1100));
        ;
        managementP.setBackground(background);
        panel.add(managementP);
        panel.setBackground(background);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
    
    // Post: Returns the number of players who have fouled out of the game.
    private int getFouledOut() {
        int fouledOut = 1;
        for (Player player : players) {
            if (player.hasFouledOut(personalFouls, technicalFouls)) {
                fouledOut++;
            }
        }
        return fouledOut;
    }
    
    // Post: Returns the total of the statistic 'stat' for the entire team.
    private int getTotal(String stat) {
        int totalPoints = 0;
        for (Player play : total) {
            totalPoints += Integer.valueOf(play.getStat(stat, false));
        }
        return totalPoints;
    }
}

