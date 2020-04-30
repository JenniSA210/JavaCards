/**
 * 
 */
package application.model;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;
import java.util.ArrayList;

import application.model.Card.CardRank;
import application.model.Card.CardSuit;
import application.model.Score.GameList;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.control.Alert.AlertType;
import application.model.Score.GameList;

/**
 * Data Model for Go Fish Game
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class GoFishGame {
	private ArrayList<Card> deckOfCards;
	private ArrayList<Card> userHand;
	private ArrayList<Card> cpuHand;
	int userBooks;
	int cpuBooks;
	public int userScore;
	Score score;
	int highScore;
	String userName;
	
	/**
	 * Constructor for GoFishGame
	 */
	public GoFishGame(String userName) {
		deckOfCards = createShuffledDeckOfCards(); // randomize a deck to deal from
		userHand = new ArrayList<Card>();
		cpuHand = new ArrayList<Card>();
		userScore = 0;
		this.userName = userName;

		
		// Load high scores from scores.txt
		try {
			score = new Score();
			highScore = score.getHighScore(GameList.GOFISH);
		} catch (Exception e) {
			System.out.println(e);
		}
		
		// deal a hand of seven random cards to each player and set number of completed books to 0
		dealHand(deckOfCards, userHand);
		userBooks = 0;
		dealHand(deckOfCards, cpuHand);
		cpuBooks = 0;
	}
	
	public ArrayList<Image> getUserHand(){
		ArrayList<Image> userHandImages = new ArrayList<Image>();
		for (int i = 0; i < userHand.size(); i++) {
			userHandImages.add(userHand.get(i).image);
		}
		return userHandImages;
	}
	
	public ArrayList<Image> getDealerHand(boolean frontVisible){
		ArrayList<Image> dealerHandImages = new ArrayList<Image>();
		for (int i = 0; i < cpuHand.size(); i++) {
			if (frontVisible) cpuHand.get(i).image.setToFrontImage();
			else cpuHand.get(i).image.setToBackImage();
			dealerHandImages.add(cpuHand.get(i).image);
			
		}
		return dealerHandImages;
	}
	
	/**
	 * When player clicks on a user card, call this method to determine card rank
	 * and search CPU's cards for a matching card
	 * and if they do, remove that card from their deck and add to user deck
     * If not, then pull another card from the deck
	 * @param cardIndex Index of card clicked on compared to user's hand images
	 */
	public void userCardSelect(int cardIndex) {
		CardRank rank = userHand.get(cardIndex).rank;
		System.out.println(userHand.get(cardIndex));
		int i = 0;
		boolean found = false;
		// Search CPU hand for matching card, if found transfer to User hand
		while (i < cpuHand.size() && !found) {
			if (rank == cpuHand.get(i).rank) {
				found = true;
				userHand.add(cpuHand.remove(i));
				cpuHand.trimToSize();
			}
			i++;
		}
		if (!found) {
			// Pull another card from deck
			userHand.add(deckOfCards.remove(0));
		}
		// TODO isBookCompleted should just check if it is completed, modification should be here or other method called
		if (isBookCompleted(userHand)) {
			
		}
	}
	
	/**
	 * alternates user and cpu turn while total books < 13.
	 * then compares cpu and user books to determine a winner.
	 */
	public void begin() { // TODO This is handled by events instead of driven by a loop, remove
    	while(userBooks + cpuBooks < 13) { // loop turns until all 13 books are completed
			userTurn();
			isBookCompleted(userHand);
			cpuTurn();
			userBooks += 1;
			isBookCompleted(cpuHand);
		}
    	
    	
    	System.out.println("Exited while loop");
		// compare number of books to determine the winner
		if(userBooks > cpuBooks) {
			userScore += 20;
	    	score.addScore(GameList.GOFISH, userName, userScore);
			// if score exceeds previous high score, set as new high score

			Alert alert = new Alert(AlertType.INFORMATION, "You won!", ButtonType.OK);
			alert.showAndWait();
		}
		else {
			Alert alert = new Alert(AlertType.INFORMATION, "CPU won!", ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	/**
	 * Creates full 52 card deck all randomized 
	 * @return ArrayList of randomized, non-repeated cards
	 */
	private ArrayList<Card> createShuffledDeckOfCards() {
		// Create deck from cards based on random numbers from 0-51 (52 cards in deck)
		ArrayList<Card> deckOfCards = new ArrayList<Card>();
		Random rand = new Random();
		// Note: use LinkedHashSet to maintain insertion order without duplicates
		Set<Integer> randNumberList = new LinkedHashSet<Integer>();
		while (randNumberList.size() < 52)
		{
		    Integer next = rand.nextInt(52);
		    randNumberList.add(next); // Add checks for duplicates
		}
		
		// Convert those random numbers to enum ordinals
		// 0-12 CLUBS, 13-25 HEARTS, 26-38 SPADES, 39-51 DIAMONDS
		for (Integer i : randNumberList) {
			if (i < 13) { // 0-12 Clubs
				deckOfCards.add(new Card(CardSuit.CLUBS, CardRank.values()[i]));
			} else if (i < 26) { // 13-25 Hearts
				deckOfCards.add(new Card(CardSuit.HEARTS, CardRank.values()[i-13]));
			} else if (i < 39) { // 26-38 Spades
				deckOfCards.add(new Card(CardSuit.SPADES, CardRank.values()[i-26]));
			} else {
				// 39-51 Diamonds
				deckOfCards.add(new Card(CardSuit.DIAMONDS, CardRank.values()[i-39]));
			}
		}
		return deckOfCards;
	}
	
	void userTurn() {
		// player chooses card from their deck to ask for
		Card request; 
		// TODO: set request to card clicked on
		// random for testing purposes
		Random rand = new Random();
		try {
			System.out.println("size of userhand: " + userHand.size());
			request = cpuHand.get(rand.nextInt(userHand.size() - 1));
			Alert alert = new Alert(AlertType.INFORMATION, "Got any " + request.rank + "s?", ButtonType.OK);
			alert.showAndWait();
			
			if(cpuAskForCard(request) == false) {
				Alert alert2 = new Alert(AlertType.INFORMATION, "Go fish!!!", ButtonType.OK);
				alert2.showAndWait();
				goFish(cpuHand);
			}
		} catch(IllegalArgumentException e) {
			System.out.println("error caught in user turn");
		}
	}

	
	/**
	* takes a random card from cpu deck as request and calls cpuAskForCard()
	* if cpyAskForCard() returns false, call goFish()
	*/
	void cpuTurn() {
		// cpu asks for a random card in its hand
		Random rand = new Random();
		try {
			System.out.println("size of cpuhand: " + cpuHand.size());
			Card request = cpuHand.get(rand.nextInt(cpuHand.size() - 1));
			Alert alert = new Alert(AlertType.INFORMATION, "Got any " + request.rank + "s?", ButtonType.OK);
			alert.showAndWait();
			
			if(cpuAskForCard(request) == false) {
				Alert alert2 = new Alert(AlertType.INFORMATION, "Go fish!!!", ButtonType.OK);
				alert2.showAndWait();
				goFish(cpuHand);
			}
		} catch(IllegalArgumentException e) {
			System.out.println("error caught in cpu turn");
		}

	}
	
	/**
	 * Searches cpu hand for cards that match the rank of a card selected by the user.
	 * If found, cards are removed from cpu hand and added to user hand.
	 * @param request, a Card selected by the user
	 * @return boolean: true if at least one card is taken, false otherwise
	 */
	boolean userAskForCard(Card request) {
		boolean successful = false;
		
		// create a temp storage for cards that will be removed/added
		ArrayList<Card> tempList = new ArrayList<Card>();
		
		for(Card c : cpuHand) {
			if(c.rank == request.rank) {
				tempList.add(c);
				successful = true;
			}
		}
		
		// remove/add from tempList
		userHand.addAll(tempList);
		cpuHand.removeAll(tempList);
		
		return successful;
	}
	
	/**
	 * Searches user hand for cards that match the rank of a card selected by the cpu.
	 * If found, cards are removed from user hand and added to cpu hand.
	 * @param request, a randomly selected Card
	 * @return boolean: true if at least one card is taken, false otherwise
	 */
	boolean cpuAskForCard(Card request) {
		boolean successful = false;
		// create a temp storage for cards that will be removed/added
		ArrayList<Card> tempList = new ArrayList<Card>();
			
		for(Card c : cpuHand) {
			if(c.rank == request.rank) {
				tempList.add(c);
				successful = true;
			}
		}
				
		// remove/add from tempList
		userHand.addAll(tempList);
		cpuHand.removeAll(tempList);
		return successful;
	}
	
	/**
	 * If there are still Cards in deckOfCards, the first will be added to a player's hand.
	 * Otherwise, a message will display and nothing will change.
	 * @param hand, an ArrayList of Cards that make up the player's hand
	 */
	void goFish(ArrayList<Card> hand) {
		if(deckOfCards.size() > 0) { // if the deck is not empty, take a card
			hand.add(deckOfCards.remove(0));
		}
		else {
			// give message that the deck is empty
			Alert alert = new Alert(AlertType.INFORMATION, "No cards left to take.", ButtonType.OK);
			alert.showAndWait();
		}
	}
	
	/**
	 * Called at beginning of game.
	 * Removes 7 Card elements from deckOfCards and adds them to a hand.
	 * @param deckOfCards, an ArrayList of Cards to remove from
	 * @param hand, an ArrayList to add Cards to
	 */
	void dealHand(ArrayList<Card> deckOfCards, ArrayList<Card> hand) {
		// deal 7 cards from beginning of deck to the hand
		for(int i = 0; i < 7; i++) {
			hand.add(deckOfCards.remove(0));
		}
	}
	
	/**
	 * Checks to see if four cards of the same rank exist in a given deck.
	 * Will then remove these cards from play.
	 * @param hand, the hand that is being checked for Cards of the same rank.
	 */
	boolean isBookCompleted(ArrayList<Card> hand) {
		int count;
		// create a temp storage for cards that will be removed
		ArrayList<Card> tempList = new ArrayList<Card>();
		
		// for each card in the hand, see how many other cards have the same rank
		for(Card c1 : hand) {
			count = 1;
			for(Card c2 : hand) {
				if(c1.rank == c2.rank) {
					count++;
				}
			}
			if(count == 4) { // if a book has been made
				// add all cards of the same rank to the templist
				tempList.add(c1);
				for(Card c2 : hand) {
					if(c1.rank == c2.rank) {
						tempList.add(c2);
					}
				}

				hand.removeAll(tempList);
				userBooks++;
				return true;
				// TODO: add graphic for a book of c1.rank cards
			}
		}
		return false;
		
	}
	
	/**
	 * Returns user score
	 * @return Total user score
	 */
	public int getScore() {
		// To be called when game is finished
		return userScore;
	}
	
	/**
	 * Returns high score from Score class (disk)
	 * @return
	 */
	public int getHighScore() {
		return highScore;
	}

}
