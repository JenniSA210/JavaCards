package application.controller;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;


/**
 * @author Jennifer Ingram UTSA ID drd411
 * @author Kathryn Dyches UTSA ID jlj082
 *
 */
public class JavaCardsController {

	public enum ScreenModes { DEFAULT, DARKMODE }
	private String userName = "";
	private ScreenModes screenMode = ScreenModes.DEFAULT;
	

    @FXML
    private TextField tf_name;

    @FXML
    private RadioButton rb_default;

    @FXML
    private RadioButton rb_dark;
/*  
    @FXML
    private ToggleGroup tg_screenmodes = new ToggleGroup();
    this.rb_default.setToggleGroup(tg_screenmodes);
    this.rb_dark.setToggleGroup(tg_screenmodes);
*/
	@FXML
    private Button playBlackJackBtn;

    @FXML
    private Button playGoFishBtn;

    @FXML
    /**
     * reloads the main screen in default mode and sets screenMode to DEFAULT
     * @param event
     */
    void rb_defaultClicked(ActionEvent event) {
    	screenMode = ScreenModes.DEFAULT;
    	// TODO: Write code to change screen colors and refresh screen
    	try {
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/JavaCards.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			Scene scene = new Scene(root,547,410);
			scene.getStylesheets().add(getClass().getResource("..//view/application.css").toExternalForm());
			newStage.setScene(scene);
			newStage.setTitle("Java Cards");
			newStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    /**
     * reloads the main screen in dark mode and sets screenMode to DARKMODE
     * @param event
     */
    void rb_darkClicked(ActionEvent event) {
    	screenMode = ScreenModes.DARKMODE;
    	// TODO: Write code to change screen colors and refresh screen
    	try {
			Stage newStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("view/JavaCards.fxml"));
			AnchorPane root = (AnchorPane)loader.load();
			Scene scene = new Scene(root,547,410);
			scene.getStylesheets().add(getClass().getResource("..//view/darkmode.css").toExternalForm());
			newStage.setScene(scene);
			newStage.setTitle("Java Cards");
			newStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
    }
    
    @FXML
    void playBlackJackClicked(ActionEvent event) {
    	LaunchBlackJackScreen();
    }

    @FXML
    void playGoFIshClicked(ActionEvent event) {
    	LaunchGoFishScreen();
    }
    
    @FXML
    void setUserName(ActionEvent event) {
    	userName = tf_name.getText();
    }
    
    /**
	 * Launch BlackJackController
	 * 
	 */
	private void LaunchBlackJackScreen() {
		try {
			Stage blackJackStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("..//view//BlackJack.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root,600,600);
			if(screenMode == ScreenModes.DEFAULT) {
				scene.getStylesheets().add(getClass().getResource("..//view/application.css").toExternalForm());
			}
			else if(screenMode == ScreenModes.DARKMODE) {
				scene.getStylesheets().add(getClass().getResource("..//view/darkmode.css").toExternalForm());
			}
			blackJackStage.initModality(Modality.APPLICATION_MODAL);
			blackJackStage.setScene(scene);
			blackJackStage.setTitle("Black Jack");
			
			BlackJackController controller = loader.<BlackJackController>getController();
			controller.initData(userName, screenMode);
			
			blackJackStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	* Launch BlackJackController
	* 
	*/
	private void LaunchGoFishScreen() {
		try {
			Stage goFishStage = new Stage();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("..//view//GoFish.fxml"));
			Parent root = loader.load();
			Scene scene = new Scene(root,600,600);
			if(screenMode == ScreenModes.DEFAULT) {
				scene.getStylesheets().add(getClass().getResource("..//view/application.css").toExternalForm());			
			}
			else if(screenMode == ScreenModes.DARKMODE) {
				scene.getStylesheets().add(getClass().getResource("..//view/darkmode.css").toExternalForm());
			}
			goFishStage.initModality(Modality.APPLICATION_MODAL);
			goFishStage.setScene(scene);
			goFishStage.setTitle("Go Fish");

			GoFishController controller = loader.<GoFishController>getController();
			controller.initData(userName, screenMode);
				
			goFishStage.show();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}