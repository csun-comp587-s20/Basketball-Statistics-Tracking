// Alex Eidt
// Basketball Statistics Tracking Program
// Table Class
// Opens a window with a table showing all player statistics in the classic
// Box Score format used in the NBA.

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class Table extends GUISettings {
    
	private static final long serialVersionUID = 1L;
	private JTable boxscore;	// The table displaying all player data
	private Dimension size;		// The size of the table
	private int font, headerFont;
    
	// Post: Constructs a new table according to the parameters given.
	// Parameters:
	// 'players': The list of players to be displayed in the table
	// 'data': The data for each cell of the table
	// 'width', 'height': Dimensions of the table
	// 'fontSize', 'headerFontSize': Font sizes
	// 'settings': Settings used to format the fonts and colors used in the table
    public Table(ArrayList<Player> players, String[][] data, int width, int height, 
    		     int fontSize, int headerFontSize, GameSettings settings) {
        
        this.setLayout(new FlowLayout());
        this.size = new Dimension(width, height);
        this.font = fontSize;
        this.headerFont = headerFontSize;
        
        // Creates a new ArrayList of the existing players in order to accommodate for the
        // 'total' player which represents the totals for each statistic.
        ArrayList<Player> newPlayers = new ArrayList<Player>(players.size() + 1);
        newPlayers.addAll(players);
        
        // Adds a row at the bottom of the table representing the totals for each statistical category.
        Player total = new Player("Total", "", "");
        int[] totalStats = new int[STATISTIC_ABBREVIATIONS.length];
        for(Player player : players) {
        	int index = 0;
        	for(String stat : STATISTIC_ABBREVIATIONS) {
        		if(!(stat.equals("FG%") || stat.equals("3P%") || stat.equals("FT%") || stat.equals("Player"))) {
        			totalStats[index] += Integer.valueOf(player.getStat(stat, false));
        		}
        		index++;
        	}
        }
        for(int i = 1; i < totalStats.length; i++) {
        	total.setStat(STATISTIC_ABBREVIATIONS[i], String.valueOf(totalStats[i]));
        }
		total.setStat(STATISTIC_ABBREVIATIONS[0], "Total");
        newPlayers.add(total);
        
        for(Player player : newPlayers){
            for(int column = 0; column < STATISTIC_ABBREVIATIONS.length; column++) {
                data[newPlayers.indexOf(player)][column] = player.getStat(STATISTIC_ABBREVIATIONS[column], true);
            }
        }
              
        this.boxscore = new JTable(data, STATISTIC_ABBREVIATIONS) {
			private static final long serialVersionUID = 1L;
			public boolean isCellEditable(int data, int STATISTIC_ABBREVIATIONS) {
                return false;
            }
            public Component prepareRenderer(TableCellRenderer r, int data, int STATISTIC_ABBREVIATIONS) {
                Component c = super.prepareRenderer(r, data, STATISTIC_ABBREVIATIONS);
                //Change color of cell when selected
                if(isCellSelected(data, STATISTIC_ABBREVIATIONS)) {
                    c.setBackground(((Color) settings.getSetting(Setting.BACKGROUND_COLOR) != Color.WHITE) ? (Color) settings.getSetting(Setting.BACKGROUND_COLOR): Color.CYAN);
                } else {
                	c.setBackground(Color.WHITE);
                }  
                return c;
            }
        };
        //Get longest player name
        String longest = "";
        int max = 0;
        for(Player player : newPlayers) {
            if(player.getName().length() > max) {
                max = player.getName().length();
            }
        }
        for(Player player : newPlayers) {
        	if(player.getName().length() == max) {
        		longest = player.getName();
        		break;
        	}
        }
        int maxWidth = getDimension(formatLabel(longest, this.font, settings), Dim.WIDTH); 
        
        this.boxscore.setPreferredScrollableViewportSize(this.size);
        this.boxscore.setFillsViewportHeight(true);
        
        JScrollPane table = new JScrollPane(this.boxscore);
        table.getVerticalScrollBar().setUnitIncrement(3);
        
        // Format Table
        this.boxscore.setFont(new Font(DEFAULT_FONT_TYPE, Font.PLAIN, this.font));
        this.boxscore.setRowHeight(Math.min(SCREENHEIGHT/newPlayers.size(), 200));
        this.boxscore.setRowMargin(20);
        this.boxscore.getColumnModel().getColumn(0).setPreferredWidth(Math.max(maxWidth, 200));
        this.boxscore.getTableHeader().setFont(new Font(DEFAULT_FONT_TYPE, Font.BOLD, this.headerFont));
        
        // Center Data in cell
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        for(int i = 0; i < STATISTIC_ABBREVIATIONS.length; i++) {
        	this.boxscore.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }
        
        this.boxscore.setEnabled(false);
        this.boxscore.setShowGrid(false);
        this.add(table);
    }
}
