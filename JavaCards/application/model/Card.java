/**
 * 
 */
package application.model;

/**
 * Class for single card containing rank, suit, and images (front and back)
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class Card {
	public enum CardSuit { CLUBS, HEARTS, SPADES, DIAMONDS }
	public enum CardRank { ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN , JACK, QUEEN, KING }
	
	CardSuit suit;
	CardRank rank;
	CardImage image;
	
	/**
	 * Constructor will assign suit and rank to card, then add image based on rank and suit
	 * @param suit
	 * @param rank
	 */
	public Card(CardSuit suit, CardRank rank) {
		this.suit = suit;
		this.rank = rank;
		
		image = new CardImage(suit, rank);
	}
	
	/**
	 * Returns the image of the card (front or back image set in CardImage)
	 * @return
	 */
	public CardImage getImage() {
		return image;
	}
	
	/**
	 * Gets rank of card
	 * @return rank of card
	 */
	public CardRank getRank() {
		return this.rank;
	}
	
	@Override
	/**
	 * To String, will show similar to "ACE of SPADES"
	 */
	public String toString() {
		return rank + " of " + suit;
	}
}
