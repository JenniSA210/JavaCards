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
 * Card Images are kept in a separate class so that the image is loaded from disk only once
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */


public class CardImage extends WritableImage {
	static Image DeckImage;
	int cardPixelWidth = 239, cardPixelHeight = 333;
	WritableImage frontImage;
	WritableImage backImage;
	
	public CardImage(Card.CardSuit suit, Card.CardRank rank) {
		super(239, 333);

		
		// Create image out of part of Deck Image
		if (DeckImage == null) {
			// TODO Load from file
			try {
				DeckImage = new Image("application/model/STANDARD--COLOR_52_FACES.png");
			} catch (IllegalArgumentException e) {
				System.out.println(e);
				return;
			}
		} // Image already loaded from file, so no need to load again
		createFrontImage(suit, rank);
		createBackImage();
		setToFrontImage();
	}
	
	public void createFrontImage(Card.CardSuit suit, Card.CardRank rank) {
		// Get correct card image pixel coordinates based on suit and rank
		// DeckImage cards are 239x333 pixels
		// 50 pixel gap between images in a row, and 25 pixels between rows
		// X will be rank x (239+49), Y suit x (333+24)
		int cardLeftX = rank.ordinal()*288, cardTopY = suit.ordinal()*358;
		//int cardLeftX = 1*289, cardTopY = 3*358; // Images not showing unless these coords are 0,0
		int cardRightX = cardLeftX + cardPixelWidth - 1;
		int cardBottomY = cardTopY + cardPixelHeight - 1;
		System.out.println(rank + " of " + suit + " " + cardLeftX + " " + cardTopY + " " + cardRightX + " " + cardBottomY);
		frontImage = new WritableImage(cardPixelWidth,cardPixelHeight);
		
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
	
	public void setToFrontImage() {
		PixelWriter pw = this.getPixelWriter();
		PixelReader pr = frontImage.getPixelReader();
		for (int y = 0; y < this.getHeight(); y++) {
			for (int x = 0; x < this.getWidth(); x++) {
				pw.setColor(x, y, pr.getColor(x, y));
			}
		}
	}
	
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
