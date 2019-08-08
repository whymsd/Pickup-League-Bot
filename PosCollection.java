/*
 *	POSCOLLECTION CLASS
 *	
 *	Class used to represent a role in a match (always used as part of an array of the 5 roles)
 *	Lists the number of players who have the role as primary + secondary
 *	Contains methods to increment/decrement the primary/secondary counts, as well as remove the role form being prioritized
 *	
 */

public class PosCollection{
	
	private int primary;
	private int secondary;
	public String pos;

	public PosCollection(int primary, int secondary, String pos){
		this.primary = primary;
		this.secondary = secondary;
		this.pos = pos;
	}

	public void reducePrimary(){
		primary--;
	}

	public void reduceSecondary(){
		secondary--;
	}

	public void addPrimary(){
		primary++;
	}

	public void addSecondary(){
		secondary++;
	}

	public void eliminate(){
		primary = 100;
		secondary = 100;
	}

	public int getPrimary(){
		return primary;
	}

	public int getSecondary(){
		return secondary;
	}

	public void printMe(){
		System.out.println(pos + "   " + primary + "   " + secondary);
	}

}