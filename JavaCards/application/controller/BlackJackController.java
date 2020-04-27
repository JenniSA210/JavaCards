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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class BlackJackController {
	private String userName;
	private ScreenModes screenMode = ScreenModes.DEFAULT;
	private BlackJackGame blackJackGame;
	ArrayList<ImageView> newUserImageViewList;
	ArrayList<ImageView> newDealerImageViewList;
	
    @FXML
    private AnchorPane blackJackAnchorPane;
    
    @FXML
    private Button blackJackNewGameBtn;
    
    @FXML
    private Button callGameBtn;
    
    @FXML
    private Button newCardBtn;
    
    @FXML
    private ImageView dealerHandBottomImg;
    
    @FXML
    private ImageView userHandBottomImg;
    
    public void initData(String userName, ScreenModes screenMode) {
    	this.userName = userName;
    	this.screenMode = screenMode;
    	blackJackGame = new BlackJackGame();
    	userHandBottomImg.setImage(blackJackGame.getNewUserCard());
    	// TODO Set bottom dealer card visual to back of card (grey with black border)
    	// Get new dealer card and add on top and offset of bottom dealer card
    	CardImage newDealerCard = blackJackGame.getNewDealerCard();
    	newDealerCard.setToBackImage();
    	dealerHandBottomImg.setImage(newDealerCard);
    	newUserImageViewList = new ArrayList<ImageView>();
    	newDealerImageViewList = new ArrayList<ImageView>();

    	// Add next dealer card
    	addNewDealerCard();

    	// Activate New Card and Call Game Buttons
    	newCardBtn.setDisable(false);
    	callGameBtn.setDisable(false);
    }
    
    @FXML
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
    	blackJackAnchorPane.getChildren().add(newUserImageViewList.get(newUserImageViewList.size()-1));
    	
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
    void newGameBtnClicked(ActionEvent event) {
    	startNewGame();
    	// Activate New Card and Call Game Buttons
    	newCardBtn.setDisable(false);
    	callGameBtn.setDisable(false);
    }
    
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
    
    public void showDealerCard() {
    	CardImage img;
    	img = (CardImage)dealerHandBottomImg.getImage();
    	img.setToFrontImage();
    }
    
    public void startNewGame() {
    	for (int i = 0; i < newUserImageViewList.size(); i++)
        	blackJackAnchorPane.getChildren().remove(newUserImageViewList.get(i));
   		newUserImageViewList.clear();
    	newUserImageViewList = null;

    	for (int i = 0; i < newDealerImageViewList.size(); i++)
        	blackJackAnchorPane.getChildren().remove(newDealerImageViewList.get(i));
    	newDealerImageViewList.clear();
    	newDealerImageViewList = null;
    	blackJackGame = null;
    	this.initData(userName, screenMode);
    }

}
