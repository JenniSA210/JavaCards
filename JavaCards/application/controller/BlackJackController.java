/**
 * 
 */
package application.controller;

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
public class BlackJackController {
	private String userName;
	private ScreenModes screenMode = ScreenModes.DEFAULT;
	private BlackJackGame blackJackGame;
	
    @FXML
    private AnchorPane blackJackAnchorPane;
    
    @FXML
    private Button blackJackNewGameBtn;
    
    @FXML
    private Button callGameButton;
    
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
    	dealerHandBottomImg.setImage(blackJackGame.getNewDealerCard());
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
    	imgNew.setLayoutX(userHandBottomImg.getLayoutX() + 30);
    	imgNew.setLayoutY(userHandBottomImg.getLayoutY() - 30);
    	blackJackAnchorPane.getChildren().add(imgNew);
    }

    @FXML
    void callGameButtonClicked(ActionEvent event) {
    	// Calculate user hand and dealer hand
    	// If user hand is higher than dealers (but 21 or less), display You Won alert dialog
    	// Update score and high score on screen
    }

}
