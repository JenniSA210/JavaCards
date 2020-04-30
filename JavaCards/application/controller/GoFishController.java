/**
 * 
 */
package application.controller;

import java.util.ArrayList;

import application.controller.JavaCardsController.ScreenModes;
import application.model.GoFishGame;
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
    /**
     * Calls startNewGame() to reinitialize game and screen and re-enables all buttons
     * @param event Button Click
     */
    void newGameBtnClicked(ActionEvent event) {
    	startNewGame();
    	// Activate New Card and Call Game Buttons
    	//newCardBtn.setDisable(false);
    	//callGameBtn.setDisable(false);
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
    			//userHandBottomImg.getParent().getStylesheets().add(getClass().getResource("..//view/application.css").toExternalForm());
    			//userHandBottomImg.getParent().getStylesheets().remove(getClass().getResource("..//view/darkmode.css").toExternalForm());
           		// Flaw fixed after Java 1.8 - won't change background using CSS - requires below code to change background
        		goFishAnchorPane.setBackground(new Background(new BackgroundFill(Color.ANTIQUEWHITE, CornerRadii.EMPTY, Insets.EMPTY)));
    		} else if (screenMode == ScreenModes.DARKMODE) {
    			//userHandBottomImg.getParent().getStylesheets().remove(getClass().getResource("..//view/application.css").toExternalForm());
    			//userHandBottomImg.getParent().getStylesheets().add(getClass().getResource("..//view/darkmode.css").toExternalForm());
    		}
    	} catch (Exception e) {
    		System.out.println(e);
    	}
    	goFishGame = new GoFishGame();
    	
    	// Initialize ArrayLists to hold user and cpu hands
    	newUserImageViewList = new ArrayList<ImageView>();
    	newCpuImageViewList = new ArrayList<ImageView>();
    	
    	// set visuals for player hand

    	// Set all cpu card visuals to back of card (grey with black border)
    	for(ImageView image : newCpuImageViewList) {
    		//image.setImage(value);
    	}

    	
    	// Initialize ArrayLists to hold completed user and cpu books
    	userBooks = new ArrayList<ImageView>();
    	cpuBooks = new ArrayList<ImageView>();
    	
    	
    	// start game loop
    	System.out.println("go fish start!");
goFishGame.begin();
    	System.out.println("go fish end!");
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
    	for (int i = 0; i < newCpuImageViewList.size(); i++)
        	goFishAnchorPane.getChildren().remove(newCpuImageViewList.get(i));
    	newCpuImageViewList.clear();
    	newCpuImageViewList = null;
    	
    	// Restart game from scratch
    	goFishGame = null;
    	this.initData(userName, screenMode);
    }
}
