/**
 * 
 */
package application.controller;

import java.util.ArrayList;

import application.controller.JavaCardsController.ScreenModes;
import application.model.GoFishGame;
import application.model.BlackJackGame;
import application.model.CardImage;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import application.model.Card.CardRank;

/**
 * Controler for Go Fish Game
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class GoFishController {
	private String userName; // Holds user name from main screen (JavaCards)
	private ScreenModes screenMode = ScreenModes.DEFAULT; // Holds screen mode from main screen (JavaCards)
	private GoFishGame goFishGame; // Data model for Go Fish Game
	ArrayList<ImageView> newUserImageViewList; // Holds dynamically created card ImageViews for user hand
	ArrayList<ImageView> newDealerImageViewList; // Holds dynamically created card ImageViews for cpu hand
	ArrayList<ImageView> newUserBookImageViewList; // Holds dynamically created card ImageViews for user hand
	ArrayList<ImageView> newDealerBookImageViewList; // Holds dynamically created card ImageViews for cpu hand
	
    @FXML
    private AnchorPane goFishAnchorPane; // Main Pane
    
    @FXML
    private Button goFishNewGameBtn; // Button to clear screen and start new game
    
    @FXML
    ArrayList<ImageView> userBooks; // dynamically created card ImageViews for user's completed books
    
    @FXML
    ArrayList<ImageView> cpuBooks; // dynamically created card ImageViews for cpu's completed books
    
    @FXML
    private Label userScoreLabel;
    
    @FXML
    private Label highScoreLabel;
    
    // User's starting card
    @FXML
    private ImageView userLeftCard;

    // CPU's starting card
    @FXML
    private ImageView dealerLeftCard;
    
    // User's starting book - stacks are offset to the right
    @FXML
    private ImageView userLeftBook;
    
    // CPU's starting book - stacks are offset to the left
    @FXML
    private ImageView dealerRightBook;

    
    @FXML
    /**
     * Calls startNewGame() to reinitialize game and screen and re-enables all buttons
     * @param event Button Click
     */
    void newGameBtnClicked(ActionEvent event) {
    	startNewGame();
    }

    /**
     * Initializes data when FXML is first loaded, and called when New Game button is clicked
     * @param userName User name entered in Main Screen
     * @param screenMode Screen mode to use
     */
    public void initData(String userName, ScreenModes screenMode) {
    	this.userName = userName;
    	this.screenMode = screenMode;

    	try {
    		if (screenMode == ScreenModes.DEFAULT) {
    			goFishAnchorPane.getStylesheets().add(getClass().getResource("..//view/application.css").toExternalForm());
    			goFishAnchorPane.getStylesheets().remove(getClass().getResource("..//view/darkmode.css").toExternalForm());
           		// Flaw fixed after Java 1.8 - won't change background using CSS - requires below code to change background
        		goFishAnchorPane.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    		} else if (screenMode == ScreenModes.DARKMODE) {
    			goFishAnchorPane.getStylesheets().remove(getClass().getResource("..//view/application.css").toExternalForm());
    			goFishAnchorPane.getStylesheets().add(getClass().getResource("..//view/darkmode.css").toExternalForm());
    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}

    	
    	// If first load, then initializes game and gives each player 7 cards
    	if (goFishGame == null) goFishGame = new GoFishGame(userName);
    	
    	// Sets high score label
    	highScoreLabel.setText("High Score: " + goFishGame.getHighScore());

    	// Initialize empty ArrayLists to hold user and cpu hands
    	newUserImageViewList = new ArrayList<ImageView>();
    	newDealerImageViewList = new ArrayList<ImageView>();
    	
    	// Initialize empty ArrayLists to hold user and cpu hands
    	newUserBookImageViewList = new ArrayList<ImageView>();
    	newDealerBookImageViewList = new ArrayList<ImageView>();
    	
    	// set visuals for player hand
    	// Get card images from GoFishGame (Model) for User Hand and display on screen
    	// Start with left card and add hand offset to that card
    	ArrayList<Image> userCardHandImages = goFishGame.getUserHand();
    	userLeftCard.setImage(userCardHandImages.get(0));
    	// Set listener to match other cards, description below
    	userLeftCard.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				// Send index from User Card Images
				goFishGame.userCardSelect(0);
				System.out.println("User Card Clicked");
			}
			
		});
    	for (int i = 1; i < userCardHandImages.size(); i++) {
    		// Create new ImageView
    		ImageView newUserImageView = new ImageView();
    		// Set to same size as user left card
    		newUserImageView.setFitWidth(userLeftCard.getFitWidth());
    		newUserImageView.setFitHeight(userLeftCard.getFitHeight());
    		// Set the image to match the card
    		newUserImageView.setImage(userCardHandImages.get(i));
    		if (i == 1) { // First card in ArrayList starts position offsets off userLeftCard
        		newUserImageView.setLayoutX(userLeftCard.getLayoutX() + 45);
        		newUserImageView.setLayoutY(userLeftCard.getLayoutY());
    		} else { // All other cards are offset in X from the card before them
    			newUserImageView.setLayoutX(newUserImageViewList.get(i-2).getLayoutX() + 45);
    			newUserImageView.setLayoutY(userLeftCard.getLayoutY());
    		}
    		// Set handler for mouse clicks
    		// On mouse click, have GoFishGame check CPU's cards for matching rank,
    		// and if they do, remove that card from their deck and add to user deck
    		// If not, then pull another card from the deck
    		newUserImageView.setOnMouseClicked(new EventHandler<MouseEvent>() {
				@Override
				public void handle(MouseEvent event) {
					// Send index from User Card Images
					ImageView iv = (ImageView) event.getTarget();
					for (int k = 0; k < userCardHandImages.size(); k++) {
						if (iv.getImage() == userCardHandImages.get(k)) {
							goFishGame.userCardSelect(k);
							updateView(); // Cards shifted, so refresh view
							System.out.println("Clicked image found");
						}
					}
					System.out.println("User Card Clicked");
				}
    			
    		});
    		newUserImageViewList.add(newUserImageView);
    	}
    	// After all user cards are added, add them to the screen
    	for (int i = 0; i < newUserImageViewList.size(); i++) {
    		goFishAnchorPane.getChildren().add(newUserImageViewList.get(i));
    	}
    	
    	// set visuals for player books
    	// Get card images from GoFishGame (Model) for User Books and display on screen
    	// Start with left card and add book offset to that card
    	if(goFishGame.getUserBooks() != null) { // if books exist
	    	ArrayList<Image> userBookImages = goFishGame.getUserBooks();
	    	userLeftBook.setImage(userBookImages.get(0));
	
	    	for (int i = 1; i < userBookImages.size(); i++) {
	    		// Create new ImageView
	    		ImageView newUserImageView = new ImageView();
	    		// Set to same size as user left card
	    		newUserImageView.setFitWidth(userLeftBook.getFitWidth());
	    		newUserImageView.setFitHeight(userLeftBook.getFitHeight());
	    		// Set the image to match the card
	    		newUserImageView.setImage(userBookImages.get(i));
	    		if (i == 1) { // First card in ArrayList starts position offsets off userLeftCard
	        		newUserImageView.setLayoutX(userLeftBook.getLayoutX() + 20);
	        		newUserImageView.setLayoutY(userLeftBook.getLayoutY());
	    		} else { // All other cards are offset in X from the card before them
	    			newUserImageView.setLayoutX(newUserBookImageViewList.get(i-2).getLayoutX() + 45);
	    			newUserImageView.setLayoutY(userLeftBook.getLayoutY());
	    		}
	    		newUserBookImageViewList.add(newUserImageView);
	    	}
	    	// After all user cards are added, add them to the screen
	    	for (int i = 0; i < newUserBookImageViewList.size(); i++) {
	    		goFishAnchorPane.getChildren().add(newUserBookImageViewList.get(i));
	    	}
    	}
    	
    	// Set all cpu card visuals to back of card (grey with black border)
    	// Get card images from GoFishGame (Model) for CPU hand, set to back image, and display on screen
    	// Start with right card and add book offset to that card
    	ArrayList<Image> dealerCardHandImages = goFishGame.getDealerHand(false);
    	dealerLeftCard.setImage(dealerCardHandImages.get(0));
    	for (int i = 1; i < dealerCardHandImages.size(); i++) {
    		// Create new ImageView
    		ImageView newDealerImageView = new ImageView();
    		// Set to same size as dealer left card
    		newDealerImageView.setFitWidth(dealerLeftCard.getFitWidth());
    		newDealerImageView.setFitHeight(dealerLeftCard.getFitHeight());
    		// Set the image to match the card
    		newDealerImageView.setImage(dealerCardHandImages.get(i));
    		if (i == 1) { // First card in ArrayList starts position offsets off dealerLeftCard
    			newDealerImageView.setLayoutX(dealerLeftCard.getLayoutX() + 45);
    			newDealerImageView.setLayoutY(dealerLeftCard.getLayoutY());
    		} else { // All other cards are offset in X from the card before them
    			newDealerImageView.setLayoutX(newDealerImageViewList.get(i-2).getLayoutX() + 45);
    			newDealerImageView.setLayoutY(dealerLeftCard.getLayoutY());
    		}
    		newDealerImageViewList.add(newDealerImageView);
    	}
    	// After all dealer cards are added, add them to the screen
    	for (int i = 0; i < newDealerImageViewList.size(); i++) {
    		goFishAnchorPane.getChildren().add(newDealerImageViewList.get(i));
    	}
    	
    	// set visuals for dealer books
    	// Get card images from GoFishGame (Model) for User Books and display on screen
    	// Start with left card and add book offset to that card
    	if(goFishGame.getCpuBooks() != null) { // if books exist
	    	ArrayList<Image> dealerBookImages = goFishGame.getCpuBooks();
	    	userLeftBook.setImage(dealerBookImages.get(0));
	
	    	for (int i = 1; i < dealerBookImages.size(); i++) {
	    		// Create new ImageView
	    		ImageView newDealerImageView = new ImageView();
	    		// Set to same size as user left card
	    		newDealerImageView.setFitWidth(dealerRightBook.getFitWidth());
	    		newDealerImageView.setFitHeight(dealerRightBook.getFitHeight());
	    		// Set the image to match the card
	    		newDealerImageView.setImage(dealerBookImages.get(i));
	    		if (i == 1) { // First card in ArrayList starts position offsets off userLeftCard
	        		newDealerImageView.setLayoutX(dealerRightBook.getLayoutX() - 20);
	        		newDealerImageView.setLayoutY(dealerRightBook.getLayoutY());
	    		} else { // All other cards are offset in X from the card before them
	    			newDealerImageView.setLayoutX(newDealerBookImageViewList.get(i-2).getLayoutX() + 45);
	    			newDealerImageView.setLayoutY(dealerRightBook.getLayoutY());
	    		}
	    		newDealerBookImageViewList.add(newDealerImageView);
	    	}
	    	// After all user cards are added, add them to the screen
	    	for (int i = 0; i < newDealerBookImageViewList.size(); i++) {
	    		goFishAnchorPane.getChildren().add(newDealerBookImageViewList.get(i));
	    	}
    	}

	}
    
    /**
     * Removes all additional ImageViews from screen, clears ArrayLists, and re-initialize for new game
     */
    public void startNewGame() {
    	// Remove New User Card ImageViews from Anchor Pane and clear ArrayList
    	for (int i = 0; i < newUserImageViewList.size(); i++)
        	goFishAnchorPane.getChildren().remove(newUserImageViewList.get(i));
   		newUserImageViewList.clear();
    	newUserImageViewList = null;

    	// Remove New Cpu Card ImageViews from Anchor Pane and clear ArrayList
    	for (int i = 0; i < newDealerImageViewList.size(); i++)
        	goFishAnchorPane.getChildren().remove(newDealerImageViewList.get(i));
    	newDealerImageViewList.clear();
    	newDealerImageViewList = null;
    	
    	// Restart game from scratch
    	goFishGame = null;
    	this.initData(userName, screenMode);
    }

    
    public void updateView() {
    	initData(userName, screenMode);
    }
}
