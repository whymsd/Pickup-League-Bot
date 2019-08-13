import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import java.io.FileNotFoundException;
import java.io.IOException;

/*	RESULTS
 *	
 *	This class takes in a sorted list of players from "match_list.txt" and remakes the matches
 *	Prints the matchup lists to the console, and asks for results on each match
 *	When the results are given, calculates the new elo for each player and prints their stats to "day_list.txt"
 */

public class Results{

     public static void main(String []args){

     	ArrayList<Player> guys = new ArrayList<Player>();
     	ArrayList<Player> fullPlayers = new ArrayList<Player>();
     	ArrayList<Match> games = new ArrayList<Match>();

     	int resetCount = 0;

     	System.out.println("------------------------------");

     	while(resetCount != -1){

	     	try{
			    
	     		inFromFile("match_list.txt", guys);
	     		inFromFile("og_list.txt", fullPlayers);
			    
			    for(int i = 0; (i + 9) < guys.size(); i += 10){
			    	ArrayList<Player> lads = new ArrayList<Player>();
			    	for(int j = 0; j < 10; j++){
			    		lads.add(guys.get(i+j));
			    	}
			    	games.add(new Match(lads, (resetCount>50 ? false : true)));
			    }
			    for(Match m : games){
				    System.out.println("MATCHUP:\nBlue side - "
					+ m.blue.team[0].getName() + ",  " + m.blue.team[1].getName() + ",  " + m.blue.team[2].getName() + ",  " + m.blue.team[3].getName() + ",  " + m.blue.team[4].getName() + "\nRed side - "
					+ m.red.team[0].getName() + ",  " + m.red.team[1].getName() + ",  " + m.red.team[2].getName() + ",  " + m.red.team[3].getName() + ",  " + m.red.team[4].getName() + "\n------------------------------------------------");
				}
			    resetCount = -1;
			}

			
			catch(IndexOutOfBoundsException a){
				System.out.println("****************Error making matches. Resetting...*********************************");
				guys = new ArrayList<Player>();
				games = new ArrayList<Match>();
				resetCount++;
			}

		}
		try{
			for(Match m : games){
				System.out.println("Enter the winner of match " + games.indexOf(m) + "\n");
				BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		    	String yeet = reader.readLine();
		    	if(yeet.equalsIgnoreCase("blue")){
		    		m.eloChange(false);
		    	}
		    	else if(yeet.equalsIgnoreCase("red")){
		    		m.eloChange(true);
		    	}
		    	else{
		    		System.out.println("Error with entering scores. Please fix manually.");
		    	}
			}

			for(Player p : guys){
				for(Player q : fullPlayers){
					if(p.getName().equals(q.getName())){
						if(p.getPoints()>q.getPoints()) q.wins++;
						else q.losses++;
						q.setPoints(p.getPoints());
						break;
					}
				}
			}
			//Collections.sort(guys);
			FileWriter fw = new FileWriter("og_list.txt");
			for(Player kevaman : fullPlayers){
		    	fw.write(kevaman.getName() + " " + kevaman.getPos() + " " + kevaman.getPoints() + " " + kevaman.getWL() + "\n");
		    }
			fw.close();
		} catch (Exception e){
			System.out.println("CAUGHT AN EXCEPTION: " + e);
		}
		
     }

     private static void inFromFile(String fName, ArrayList<Player> players){
     	try{
	     	BufferedReader br = new BufferedReader(new FileReader(fName));
		    String line = br.readLine();
		    while (line != null) {
		        String[] breaker = line.split("\\s+");
		        String wl = breaker[breaker.length - 1];
		        int mmr = Integer.parseInt(breaker[breaker.length -2]);
		        String pos = breaker[breaker.length - 3];
		        String[] fragments = Arrays.copyOfRange(breaker, 0, breaker.length - 3);
		        String ign = String.join(" ", fragments);
		        players.add(new Player(ign, pos, mmr, wl));
		        line = br.readLine();
		    }
		    br.close();
		} catch (Exception e){
			System.out.println("CAUGHT AN EXCEPTION: " + e);
		}
     }
}