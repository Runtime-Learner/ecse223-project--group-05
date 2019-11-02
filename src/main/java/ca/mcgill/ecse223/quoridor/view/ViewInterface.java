//THE IMPORT STATEMENT BELOW IS REQUIRED IN THE .fxml FILE
//IT GETS DELETED WHEN YOU REGENERATE THE .fxml FILE WITH SCENE BUILDER
//YOU MUST ADD IT IN MANUALLY WHEN THAT HAPPENS

//<?import java.util.ArrayList?>


package ca.mcgill.ecse223.quoridor.view;

import ca.mcgill.ecse223.quoridor.QuoridorApplication;
import ca.mcgill.ecse223.quoridor.controller.QuoridorController;

import java.sql.Time;
import java.util.List;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Timer;

import ca.mcgill.ecse223.quoridor.model.Quoridor;
import javafx.beans.property.Property;
import javafx.collections.FXCollections;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;


public class ViewInterface {

    private static Quoridor quoridor = QuoridorApplication.getQuoridor();
	public Timer timer;
	//these are the pages that the user can travel to/interact with
	private enum Page {
		TOP_BUTTONS,
		MAIN_PAGE,
		NEW_GAME_PAGE,
		RULES_PAGE,
		LOAD_GAME_PAGE,
		SELECT_HOST_PAGE,
		GAME_SESSION_PAGE,
		CHOOSE_OPPONENT_PAGE;
	};
	
	//this list is initialized in the fxml file and is populated with 
	//GUI pages that the user can travel to/interact with.
//////ORDER MATTERS. IF YOU ADD PAGES IN THE FXML PAGE, MAKE SURE THAT	////////
//////YOU UPDATE THE ENUM AND THE getPage(Page Cur) FUNCTION			////////
	@FXML private List<AnchorPane> pageList;
	//generalize events by storing current page as a page/state pair
	private Page currentState = Page.MAIN_PAGE;
	AnchorPane CurrentPage = null;

	@FXML private ComboBox ComboBox_username1;
	@FXML private ComboBox ComboBox_username2;
	
	@FXML ListView loadedGameList;
	private int counter = 0;
	
	
//Load Game Page
	@FXML private Label lbl_directory;

	//Game Session Page
	@FXML private Label whiteTimer;
	@FXML private Label blackTimer;
	
	
	public void addToLoadedGameList() {
            	Stage stage = new Stage();
            	DirectoryChooser fileChooser = new DirectoryChooser();
            	fileChooser.setTitle("Open Resource File");
            	File file = fileChooser.showDialog(stage);
            	stage.close();
                if (file != null) {
                    lbl_directory.setText(file.toString());
                    detectGameFiles(file);
                }
                else {
                	lbl_directory.setText("Directory could not be opened");
                }
	}
	
	private void detectGameFiles(File file) {
		File[] gameFiles = file.listFiles(new FilenameFilter() {
		    public boolean accept(File dir, String name) {
		    	return name.endsWith("txt");
		        //return name.startsWith("temp") && name.endsWith("txt");
		    }
		});
		System.out.println(gameFiles);
		loadedGameList.getItems().clear();
		for (File f : gameFiles)
			loadedGameList.getItems().add(f.toString());
	}

