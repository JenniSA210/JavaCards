/**
 * 
 */
package application.controller;

import application.controller.JavaCardsController.ScreenModes;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

/**
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class GoFishController {
	private String userName;
	private ScreenModes screenMode = ScreenModes.DEFAULT;

    @FXML
    public void newGameBtnClicked(ActionEvent event) {
    
    }

    public void initData(String userName, ScreenModes screenMode) {
    	this.userName = userName;
    	this.screenMode = screenMode;
	}
}
