// Alex Eidt
// Basketball Statistics Tracking Program
// InstructionPanel Class
// Opens a window that shows the documentation of the program in a user friendly way.
// Paragraphs accompany sample windows that are user interactive and allow the user
// to understand how to use the program before they begin tracking their own game.

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

public class InstructionPanel extends GUISettings {

	private static final long serialVersionUID = 1L;
	
	// Indices of buttons in the 'buttonArray'
	private static final int MAINMENU_BUTTON = 0;
	private static final int ABBREVIATIONS_BUTTON = 1;
	
	// Name of the text file containing the written explanations for each statistic being
	// tracked by the program
	private static final String STAT_EXPLANATIONS_FILE = "Stat_Explanations.txt";
	// Name of the text file containing the explanations found in the Instructions window
	private static final String TEXT_FILE_NAME = "Instructions.txt";	
	// The sequence of characters in the TEXT_FILE that signals a new paragraph
	private static final String PARAGRAPH_SPLITTER = "*-*";
	// Name of the text file that contains the game data for the simulated games
	private static final String GAMEDATA_FILE_NAME = "InstructionExample." + FILETYPE;
	// Used when scanning the text file to enter the correct number of starters
	private static final String STARTER_KEY = "STARTER_KEY";
	// Used to convert numbers to letters for the sample players used
	private static final String[] NUMBER_NAMES = {"One", "Two", "Three", "Four", "Five", "Six", "Seven", "Eight",
			                                      "Nine", "Ten", "Eleven", "Twelve", "Thirteen", "Fourteen", "Fifteen"};
	
	private ArrayList<JTextArea> textFields; // List of paragraphs in the TEXT_FILE
	private ArrayList<Player> samplePlayers; // Sample player list used to demo all the program functions
    private ArrayList<Undo> undoArray; // History of all events of the game
	private JFrame frame; // The frame that opens when the 'Instructions' button is pressed
	private JFrame abbFrame; // The frame that opens when the 'Abbreviations' button is pressed
	private JFrame explainFrame; // The frame that opens when a statistic in the 'abbFrame' is pressed
    private JButton[] buttonArray; // All the buttons on the InstructionPanel
    private GameSettings SETTINGS; // The settings used to configure the simulated games and the InstructionPanel
    
    // Post: Constructs a InstructionPanel with the given 'settings'.
    public InstructionPanel(GameSettings settings) {
    	this.SETTINGS = settings;
		this.textFields = new ArrayList<JTextArea>();
		this.samplePlayers = new ArrayList<Player>(MAX_SAMPLE_PLAYERS);
		this.undoArray = new ArrayList<Undo>();
		String[] buttonNames = {" Main Menu", " Abbreviations"};
		int[] sizes = {FONT_SIZE * 2 / 3, FONT_SIZE * 2 / 3};
		this.buttonArray = createButtonArray(buttonNames, sizes, SETTINGS);
		this.frame = new JFrame("Instructions");
		this.abbFrame = new JFrame("Abbreviations Explanations");
		this.explainFrame = new JFrame("Explanation");
		scanFile();
		int[] indices = {MAINMENU_BUTTON, ABBREVIATIONS_BUTTON};
		String[] icons = {MAINMENU_BUTTON_ICON, ABBREVIATIONS_BUTTON_ICON};
		formatIcons(this.buttonArray, indices, icons);
    }
    
