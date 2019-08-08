import java.util.*;

/*
 *	MATCH CLASS
 *	
 *	Class that contains all methods for match generation and player elo calculation
 *	Contains objects for the two teams in the match, and a list of players for the match
 *	
 */

public class Match{

	public ArrayList<Player> lads;

	public class Team{
		public Player[] team;
		private int skill;

		public int getSkill(){
			int i = 0;
			for(Player guy : team){
				i += guy.getPoints();
			}
			return(i / 5);
		}

		private Team(){
			this.team = new Player[5];
			skill = 0;
		}

	}

	public Team blue = new Team();
	public Team red = new Team();

	public Match(ArrayList<Player> lads){
		this.lads = lads;
		makeTeams();
		
	}

	//	**makeTeams method**
	//	Called by Match constructor, generates the teams by calling other methods in sequence
	private void makeTeams(){
		PosCollection[] roles = {new PosCollection(0,0,"T"),
								new PosCollection(0,0,"J"),
								new PosCollection(0,0,"M"),
								new PosCollection(0,0,"A"),
								new PosCollection(0,0,"S")};
		for(Player man : lads){
			assign(man, roles);
		}
		/*System.out.println("---------SORTING--------------");
		for(PosCollection sure : roles){
			sure.printMe();
		}
		System.out.println("------------------------------");*/
		setPlayers(roles);
		balanceTeams();
		/*System.out.println("MATCHUP:\n"
				+ blue.team[0].getName() + " vs. " + red.team[0].getName() + "\n"
				+ blue.team[1].getName() + " vs. " + red.team[1].getName() + "\n"
				+ blue.team[2].getName() + " vs. " + red.team[2].getName() + "\n"
				+ blue.team[3].getName() + " vs. " + red.team[3].getName() + "\n"
				+ blue.team[4].getName() + " vs. " + red.team[4].getName() + "\n------------------------------------------------");*/
	}

	//	**reprio method**
	//	Sorts the 5 roles in a given game into which order they should be picked (the role with the fewest primary/secondaries in a match gets priority)
	//	Called once in setPlayers method
	private void reprio(PosCollection[] roles){
		for(int b = 0; b < 4; b++){
			for(int i = (b + 1); i < 5; i++){
				if(roles[b].getPrimary() > roles[i].getPrimary() || (roles[b].getPrimary() == roles[i].getPrimary() && roles[b].getSecondary() > roles[i].getSecondary())){
					PosCollection temp = roles[b];
					roles[b] = roles[i];
					roles[i] = temp;
				}
			}
		}
	}

	//	**assign method**
	//	Records how many players have selected each role as primary/secondary
	//	Called for each player in makeTeams method
	private void assign(Player x, PosCollection[] collect){
		for(int i = 0; i < collect.length; i++){
			if(x.primary.equals(collect[i].pos)){
				collect[i].addPrimary();
			}
			else if(x.secondary.equals(collect[i].pos)){
				collect[i].addSecondary();
			}
		}
	}

	//	**reduce method**
	//	Refreshes the current number of unassigned players' position preference, the inverse of assign method
	//	Called whenever a player is added to the game in a specific position
	private void reduce(Player x, PosCollection[] collect){
		for(int i = 0; i < collect.length; i++){
			if(x.primary.equals(collect[i].pos)){
				collect[i].reducePrimary();
			}
			else if(x.secondary.equals(collect[i].pos)){
				collect[i].reduceSecondary();
			}
		}
	}