	public void gotSelected() {
		System.out.println(loadedGameList.getSelectionModel().getSelectedItem());
	}
	
	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Create New Game Page.
	 * Disables and turns the past page invisible.
	 */
	public void Goto_New_Game_Page() {
		Goto_Page(Page.NEW_GAME_PAGE);
	}


	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Main Page.
	 * Disables and turns the past page invisible.
	 */
	public void Goto_Main_Page() {
		Goto_Page(Page.MAIN_PAGE);
	}
	
	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Choose Opponent Page.
	 * Disables and turns the past page invisible.
	 */
	public void Goto_Choose_Opponent_Page() { Goto_Page(Page.CHOOSE_OPPONENT_PAGE);
	}
	
	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Game Session Page.
	 * Disables and turns the past page invisible.
	 */
	public void Goto_Game_Session_Page() {

		try {
			QuoridorController.initializeBoard(QuoridorApplication.getQuoridor(), timer);
		}
		catch(Exception e){
			throw new java.lang.UnsupportedOperationException("Cannot initialize the board");
		}
		whiteTimer.setText(QuoridorApplication.getQuoridor().getCurrentGame().getWhitePlayer().getRemainingTime().toString());
		//blackTimer.textProperty().bindBidirectional((Property<String>) QuoridorApplication.getQuoridor().getCurrentGame().getBlackPlayer().getRemainingTime());

		Goto_Page(Page.GAME_SESSION_PAGE);
	}
	
	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Load Game Page.
	 * Disables and turns the past page invisible.
	 */
	public void Goto_Load_Game_Page() {
		Goto_Page(Page.LOAD_GAME_PAGE);
	}
	
	/**
	 * @author Matthias Arabian
	 * Changes the GUI CurrentPage to the Select Host Page.
	 * Disables and turns the past page invisible.
	 */
	public void Goto_Select_Host_Page() {
		Goto_Page(Page.SELECT_HOST_PAGE);
	}
	
	/**
	 * @author Matthias Arabian
	 * @param p The page to go to 
	 * Changes the GUI CurrentPage to the page defined by the parameter p
	 * This function is called by every other Goto_ functions
	 * and serves as a generalized template for page travel.
	 */
	private void Goto_Page(Page p) {
		CurrentPage = getCurrentPage();
		
//		Restrict page to page movement when the current page is disabled 
//		(something else is happening atm)
		if (CurrentPageIsDisabled())
			return;
			
		CurrentPage.setDisable(true);
		CurrentPage.setVisible(false);

		currentState = p;
		CurrentPage = getCurrentPage();
		CurrentPage.setDisable(false);
		CurrentPage.setVisible(true);
		CurrentPage.toFront();
	}
	
	/**
	 * @author Matthias Arabian
	 * Displays the Rules page. 
	 * Disables the CurrentPane, but does not replace it.
	 * Overlays the rules on top of the CurrentPage to allow user to return to what
	 * they were doing.
	 */
	public void openRulesPage() {
		CurrentPage = getCurrentPage();
		
		//if page is disabled, then the rules are already displayed. Therefore, the rules page should be closed.
		if (CurrentPageIsDisabled()) { 
			closeRulesPage();
			return;
		}
		
		
		//disable the current page, but do not make it invisible or replace it.
		CurrentPage.setDisable(true);
		
		//display the Rules page, and bring it to front to allow for user interaction.
		CurrentPage = getPage(Page.RULES_PAGE);
		CurrentPage.setDisable(false);
		CurrentPage.setVisible(true);
		CurrentPage.toFront();
		getPage(Page.TOP_BUTTONS).toFront(); //those need to be on top of the rules to allow for clicking on Rules label to close rules
		
	}
	
	/**
	 * @author Matthias Arabian
	 * this function closes the Rules page and re-enables the CurrentPage.
	 */
	public void closeRulesPage() {
		CurrentPage = getPage(Page.RULES_PAGE);
		CurrentPage.setDisable(true);
		CurrentPage.setVisible(false);
		CurrentPage = getCurrentPage();
		CurrentPage.setDisable(false);
		CurrentPage.toFront();
	}
	
	/**
	 * @author Matthias Arabian
	 * @return whether CurrentPage is disabled
	 */
	private boolean CurrentPageIsDisabled() {
		return getPage(currentState).isDisable();
	}
	
	/**
	 * @author Matthias Arabian
	 * @return the current page the user is interacting with
	 */
	private AnchorPane getCurrentPage() {
		return getPage(currentState);
	}
	
