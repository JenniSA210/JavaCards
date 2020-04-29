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

/**
 * Data Model for Go Fish Game
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class GoFishGame {
	ArrayList<Card> deckOfCards;
	ArrayList<Card> userHand;
	ArrayList<Card> cpuHand;
	int userBooks;
	int cpuBooks;
	
	/**
	 * Constructor for GoFishGame
	 */
	public GoFishGame() {
		deckOfCards = createShuffledDeckOfCards(); // randomize a deck to deal from
		// deal a hand to each player and set number of completed books to 0
		userHand = dealHand(deckOfCards);
		userBooks = 0;
		cpuHand = dealHand(deckOfCards);
		cpuBooks = 0;
		
		while(userBooks + cpuBooks < 13) { // loop turns until all 13 books are completed
			userTurn();
			isBookCompleted(userHand);
			cpuTurn();
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
	 * @return Card[52] full deck of randomized, non-repeated cards
	 */
	private ArrayList<Card> createShuffledDeckOfCards() {
		// Create deck from cards based on random numbers from 0-51 (52 cards in deck)
		ArrayList<Card> deckOfCards = new ArrayList<Card>;
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
	
	void userTurn() {
		// player chooses card from their deck to ask for
		Card request; // TODO: set by card clicked on
		if(userAskForCard(request) == false) { // if unsuccessful
			goFish(userHand);
		}
	}
	
	void cpuTurn() {
		// cpu asks for a random card in its hand
		Card request = cpuHand.get(new Random.nextInt(cpuHand.size));
		if(cpuAskForCard(request) == false) {
			goFish(cpuHand);
		}
	}
	
	// TODO method to "ask" for a card
	// select a card from user deck to ask for
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
	
	private void goFish(ArrayList<Card> hand) {
		if(deckOfCards.size() > 0) { // if the deck is not empty, take a card
			hand.add(deckOfCards.remove(0));
		}
		else {
			// TODO: give message that the deck is empty
		}
	}
	
	void dealHand(ArrayList<Card> newDeck) {
		// deal 7 cards from deck to the hand
		for(i = 0; i < 7; i++) {
			newDeck.add(deckOfCards.remove(0));
		}
	}
	
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
