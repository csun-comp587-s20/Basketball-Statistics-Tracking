// Alex Eidt
// Basketball Statistics Tracking Program
// Undo Class
// An Undo is a piece of information that stores a statistic, the player to whom the statistic
// belongs to, the time the statistic was earned and a player that assisted that players shot if
// necessary.

import java.util.*;

public class Undo implements Comparable<Undo> {
	
	private String stat; 		// The statistic being tracked
	private String time;    	// The time the statistic was recorded
	private Player player;  	// The player who got that statistic
	private Player astPlayer;	// If necessary, the player who assisted the 'players' shot
	
	// Post: Constructs an Undo with the given 'player', 'stat', and 'time'.
	//       astPlayer is initialized to a special TEST version.
	public Undo(Player player, String stat, String time) {
		this.player = player;
		this.stat = stat;
		this.time = time;
		this.astPlayer = new Player("TEST", "TEST", "TEST");
	}
	
	// Post: Sets the astPlayer to be the player given.
	public void setAstPlayer(Player player) {
		this.astPlayer = player;
	}
	
	// Post: Returns the player who got the statistic.
	public Player getPlayer() {
		return this.player;
	}
	
	// Post: Returns the player who assisted the shot.
	public Player getAstPlayer() {
		return this.astPlayer;
	}
	
	// Post: Returns the statistic.
	public String getStatFromPlayer() {
		return this.stat;
	}
	
	// Post: Returns the time of the statistic.
	public String getTimeOf() {
		return this.time;
	}
	
	// Post: Returns true if 'astPlayer' is a real player.
	//       Returns false otherwise.
	public boolean hasAssistPlayer() {
		return !this.astPlayer.getName().equals("TEST") 
				&& !this.astPlayer.getLastName().equals("TEST") 
				&& !this.astPlayer.getDisplayName().equals("TEST");
	}
	
	// Post: Returns an ArrayList<String> of all the player data and statistical data
    public ArrayList<String> toStringArray() {
    	ArrayList<String> data = new ArrayList<String>(8);
    	data.add(this.player.getName());
    	data.add(this.player.getLastName());
    	data.add(this.player.getDisplayName());
    	data.add(this.astPlayer.getName());
    	data.add(this.astPlayer.getLastName());
    	data.add(this.astPlayer.getDisplayName());
    	data.add(this.stat);
    	data.add(this.time);
    	return data;
    }
    
    // Post: Returns a String in the form: '(stat) (time)' 
    //                        If assisted: '(stat) - Assisted by (astPlayer) (time)'
    public String toString() {
    	String text = this.stat;
    	if(text.equals("Substitution")) {
    		return "Substitution for: " + this.astPlayer.getDisplayName() + " " + time;
    	} else {
        	boolean isAst = this.astPlayer.getName().equals("TEST") && this.astPlayer.getLastName().equals("TEST") 
        			&& this.astPlayer.getDisplayName().equals("TEST");
        	if(isAst) {
        		return text + " " + time;
        	} else {
        		text += " - Assisted by: " + this.astPlayer.getName() + " " + this.astPlayer.getLastName();
        		return text + " " + time;
        	}
    	}
    }
    
    public int compareTo(Undo other) {
    	return getTime(getTimeOf()) - getTime(other.getTimeOf());
    }
    
    private int getTime(String time) {
    	String[] data = time.split(":");
    	String minutes = data[0].trim();
    	String seconds = data[1].trim();
    	int min = Integer.valueOf(minutes);
    	int sec = Integer.valueOf(seconds);
    	return (min * 60) + sec;
    }
}