	/**
	 * @author Matthias Arabian
	 * @param Cur: an enum type used to discriminate between GUI pages in a user-friendly manner.
	 * @return the page described by the variable Cur
	 * 
	 * The order of the pages stored in the pageList variable depends on 
	 * their order when the list was declared in the FXML file.
	 * You must ensure that the pages referenced in this function are the intended ones.
	 * 
	 * This is a necessary evil that allows for greater generalization in event handlers.
	 */
	private AnchorPane getPage(Page Cur) {
		if (Cur == Page.TOP_BUTTONS)
			return pageList.get(0);
		if (Cur == Page.MAIN_PAGE)
			return pageList.get(1);
		if (Cur == Page.NEW_GAME_PAGE)
			return pageList.get(2);
		if (Cur == Page.RULES_PAGE)
			return pageList.get(3);
		if (Cur == Page.LOAD_GAME_PAGE)
			return pageList.get(4);
		if (Cur == Page.SELECT_HOST_PAGE)
			return pageList.get(5);
		if (Cur == Page.GAME_SESSION_PAGE)
			return pageList.get(6);
		if (Cur == Page.CHOOSE_OPPONENT_PAGE)
			return pageList.get(7);
		else 
			return null;
	}
	
	
	/**
	 * @author Matthias Arabian
	 * initializes the FXML components. this code runs once the application is launched but before the GUI is displayed.
	 */
	public void initialize() {

		ComboBox_username1.setItems(FXCollections.observableArrayList(
			    "A", "B", "C", "D"));
		ComboBox_username2.setItems(FXCollections.observableArrayList(
			    "A", "B", "C", "D"));

		QuoridorController.initializeQuoridor(QuoridorApplication.getQuoridor());

		timer = new Timer();
	}

    /**
     * @author Alex Masciotra
     * method to display the existingusernames in quoridor when arrow is pressed
     */
	public void displayExistingUserNames(){
	    //for testing purposes manually adding usernames

        //when the arrow is pressed
        List<String> existingUserNames = QuoridorController.provideExistingUserNames(quoridor);

        //this comboBox is for whiteUserChooseFromExistingArrow
        ComboBox_username1.setItems(FXCollections.observableList(existingUserNames));

        //this comboBox is for blackUserChooseFromExistingArrow
        ComboBox_username2.setItems(FXCollections.observableList(existingUserNames));

    }

    /**
     * @author Alex Masciotra
     * method to selectAnExistingUserNAme when arrow is pressed to view
     */
	public void selectExistingUserNameFromDropDownList(){

	    //when user selects one of the usernames from the drop downlist
        //check if the event is coming from selecting from the white or black combobox username

        /*
        // the if and else conditions is more, if the event is coming from the whiteplayer, or if its coming from black
        if(ComboBox_username1.toString().toLowerCase().contains("white")){
            QuoridorController.assignPlayerColorToUserName("white", quoridor);
            QuoridorController.selectExistingUserName(ComboBox_username1.getContentOfBoxAsString, quoridor);
        } else if (ComboBox_username2.toString().toLowerCase().contains("black")){
            QuoridorController.assignPlayerColorToUserName("black", quoridor);
            QuoridorController.selectExistingUserName(ComboBox_username2.getContentOfBoxAsString, quoridor);
        }

         */

    }

    /**
     * @author Alex Masciotra
     * Method to create a new username
     */
    public void createNewUserName(){

        /*        Boolean IsValid;
        //here comboBox should be like newWhiteUserName or newBlackUserName
	    if(ComboBox_username1.toString().toLowerCase().contains("white")){
	        QuoridorController.assignPlayerColorToUserName("white", quoridor);

	        isValid = QuoridorController.selectNewUserName(ComboBox_username1.getContentOfBoxAsString, quoridor);

	        if (isValid == false){

	            //THROW POPUP SAYING USERNAME ALREADY IN USE AND SELECT A NEW ONE AND RECALL THIS METHOD
            }

        } else if (ComboBox_username2.toString().toLowerCase().contains("black")){
            QuoridorController.assignPlayerColorToUserName("black", quoridor);

            isValid = QuoridorController.selectNewUserName(ComboBox_username2.getContentOfBoxAsString, quoridor);

            if (isValid == false){

                //THROW POPUP SAYING USERNAME ALREADY IN USE AND SELECT A NEW ONE AND RECALL THIS METHOD
            }
        }

	    */
    }
}