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
// TODO: change book from int to array list that stores a card of that rank
	private ArrayList<Card> userBooks;
	private ArrayList<Card> cpuBooks;
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
		userBooks = new ArrayList<Card>();
		dealHand(deckOfCards, cpuHand);
		cpuBooks = new ArrayList<Card>();
	}
	
	public ArrayList<Image> getUserHand(){
		ArrayList<Image> userHandImages = new ArrayList<Image>();
		for (int i = 0; i < userHand.size(); i++) {
			userHandImages.add(userHand.get(i).image);
		}
		return userHandImages;
	}
	
	public ArrayList<Image> getUserBooks(){
		if(userBooks.size() == 0)
			return null;
		ArrayList<Image> userBookImages = new ArrayList<Image>();
		for(int i = 0; i < userBooks.size(); i++) {
			userBookImages.add(userBooks.get(i).image);
		}
		return userBookImages;
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
	
	public ArrayList<Image> getCpuBooks(){
		if(cpuBooks.size() == 0)
			return null;
		ArrayList<Image> cpuBookImages = new ArrayList<Image>();
		for(int i = 0; i < cpuBooks.size(); i++) {
			cpuBookImages.add(cpuBooks.get(i).image);
		}
		return cpuBookImages;
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
		ArrayList<Card> tempList = new ArrayList<Card>(); // stores cards to transfer
		
		// Search CPU hand for matching cards, if found transfer all to User hand
		while (i < cpuHand.size()) {
			if (rank == cpuHand.get(i).rank) {
				found = true;
				tempList.add(cpuHand.get(i));
			}
			i++;
		}
		if(tempList.size() != 0) {
			// first set the images of the transferred cards to the front
			cpuHand.removeAll(tempList);
			for(Card card : tempList) {
				card.image.setToFrontImage();
			}
			userHand.addAll(tempList);
			
			cpuHand.trimToSize();
		}
		
		if (!found) {
			// Pull another card from deck if deck is not empty
			if(checkIfDeckEmpty() == false)
				userHand.add(deckOfCards.remove(0));
		}
		
		// check if this has completed a book
		if (isBookCompleted(userHand)) {
			removeBookFromHand(userHand, userBooks);
			
			// check if adding book has ended the game
			if(userBooks.size() + cpuBooks.size() == 13) {
				// TODO: write function to end the game and determine a winner
				System.out.println("game has ended");
			}
			
			// check if removing the book has emptied user's hand
			if(checkIfHandEmpty(userHand)) {
				if(checkIfDeckEmpty() == true)
					userHand.add(deckOfCards.remove(0));
			}
		}
		
		// check if removing cards has emptied cpu's hand
		if(checkIfHandEmpty(cpuHand)) {
			if(checkIfDeckEmpty() == true)
				cpuHand.add(deckOfCards.remove(0));
		}
		
		cpuTurn();	
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
	
	/**
	* takes a random card from cpu deck as request and calls cpuAskForCard()
	* if cpyAskForCard() returns false, call goFish()
	*/
	void cpuTurn() {
		// cpu asks for a random card in its hand
		Random rand = new Random();
		try {
			System.out.println("size of cpuhand: " + cpuHand.size());
			Card request = cpuHand.get(rand.nextInt(cpuHand.size()));
			Alert alert = new Alert(AlertType.INFORMATION, "Got any " + request.rank + "s?", ButtonType.OK);
			alert.showAndWait();
			
			if(cpuAskForCard(request) == false) {
				if(checkIfDeckEmpty() == false)
					cpuHand.add(deckOfCards.remove(0));
			}
		} catch(IllegalArgumentException e) {
			System.out.println("error caught in cpu turn");
		}
		
		if(isBookCompleted(cpuHand)) {
			removeBookFromHand(cpuHand, cpuBooks);
			
			// check if adding book has ended the game
			if(userBooks.size() + cpuBooks.size() == 13) {
				// TODO: write function to end the game and determine a winner
				System.out.println("game has ended");
			}
			
			// check if removing the book has emptied user's hand
			if(checkIfHandEmpty(cpuHand)) {
				if(checkIfDeckEmpty() == true)
					cpuHand.add(deckOfCards.remove(0));
			}
		}
		
		// check if removing cards has emptied user's hand
		if(checkIfHandEmpty(userHand)) {
			if(checkIfDeckEmpty() == true)
				userHand.add(deckOfCards.remove(0));
		}

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
			
		for(Card c : userHand) {
			if(c.rank == request.rank) {
				tempList.add(c);
				successful = true;
			}
		}
				
		// remove/add from tempList
		cpuHand.addAll(tempList);
		userHand.removeAll(tempList);
		return successful;
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
	 * @param hand, the hand that is being checked for Cards of the same rank.
	 * @return true if a book is completed and false if not
	 */
	boolean isBookCompleted(ArrayList<Card> hand) {
		int count;
	
		// for each card in the hand, see how many other cards have the same rank
		for(Card c1 : hand) {
			count = 1;
			for(Card c2 : hand) {
				if(c1.rank == c2.rank) {
					count++;
				}
			}
			if(count == 4) { // if a book has been made
				return true;
			}
		}
		return false;
		
	}
	
	/**
	 * Checks if there are no cards left in a given hand.
	 * @param hand, the ArrayList of Cards being checked for elements
	 * @return true if empty and false if not
	 */
    public boolean checkIfHandEmpty(ArrayList<Card> hand) {
    	if(hand.size() == 0)	
    		return true;
    	else
    		return false;
    }
    
    /**
     * Checks if there are no cards left in the deck.
     * @return true if empty and false if not
     */
    public boolean checkIfDeckEmpty() {
    	if(deckOfCards.size() == 0)
    		return true;
    	else
    		return false;
    }
    
    public void removeBookFromHand(ArrayList<Card> hand, ArrayList<Card> books) {
    	int count = 0;
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
				books.add(c1); // c1 will be referenced for the book graphic
			}
		}
		hand.removeAll(tempList);
		hand.trimToSize();
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
