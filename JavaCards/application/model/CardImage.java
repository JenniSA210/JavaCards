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
	
	public CardImage(Card.CardSuit suit, Card.CardRank rank) {
		super(239, 333);
		int cardPixelWidth = 239, cardPixelHeight = 333;

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
		
		// TODO Get correct card image pixel coordinates based on suit and rank
		// DeckImage cards are 239x333 pixels
		// 50 pixel gap between images in a row, and 25 pixels between rows
		// X will be rank x (239+49), Y suit x (333+24)
		int cardLeftX = rank.ordinal()*288, cardTopY = suit.ordinal()*358;
		//int cardLeftX = 1*289, cardTopY = 3*358; // Images not showing unless these coords are 0,0
		int cardRightX = cardLeftX + cardPixelWidth - 1;
		int cardBottomY = cardTopY + cardPixelHeight - 1;
		System.out.println(rank + " of " + suit + " " + cardLeftX + " " + cardTopY + " " + cardRightX + " " + cardBottomY);
		
	    PixelReader pr =  DeckImage.getPixelReader();
	    PixelWriter pw;
	    pw = this.getPixelWriter();
	    for (int deckY = cardTopY; deckY < cardBottomY; deckY++) {
	    	for (int deckX = cardLeftX; deckX < cardRightX; deckX++) {
	    		Color color = pr.getColor( deckX, deckY ); //get pixel at X  & Y position
                pw.setColor(deckX - cardLeftX, deckY - cardTopY, color); //set pixel to writableimage through PixelWriter
	    	}	
	    }
	    pr = null;
	    pw = null;
	}
	
	// Delete this after above code works
	/*
	suppose i have an image strip of 10 frames of 32x32 ( 320x32 ), then i want to separate all frames in individual images
	getImage( 10, 32,32,"myStrip.png" );
	*/
	public Image[] getImage(int frames, int w, int h,  String pathFile) {
		Image[] imgs =  new Image[frames];

	    //img that contains all frames
	    Image stripImg = new Image(pathFile);
	    PixelReader pr =  stripImg.getPixelReader();
	    PixelWriter pw = null;
        for( int i = 0; i < frames ; i++){
        	WritableImage wImg = new WritableImage( w, h );
            pw = wImg.getPixelWriter();
            for( int readY = 0 ; readY < h; readY++ ){
            	int ww = (w * i);
	            for( int readX = ww; readX < ww + w; readX++ ){
	            	Color color = pr.getColor( readX, readY ); //get pixel at X  & Y position
	                 pw.setColor(readX - ww, readY, color); //set pixel to writableimage through pixel writer
	            }//X
            }//Y
	        imgs[ i ] = wImg; //finally new image is stored
        }
	    stripImg = null;
	    pr = null;
	    pw = null;
	    return imgs;
	}
	
	
}
