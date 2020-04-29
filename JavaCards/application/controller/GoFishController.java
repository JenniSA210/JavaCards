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
	ArrayList<ImageView> newUserImageViewList; // Holds dynamically created card ImageViews for user hand
	ArrayList<ImageView> newCpuImageViewList; // Holds dynamically created card ImageViews for cpu hand
	
    @FXML
    private AnchorPane goFishAnchorPane; // Main Pane
    
    @FXML
    private Button goFishNewGameBtn; // Button to clear screen and start new game
    
    @FXML
    ArrayList<ImageView> userBooks; // dynamically created card ImageViews for user's completed books
    
    @FXML
    ArrayList<ImageView> cpuBooks; // dynamically created card ImageViews for cpu's completed books

    @FXML
    public void newGameBtnClicked(ActionEvent event) {
    
    }

    /**
     * Initializes data when FXML is first loaded, and called when New Game button is clicked
     * @param userName User name entered in Main Screen
     * @param screenMode Screen mode to use
     */
    public void initData(String userName, ScreenModes screenMode) {
    	this.userName = userName;
    	this.screenMode = screenMode;
    	goFishGame = new GoFishGame();
    	
    	// Initialize ArrayLists to hold user and cpu hands
    	newUserImageViewList = new ArrayList<ImageView>();
    	newCpuImageViewList = new ArrayList<ImageView>();

    	// Set all cpu card visuals to back of card (grey with black border)
    	for(ImageView image : newCpuImageViewList) {
    		image.setToBackImage();
    	}

    	
    	// Initialize ArrayLists to hold completed user and cpu books
    	userBooks = new ArrayList<ImageView>();
    	cpuBooks = new ArrayList<ImageView>();

	}
}
