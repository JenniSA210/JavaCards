/**
 * 
 */
package application.model;

import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

import application.model.Card.CardRank;
import application.model.Card.CardSuit;

/**
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class BlackJackGame {
	Card[] userDeckOfCards;
	Card[] dealerDeckOfCards;
	int lastUserCardUsed; // Last card used/played
	int lastDealerCardUsed; // Last card used/played
	public enum GameStatus { CONTINUE, USERWON, DEALERWON, PUSH }
	
	public BlackJackGame() {
		userDeckOfCards = createShuffledDeckOfCards(); // Create and fill userDeckOfCards
		dealerDeckOfCards = createShuffledDeckOfCards(); // Create and fill dealerDeckOfCards
		lastUserCardUsed = -1; // getNextUserCard is called and increments to 0
		lastDealerCardUsed = -1; // getNextDealerCard is called and increments to 0
		// Load high scores from scores.txt
		
	}
	
	private Card[] createShuffledDeckOfCards() {
		// Create deck from cards based on random numbers from 0-51 (52 cards in deck)
		Card[] deckOfCards = new Card[52];
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
			System.out.println(i);
			if (i < 13) { // 0-12 Clubs
				deckOfCards[index] = new Card(CardSuit.CLUBS, CardRank.values()[i]);
			} else if (i < 26) { // 13-25 Hearts
				deckOfCards[index] = new Card(CardSuit.HEARTS, CardRank.values()[i-13]);
			} else if (i < 39) { // 26-38 Spades
				deckOfCards[index] = new Card(CardSuit.SPADES, CardRank.values()[i-26]);
			} else {
				// 39-51 Diamonds
				deckOfCards[index] = new Card(CardSuit.DIAMONDS, CardRank.values()[i-39]);
			}
			index ++;
		}
		return deckOfCards;
	}
	
	public CardImage getNewUserCard() {
		lastUserCardUsed++;
		return userDeckOfCards[lastUserCardUsed].getImage(); // Moves to next card in shuffled deck
	}

	public CardImage getNewDealerCard() {
		lastDealerCardUsed++;
		return dealerDeckOfCards[lastDealerCardUsed].getImage(); // Moves to next card in shuffled deck
	}

	public GameStatus callGame() {
    	// Compare current user and dealer scores
		// Return status
		
    	// Update score and high score on disk if needed
		int userCardTotal = 0, dealerCardTotal = 0;

		for (int i = 0; i <= lastUserCardUsed; i++) {
			if (userDeckOfCards[i].rank.equals(CardRank.ACE)) {
				if (userCardTotal + 11 > 21) userCardTotal++;
				else userCardTotal += 11;
			} else {
				userCardTotal += userDeckOfCards[i].rank.ordinal();
			}
		}

		for (int i = 0; i <= lastDealerCardUsed; i++) {
			if (dealerDeckOfCards[i].rank.equals(CardRank.ACE)) {
				if (dealerCardTotal + 11 > 21) dealerCardTotal++;
				else dealerCardTotal += 11;
			} else {
				dealerCardTotal += dealerDeckOfCards[i].rank.ordinal();
			}
		}

		// Now compare totals
		if (userCardTotal > 21) return GameStatus.DEALERWON;
		else if (dealerCardTotal > 21) return GameStatus.USERWON;
		else if (dealerCardTotal < userCardTotal) return GameStatus.USERWON;
		else if (userCardTotal < dealerCardTotal) return GameStatus.DEALERWON;
		else if (userCardTotal == dealerCardTotal) return GameStatus.PUSH;

		return GameStatus.CONTINUE;
	}
	
	/**
	 * Called after every new card
	 * Gets total for user cards and dealer cards, and checks to see if won
	 * @return CONTINUE if not won, who won, or PUSH if totals match
	 */
	public GameStatus checkIfGameWonOrLost() {
		// Add all user cards and dealer cards used so far
		// If either totals are over 21, then return user or dealer won
		// If totals match, then return PUSH
		// Add special cases for Aces, since they can be value of 1 or 11
		int userCardTotal = 0, dealerCardTotal = 0;

		for (int i = 0; i <= lastUserCardUsed; i++) {
			if (userDeckOfCards[i].rank.equals(CardRank.ACE)) {
				if (userCardTotal + 11 > 21) userCardTotal++;
				else userCardTotal += 11;
			} else {
				userCardTotal += userDeckOfCards[i].rank.ordinal();
			}
		}

		for (int i = 0; i <= lastDealerCardUsed; i++) {
			if (dealerDeckOfCards[i].rank.equals(CardRank.ACE)) {
				if (dealerCardTotal + 11 > 21) dealerCardTotal++;
				else dealerCardTotal += 11;
			} else {
				dealerCardTotal += dealerDeckOfCards[i].rank.ordinal();
			}
		}

		// Now compare totals
		if (dealerCardTotal > 21) return GameStatus.USERWON;
		else if (userCardTotal > 21) return GameStatus.DEALERWON;
		else if (userCardTotal > 18 && userCardTotal == dealerCardTotal) return GameStatus.PUSH;
		
		return GameStatus.CONTINUE;
	}
	
	public int getScore() {
		// To be called when game is finished
		// Calculate score
		return 0;
	}
}
