/**
 * 
 */
package application.model;

/**
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
	
	public Card(CardSuit suit, CardRank rank) {
		this.suit = suit;
		this.rank = rank;
		
		image = new CardImage(suit, rank);
	}
	
	public CardImage getImage() {
		return image;
	}
	
	@Override
	public String toString() {
		return rank + " of " + suit;
	}
}
