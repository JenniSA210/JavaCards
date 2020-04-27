/**
 * 
 */
package application.model;

import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

/**
 * Card Images are kept in a separate class so that the image is loaded from disk only once ideally
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class CardImage extends WritableImage {
	static Image DeckImage; // Contains image of all cards from disk
	int cardPixelWidth = 239, cardPixelHeight = 333; // Measured pixel dimensions of individual cards
	WritableImage frontImage; // Holds front of card image
	WritableImage backImage; // Holds back of card image
	
	public CardImage(Card.CardSuit suit, Card.CardRank rank) {
		super(239, 333); // Creates new WritableImage with card dimensions

		// Create image out of part of Deck Image
		if (DeckImage == null) { // DeckImage is static, so only call when empty to minimize disk reads
			// Load from file
			try {
				DeckImage = new Image("application/model/STANDARD--COLOR_52_FACES.png");
			} catch (IllegalArgumentException e) {
				System.out.println(e);
				return;
			}
		} // Image already loaded from file, so no need to load again
		createFrontImage(suit, rank);
		createBackImage();
		setToFrontImage(); // Front of card is displayed by default
	}
	
	/**
	 * Creates an image of the front of the card and stores in frontImage
	 * @param suit Suit of Card
	 * @param rank Rank of Card
	 */
	public void createFrontImage(Card.CardSuit suit, Card.CardRank rank) {
		// Get correct card image pixel coordinates based on suit and rank
		// DeckImage cards are 239x333 pixels
		// 50 pixel gap between images in a row, and 25 pixels between rows
		// X will be rank x (239+49), Y suit x (333+24)
		int cardLeftX = rank.ordinal()*288, cardTopY = suit.ordinal()*358;
		int cardRightX = cardLeftX + cardPixelWidth - 1;
		int cardBottomY = cardTopY + cardPixelHeight - 1;
		//System.out.println(rank + " of " + suit + " " + cardLeftX + " " + cardTopY + " " + cardRightX + " " + cardBottomY);

		// frontImage initialized to card size
		frontImage = new WritableImage(cardPixelWidth,cardPixelHeight);
		
		// Read pixels from DeckImage card and write to frontImage
	    PixelReader pr =  DeckImage.getPixelReader();
	    PixelWriter pw;
	    pw = frontImage.getPixelWriter();
	    for (int deckY = cardTopY; deckY < cardBottomY; deckY++) {
	    	for (int deckX = cardLeftX; deckX < cardRightX; deckX++) {
	    		Color color = pr.getColor( deckX, deckY ); //get pixel at X  & Y position
                pw.setColor(deckX - cardLeftX, deckY - cardTopY, color); //set pixel to writableimage through PixelWriter
	    	}	
	    }
	    pr = null;
	    pw = null;
	}
		
	/**
	 * Creates an image of the back of the card and stores in backImage
	 * Card back will be black outline with inside filled gray, same dimensions as card
	 */
	public void createBackImage() {
		backImage = new WritableImage(cardPixelWidth,cardPixelHeight);
		PixelWriter pw = backImage.getPixelWriter();
		for (int y = 0; y < backImage.getHeight(); y++) {
			for (int x = 0; x < backImage.getWidth(); x++) {
				if (y < 3 || y > backImage.getHeight() - 4 || x < 3 || x > backImage.getWidth() - 4) {
					pw.setColor(x, y, Color.BLACK);
				} else {
					pw.setColor(x, y, Color.GRAY);
				}
			}
		}
	}
	
	/**
	 * Sets image (this) to be displayed to Front of Card
	 */
	public void setToFrontImage() {
		PixelWriter pw = this.getPixelWriter();
		PixelReader pr = frontImage.getPixelReader();
		for (int y = 0; y < this.getHeight(); y++) {
			for (int x = 0; x < this.getWidth(); x++) {
				pw.setColor(x, y, pr.getColor(x, y));
			}
		}
	}

	/**
	 * Sets image (this) to be displayed to Back of Card
	 */	
	public void setToBackImage() {
		PixelWriter pw = this.getPixelWriter();
		PixelReader pr = backImage.getPixelReader();
		for (int y = 0; y < this.getHeight(); y++) {
			for (int x = 0; x < this.getWidth(); x++) {
				pw.setColor(x, y, pr.getColor(x, y));
			}
		}
	}
	
}
