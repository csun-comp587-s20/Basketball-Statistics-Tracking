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
import java.util.List;
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
	private static final int TIMEOUT_BUTTON = 4;
	private static final int HOME_BUTTON = 5;
	
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
    private List<Player> players, bench, total; // Player lists
    private List<JButton> playerButtons; // List of player buttons.
    private List<Undo> undo; // List of 'undo' representing every statistic tracked in chronological order.
    private JFrame frame; // The frame used for the ManagementPanel.
    private JLabel score; // The label showing the total points scored by the team.
    private JLabel teamFouls; // The label showing the total team fouls.
    private boolean splitPane; // If more players are entered than starters, then the lower panel is split.
    private boolean startStop; // Alternates the function of the 'Start/Stop' button.
    private boolean gameOver; // True if game is over, false if not.
    private boolean isQuarters; // True if quarters is period type, false if Halves is period type.
    private GameSettings SETTINGS; // Settings used for the game.
    private JPanel managementPanel; // Used to frame the ManagementPanel in a JFrame.
    private JPanel pane; // Panel used to switch between different windows.
    private JPanel timePanel; // Panel used to store the timer.
    private GameClock clock; // The timer used for the game.
    private int period; // The current period of the game.
    private int timeouts; // The number of timeouts for the team.
    private int teamFoulsInPeriod; // The number of team fouls for the current period.
    
	private final int personalFouls; // The number of personal fouls allowed selected by the user in the settings.
	private final int technicalFouls; // The number of technical fouls allowed selected by the user in the settings.
	private final int flagrantI; // The number of Flagrant I fouls allowed selected by the user in the settings.
	private final int flagrantII; // The number of Flagrant II fouls allowed selected by the user in the settings.
	private final int numberStarters; // The number of starters for the game selected by the user in the settings.
	private final Color background; // The background color selected by the user in the settings.
    
    // Parameters:
    // 'players': List of players currently on the court (Starting if new game is being started).
    // 'bench': List of players currently on the bench.
    // 'undo': Statistical history of the game (empty if new game is being started).
    // 'fileName': The name of the file the game data will be stored in.
    // 'settings': The settings for the current game.
    public ManagementPanel(List<Player> players, List<Player> bench, 
    		               List<Undo> undo, String fileName, GameSettings settings) {
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
    	this.flagrantI = (int) SETTINGS.getSetting(Setting.FLAGRANT_I);
    	this.flagrantII = (int) SETTINGS.getSetting(Setting.FLAGRANT_II);
    	this.background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
        this.timeouts = (int) SETTINGS.getSetting(Setting.TIMEOUTS);
        this.period = (int) SETTINGS.getSetting(Setting.CURRENT_PERIOD);
        this.numberStarters = (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS);
        this.isQuarters = (boolean) SETTINGS.getSetting(Setting.PERIOD_TYPE);
        this.playerButtons = new ArrayList<JButton>(this.total.size());
        this.clock = new GameClock((int)((double) this.SETTINGS.getSetting(Setting.TIME_REMAINING) * 60 * 10));
        this.score = formatLabel("Score: " + getTotal("PTS") + "    ", FONT_SIZE * 4 / 7, SETTINGS);
        for (Undo u : this.undo) {
        	char periodChar = u.getTimeOf().trim().charAt(0);
        	int adjust = 0;
        	if (periodChar == 'O') {
        		periodChar = u.getTimeOf().trim().charAt(2);
        		if (this.isQuarters) {
        			adjust = QUARTERS - 1;
        		} else {
        			adjust = HALVES - 1;
        		}
        	}
        	if (Integer.valueOf(periodChar + "") + adjust == this.period + 1) {
        		if (u.getStatFromPlayer().equals("Personal Foul")) {
            		this.teamFoulsInPeriod++;
        		}
        	}
        }
        this.teamFouls = formatLabel("  Team Fouls: " + this.teamFoulsInPeriod, FONT_SIZE * 4 / 7, SETTINGS);
        this.playersOnBenchPanel = new JPanel();
        this.headerPanel = new JPanel();  
        this.onFloorPanel = new JPanel(new GridLayout(this.players.size() + 1, 1));
        this.managementPanel = new JPanel();
        this.managementPanel.setBackground(this.background);
        this.pane = new JPanel();
        this.pane.setBackground(this.background);
        this.frame = new JFrame("Roster Management - " + this.fileName);
        if (this.total.size() != this.numberStarters) {
            this.playersOnBenchPanel.setLayout(new GridLayout(bench.size(), 1));
            this.splitPane = false;
            this.onFloorPanel.setBackground(DEFAULT_BACKGROUND_COLOR);
        } else {
            this.playersOnBenchPanel.setLayout(new FlowLayout(1, 60, 40));
            this.splitPane = true;
        }
        this.gameOver = period == END_OF_GAME;
        this.playersOnBenchPanel.setBackground(background);
        // Create all buttons in the ManagementPanel
        String[] buttonNames = {" Box Score", " Undo", " Done", "   Start", " Timeouts: " + this.timeouts, "  Return to Game"};
        int size = FONT_SIZE * 4 / 7;
        int[] sizes = {size, size, size, size, size, size}; 
        this.buttonArray = createButtonArray(buttonNames, sizes, SETTINGS);
        this.buttonArray[UNDO_BUTTON].setEnabled(!this.undo.isEmpty());
		this.buttonArray[TIMEOUT_BUTTON].setEnabled(this.timeouts != 0);
		this.buttonArray[HOME_BUTTON].setBorder(null);
        setIcon(buttonArray, HOME_BUTTON, ROSTER_MANAGEMENT_ICON);
        // Add Icons to all buttons
        int[] indices = {DONE_BUTTON, BOXSCORE_BUTTON, UNDO_BUTTON, START_BUTTON, TIMEOUT_BUTTON};
        String[] icons = {DONE_BUTTON_ICON, BOXSCORE_BUTTON_ICON, UNDO_BUTTON_ICON, 
        		          PLAY_BUTTON_ICON, TIMEOUT_BUTTON_ICON};
        formatIcons(this.buttonArray, indices, icons);
    }
    
    // Post: Adds function to the 'Box Score' button. Opens a new window with a table showing the
    //       statistics for each player.
    public void createTableButton() {
        this.buttonArray[BOXSCORE_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	pane.removeAll();
            	pane.setLayout(new BorderLayout());
            	JPanel header = new JPanel(new GridLayout(1, 2));
            	JLabel title = formatLabel("  Box Score", FONT_SIZE * 5 / 6, SETTINGS);
            	setIcon(title, BOXSCORE_BUTTON_ICON);
            	header.add(title);
            	header.setBorder(new MatteBorder(0, 0, 30, 0, background));
            	header.setBackground(background);
            	pane.add(header, BorderLayout.NORTH);
            	JPanel allPanels = new JPanel();
                Table table = new Table(total, getData(), FONT_SIZE / 3, FONT_SIZE / 3, SETTINGS);
                allPanels.add(table.getTable());
                allPanels.setLayout(new GridLayout(1, 1));
                allPanels.setBackground(background);
                JScrollPane scrollTable = addScrollPane(allPanels);
                scrollTable.setBorder(null);
                scrollTable.setBackground(background);
                JPanel scroll = new JPanel(new GridLayout(1, 1));
                scroll.add(scrollTable);
                scroll.setBackground(background);
                frame.setTitle("Box Score");
                pane.add(scroll, BorderLayout.CENTER);
                pane.add(buttonArray[HOME_BUTTON], BorderLayout.SOUTH);
                pane.repaint();
                pane.revalidate();
            }
        });
    }
    
    // Post: Adds functionality to the Home Button. Brings the user back to the Roster Management window.
    public void createHomeButton() {
    	this.buttonArray[HOME_BUTTON].addActionListener(new ActionListener() {
    		public void actionPerformed(ActionEvent e) {
    			pane.removeAll();
    			setPane();
    			frame.setTitle("Roster Management - " + fileName);   
    			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    		}
    	});
    }
    
    // Post: Adds function to the 'Undo' button. Allows the user to select which period of the game
    //       they want to eliminate a statistic from and shows a history of all statistics entered
    //       during that period with the most recently entered statistic shown at the top.
    public void createUndoButton() {
        this.buttonArray[UNDO_BUTTON].addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
            	pane.removeAll();
            	pane.setLayout(new BorderLayout());
            	frame.setTitle("Undo Statistic");
                int numPeriods;
                String periodType;
                int periodNumbers = period;
                if (gameOver) {
                	if (isQuarters) {
                		periodNumbers = QUARTERS;
                	} else {
                		periodNumbers = HALVES;
                	}
                }
                if (isQuarters) {
                	numPeriods = QUARTERS;
                	periodType = "Quarter";
                } else {
                	numPeriods = HALVES;
                	periodType = "Half";
                }
                boolean[] hasPeriod = new boolean[Math.max(periodNumbers + 1, numPeriods)];
                for (int i = 0; i < hasPeriod.length; i++) {
                	for (Undo u : undo) {
                		if (i < GAME_PERIODS.length) {
                    		if (u.getTimeOf().contains(GAME_PERIODS[i])) {
                    			hasPeriod[i] = true;
                    			break;
                    		}
                		} else {
                			if (u.getTimeOf().contains("OT" + (i - GAME_PERIODS.length + 1))) {
                				hasPeriod[i] = true;
                				break;
                			}
                		}
                	}
                }
                JPanel periods = new JPanel(new GridLayout(1, hasPeriod.length));
                JPanel undoPanel = new JPanel();
                for (int i = 0; i < hasPeriod.length; i++) {
                	String periodText;
                	String check;
                	if (i < GAME_PERIODS.length) {
                		periodText = GAME_PERIODS[i] + " " + periodType;
                		check = GAME_PERIODS[i];
            		} else {
            			check = periodText = "OT" + (i - GAME_PERIODS.length + 1);
            		}
                	JButton selectPeriod = new JButton(periodText);
                	selectPeriod.setEnabled(hasPeriod[i]);
                	formatButton(selectPeriod, BUTTON_HEIGHT, BUTTON_HEIGHT, FONT_SIZE / 3, SETTINGS);
                	periods.add(selectPeriod);
                	selectPeriod.addActionListener(new ActionListener() {
                		public void actionPerformed(ActionEvent e) {
                			undoPanel.removeAll();
                			int grid = 0;
                			for (Undo u : undo) {
                            	final Player play = u.getPlayer();
                            	String undoString = u.toString();
                            	if (u.getTimeOf().contains(check)) {
                            		grid++;
                            		JButton undoButton = new JButton(play.toString() + " " + undoString);
                                    formatButton(undoButton, BUTTON_HEIGHT, BUTTON_HEIGHT, FONT_SIZE / 3, SETTINGS);
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
                                                	break;
                                                }
                                            }
                                            undo.remove(u);
                                            score.setText("Score: " + getTotal("PTS") + "    ");
                                            buttonArray[UNDO_BUTTON].setEnabled(!undo.isEmpty());
                                			pane.removeAll();
                                			setPane();
                                			frame.setTitle("Roster Management - " + fileName);   
                                			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                                            buttonArray[HOME_BUTTON].setText("  Return to Game");
                                            setIcon(buttonArray, HOME_BUTTON, ROSTER_MANAGEMENT_ICON);
                                            Collections.sort(undo);
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
                JLabel label = formatLabel("  Undo", FONT_SIZE * 3 / 4, SETTINGS);
                setIcon(label, UNDO_BUTTON_ICON);
                JScrollPane scrollPane = addScrollPane(undoPanel);
                scrollPane.setBorder(null);
                scrollPane.setBackground(background);
                panel.add(label);
                panel.setBackground(background);
                panel.setBorder(new MatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE * 2, BORDER_SIZE, background));
                JPanel scroll = new JPanel(new GridLayout(1, 1));
                scroll.add(scrollPane);
                scroll.setBackground(background);
                JPanel total = new JPanel();
                total.add(panel);
                total.add(periods);
                total.add(scroll);
                total.setBackground(background);
                total.setLayout(new BoxLayout(total, BoxLayout.Y_AXIS));
                pane.add(total, BorderLayout.CENTER);
            	pane.add(buttonArray[HOME_BUTTON], BorderLayout.SOUTH);
                pane.revalidate();
                pane.repaint();
            }
        });
    }
    
    // Post: Creates all the player buttons in the case that the number of players entered equals the
    //       number of starters entered by the user.
    public void createTeamButtons() {
    	for (Player player : this.total) {
            final Player play = player;
            JButton playerButton = new JButton(player.toString());
            final JButton button = playerButton;
            boolean isOut = !player.hasFouledOut(personalFouls, technicalFouls, flagrantI, flagrantII);
            playerButton.setEnabled(isOut);
            if (isOut) {
            	playerButton.setBackground(FOULED_OUT_BUTTON_COLOR);
            }
            int width = (int) getDimension(formatLabel(playerButton.getText(), FONT_SIZE * 5 / 7, SETTINGS)).getWidth();
            formatButton(playerButton, width * 2, BUTTON_HEIGHT * 6 / 5, FONT_SIZE * 5 / 7, SETTINGS);
            playersOnBenchPanel.add(playerButton);
            playerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JFrame statsMenu = new JFrame(play.getStatLine());
                    JPanel panel = addStatButton(play, statsMenu, button);
                    formatFrame(statsMenu, panel, SCREENWIDTH - (BUTTON_HEIGHT * 5 / 2), SCREENHEIGHT - (BUTTON_HEIGHT * 2));
                }
            });
            playerButtons.add(playerButton);
        }
    }
    
    // Post: Creates all the player buttons in the case that the number of players entered is greater
    //       than the number of starters entered by the user.
    public void createPlayerButtons() {
        for (Player play : this.total) {
        	JButton playerButton = new JButton(play.toString());
            final JButton button = playerButton;
            formatButton(playerButton, BUTTON_HEIGHT, BUTTON_HEIGHT, FONT_SIZE * 2 / 3, SETTINGS);
            playerButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (play.isStarter()) {
                        JFrame statsMenu = new JFrame(play.getStatLine());
                        JPanel panel = addStatButton(play, statsMenu, button);
                        if (panel != null) {
                            formatFrame(statsMenu, panel, SCREENWIDTH - (BUTTON_HEIGHT * 4), SCREENHEIGHT - (BUTTON_HEIGHT * 2));
                        }
                    } else {
                    	subNewPlayer(play, players, bench, false);
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
                Collections.sort(undo);
                updateFile(total);
                if (clock.isRunning()) {
                	clock.stopTimer();
                }
                frame.dispose();
            }
        });
	    this.frame.addWindowListener(new WindowAdapter() {
	        public void windowClosed(WindowEvent w) {
                Player player = new Player("DONE", "DONE", "DONE");
                Undo newest = new Undo(player, "DONE", "DONE");
                if (!undo.contains(newest)) {
                    undo.add(newest);
                }
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
                	if (clock.isRunning()) {
                		clock.stopTimer();
                	}
                    setIcon(buttonArray, START_BUTTON, PLAY_BUTTON_ICON);
                    buttonArray[START_BUTTON].setText("   Start");
                }
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
        			buttonArray[TIMEOUT_BUTTON].setEnabled(timeouts != 0);
        			if (clock.isRunning()) {
        				clock.stopTimer();
        				startStop = !startStop;
        				setIcon(buttonArray, START_BUTTON, PLAY_BUTTON_ICON);
                        buttonArray[START_BUTTON].setText("   Start");
        			}
        			SETTINGS.setSetting(timeouts, Setting.TIMEOUTS);
        			updateFile(total);
    			} 
    			buttonArray[TIMEOUT_BUTTON].setText(" Timeouts: " + timeouts);
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
        
        startTimerWindow();
        
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
        		    new int[] {0, 0, BORDER_SIZE * 4, 0}, headerPanel.getLayout(), background, background);
        
        // The panel that stores the 'Bench' label that appears above all the players currently on the bench
        JPanel bench = new JPanel();
        formatPanel(bench, new Component[] {formatLabel("Bench", FONT_SIZE * 2 / 3, SETTINGS)}, 
        		    new int[] {BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE}, bench.getLayout(), 
        		    DEFAULT_TEXT_BORDER_COLOR, DEFAULT_BACKGROUND_COLOR);
        // The panel that stores all the players on the bench. A scroll bar is added if the number of players on the bench
        // is large
        JPanel menu = new JPanel();
        JScrollPane scrollPane = addScrollPane(this.playersOnBenchPanel);
        scrollPane.setBorder(null);
        formatPanel(menu, new Component[] {bench, scrollPane}, null, null, null, background);
        // The panel that stores all the players currently on the court
        JPanel onCourt = new JPanel();
        formatPanel(onCourt, new Component[] {formatLabel("Currently on Court", FONT_SIZE * 2 / 3, SETTINGS)}, 
    		        new int[] {BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE}, onCourt.getLayout(), 
    		        DEFAULT_TEXT_BORDER_COLOR, DEFAULT_BACKGROUND_COLOR);
        JPanel onCourtPanel = new JPanel();
        JScrollPane scrollPaneOnCourt = addScrollPane(this.onFloorPanel);
        scrollPaneOnCourt.setBorder(null);
        formatPanel(onCourtPanel, new Component[] {onCourt, scrollPaneOnCourt}, null, null, null, background);
        
        // Switches which side the bench panel and the on court panel are based on if the user selected
        // left/right handed
        if (!this.splitPane) {
            JPanel pane = new JPanel(new GridLayout(1, 2));
            if (RIGHT_HANDED) {
            	pane.add(menu);
            	pane.add(onCourtPanel);
            } else {
            	pane.add(onCourtPanel);
            	pane.add(menu);
            }
        	pane.setBackground(background);
            this.managementPanel = pane;
        } else {
            this.managementPanel = this.playersOnBenchPanel;
        }
    }
    
    // Post: Starts the loop that updates the 'timePanel' which stores the timer for the game.
    private void startTimerWindow() {
    	// Timer
        this.timePanel = new JPanel();
        JLabel currentTime = formatLabel(getPeriodText(), FONT_SIZE * 6 / 7, SETTINGS);
        this.timePanel.add(currentTime);
        Timer timer = new Timer();
        TimerTask t = new TimerTask() {
            public void run() {
            	// If the clock is at 0.0, then it is time for the next period of the game.
                if (clock.isRunning()) {
                	if (clock.getTime().equals("0.0")) {
                        buttonArray[START_BUTTON].setText("   Start");
                        startStop = !startStop;
                        setIcon(buttonArray, START_BUTTON, PLAY_BUTTON_ICON);
                        clock.setTime((int) ((double) SETTINGS.getSetting(Setting.GAME_LENGTH) * 10 * 60));
                        if (clock.isRunning()) {
                            clock.stopTimer();
                        }
                        teamFoulsInPeriod = 0;
                        period++;
                        SETTINGS.setSetting(period, Setting.CURRENT_PERIOD);
                        // If the last period of the game is over, then the current period is set to END_OF_GAME to signal the
                        // end of the game.
                        if (((isQuarters && period > HALVES) || (!isQuarters && period > QUARTERS)) && !gameOver) {
                        	ActionListener yesButton = new ActionListener() {
                        		public void actionPerformed(ActionEvent e) {
                        			SETTINGS.setSetting(period, Setting.CURRENT_PERIOD);
                        			double gameLength = (double) SETTINGS.getSetting(Setting.OVERTIME_LENGTH);
                        			SETTINGS.setSetting(gameLength, Setting.TIME_REMAINING);
                        			SETTINGS.setSetting(gameLength, Setting.GAME_LENGTH);
                        			clock.setTime((int) ((double) SETTINGS.getSetting(Setting.GAME_LENGTH) * 10 * 60));
                        			pane.removeAll();
                        			setPane();
                        			gameOver = false;
                        			timeouts = (int) SETTINGS.getSetting(Setting.OT_TIMEOUTS);
                        			buttonArray[TIMEOUT_BUTTON].setText(" Timeouts: " + timeouts);
                        		}
                        	};
                        	ActionListener noButton = new ActionListener() {
                        		public void actionPerformed(ActionEvent e) {
                        			period = END_OF_GAME;
                                    SETTINGS.setSetting(END_OF_GAME, Setting.CURRENT_PERIOD);
                        			pane.removeAll();
                        			setPane();
                        			gameOver = true;
                        		}
                        	};
                        	confirmPane(pane, frame, noButton, yesButton, "Overtime?", OVERTIME_ICON, SETTINGS);
                        }
                    	updateFile(total);
                    }
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
                if (timePanel.isDisplayable()) {
                	String result = getPeriodText();
                    if (period == END_OF_GAME) { // If the game has ended
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
                    currentTime.setText(result);
                    currentTime.revalidate();
                    currentTime.repaint();
                }            
            }
        };
        timer.schedule(t, 0, TIMER_SETTING);        
        this.timePanel.setBackground(background);
    }
    
    // Post: Opens a window asking the user to substitute the given Player 'play' for a new player.
    // Parameters:
    // 'play': The player being substituted.
    // 'players': The list of Players from which the substitute is chosen.
    // 'bench': The list of Players from which the player is pulled out of.
    // 'freeze': Whether a player is being substituted because they have fouled out of the game (If so freeze: true)
    private void subNewPlayer(Player play, List<Player> players, List<Player> bench, boolean freeze) {
    	this.frame.setTitle("Substitution - " + play.toString() + " for:");
    	this.pane.removeAll();
    	this.pane.setLayout(new GridBagLayout());
    	JPanel header = new JPanel(new GridBagLayout());
    	JLabel label = formatLabel(" SUBSTITUTION", FONT_SIZE * 3 / 4, SETTINGS);
    	header.add(label);
    	setIcon(label, SUB_PLAYER_ICON);
    	header.setBackground(background);
    	header.setBorder(new MatteBorder(BORDER_SIZE * 2, 0, BORDER_SIZE * 3, 0, background));
    	JPanel subHeader = new JPanel(new GridBagLayout());
    	subHeader.add(formatLabel(play.toString() + " for...", FONT_SIZE * 2 / 3, SETTINGS));
    	subHeader.setBackground(background);
    	JPanel headers = new JPanel(new GridLayout(2, 1));
    	headers.add(header);
    	headers.add(subHeader);
    	headers.setBorder(new MatteBorder(0, 0, 0, BORDER_SIZE * 10, background));
    	headers.setBackground(background);
    	this.pane.add(headers);
    	JPanel buttons = new JPanel();
    	buttons.setBorder(new MatteBorder(0, BORDER_SIZE, 0, 0, DEFAULT_TEXT_BORDER_COLOR));
        int numberButtons = 0;
        JPanel subPanel = new JPanel();
        subPanel.setBackground(background);
        for (Player player : players) {
        	if (!player.hasFouledOut(personalFouls, technicalFouls, flagrantI, flagrantII)) {
        		numberButtons++;
        		JButton sub = new JButton(player.toString());
                formatButton(sub, BUTTON_HEIGHT * 4, BUTTON_HEIGHT, FONT_SIZE / 2, SETTINGS);
                sub.setBorder(null);
                buttons.add(sub);
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
                    	buttonArray[HOME_BUTTON].setText("  Return to Game");
                    	setIcon(buttonArray, HOME_BUTTON, ROSTER_MANAGEMENT_ICON);
                        pane.removeAll();
                        setPane();
                        updateOnFloorPanel();
                    }
                });	
        	}
        }
        if (numberButtons == 0) {
        	updateOnFloorPanel();
        } else {
        	int grid;
            if (!freeze) {
            	JButton cancel = new JButton("Cancel");
            	formatButton(cancel, BUTTON_HEIGHT * 4, BUTTON_HEIGHT, FONT_SIZE / 2, SETTINGS);
            	cancel.setBorder(null);
            	cancel.addActionListener(this.buttonArray[HOME_BUTTON].getActionListeners()[0]);
            	buttons.add(cancel);
            	grid = players.size() + 1;
            } else {
            	grid = numberButtons;
            }
            buttons.setLayout(new GridLayout(grid, 1));
            this.pane.add(buttons);
	        this.pane.repaint();
	        this.pane.revalidate();
	        this.frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
	        this.playersOnBenchPanel.revalidate();
        }
    }
    
    // Post: Returns a two-dimensional String Array with all the player data. Used for the table from the 'Box Score'
    //       button.
    private String[][] getData() {
    	int rows;
    	if (this.total.size() == 1) {
    		rows = 1;
    	} else {
    		rows = this.total.size() + 1;
    	}
        String[][] data = new String[rows][STATISTIC_ABBREVIATIONS.length];
        int i = 0;
        for (Player player : this.total){
            for (int column = 0; column < STATISTIC_ABBREVIATIONS.length; column++) {
                data[i][column] = player.getStat(STATISTIC_ABBREVIATIONS[column]).replaceAll("_", " ");
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
        	if (player.hasFouledOut(personalFouls, technicalFouls, flagrantI, flagrantII)) {
        		fouledOut.add(player.toString());
        	}
        }
    	if (!this.splitPane) {
    		this.onFloorPanel.removeAll();
        	this.playersOnBenchPanel.removeAll();
        	this.onFloorPanel.setLayout(new GridLayout(this.players.size(), 1));
            Set<String> names = new HashSet<String>(this.players.size());
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
        	if (this.onFloorPanel.isDisplayable()) {
            	this.onFloorPanel.repaint();
            	this.onFloorPanel.revalidate();
        	}
        	if (this.playersOnBenchPanel.isDisplayable()) {
            	this.playersOnBenchPanel.repaint();
            	this.playersOnBenchPanel.revalidate();	
        	}
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
    	this.pane.revalidate();
    	this.pane.repaint();
    }
    
    // Post: Returns a String used to display the time in the ManagementPanel.
    private String getPeriodText() {
        String labelText;
        if (this.period != END_OF_GAME) {
        	if (this.period < GAME_PERIODS.length) {
                labelText = GAME_PERIODS[this.period] + "   " + this.clock.getTime();
        	} else {
        		int OTConstant;
        		if (this.isQuarters) {
        			OTConstant = QUARTERS;
        		} else {
        			OTConstant = HALVES;
        		}
        		labelText = "OT" + (this.period - OTConstant + 1) + "   " + this.clock.getTime();
        	}
        } else {
        	labelText = "FINAL";
        }
        return labelText;
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
            statbutton = new JButton(stat + "  " + play.getStat(stat));
            formatButton(statbutton, BUTTON_HEIGHT, BUTTON_HEIGHT * 7 / 10, FONT_SIZE / 3, SETTINGS);
            statsMenuPanel.add(statbutton);   
            Undo recent = new Undo(play, stat, getPeriodText().substring(0, 5).trim() + " " + clock.getTime());
            if (frame != null) {
                statbutton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        play.add(true, stat);
                    	if (play.hasFouledOut(personalFouls, technicalFouls, flagrantI, flagrantII)) {
                    		if (stat.equals("Personal Foul")) {
                        		teamFoulsInPeriod++;
                    		}
                    		if (!splitPane && getFouledOut() == 1) {
                                subNewPlayer(play, bench, players, true);
                    		}
                    		button.setEnabled(false);
                    		button.setBackground(FOULED_OUT_BUTTON_COLOR);
                    	} else if (numberStarters != 1) {
                    		switch (stat) {
                        	case "Made FG":
                        	case "Made 3pt FG":
                        		openNewStatWindow(recent, play, "Assist", "Assisted FG?", "Who got the Assist?", false);
                    			break;
                            case "Personal Foul":
                            	teamFoulsInPeriod++;
                            case "Technical Foul":
                            case "Flagrant I Foul":
                            case "Flagrant II Foul":
                            	// Stop timer if foul is called
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
                            	openNewStatWindow(recent, play, "Offensive Rebound", "Offensive Rebounded?", 
                            			          "Who got the Offensive Rebound?", true);
                            	break;
                            case "Block":
                            	openNewStatWindow(recent, play, "Defensive Rebound", "Defensive Rebounded?", 
                            			          "Who got the Defensive Rebound?", true);
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
    // 'frameHeder': The text that appears in the frame that opens when 'Yes' is clicked in the 'assistFrame'.
    private void openNewStatWindow(Undo recent, Player play, String stat, String frameText, String frameHeader, boolean allPlayers) {
    	JFrame assistFrame = new JFrame(frameText);
        JPanel assistPanel = new JPanel(new GridLayout(2, 1));
        formatFrame(assistFrame, assistPanel, SCREENWIDTH / 3, SCREENHEIGHT / 2);
        JButton yes = new JButton("Yes");
        formatButton(yes, BUTTON_HEIGHT, BUTTON_HEIGHT, FONT_SIZE * 2 / 3, SETTINGS);
        yes.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new JFrame(frameHeader);
                int size = players.size();
                if (!allPlayers) {
                	size -= 1;
                } 
                JPanel panel = new JPanel(new GridLayout(size, 1));
                formatFrame(frame, panel, SCREENWIDTH / 3, SCREENHEIGHT / 2 + (SCREENHEIGHT / 6));
                JButton assistPlayerButton;
                for (Player assistPlayer : players) {
                	boolean check = assistPlayer != play;
                	if (allPlayers) {
                		check = true;
                	}
                	if (check && !assistPlayer.hasFouledOut(personalFouls, technicalFouls, flagrantI, flagrantII)) {
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
                                case "Defensive Rebound":
                            		undo.add(new Undo(assistPlayer, stat, recent.getTimeOf()));
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
    private void updateFile(List<Player> players) {
        CreateFile gameFile = new CreateFile(this.fileName);
        gameFile.openFile();
        gameFile.addGameConfig(SETTINGS);
        for (Player player : players){
            for (int column = 0; column < STATISTIC_ABBREVIATIONS.length; column++) {
            	gameFile.addInformation(STATISTIC_ABBREVIATIONS[column], player.getStat(STATISTIC_ABBREVIATIONS[column]));
            }
        	gameFile.addInformation("OnCourt", player.isStarter() + "");
        }
        gameFile.addUndo(this.undo);
        gameFile.closeFile();
    }
    
    // Post: Resets the window to have all the components of the standard ManagementPanel.
    private void setPane() {
    	this.frame.setTitle("Roster Management - " + this.fileName);
    	this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    	if (this.splitPane) {
        	this.pane.setLayout(new BoxLayout(this.pane, BoxLayout.Y_AXIS));
        	this.pane.add(this.headerPanel);
        	this.pane.add(this.managementPanel);
    	} else {
    		this.pane.setLayout(new BorderLayout());
        	this.pane.add(this.headerPanel, BorderLayout.NORTH);
        	this.pane.add(this.managementPanel, BorderLayout.CENTER);
    	}
		this.pane.revalidate();
		this.pane.repaint();
    }
    
    // Post: Puts all the Components of the ManagementPanel into a window that opens when the user starts the game.
    public void frame() {
    	setPane();
    	this.frame.add(this.pane);
        this.frame.getRootPane().setDefaultButton(this.buttonArray[START_BUTTON]);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    
    // Post: Returns a JPanel with all the components of a ManagementPanel. Used in the InstructionsPanel.
    public JPanel getManagementPanel() {
        JPanel panel = new JPanel();
        panel.add(this.headerPanel, BorderLayout.NORTH);
        JPanel managementP = new JPanel(new GridLayout(1, 1));
        managementP.add(this.managementPanel);
        managementP.setPreferredSize(new Dimension(SCREENWIDTH - (BUTTON_HEIGHT * 5 / 2), 700));
        panel.setBorder(new MatteBorder(BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, BORDER_SIZE, DEFAULT_TEXT_BORDER_COLOR));
        panel.setPreferredSize(new Dimension(SCREENWIDTH - (BUTTON_HEIGHT * 5 / 2), 1100));
        managementP.setBackground(background);
        panel.add(managementP);
        panel.setBackground(background);
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        return panel;
    }
    
    // Post: Returns the number of players who have fouled out of the game.
    private int getFouledOut() {
        int fouledOut = 0;
        for (Player player : this.bench) {
            if (!player.hasFouledOut(personalFouls, technicalFouls, flagrantI, flagrantII)) {
                fouledOut++;
            }
        }
        return fouledOut;
    }
    
    // Post: Returns the total of the statistic 'stat' for the entire team.
    private int getTotal(String stat) {
        int totalPoints = 0;
        for (Player play : this.total) {
            totalPoints += Integer.valueOf(play.getStat(stat));
        }
        return totalPoints;
    }
}

