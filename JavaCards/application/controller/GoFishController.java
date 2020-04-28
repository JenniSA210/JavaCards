/**
 * 
 */
package application.controller;

import java.util.ArrayList;

import application.controller.JavaCardsController.ScreenModes;
import application.model.BlackJackGame;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

/**
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class GoFishController {
	private String userName; // Holds user name from main screen (JavaCards)
	private ScreenModes screenMode = ScreenModes.DEFAULT; // Holds screen mode from main screen (JavaCards)
	private GoFishGame goFishGame; // Data model for Go Fish Game
	ArrayList<ImageView> newUserImageViewList; // Holds dynamically created card ImageViews for User Deck
	ArrayList<ImageView> newDealerImageViewList; // Holds dynamically created card ImageViews for Dealer Deck
	
    @FXML
    private AnchorPane goFishAnchorPane; // Main Pane
    
    @FXML
    private Button goFishNewGameBtn; // Button to clear screen and start new game
    
    @FXML
    private Button callGameBtn; // Button to end game on user click
    
    // TODO: add user hand as buttons

    @FXML
    public void newGameBtnClicked(ActionEvent event) {
    
    }

    public void initData(String userName, ScreenModes screenMode) {
    	this.userName = userName;
    	this.screenMode = screenMode;
	}
}
