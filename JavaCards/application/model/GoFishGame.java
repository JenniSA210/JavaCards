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
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

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
	
	/**
	 * Constructor for GoFishGame
	 */
	public GoFishGame() {
		deckOfCards = createShuffledDeckOfCards(); // randomize a deck to deal from
		
		// deal a hand to each player and set number of completed books to 0
		dealHand(deckOfCards, userHand);
		userBooks = 0;
		dealHand(deckOfCards, cpuHand);
		cpuBooks = 0;
		
		while(userBooks + cpuBooks < 13) { // loop turns until all 13 books are completed
			//userTurn();
			isBookCompleted(userHand);
			//cpuTurn();
			isBookCompleted(cpuHand);
		}
		
		// compare number of books to determine the winner
		if(userBooks > cpuBooks) {
			// TODO: if number of books exceeds previous high score, set as new high score
			// give victory message
		}
		else if(cpuBooks > userBooks) {
			// give loss message
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
		int index = 0;
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
			index ++;
		}
		return deckOfCards;
	}
	
	/**
	* takes user card request and calls userAskForCard()
	* if userAskForCard() returns false, call goFish()
	*/
	/*
	void userTurn() {
		// player chooses card from their deck to ask for
		Card request; 
		// TODO: set request to card clicked on
		
		if(userAskForCard(request) == false) { // if unsuccessful
			goFish(userHand);
		}
	}
	*/
	
	/**
	* takes a random card from cpu deck as request and calls cpuAskForCard()
	* if cpyAskForCard() returns false, call goFish()
	*/
	void cpuTurn() {
		// cpu asks for a random card in its hand
		Random rand = new Random();
		Card request = cpuHand.get(rand.nextInt(cpuHand.size()));
		if(cpuAskForCard(request) == false) {
			goFish(cpuHand);
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
		for(Card c : cpuHand) {
			if(c.rank == request.rank) {
				userHand.add(cpuHand.remove(cpuHand.indexOf(c)));
				successful = true;
			}
		}
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
		for(Card c : userHand) {
			if(c.rank == request.rank) {
				cpuHand.add(userHand.remove(userHand.indexOf(c)));
				successful = true;
			}
		}
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
	void isBookCompleted(ArrayList<Card> hand) {
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
				// remove all cards with c1.rank
				for(Card c2 : hand) {
					if(c1.rank == c2.rank) {
						hand.remove(hand.indexOf(c2));
					}
				}
				hand.remove(hand.indexOf(c1));
				// TODO: add graphic for a book of c1.rank cards
			}
		}
	}
	


}