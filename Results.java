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
     	ArrayList<Match> games = new ArrayList<Match>();

     	boolean valid = false;

     	System.out.println("------------------------------");

     	try{

	     	while(!valid){

		     	try(BufferedReader br = new BufferedReader(new FileReader("match_list.txt"))) {
				    String line = br.readLine();

				    while (line != null) {
				        String[] breaker = line.split("\\s+");
				        int i = Integer.parseInt(breaker[breaker.length - 1]);
				        String am = breaker[breaker.length - 2];
				        String[] not = Arrays.copyOfRange(breaker, 0, breaker.length - 2);
				        String groot = String.join(" ", not);
				        guys.add(new Player(groot, am, i));
				        line = br.readLine();
				    }
				    br.close();
				    /*for(Player kevaman : guys){
				    	System.out.println(kevaman.getName() + " " + kevaman.getPos() + " " + kevaman.getPoints());
				    }
				    System.out.println("------------------------------");*/
				    for(int i = 0; (i + 9) < guys.size(); i += 10){
				    	ArrayList<Player> lads = new ArrayList<Player>();
				    	for(int j = 0; j < 10; j++){
				    		lads.add(guys.get(i+j));
				    	}
				    	games.add(new Match(lads));
				    }
				    for(Match m : games){
					    System.out.println("MATCHUP:\nBlue side - "
						+ m.blue.team[0].getName() + ",  " + m.blue.team[1].getName() + ",  " + m.blue.team[2].getName() + ",  " + m.blue.team[3].getName() + ",  " + m.blue.team[4].getName() + "\nRed side - "
						+ m.red.team[0].getName() + ",  " + m.red.team[1].getName() + ",  " + m.red.team[2].getName() + ",  " + m.red.team[3].getName() + ",  " + m.red.team[4].getName() + "\n------------------------------------------------");
					}
				    valid = true;
				}

				
				catch(IndexOutOfBoundsException a){
					System.out.println("****************Error making matches. Resetting...*********************************");
					guys = new ArrayList<Player>();
					games = new ArrayList<Match>();

				}

			}

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
			Collections.sort(guys);
			FileWriter fw = new FileWriter("day_list.txt");
			for(Player kevaman : guys){
		    	fw.write(kevaman.getName() + " " + kevaman.getPos() + " " + kevaman.getPoints() + "\n");
		    }
			fw.close();

		} catch(IOException e){
				System.out.println("Ok something went really wrong: " + e);
		}

		
     }
}