import java.io.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import java.io.FileNotFoundException;
import java.io.IOException;

/*	GENERATOR
 *	
 *	This class takes in a list of players from "day_list.txt" and makes matches
 *	Prints the matchup lists to the console, and puts the sorted list of players into "match_list.txt"
 *	Reruns the code until valid matchups are found (currently can get stuck infinitely...)
 */

public class Generator{

     public static void main(String []args){

     	ArrayList<Player> players = new ArrayList<Player>();
     	ArrayList<Match> games = new ArrayList<Match>();

     	int resetCount = 0;

     	System.out.println("------------------------------");

     	try{

	     	while(resetCount != -1){

		     	try(BufferedReader br = new BufferedReader(new FileReader("day_list.txt"))) {
				    String line = br.readLine();

				    // Parse player info from day_list.txt and add to player list
				    while (line != null) {
				        String[] breaker = line.split("\\s+");
				        int mmr = Integer.parseInt(breaker[breaker.length - 2]);
				        String pos = breaker[breaker.length - 3];
				        String[] fragments = Arrays.copyOfRange(breaker, 0, breaker.length - 3);
				        String ign = String.join(" ", fragments);
				        players.add(new Player(ign, pos, mmr));
				        line = br.readLine();
				    }
				    br.close();

				    // Rank players by MMR and print
				    Collections.shuffle(players, new Random());
				    players.subList((players.size()/10) * 10, players.size()).clear();
				    Collections.sort(players);
				    for(Player kevaman : players){
				    	System.out.println(kevaman.getName() + " " + kevaman.getPos() + " " + kevaman.getPoints());
				    }
				    System.out.println("------------------------------");

				    // Make a list of matches with defined players for each match
				    for(int i = 0; (i + 9) < players.size(); i += 10){
				    	ArrayList<Player> lads = new ArrayList<Player>();
				    	for(int j = 0; j < 10; j++){
				    		lads.add(players.get(i+j));
				    	}
				    	games.add(new Match(lads, (resetCount>50 ? false : true)));
				    }

				    // Print the match to the console
				    for(Match m : games){
					    System.out.println("MATCHUP:\nBlue side - "
						+ m.blue.team[0].getName() + ",  " + m.blue.team[1].getName() + ",  " + m.blue.team[2].getName() + ",  " + m.blue.team[3].getName() + ",  " + m.blue.team[4].getName() + "\nRed side - "
						+ m.red.team[0].getName() + ",  " + m.red.team[1].getName() + ",  " + m.red.team[2].getName() + ",  " + m.red.team[3].getName() + ",  " + m.red.team[4].getName() + "\n------------------------------------------------");
					}
				    resetCount = -1;
				}

				// Allows program to re-randomize the list until valid matchups are found
				catch(IndexOutOfBoundsException a){
					System.out.println("****************Error making matches. Resetting...*********************************");
					players = new ArrayList<Player>();
					games = new ArrayList<Match>();
					resetCount++;

				}

			}

			/*for(Match m : games){
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
			Collections.sort(players);*/

			// Writes list of ranked players for this round to match_list
			FileWriter fw = new FileWriter("match_list.txt");
			for(Player kevaman : players){
		    	fw.write(kevaman.getName() + " " + kevaman.getPos() + " " + kevaman.getPoints() + "\n");
		    }
			fw.close();

		} catch(IOException e){
				System.out.println("Ok something went really wrong: " + e);
		}

		
     }
}