	//	**setPlayers method**
	//	Finds what order to assign positions, then assigns players to those positions
	//	Players are selected by following criteria: Primary matches current role > Primary is "F" (fill) > Secondary matches current role > Secondary is "F"
	//	Puts weaker player on blue team, and removes player and role from selection at the end of each iteration
	//	Called as part of makeTeams method
	private void setPlayers(PosCollection[] roles){
		for(int turns = 0; turns < 5; turns++){
			reprio(roles);
			int guard = 0;
			ArrayList<Integer> indexes = new ArrayList<Integer>();
			for(int i = 0; i < lads.size(); i++){
				if(lads.get(i).primary.equals(roles[0].pos)){
					indexes.add(i);
				}
			}
			if(indexes.size() < 2){
				guard = indexes.size();
				for(int i = 0; i < lads.size(); i++){
					if(lads.get(i).primary.equals("F")){
						indexes.add(i);
						if (indexes.size() > 2){
							indexes.subList(2, indexes.size()).clear();
						}
					}
				}
				if(indexes.size() < 2){
					guard = indexes.size();
					for(int i = 0; i < lads.size(); i++){
						if(lads.get(i).secondary.equals(roles[0].pos)){
							indexes.add(i);
							if (indexes.size() > 2){
								if(lookupP(lads.get(indexes.get(1)), roles) > lookupP(lads.get(indexes.get(2)), roles)){
									int temp = indexes.get(2);
									indexes.add(2, indexes.get(1));
									indexes.add(1, temp);
									if(guard == 0 && lookupP(lads.get(indexes.get(0)), roles) > lookupP(lads.get(indexes.get(2)), roles)){
										int temp2 = indexes.get(2);
										indexes.add(2, indexes.get(0));
										indexes.add(0, temp2);
									}
								}
								indexes.subList(2, indexes.size()).clear();
							}
						}
					}
					if(indexes.size() < 2){
						guard = indexes.size();
						for(int i = 0; i < lads.size(); i++){
							if(lads.get(i).secondary.equals("F")){
								indexes.add(i);
								if (indexes.size() > 2){
									if(lookupP(lads.get(indexes.get(1)), roles) > lookupP(lads.get(indexes.get(2)), roles)){
										int temp = indexes.get(2);
										indexes.add(2, indexes.get(1));
										indexes.add(1, temp);
										if(guard == 0 && lookupP(lads.get(indexes.get(0)), roles) > lookupP(lads.get(indexes.get(2)), roles)){
											int temp2 = indexes.get(2);
											indexes.add(2, indexes.get(0));
											indexes.add(0, temp2);
										}
									}
									indexes.subList(2, indexes.size()).clear();
								}
							}
						}
					}
				}
			}
			int p1 = indexes.get(0);
			int p2 = indexes.get(1);
			if(p1 > p2){
				int temp = p1;
				p1 = p2;
				p2 = temp;
			}
			blue.team[turns] = lads.get(p1);
			red.team[turns] = lads.get(p2);
			reduce(lads.get(p1), roles);
			reduce(lads.get(p1), roles);
			Player k = lads.remove(p2);
			k = lads.remove(p1);
			roles[0].eliminate();
		}
	}

	//	**lookupP method**
	//	Checks the popularity of a player's primary role in the current role list
	//	Called when prioritising players who have the same secondary role
	private int lookupP(Player guy, PosCollection[] roles){
		for(int i = 0; i < roles.length; i++){
			if(roles[i].pos.equals(guy.primary)){
				return roles[i].getPrimary();
			}
		}
		return -1;
	}

	//	**reduce method**
	//	Not used afaik :) outdated version of the previous "reduce" method
	private void reduce(String x, int[] collect){
		switch(x){
			case "T":
				collect[0]--;
				break;
			case "J":
				collect[1]--;
				break;
			case "M":
				collect[2]--;
				break;
			case "A":
				collect[3]--;
				break;
			case "S":
				collect[4]--;
				break;
			default:
				break;
		}
	}

	//	**balanceTeams method**
	//	Repeatedly checks to see if individual player swaps make teams more balanced
	// 	May need reworking in future...
	private void balanceTeams(){
		
        boolean finished = false;
        while(!finished){
        	finished = true;
        	int eloDiff = Math.abs(blue.getSkill() - red.getSkill());
        	for(int i = 0; i < 5; i++){
        		swapSpots(i);
            	if(Math.abs(blue.getSkill() - red.getSkill()) > eloDiff){
            		swapSpots(i);
            	} else {
            		eloDiff = Math.abs(blue.getSkill() - red.getSkill());
            		finished = false;
            	}
        	}
        }
        int b = blue.getSkill();
		int r = red.getSkill();
		if(b > r){
			Team temp = blue;
			blue = red;
			red = temp;
		}
		System.out.println("Blue skill - " + blue.getSkill());
		System.out.println("Red skill - " + red.getSkill());
	}

	//	**swapSpots method**
	//	Swaps two players assigned to the same position between teams
	//	Used when balancing teams
	private void swapSpots(int i){
		Player swapping = blue.team[i];
    	blue.team[i] = red.team[i];
    	red.team[i] = swapping;
	}

	//	**eloChange method**
	//	Changes the elo for each player in the game dependent on the result (passed in as arg)
	public void eloChange(boolean winner){

		int b = blue.getSkill();
		System.out.println("Blue skill - " + b);
		int r = red.getSkill();
		System.out.println("Red skill - " + r);

		for(Player p : blue.team){
			p.setPoints(eloCalculation(!winner, p.getPoints(), r));
		}
		for(Player p : red.team){
			p.setPoints(eloCalculation(winner, p.getPoints(), b));
		}

	}

	//	**eloCalculation method**
	//	Runs the calculation algorithm for a player and returns their elo
	//	Need to test whether or not this works
	private int eloCalculation(boolean winner, int pSkill, int oppSkill){
		int eloDifference = oppSkill - pSkill;
		double percentage = 1 / ( 1 + Math.pow( 10, (double) eloDifference / 400 ) );
		int win = (int) Math.round( 40 * ( 1 - percentage ) );
		int loss = (int) Math.round( 40 * ( 0 - percentage ) );
		//System.out.println("LOSS SCORE: " + loss);

		System.out.println("Elo difference: " + eloDifference + " / Percentage: " + percentage);

		if(winner){
			return (pSkill + win);
		}
		return (pSkill + loss);
	}
}