    // Post: Scans the Instructions file with all the text that appears in the InstructionPanel.
    private void scanFile() {
    	Scanner scan = null;
    	Scanner lineScan = null;
		try {
			scan = new Scanner(new File(TEXT_FILE_NAME));
			lineScan = new Scanner(new File(TEXT_FILE_NAME));
		} catch (FileNotFoundException e) {
			System.out.println(TEXT_FILE_NAME + " was not found");
		}
		// Count the number of paragraphs separated by the PARAGRAPH_SPLITTER
    	int textAreas = 0;
		while (lineScan.hasNextLine()) {
			if (lineScan.nextLine().contains(PARAGRAPH_SPLITTER)) {
				textAreas++;
			}
		}
		// Format each text area for each paragraph
		for (int i = 1; i <= textAreas; i++) {
			JTextArea area = formatTextArea(SCREENWIDTH - 400, 200, SETTINGS);
			area.setFont(new Font(DEFAULT_FONT_TYPE, Font.PLAIN, 35));
			this.textFields.add(area);
		}
		// Scan in the text from the input file
		ArrayList<String> textBoxes = new ArrayList<String>();
		while (scan.hasNextLine()) {
			textBoxes.add(scan.nextLine());
		}
		ArrayList<String> text = new ArrayList<String>();
		String paragraph = "";
		for (String line : textBoxes) {
			if (!line.contains(PARAGRAPH_SPLITTER)) {
				paragraph += (line + " ");
			} else {
				paragraph = paragraph.replaceAll(STARTER_KEY, (int) (SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS)) + "");
				text.add(paragraph);
				paragraph = "";
			}
		}	
		// Set the text of each text area in the InstructionsPanel
		int index = 0;
		for (JTextArea area : this.textFields) {
			area.setText(text.get(index));
			index++;
		}
    }
    
    // Post: Adds functions to each button in the 'buttonArray'.
    public void addButtonFunctions() {
    	// Main Menu button closes the Instructions Panel and brings the user back to the GetPlayersPanel
        this.buttonArray[MAINMENU_BUTTON].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		explainFrame.dispose();
        		abbFrame.dispose();
        		frame.dispose();
        	}
        });  
        // Abbreviations button shows the user a list of all abbreviations used for statistics in
        // basketball and allows the user to press a button to learn more about that statistic
        this.buttonArray[ABBREVIATIONS_BUTTON].addActionListener(new ActionListener() {
        	public void actionPerformed(ActionEvent e) {
        		Color secondaryColor = DEFAULT_BACKGROUND_COLOR;
        		Color textBorderColor = DEFAULT_TEXT_BORDER_COLOR;
        		Border border = new MatteBorder(5, 5, 5, 5, textBorderColor);
        		// STATISTIC ABBREVIATIONS COLUMN
        		// ****************************************************************************************
        		JPanel abbreviations = new JPanel(new GridLayout(STATISTIC_ABBREVIATIONS.length + 1, 1));
        		JLabel labelColumn = formatLabel("Statistic Name", 30, SETTINGS);
        		labelColumn.setBackground(secondaryColor);
    			labelColumn.setBorder(border);
    			labelColumn.setOpaque(true);
        		abbreviations.add(labelColumn);
        		// Add all abbreviated statistic names to the abbreviations column
        		for (String s : STATISTIC_ABBREVIATIONS) {
        			JLabel label = formatLabel(s, 25, SETTINGS);
        			label.setBackground(secondaryColor);
        			label.setOpaque(true);
        			label.setBorder(border);
        			abbreviations.add(label);
        		}
        		// STATISTIC EXPLANATIONS COLUMN
        		// ****************************************************************************************
        		JPanel fullStatWords = new JPanel(new GridLayout(STATISTIC_ENTIRE_WORDS.length + 1, 1));
        		JLabel descriptionColumn = formatLabel("Explanation", FONT_SIZE / 2, SETTINGS);
        		descriptionColumn.setBackground(secondaryColor);
    			descriptionColumn.setBorder(border);
    			descriptionColumn.setOpaque(true);
        		fullStatWords.add(descriptionColumn);
				scanExplanationsFile(fullStatWords);
				
        		JPanel total = new JPanel(new GridLayout(1, 2));
        		total.add(abbreviations);
        		total.add(fullStatWords);
        		JScrollPane abbScrollPane = addScrollPane(total);
        		abbScrollPane.setPreferredSize(new Dimension(SCREENWIDTH / 2, abbFrame.getHeight()));
        		formatFrame(abbFrame, abbScrollPane, SCREENWIDTH / 2, 800);
        	}
        });
    }
    
    // Post: Adds a JButton with the full-length name of the statistic that corresponds to the abbreviated
    //       statistic name on the other column. 
    //       Adds an actionListener to the JButton that opens a new frame with a detailed explanation of 
    //       what that statistic is.
    private void scanExplanationsFile(JPanel panel) {
    	Scanner scanner = null; 
		try {
			scanner = new Scanner(new File(STAT_EXPLANATIONS_FILE));
		} catch (FileNotFoundException e1) {
			System.out.println(STAT_EXPLANATIONS_FILE + " does not exist.");
		}
		ArrayList<String> data = new ArrayList<String>(STATISTIC_ENTIRE_WORDS.length * 2);
		while (scanner.hasNextLine()) {
			data.add(scanner.nextLine());
		}
		// Add a JButton for each entire statistic word
		for (String fullStatWord : STATISTIC_ENTIRE_WORDS) {
			JButton label = new JButton(fullStatWord);
			formatButton(label, 400, 100, 25, SETTINGS);
			// When the button is pressed, a new window opens with a explanation from the 'data' list
			label.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					int index = data.indexOf(label.getText());
					String explanationText = data.get(index + 1);
					JPanel explainPanel = new JPanel(new GridLayout(2, 1));
					JLabel explainLabel = formatLabel(data.get(index), 35, SETTINGS);
					explainLabel.setOpaque(true);
					explainLabel.setBackground((Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR));
					explainPanel.add(explainLabel);
					JTextPane explain = formatTextPane(explanationText, FONT_SIZE / 3, SETTINGS);
					explainPanel.add(explain);
					formatFrame(explainFrame, explainPanel, 600, 600);
				}
			});
			panel.add(label);
		}
    }
    
    // Indices of paragraphs in 'textFields'
    private static final int PARAGRAPH1 = 0;
    private static final int PARAGRAPH2 = 1;
    private static final int PARAGRAPH3 = 2;
    private static final int PARAGRAPH4 = 3;
    private static final int PARAGRAPH5 = 4;
    private static final int PARAGRAPH6 = 5;
    private static final int PARAGRAPH7 = 6;    
    private static final int PARAGRAPH8 = 7;
    private static final int MAX_SAMPLE_PLAYERS = 10;
    
    // Post: Adds all the Components in the Instructions Panel into one Frame
    public void addElements() {
    	Color background = (Color) SETTINGS.getSetting(Setting.BACKGROUND_COLOR);
    	int numberOfStarters = (int) SETTINGS.getSetting(Setting.NUMBER_OF_STARTERS);
    	
    	ArrayList<JPanel> textPanels = new ArrayList<JPanel>();
		for (int i = 0; i < this.textFields.size(); i++) {
			JPanel textPanel = new JPanel();
			textPanel.add(textFields.get(i));
			textPanel.setBorder(new MatteBorder(30, 30, 30, 30, background));
	        textPanel.setBackground(background);
	        textPanels.add(textPanel);
		}
		// Add players to 'samplePlayers'
		for (int i = 1; i <= numberOfStarters; i++) {
			Player player = new Player("Player", NUMBER_NAMES[i - 1], "Player " + i + ".");
			player.setOnFloor();
			this.samplePlayers.add(player);
		}
        
		// Panel that stores the 'Back to Main Menu' button
        JPanel menuPanel = new JPanel();
        menuPanel.add(this.buttonArray[MAINMENU_BUTTON]);
        menuPanel.setBackground(background);
        
        // Panel that stores the 'Abbreviations' button
        JPanel abbreviate = new JPanel();
        abbreviate.add(this.buttonArray[ABBREVIATIONS_BUTTON]);
        abbreviate.setBackground(background);

        // List of sample players that start on the bench
		ArrayList<Player> bench = new ArrayList<Player>();
		// The first ManagementPanel window showing what a ManagementPanel window looks like when the number of players
		// entered equals the number of starters selected by the user.
		// In this case, the bench is empty
        ManagementPanel mPanel = new ManagementPanel(this.samplePlayers, bench, this.undoArray, GAMEDATA_FILE_NAME, SETTINGS);
        mPanel.createTeamButtons();
        mPanel.createTableButton();
        mPanel.createStartStopButton();
        mPanel.createTimeoutButton();
        mPanel.createUndoButton();
        mPanel.addElements();
        
        // The panel that stores the first Management Panel constructed above ^
        JPanel frameMP = new JPanel();
        frameMP.add(mPanel.getManagementPanel());
        frameMP.setBackground(background);
        
        // The bench is filled up with sample players numbered 6-10
        for (int i = numberOfStarters + 1; i <= MAX_SAMPLE_PLAYERS; i++) {
        	Player player = new Player("Player", NUMBER_NAMES[i - 1], "Player " + i + ".");
        	player.setOffFloor();
        	bench.add(player);
        }
        
        // For instructional purposes, the 6th player (first player off the bench) is fouled out by default to show the 
        // user what a fouled out player looks like
        bench.get(0).setStat("TF", (int) SETTINGS.getSetting(Setting.TECHNICAL_FOULS) + "");
        
        // The second version of the ManagementPanel showing what a ManagementPanel looks like when more players than starters
        // are entered
        ManagementPanel mPanelBench = new ManagementPanel(this.samplePlayers, bench, 
        		                                          this.undoArray, GAMEDATA_FILE_NAME, SETTINGS);
        mPanelBench.createTableButton();
        mPanelBench.createUndoButton();
        mPanelBench.createPlayerButtons();
        mPanelBench.createTimeoutButton();
        mPanelBench.createStartStopButton();
        mPanelBench.addElements();      
        
        // The panel that stores the ManagementPanel constructed above ^
        // MPB (Management Panel w/ Bench)
        JPanel frameMPB = new JPanel();
        frameMPB.add(mPanelBench.getManagementPanel());
        frameMPB.setBackground(background);
        
        // The GetPlayersPanel. Appears when the program is opened. Serves as a 'Main Menu'
        GetPlayersPanel gPPanel = new GetPlayersPanel(SETTINGS);
        gPPanel.addElements();
        gPPanel.createTextField();
        gPPanel.createSubmitButton();
        gPPanel.createUndoButton();
        
        // The panel that stores the GetPlayersPanel constructed above ^
        JPanel frameGPPanel = new JPanel();
        frameGPPanel.add(gPPanel.getPlayersPanel());
        frameGPPanel.setBackground(background);
        
        // Puts all players into one list
        ArrayList<Player> totalSamplePlayers = new ArrayList<Player>();
        totalSamplePlayers.addAll(samplePlayers);
        totalSamplePlayers.addAll(bench);
        
        // The StartersPanel that appears when more players are entered than starters
        StartersPanel startersPanel = new StartersPanel(totalSamplePlayers, GAMEDATA_FILE_NAME, SETTINGS);
        startersPanel.createPlayerButtons();
        startersPanel.createUndoButton();
        startersPanel.addElements();
        
        // The panel that stores the StartersPanel constructed above ^
        JPanel frameSP = new JPanel();
        frameSP.add(startersPanel.getStartersPanel());
        frameSP.setBackground(background);
        
        // The panel that stores the header for the InstructionsPanel
		JPanel header = new JPanel();
		header.add(formatLabel("Instructions", 65, SETTINGS));
		header.setBackground(background);
		
		// The panel that stores all panels in the InstructionsPanel
		JPanel panels = new JPanel();
		// All elements in 'panels' in descending order (BoxLayout.Y_AXIS used)
		Component[] components = {header, textPanels.get(PARAGRAPH1), frameGPPanel, textPanels.get(PARAGRAPH2), frameSP,
						          textPanels.get(PARAGRAPH3), frameMP, textPanels.get(PARAGRAPH4), abbreviate, 
						          textPanels.get(PARAGRAPH5), frameMPB, textPanels.get(PARAGRAPH6), 
						          textPanels.get(PARAGRAPH7), textPanels.get(PARAGRAPH8), menuPanel, textPanels.get(PARAGRAPH8)};
		formatPanel(panels, components, null, null, null, background);
        
		// Add a scroll bar for the InstructionsPanel     
        JPanel panePanel = new JPanel(new GridLayout(1, 1));
        panePanel.add(addScrollPane(panels));
        
        this.frame.add(panePanel);
        this.frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        this.frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.frame.setVisible(true);
    }
}