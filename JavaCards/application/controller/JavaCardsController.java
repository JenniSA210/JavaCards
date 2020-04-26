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

	@FXML
    private Button playBlackJackBtn;

    @FXML
    private Button playGoFishBtn;

    @FXML
    void rb_defaultClicked(ActionEvent event) {
    	screenMode = ScreenModes.DEFAULT;
    	// TODO: Write code to change screen colors and refresh screen
    }
    
    @FXML
    void rb_darkClicked(ActionEvent event) {
    	screenMode = ScreenModes.DARKMODE;
    	// TODO: Write code to change screen colors and refresh screen
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
			scene.getStylesheets().add(getClass().getResource("..//view/application.css").toExternalForm());
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
				scene.getStylesheets().add(getClass().getResource("..//view/application.css").toExternalForm());
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