import java.util.*;

/*
 *	PLAYER CLASS
 *	
 *	Class that represents a player in the system
 *	Player has a name, a primary + secondary position and a current points total
 *	Comparator ranks players by points
 *	
 */

public class Player implements Comparable<Player>{
	
	private String name;
	public String primary;
	public String secondary;
	private int points;

	public Player(String name, String pos, int points){
		this.name = name;
		this.points = points;
		String[] yeah = pos.split("/");
		this.primary = yeah[0];
		this.secondary = yeah[1];
	}

	public String getName(){
		return name;
	}

	public String getPos(){
		return (primary + "/" + secondary);
	}

	public int getPoints(){
		return points;
	}

	public void setPoints(int points){
		this.points = points;
	}

	public int compareTo(Player comp){
		int compPoints=((Player)comp).getPoints();

        /*For Descending order do like this */
        return compPoints-this.points;
	}
}