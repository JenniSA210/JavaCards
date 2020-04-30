/**
 * 
 */
package application.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;

import application.model.Score.GameList;

/**
 * Loads scores from scores.txt on disk
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class Score {
	public enum GameList { BLACKJACK, GOFISH };
	ArrayList<PlayerScore> scoreList;
	
	Score() throws Exception {
		scoreList = new ArrayList<PlayerScore>();
		// Load scores from scores.txt
		// File format: First line: Game, Second Line: User name, Third line: Score 

		File file;
		Scanner scanner;
		try {
			file = new File("application/model/scores.txt");
			scanner = new Scanner(file); // Scanner to read input
		} catch(FileNotFoundException e) {
			throw new FileNotFoundException("scores.txt not found"); // If file not throw error
		}
		if (scanner.hasNextLine()) {
			while (scanner.hasNextLine()) {
				String gameStr, username;
				int score;
				GameList game;
				try {
					gameStr = scanner.nextLine().trim();
					if (gameStr.equalsIgnoreCase("BlackJack")){
						game = GameList.BLACKJACK;
					} else if (gameStr.equalsIgnoreCase("GoFish")){
						game = GameList.GOFISH;
					} else {
						throw new Exception("Scores.txt corrupted");
					}
					username = scanner.nextLine();
					score = Integer.parseInt(scanner.nextLine().trim());
					scoreList.add(new PlayerScore(game, username, score));
				} catch (NoSuchElementException | NumberFormatException e) {	// If no element or non-number character catch exception
					System.out.println("Invalid input");
					scanner.close();
					throw new Exception("scores.txt corrupted");
				}
			}
			scanner.close();
		} else {
			System.out.println("scores.txt is empty.");
			scanner.close();
			throw new Exception(""); // Throw exception if score.txt is empty
		}
	}
	
	/**
	 * Returns score of highest scoring player for particular game
	 * @return
	 */
	public int getHighScore(GameList gl) {
		int highScore = 0;
		for (int i = 0; i < scoreList.size(); i++) {
			if (scoreList.get(i).game == gl && scoreList.get(i).score > highScore) {
				highScore = scoreList.get(i).score;
			}
		}
		return highScore;
	}
	
	/**
	 * Adds score to score list and saves to scores.txt
	 * Checks for duplicates
	 * @param game
	 * @param username
	 * @param score
	 */
	public void addScore(GameList game, String username, int score) {
		if (username.trim().equals("")) return; // Can't add empty string
		// Check for existing username
		boolean listChanged = false;
		int duplicateIndex = -1;
		for (int i = 0; i < scoreList.size(); i++) {
			if (scoreList.get(i).username.equalsIgnoreCase(username))
				duplicateIndex = i;
		}
		// Add to scoreList if no duplicate
		if (duplicateIndex == -1) {
			scoreList.add(new PlayerScore(game, username, score));
			listChanged = true;
		}
		else if (score > scoreList.get(duplicateIndex).score) {
			scoreList.get(duplicateIndex).score = score;
			listChanged = true;
		}
		
		// Save scoreList to scores.txt if list changed
		if (listChanged) {
			File file = new File("application/model/scores.txt");
			try {
				FileWriter outFile= new FileWriter(file);
			
				for (int i = 0; i < scoreList.size(); i++) {
					outFile.write(scoreList.get(i).game + "\n");
					outFile.write(scoreList.get(i).username + "\n");
					outFile.write(scoreList.get(i).score + "\n");
				}
				outFile.close();
			} catch (IOException ioe) {
				ioe.printStackTrace();
				System.out.println("Error writing scores.txt");
			}
		}
	}
}

/**
 * Holds Game, Player Name, and Score for single player  
 *
 */
class PlayerScore{
	public GameList game;
	public String username;
	public int score;
	
	public PlayerScore(GameList game, String username, int score) {
		this.username = username;
		this.game = game;
		this.score = score;
	}
}
