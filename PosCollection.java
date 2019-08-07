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