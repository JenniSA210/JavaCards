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
import javafx.scene.image.Image;

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
	
	/**
	 * Gets Images of all cards currently in User Hand
	 * @return ArrayList of Card Images
	 */
	public ArrayList<Card> getUserHand(){
		//ArrayList<Image> userHandImages = new ArrayList<Image>();
		for (int i = 0; i < userHand.size(); i++) {
			userHand.get(i).image.setToFrontImage();
			//userHandImages.add(userHand.get(i).image);
		}
		return userHand;
	}
	
	/**
	 * Gets Images of top card currently in User Books (Already found 4 of rank and placed into userBooks)
	 * @return ArrayList of Card Images
	 */
	public ArrayList<Image> getUserBooks(){
		userBooks.trimToSize();
		if(userBooks.size() == 0)
			return null;
		ArrayList<Image> userBookImages = new ArrayList<Image>();
		for(int i = 0; i < userBooks.size(); i++) {
			userBooks.get(i).image.setToFrontImage();
			userBookImages.add(userBooks.get(i).image);
		}
		return userBookImages;
	}
	
	/**
	 * Gets Images of all cards currently in CPU Hand
	 * @return ArrayList of Card Images
	 */
	public ArrayList<Image> getCPUHand(boolean frontVisible){
		ArrayList<Image> cpuHandImages = new ArrayList<Image>();
		for (int i = 0; i < cpuHand.size(); i++) {
			if (frontVisible) cpuHand.get(i).image.setToFrontImage();
			else cpuHand.get(i).image.setToBackImage();
			cpuHandImages.add(cpuHand.get(i).image);
			
		}
		return cpuHandImages;
	}
	
	/**
	 * Gets Images of top card currently in CPU Books (Already found 4 of rank and placed into userBooks)
	 * @return ArrayList of Card Images
	 */
	public ArrayList<Image> getCpuBooks(){
		if(cpuBooks.size() == 0)
			return null;
		ArrayList<Image> cpuBookImages = new ArrayList<Image>();
		for(int i = 0; i < cpuBooks.size(); i++) {
			cpuBooks.get(i).image.setToFrontImage();
			cpuBookImages.add(cpuBooks.get(i).image);
		}
		return cpuBookImages;
	}
	
	/**
	 * Check if cpu has card in hand
	 * @return true if cpu has card in hand
	 */
	public boolean cpuHasCard(CardRank rank) {
		for (Card c: cpuHand) {
			if (c.rank == rank)
				return true;
		}
		return false;
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
			cpuHand.removeAll(tempList); // Remove found cards from cpuHand
			for(Card card : tempList) {  // Set the images of the transferred cards to the front
				card.image.setToFrontImage();
			}
			userHand.addAll(tempList); // Add to userHand
			
			cpuHand.trimToSize(); // Set CpuHand so size matches number of elements
		}
		
		if (!found) {
			// Pull another card from deck if deck is not empty
			if(!isDeckEmpty())
				userHand.add(deckOfCards.remove(0));
		}
		

		// Repeat below steps to make sure that card added after removing book doesn't create a new book
		do {
			// Check if userHand has a completed book
			if (isBookCompleted(userHand)) {
				removeBookFromHand(userHand, userBooks);
			
				// Check if adding book has ended the game
				checkIfGameEnded();
			
				// check if removing the book has emptied user's hand
				// If hand empty then add a card to it
				if(checkIfHandEmpty(userHand)) {
					if(!isDeckEmpty())
						userHand.add(deckOfCards.remove(0)); // Remove card from deck and add to userHand
				}
			}
		
			// Check if removing cards has emptied cpu's hand
			if(checkIfHandEmpty(cpuHand)) {
				if(!isDeckEmpty())
					cpuHand.add(deckOfCards.remove(0));
			}
		} while (isBookCompleted(userHand));
		// Now it is CPU's turn
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
	* Takes a random card from cpuHand as request and calls cpuAskForCard()
	* If cpuAskForCard() returns false, call goFish()
	*/
	void cpuTurn() {
		// CPU asks for a random card in its hand
		// And if user has card, adds to cpuHand
		// If not, gets new card from deck
		Random rand = new Random();
		try {
			System.out.println("size of cpuhand: " + cpuHand.size());
			Card request = cpuHand.get(rand.nextInt(cpuHand.size()));
			System.out.println("Cpu has asked for " + request.rank);
		
			if(cpuAskForCard(request) == false) { // False if user does not have card
				if(!isDeckEmpty())
					cpuHand.add(deckOfCards.remove(0)); // Add card to cpuHand from deck
			}
		} catch(IllegalArgumentException e) {
			System.out.println("Error caught in CPU turn");
		}

		// Repeat below steps to make sure that card added after removing book doesn't create a new book
		do {
			// Check if book completed 
			if(isBookCompleted(cpuHand)) {
				removeBookFromHand(cpuHand, cpuBooks);
			
				// Check if adding book has ended the game (all card into 13 books)
				checkIfGameEnded();
			
				// Check if removing the book has emptied user's hand
				if(checkIfHandEmpty(cpuHand)) {
					if(!isDeckEmpty())
						cpuHand.add(deckOfCards.remove(0));
				}
			}
		} while (isBookCompleted(cpuHand));
		
		// Check if removing cards has emptied user's hand
		if(checkIfHandEmpty(userHand)) {
			if(!isDeckEmpty())
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
		// create a temp storage for cards that will be removed/added TODO
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
	 * Checks if there are no cards left in a given hand.
	 * @param hand, the ArrayList of Cards being checked for elements
	 * @return true if empty and false if not
	 */
    public boolean checkIfHandEmpty(ArrayList<Card> hand) {
    	hand.trimToSize();
    	if(hand.size() == 0)	
    		return true;
    	else
    		return false;
    }
    
    /**
     * Checks if there are no cards left in the deck.
     * @return true if empty and false if not
     */
    public boolean isDeckEmpty() {
    	deckOfCards.trimToSize();
    	if(deckOfCards.size() == 0)
    		return true;
    	else
    		return false;
    }
    
	/**
	 * Checks to see if four cards of the same rank exist in a given deck.
	 * @param hand, the hand that is being checked for Cards of the same rank.
	 * @return true if a book is completed and false if not
	 */
	boolean isBookCompleted(ArrayList<Card> hand) {
		int count;
	
		// For each card in the hand, see how many other cards have the same rank
		for(Card c1 : hand) { // Gets each card in deck
			count = 0; // Set to 0 because for c2 for loop will count c1 in count
			for(Card c2 : hand) {
				if(c1.rank == c2.rank) { // And compares to other cards in deck
					count++;			 // If matches add to count
				}
			}
			if(count == 4) { // If a book has been made
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Searches and removes 4 cards of matching rank, adds first card to books for display
	 * @param hand userHand or cpuHand to remove book from
	 * @param books userBooks or cpuBooks to add first book card to
	 */
    public void removeBookFromHand(ArrayList<Card> hand, ArrayList<Card> books) {
    	int count = 0;
		// Create a temp storage for cards that will be removed
		ArrayList<Card> tempList = new ArrayList<Card>();
		Card bookCard = null;
		
		// For each card in the hand, see how many other cards have the same rank
		for(Card c1 : hand) { // Sets to card in hand
			count = 0; // Count is 0 because next for loop will include c1 in count
			for(Card c2 : hand) {
				if(c1.rank == c2.rank) { // And compares to other cards in deck
					count++;
				}
			}
			if(count == 4) { // If a book has been made
				// Add all cards of the same rank to the templist
				tempList.add(c1);
				for(Card c2 : hand) {
					if(c1.rank == c2.rank) {
						tempList.add(c2);
					}
				}
				bookCard = c1;
				break;
			}
		}
		System.out.println("Book made: " + bookCard.rank);
		books.add(bookCard); // c1 will be referenced for the book graphic
		
		hand.removeAll(tempList);
		hand.trimToSize();
    }
    
	/**
	 * Called after every turn
	 * Gets total for user books and cpu books, and checks to see if won
	 * Exits with no return if game continues
	 * TODO: change to return a GAMESTATUS to the controller?
	 */
	public boolean checkIfGameEnded() {
		// Add together user and cpu books
		// If total is 13, find out who won

		int totalBooks = userBooks.size() + cpuBooks.size();

		// Now compare totals
		if (totalBooks == 13) {
			// since game is over, see who won
			if(userBooks.size() > cpuBooks.size()) {
				userScore += 20;
				score.addScore(GameList.GOFISH, userName, userScore);
				return true;
			}
		}
		System.out.println("User Book Total: " + userBooks.size());
		return false;
	}
	
	/**
	 * If game already ended, then check if user won (used for GUI)
	 * @return
	 */
	public boolean didUserWin() {
		if (userBooks.size() > cpuBooks.size())
			return true;
		else return false;
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
