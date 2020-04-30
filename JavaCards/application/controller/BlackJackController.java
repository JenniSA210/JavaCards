/**
 * 
 */
package application.controller;

import java.util.ArrayList;

import application.controller.JavaCardsController.ScreenModes;
import application.model.BlackJackGame;
import application.model.BlackJackGame.GameStatus;
import application.model.CardImage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

/**
 * Controller for Black Jack Game
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class BlackJackController {
	private String userName; // Holds user name from main screen (JavaCards)
	private ScreenModes screenMode = ScreenModes.DEFAULT; // Holds screen mode from main screen (JavaCards)
	private BlackJackGame blackJackGame; // Data model for Black Jack Game
	ArrayList<ImageView> newUserImageViewList; // Holds dynamically created card ImageViews for User Deck
	ArrayList<ImageView> newDealerImageViewList; // Holds dynamically created card ImageViews for Dealer Deck
	
    @FXML
    private AnchorPane blackJackAnchorPane; // Main Pane
    
    @FXML
    private Button blackJackNewGameBtn; // Button to clear screen and start new game
    
    @FXML
    private Button callGameBtn; // Button to end game on user click
    
    @FXML
    private Button newCardBtn; // Buttton to pull a new card from the deck
    
    @FXML
    private ImageView dealerHandBottomImg; // Bottom card image for dealer in FXML, used for size and location start
    
    @FXML
    private ImageView userHandBottomImg; // Bottom card image for user in FXML, used for size and location start
    
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
    			userHandBottomImg.getParent().getStylesheets().add(getClass().getResource("..//view/application.css").toExternalForm());
    			userHandBottomImg.getParent().getStylesheets().remove(getClass().getResource("..//view/darkmode.css").toExternalForm());
           		// Flaw fixed after Java 1.8 - won't change background using CSS - requires below code to change background
        		blackJackAnchorPane.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    		} else if (screenMode == ScreenModes.DARKMODE) {
    			userHandBottomImg.getParent().getStylesheets().remove(getClass().getResource("..//view/application.css").toExternalForm());
    			userHandBottomImg.getParent().getStylesheets().add(getClass().getResource("..//view/darkmode.css").toExternalForm());
    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	blackJackGame = new BlackJackGame();

    	// Sets first user card
    	userHandBottomImg.setImage(blackJackGame.getNewUserCard());

    	// Sets first dealer card displaying back of card
    	CardImage newDealerCard = blackJackGame.getNewDealerCard();
    	// Set bottom dealer card visual to back of card (grey with black border)
    	newDealerCard.setToBackImage();
    	dealerHandBottomImg.setImage(newDealerCard);

    	// Initialize ArrayLists to hold new cards
    	newUserImageViewList = new ArrayList<ImageView>();
    	newDealerImageViewList = new ArrayList<ImageView>();

    	// Get new dealer card and add on top and offset of bottom dealer card
    	addNewDealerCard();

    	// Activate New Card and Call Game Buttons
    	newCardBtn.setDisable(false);
    	callGameBtn.setDisable(false);
    }
    
    @FXML
    /**
     * Handler for button clicked when used wants another card
     * Gets new card from deck and adds to screen
     * Then checks card totals and if game won
     * @param event Button Click
     */
    void newCardBtnClicked(ActionEvent event) {
    	// Get new card and add to screen above and offset of bottom user card or last card used
    	// Create new ImageView using same dimensions as userHandBottomImg
    	ImageView imgNew = new ImageView();
    	imgNew.resize(userHandBottomImg.getFitWidth(), userHandBottomImg.getFitHeight());
    	// getNewUserCard for new ImageView
    	imgNew.setImage(blackJackGame.getNewUserCard());

    	// Add new ImageView to screen offset from last card added
    	// Should add in location so rank of card is visible
    	if (newUserImageViewList.isEmpty()) {
    		imgNew.setLayoutX(userHandBottomImg.getLayoutX() + 30);
    		imgNew.setLayoutY(userHandBottomImg.getLayoutY() - 30);
    		newUserImageViewList.add(imgNew);
    	} else {
    		imgNew.setLayoutX(newUserImageViewList.get(newUserImageViewList.size()-1).getLayoutX() + 30);
    		imgNew.setLayoutY(newUserImageViewList.get(newUserImageViewList.size()-1).getLayoutY() - 30); 
    		newUserImageViewList.add(imgNew);
    	}
    	// Adds to anchor pane
    	blackJackAnchorPane.getChildren().add(newUserImageViewList.get(newUserImageViewList.size()-1));
    	
    	// Check game status and respond accordingly
    	GameStatus status = blackJackGame.checkIfGameWonOrLost();
    	if (status == GameStatus.DEALERWON) {
    		// TODO Update Score
    		// Display Dealer Won Alert
			Alert alert = new Alert(AlertType.INFORMATION, "Dealer won!", ButtonType.OK);
			alert.showAndWait();
			showDealerCard();
	    	// Deactivate New Card and Call Game Buttons
	    	newCardBtn.setDisable(true);
	    	callGameBtn.setDisable(true);
    	} else if (status == GameStatus.USERWON) {
    		// TODO Update Score
    		// Display User Won Alert
			Alert alert = new Alert(AlertType.INFORMATION, "User won!", ButtonType.OK);
			alert.showAndWait();
			showDealerCard();
	    	// Deactivate New Card and Call Game Buttons
	    	newCardBtn.setDisable(true);
	    	callGameBtn.setDisable(true);
    	} else if (status == GameStatus.PUSH) {
    		// TODO Update Score
    		// Display Puah Alert
			Alert alert = new Alert(AlertType.INFORMATION, "Push!", ButtonType.OK);
			alert.showAndWait();
			showDealerCard();
	    	// Deactivate New Card and Call Game Buttons
	    	newCardBtn.setDisable(true);
	    	callGameBtn.setDisable(true);
    	}
    }

    @FXML
    /**
     * User wants to end current game and check if won
     * Uses BlackJackGame .callGame() instead of checkIfGameWonOrLost()
     * @param event Button Click
     */
    void callGameButtonClicked(ActionEvent event) {
    	// Calculate user hand and dealer hand
    	// If user hand is higher than dealers (but 21 or less), display You Won alert dialog
    	GameStatus status = blackJackGame.callGame();
    	if (status == GameStatus.DEALERWON) {
    		// TODO Update Score
    		// Display Dealer Won Alert
			Alert alert = new Alert(AlertType.INFORMATION, "Dealer won!", ButtonType.OK);
			alert.showAndWait();
			showDealerCard();
    	} else if (status == GameStatus.USERWON) {
    		// TODO Update Score
    		// Display User Won Alert
			Alert alert = new Alert(AlertType.INFORMATION, "User won!", ButtonType.OK);
			alert.showAndWait();
			showDealerCard();
    	} else if (status == GameStatus.PUSH) {
    		// TODO Update Score
    		// Display Puah Alert
			Alert alert = new Alert(AlertType.INFORMATION, "Push!", ButtonType.OK);
			alert.showAndWait();
			showDealerCard();
    	}
    	
    	// Flip dealer cards to front
    	showDealerCard();
    	
    	// Deactivate New Card and Call Game Buttons
    	newCardBtn.setDisable(true);
    	callGameBtn.setDisable(true);
    }
    
    @FXML
    /**
     * Calls startNewGame() to reinitialize game and screen and re-enables all buttons
     * @param event Button Click
     */
    void newGameBtnClicked(ActionEvent event) {
    	startNewGame();
    	// Activate New Card and Call Game Buttons
    	newCardBtn.setDisable(false);
    	callGameBtn.setDisable(false);
    }
    
    /**
     * Pulls a new card for dealer and displays on screen offset to last card displayed
     */
    public void addNewDealerCard() {
    	// Get new card and add to screen above and offset of bottom user card or last card used
    	// Create new ImageView using same dimensions as userHandBottomImg
    	ImageView imgNew = new ImageView();
    	imgNew.setPreserveRatio(true);
    	imgNew.setFitWidth(dealerHandBottomImg.getFitWidth());
    	imgNew.setFitHeight(dealerHandBottomImg.getFitHeight());
    	// getNewUserCard for new ImageView
    	imgNew.setImage(blackJackGame.getNewDealerCard());

    	// Add new ImageView to screen offset from last card added
    	// Should add in location so rank of card is visible
    	if (newDealerImageViewList.isEmpty()) {
    		imgNew.setLayoutX(dealerHandBottomImg.getLayoutX() + 25);
    		imgNew.setLayoutY(dealerHandBottomImg.getLayoutY() - 25);
    		newDealerImageViewList.add(imgNew);
    	} else {
    		imgNew.setLayoutX(newDealerImageViewList.get(newDealerImageViewList.size()-1).getLayoutX() + 25);
    		imgNew.setLayoutY(newDealerImageViewList.get(newDealerImageViewList.size()-1).getLayoutY() - 25); 
    		newDealerImageViewList.add(imgNew);
    	}
    	blackJackAnchorPane.getChildren().add(newDealerImageViewList.get(newDealerImageViewList.size()-1));
    }
    
    /**
     * Shows front of bottom dealer card
     */
    public void showDealerCard() {
    	CardImage img;
    	img = (CardImage)dealerHandBottomImg.getImage();
    	img.setToFrontImage();
    }
    
    /**
     * Removes all additional ImageViews from screen, clears ArrayLists, and re-initialize for new game
     */
    public void startNewGame() {
    	// Remove New User Card ImageViews from Anchor Pane and clear ArrayList
    	for (int i = 0; i < newUserImageViewList.size(); i++)
        	blackJackAnchorPane.getChildren().remove(newUserImageViewList.get(i));
   		newUserImageViewList.clear();
    	newUserImageViewList = null;

    	// Remove New Dealer Card ImageViews from Anchor Pane and clear ArrayList
    	for (int i = 0; i < newDealerImageViewList.size(); i++)
        	blackJackAnchorPane.getChildren().remove(newDealerImageViewList.get(i));
    	newDealerImageViewList.clear();
    	newDealerImageViewList = null;
    	
    	// Restart game from scratch
    	blackJackGame = null;
    	this.initData(userName, screenMode);
    }

}
